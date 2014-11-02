package com.basu.testproject;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class SecondActivity extends Activity {
	
	TextView tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second_page);
		
		tv = (TextView) findViewById(R.id.textView1);
		
		//Dialog dateAndTime = new Dialog();
	}
	
	public void onClick(View v) {
		showAlert();
//		alert.addContentView(view, params)
	}
	
	
	
	private void showAlert() {
		
		final Dialog dialog = new Dialog(this);
		
		dialog.setContentView(R.layout.custom_dialogue);
		dialog.setTitle("Date and Time picker");
		
		Button dismiss = (Button) dialog.findViewById(R.id.btDialog);
		dismiss.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				

				
				dialog.dismiss();
				
			}
		});
		
		dialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				TimePicker time = (TimePicker) findViewById(R.id.timePicker1);
				int hr = time.getCurrentHour();
				Log.e("dkjbdsc", ""+hr);
				tv.setText("Hello");
			}
		});
		
		dialog.show();
		
	}

}
