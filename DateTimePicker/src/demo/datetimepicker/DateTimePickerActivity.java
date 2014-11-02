package demo.datetimepicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ViewSwitcher;

public class DateTimePickerActivity extends Activity {
	/** Called when the activity is first created. */
	Button ShowDTPicker;
	Button ShowDatePicker;
	Button ShowTimePicker;
	Button Set;
	Button ReSet;
	Button Cancel;
	DatePicker DPic;
	TimePicker TPic;
	TextView Date;

	private ViewSwitcher switcher;
	static final int DATE_TIME_DIALOG_ID = 999;
	Dialog dialog;
	final Calendar c = Calendar.getInstance();
	SimpleDateFormat dfDate = new SimpleDateFormat("dd-MMM-yyyy hh:mm");
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ShowDTPicker = ((Button) findViewById(R.id.ButtonDemo));


		Date = ((TextView) findViewById(R.id.Date));
		Date.setText(dfDate.format(c.getTime()));
		dialog = new Dialog(this);
		dialog.setContentView(R.layout.datetimepicker);
		switcher = (ViewSwitcher) dialog.findViewById(R.id.DateTimePickerVS);
		DPic = (DatePicker) dialog.findViewById(R.id.DatePicker);
		TPic = (TimePicker) dialog.findViewById(R.id.TimePicker);
		
		ShowDatePicker = ((Button) dialog.findViewById(R.id.SwitchToDate));
		ShowDatePicker.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switcher.showPrevious();
				ShowDatePicker.setEnabled(false);
				ShowTimePicker.setEnabled(true);
			}
		});
		ShowTimePicker = ((Button) dialog.findViewById(R.id.SwitchToTime));
		ShowTimePicker.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switcher.showNext();
				ShowDatePicker.setEnabled(true);
				ShowTimePicker.setEnabled(false);
			}
		});
        
		Set = ((Button) dialog.findViewById(R.id.SetDateTime));
		Set.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				c.set(DPic.getYear(),  DPic.getMonth(), DPic.getDayOfMonth(), TPic.getCurrentHour(), TPic.getCurrentMinute());
				Date.setText(dfDate.format(c.getTime()));
				dialog.cancel();
			}
		});
		ReSet = ((Button) dialog.findViewById(R.id.ResetDateTime));
		ReSet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DPic.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
				TPic.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
				TPic.setCurrentMinute(c.get(Calendar.MINUTE));
			}
		});
		Cancel = ((Button) dialog.findViewById(R.id.CancelDialog));
		Cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		ShowDTPicker.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DPic.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
				TPic.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
				TPic.setCurrentMinute(c.get(Calendar.MINUTE));
				showDialog(DATE_TIME_DIALOG_ID);
			}
		});
		dialog.setTitle("Select Date Time");
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_TIME_DIALOG_ID:
			
			return dialog;
		}
		return null;
	}
}