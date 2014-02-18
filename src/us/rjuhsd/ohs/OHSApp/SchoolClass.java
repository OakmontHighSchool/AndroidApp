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
		scJson.put("aeriesID", aeriesID);
		JSONArray assignJson = new JSONArray(); //TODO: FILL THIS!
		scJson.put("assignments", assignJson);
		return scJson;
	}
}
