package us.rjuhsd.ohs.OHSApp;

import android.app.Application;

public class OHSApplication extends Application {

	public AeriesManager aeriesManager;

	@Override
	public void onCreate() {
		super.onCreate();
		initSingletons();
	}

	protected void initSingletons() {
		this.aeriesManager = new AeriesManager();
	}
}
