package us.rjuhsd.ohs.OHSApp;

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
}
