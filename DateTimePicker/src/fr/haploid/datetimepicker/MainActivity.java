package fr.haploid.datetimepicker;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.View;
/*souissi haythem*/
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
	}
	
	public void showTimePickerDialog(View v) {
		
		Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.date_time_layout);
		
		dialog.setTitle("Choix de l'horaire");
		
		dialog.show();
	}

}
