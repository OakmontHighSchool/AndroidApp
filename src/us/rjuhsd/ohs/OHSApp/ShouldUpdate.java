package us.rjuhsd.ohs.OHSApp;

public abstract class ShouldUpdate {
	private ShouldUpdate() {} //Prevent instantiation

	public static boolean check(long lastUpdate) {
		return ((System.currentTimeMillis() / 1000L)-lastUpdate) > 86400; //86400 is the number of seconds in a day
	}
}
