package us.rjuhsd.ohs.OHSApp;

import org.json.JSONException;
import org.json.JSONObject;

public class Assignment {
	public String description;
	public String type;
	public String category;
	public String score;
	public String correct;
	public String percent;
	public String status;
	public String dateCompleted;
	public String dateDue;
	public boolean gradingComplete;

	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("description",description);
		json.put("type",type);
		json.put("category",category);
		json.put("score",score);
		json.put("correct",correct);
		json.put("percent",percent);
		json.put("status",status);
		json.put("dateCompleted", dateCompleted);
		json.put("dateDue",dateDue);
		json.put("gradingComplete",gradingComplete);
		return json;
	}

	public static Assignment fromJSON(JSONObject json) throws JSONException {
		Assignment asg = new Assignment();
		asg.description = json.getString("description");
		asg.type = json.getString("type");
		asg.category = json.getString("category");
		asg.score = json.getString("score");
		asg.correct = json.getString("correct");
		asg.percent = json.getString("percent");
		asg.status = json.getString("status");
		asg.dateCompleted = json.getString("dateCompleted");
		asg.dateDue = json.getString("dateDue");
		asg.gradingComplete = json.getBoolean("gradingComplete");
		return asg;
	}
}
