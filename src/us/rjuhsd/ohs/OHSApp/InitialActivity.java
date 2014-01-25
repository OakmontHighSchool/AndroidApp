package us.rjuhsd.ohs.OHSApp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class InitialActivity extends Activity {
	//Variables:
	private LinearLayout OHSNoteViewer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		this.getAllByID();
		new OHSNotification(0, "TEST", OHSNoteViewer, this);
	}

	public void getAllByID() {
		OHSNoteViewer = (LinearLayout) this.findViewById(R.id.OHSNoteViewer);
	}
}
