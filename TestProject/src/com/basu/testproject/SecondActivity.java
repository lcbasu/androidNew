package com.basu.testproject;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class SecondActivity extends Activity {
	
	TextView tv;
	private View alertView;
	private AlertDialog alertDialog;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second_page);
		
		tv = (TextView) findViewById(R.id.textView1);
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		alertView = inflater.inflate(R.layout.custom_dialogue, null);
		
		builder.setView(alertView);
		
		builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				
				tv.setText("Hello Dialog");
				
			}
		});
		
		builder.setCancelable(false);
		alertDialog = builder.create();
		
	}
	
	public void onClick(View v) {
		alertDialog.show();
		
	}

}
