package us.rjuhsd.ohs.OHSApp.tasks;

public interface ClassesTaskShim {
	public void onGradesStart();
	public void onGradesError(String errorMsg);
	public void onGradesDone();
}
