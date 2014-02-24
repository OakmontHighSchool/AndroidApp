package us.rjuhsd.ohs.OHSApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SchoolClass {
	public String className;
	public String period;
	public String teacherName;
	public String percentage;
	public String mark;
	public String missingAssign;
	public String lastUpdate;
	public int ID;
	public String aeriesID;
	public ArrayList<Assignment> assignments;

	public SchoolClass(int id) {
		this.ID = id;
		assignments = new ArrayList<Assignment>();
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject scJson = new JSONObject();
		scJson.put("className", className);
		scJson.put("period", period);
		scJson.put("teacherName", teacherName);
		scJson.put("percentage", percentage);
		scJson.put("mark", mark);
		scJson.put("missingAssign", missingAssign);
		scJson.put("lastUpdate", lastUpdate);
		scJson.put("ID", ID);
		scJson.put("aeriesID", aeriesID+"");
		JSONArray assignJson = new JSONArray();
		for(Assignment assignment : assignments) {
			assignJson.put(assignment.toJSON());
		}
		scJson.put("assignments", assignJson);
		return scJson;
	}

	public static SchoolClass fromJSON(JSONObject scJSON) throws JSONException {
		SchoolClass sc = new SchoolClass(scJSON.getInt("ID"));
		sc.className = scJSON.getString("className");
		sc.period = scJSON.getString("period");
		sc.teacherName = scJSON.getString("teacherName");
		sc.percentage = scJSON.getString("percentage");
		sc.mark = scJSON.getString("mark");
		sc.missingAssign = scJSON.getString("missingAssign");
		sc.lastUpdate = scJSON.getString("lastUpdate");
		sc.aeriesID = scJSON.getString("aeriesID");
		JSONArray assignJson = scJSON.getJSONArray("assignments");
		for(int i=0;i<assignJson.length();i++) {
			sc.assignments.add(Assignment.fromJSON(assignJson.getJSONObject(i)));
		}
		return sc;
	}
}
