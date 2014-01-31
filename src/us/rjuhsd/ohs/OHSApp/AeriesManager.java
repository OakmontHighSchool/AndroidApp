package us.rjuhsd.ohs.OHSApp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class AeriesManager {
	private AeriesManager() {} //Disallow instantiation of this class
    private static String LOGIN_URL = "https://homelink.rjuhsd.us/LoginParent.aspx";

	public static ArrayList<String> getGrades(Context context) {
		ArrayList<String> grades = new ArrayList<String>();
		try {
			String[] loginData = aeriesLoginData(context);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("portalAccountUsername", loginData[0]));
			nvps.add(new BasicNameValuePair("portalAccountPassword", loginData[1]));
			nvps.add(new BasicNameValuePair("checkCookiesEnabled", "true"));
			nvps.add(new BasicNameValuePair("checkSilverlightSupport", "true"));
			nvps.add(new BasicNameValuePair("checkMobileDevice", "false"));
			nvps.add(new BasicNameValuePair("checkStandaloneMode", "false"));
			nvps.add(new BasicNameValuePair("checkTabletDevice", "false"));

			HttpPost request = new HttpPost(LOGIN_URL);
			request.setEntity(new UrlEncodedFormEntity(nvps,"UTF-8"));
			HttpClient client = sslClient(new DefaultHttpClient());
			HttpResponse response = client.execute(request);

			Document doc = Jsoup.parse(response.getEntity().getContent(),null,LOGIN_URL);
			Element trClass1 = doc.select("tr#ctl00_MainContent_ctl19_DataDetails_ctl01_trGBKItem").first();
			if(trClass1 == null) {
				Log.d("JSoup", "Its null doctor, what do we do?");
 			} else {
				Log.d("JSoup", trClass1.select("td").get(3).text());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return grades;
	}

	public static String[] aeriesLoginData(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String[] toReturn = new String[2];
		toReturn[0] = prefs.getString("aeries_username",null);
		toReturn[1] = prefs.getString("aeries_password",null);
		return toReturn;
	}

    private static HttpClient sslClient(HttpClient client) {
        try {
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new FixedSSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = client.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf, 443));
            return new DefaultHttpClient(ccm, client.getParams());
        } catch (Exception ex) {
            return null;
        }
    }
}