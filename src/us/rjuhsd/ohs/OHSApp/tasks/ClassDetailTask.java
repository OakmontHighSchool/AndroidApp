package us.rjuhsd.ohs.OHSApp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.rjuhsd.ohs.OHSApp.Assignment;
import us.rjuhsd.ohs.OHSApp.SchoolClass;
import us.rjuhsd.ohs.OHSApp.managers.AeriesManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassDetailTask extends AsyncTask<SchoolClass,Void,Void> {
	private final ClassDetailTaskReceiver layer;
	private String error = "An unknown error occurred while loading your classes"; //This text should never appear, its the default
	private final AeriesManager aeriesManager;

	public ClassDetailTask(Context context, ClassDetailTaskReceiver layer) {
		this.layer = layer;
		this.aeriesManager = new AeriesManager(context);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		layer.onGradesStart();
	}

	@Override
	protected Void doInBackground(SchoolClass... schoolClasses) {
		try {
			aeriesManager.client.execute(aeriesManager.getLoginRequest());
			//Navigate to the class_detail page, used to rip the class IDs for further navigation
			HttpResponse response_main = aeriesManager.client.execute(new HttpGet(AeriesManager.GRADES_DETAIL));

			Document doc_main = Jsoup.parse(response_main.getEntity().getContent(), null, AeriesManager.GRADES_DETAIL);
			Elements options = doc_main.select("#ctl00_MainContent_subGBS_dlGN").first().children();
			String viewState = doc_main.select("#__VIEWSTATE").val();

			for(Element option: options) {
				String stupidTitle = option.text();
				String stupidId = option.attr("value");
				if(!stupidTitle.startsWith("<<")) {
					for(SchoolClass schoolClass: schoolClasses) {
						if(stupidTitle.matches("(.*)" + schoolClass.className + "(.*)")) {
							schoolClass.aeriesID = stupidId;
						}
					}
				}
			}
			for (SchoolClass schoolClass : schoolClasses) {
				if (schoolClass.aeriesID == null) {
					continue; //Skip classes where we don't know the id, this shouldn't happen in theory
				}
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				nvps.add(new BasicNameValuePair("ctl00$MainContent$subGBS$dlGN", schoolClass.aeriesID));
				nvps.add(new BasicNameValuePair("__EVENTTARGET", "ctl00$MainContent$subGBS$dlGN"));
				nvps.add(new BasicNameValuePair("__VIEWSTATE", viewState)); //This is not some stupid constant

				HttpPost request = new HttpPost(AeriesManager.GRADES_DETAIL);
				request.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
				HttpResponse response = aeriesManager.client.execute(request);
				Document doc = Jsoup.parse(response.getEntity().getContent(), null, request.getURI().toString());
				Elements rows = doc.select("table#ctl00_MainContent_subGBS_tblEverything div#ctl00_MainContent_subGBS_upEverything > table").get(1).children().first().children().first().children().first().children().first().children().first().children();
				if (rows == null) {
					//No data was loaded, calling it quits here and posting a dialog explaining why
					error = "An error occurred, Aeries might be down";
					cancel(true);
					return null;
				}
				schoolClass.assignments.clear();
				for(Element row : rows) {
					if (row.className().equals("SubHeaderRow")) {
						continue; //Skip the header row
					}
					if (row.children().size() >= 3) {
						Assignment assign = new Assignment();
						row.children().get(2).children().remove();
						assign.description = row.children().get(2).text();
						assign.type = row.children().get(3).text();
						assign.category = row.children().get(4).text();
						assign.score = row.children().get(5).text();
						assign.correct = row.children().get(6).text();
						assign.percent = row.children().get(7).text();
						assign.status = row.children().get(8).text();
						assign.dateCompleted = row.children().get(9).text();
						assign.dateDue = row.children().get(10).text();
						assign.gradingComplete = row.children().get(11).text().contains("Yes");

						schoolClass.assignments.add(assign);
					}
					aeriesManager.setAssignments(schoolClass.ID, schoolClass.assignments);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		layer.onGradesError(error);
	}

	@Override
	protected void onPostExecute(Void v) {
		super.onPostExecute(v);
		if(isCancelled()) {
			onCancelled();
			return;
		}
		aeriesManager.writeAllData();
		layer.onGradesDone();
	}
}
