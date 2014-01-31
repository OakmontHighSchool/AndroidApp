package us.rjuhsd.ohs.OHSApp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import org.jsoup.select.Elements;

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
	//private static String DEFAULT_URL = "http://homelink.rjuhsd.us/Default.aspx";

	public static ArrayList<SchoolClass> getGrades(Context context) {
		ArrayList<SchoolClass> grades = new ArrayList<SchoolClass>();
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
			HttpClient client = HttpsClientFactory.sslClient();
			HttpResponse response = client.execute(request);

			Document doc = Jsoup.parse(response.getEntity().getContent(),null,LOGIN_URL);
			int rowCount = 1;
			while(true) {
				String trId = "tr#ctl00_MainContent_ctl19_DataDetails_ctl0"+rowCount+"_trGBKItem";
				Element tr = doc.select(trId).first();
				if(tr == null) {
					if(rowCount == 1) {
						new AlertDialog.Builder(context)
								.setTitle("Login Failure!")
								.setMessage("Either the grades system is unavailable or your login is incorrect.")
								.setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										// continue with delete
									}
								})
								.show();
						return grades;
					}
					break;
				} else {
					SchoolClass sClass = new SchoolClass();
					Elements tds = tr.select("td");
					Element className = tds.get(1);
					if(className != null) {
						sClass.className = className.text();
					}
					Element period = tds.get(2);
					if(period != null) {
						sClass.period= period.text();
					}
					Element teacherName = tds.get(3);
					if(teacherName != null) {
						sClass.teacherName= teacherName.text();
					}
					Element percentage = tds.get(4);
					if(percentage != null) {
						sClass.percentage= percentage.text();
					}
					Element mark = tds.get(6);
					if(mark != null) {
						sClass.mark = mark.text();
					}
					Element missingAssign = tds.get(8);
					if(missingAssign!= null) {
						sClass.missingAssign = missingAssign.text();
					}
					Element lastUpdate = tds.get(10);
					if(lastUpdate != null) {
						sClass.lastUpdate = lastUpdate.text();
					}
					grades.add(sClass);
				}
				rowCount++;
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