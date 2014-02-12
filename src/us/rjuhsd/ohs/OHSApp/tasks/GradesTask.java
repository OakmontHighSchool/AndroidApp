package us.rjuhsd.ohs.OHSApp.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.rjuhsd.ohs.OHSApp.GradesArrayAdapter;
import us.rjuhsd.ohs.OHSApp.R;
import us.rjuhsd.ohs.OHSApp.SchoolClass;
import us.rjuhsd.ohs.OHSApp.activities.ClassDetailActivity;
import us.rjuhsd.ohs.OHSApp.managers.AeriesManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GradesTask extends AsyncTask<Void, Void, Void> {
	Activity activity;
	ProgressDialog progressDialog;
	AeriesManager aeriesManager;
	ArrayList<SchoolClass> grades;
	String error = "An unknown error occurred while loading your classes"; //This text should never appear, its the default

	public GradesTask(Activity activity, AeriesManager aeriesManager) {
		this.activity = activity;
		this.aeriesManager = aeriesManager;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = new ProgressDialog(activity);
		progressDialog.setMessage("Loading your classes. Please Wait");
		progressDialog.setIndeterminate(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(true);
		progressDialog.show();
	}

	@Override
	protected Void doInBackground(Void... voids) {
		grades = new ArrayList<SchoolClass>();
		try {
			String[] loginData = AeriesManager.aeriesLoginData(activity);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("portalAccountUsername", loginData[0]));
			nvps.add(new BasicNameValuePair("portalAccountPassword", loginData[1]));
			nvps.add(new BasicNameValuePair("checkCookiesEnabled", "true"));
			nvps.add(new BasicNameValuePair("checkSilverlightSupport", "true"));
			nvps.add(new BasicNameValuePair("checkMobileDevice", "false"));
			nvps.add(new BasicNameValuePair("checkStandaloneMode", "false"));
			nvps.add(new BasicNameValuePair("checkTabletDevice", "false"));

			HttpPost request = new HttpPost(AeriesManager.LOGIN_URL);
			request.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			HttpResponse response = aeriesManager.client.execute(request);


			Document doc = Jsoup.parse(response.getEntity().getContent(), null, request.getURI().toString());
			int rowCount = 1;
			while (true) {
				String trId = "tr#ctl00_MainContent_ctl19_DataDetails_ctl0" + rowCount + "_trGBKItem";
				Element tr = doc.select(trId).first();
				if (tr == null) {
					if (rowCount == 1) {
						//No data was loaded, calling it quits here and posting a dialog explaining why
						if(loginData[0].equals("") || loginData[1].equals("")) {
							error = "Please check that you have entered your Aeries information in preferences correctly";
						} else {
							error = "Either the grades system is unavailable or your login is incorrect";
						}
						cancel(true);
						return null;
					}
					break;
				} else {
					SchoolClass sClass = new SchoolClass(rowCount - 1);
					Elements tds = tr.select("td");
					Element className = tds.get(1);
					if (className != null) {
						sClass.className = className.text();
					}
					Element period = tds.get(2);
					if (period != null) {
						sClass.period = period.text();
					}
					Element teacherName = tds.get(3);
					if (teacherName != null) {
						sClass.teacherName = teacherName.text();
					}
					Element percentage = tds.get(4);
					if (percentage != null) {
						sClass.percentage = percentage.text();
					}
					Element mark = tds.get(6);
					if (mark != null) {
						sClass.mark = mark.text();
					}
					Element missingAssign = tds.get(8);
					if (missingAssign != null) {
						sClass.missingAssign = missingAssign.text();
					}
					Element lastUpdate = tds.get(10);
					if (lastUpdate != null) {
						sClass.lastUpdate = lastUpdate.text();
					}
					grades.add(sClass);
				}
				rowCount++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		aeriesManager.errorLoadingGrades(error);
	}

	@Override
	protected void onPostExecute(Void v) {
		super.onPostExecute(v);
		if(isCancelled()) {
			onCancelled();
			return;
		}
		aeriesManager.setSchoolClasses(grades);
		inflateList(activity);
		progressDialog.dismiss();
	}

	public void inflateList(final Activity act) {
		final ArrayAdapter adapter = new GradesArrayAdapter(activity, R.layout.grades_list_item, grades);
		final ListView listview = (ListView) act.findViewById(R.id.listview);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent gradeDetailIntent = new Intent(act, ClassDetailActivity.class);
				gradeDetailIntent.putExtra("schoolClassId",arg2);
				act.startActivity(gradeDetailIntent);
			}

		});
	}
}
