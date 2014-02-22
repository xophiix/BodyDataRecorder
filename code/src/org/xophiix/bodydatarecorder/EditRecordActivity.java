package org.xophiix.bodydatarecorder;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.xophiix.bodydatarecorder.model.BodyDataRecord;
import org.xophiix.bodydatarecorder.model.DbHelper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class EditRecordActivity extends Activity implements OnClickListener {	
		
	Calendar m_date;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_record);
        
        int[] buttonIds = {
    		R.id.EditTextWeight,
    		R.id.EditTextBreastSize,
    		R.id.EditTextWaistSize,
    		R.id.EditTextHipSize,
    		R.id.Date,
    		R.id.Time,
    		R.id.save,
        };
        
        for (int i = 0; i < buttonIds.length; ++i) {
        	findViewById(buttonIds[i]).setOnClickListener(this);        	
        }
        
        m_date = Calendar.getInstance();
        updateTimeDisplay();
    }
    
    void updateTimeDisplay() {
    	CharSequence text = DateFormat.format("yyyy/MM/dd", m_date);
    	((TextView)findViewById(R.id.Date)).setText(text);
    	
    	text = DateFormat.format("kk:mm", m_date);
    	((TextView)findViewById(R.id.Time)).setText(text);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 1, 0, "历史记录");
		menu.add(0, 2, 0, "退 出");	
		return true;
    }
    
    public void onClick(View v) {
    	int viewId = v.getId();
    	
		if (viewId == R.id.save) {
			BodyDataRecord record = new BodyDataRecord();
			
			String value = ((EditText)findViewById(R.id.EditTextWeight)).getText().toString();
			if (!value.isEmpty()) {
				record.weight = Double.parseDouble(value);	
			}
			
			value = ((EditText)findViewById(R.id.EditTextBreastSize)).getText().toString();
			if (!value.isEmpty()) {
				record.breastSize = Double.parseDouble(value);	
			}
			
			value = ((EditText)findViewById(R.id.EditTextWaistSize)).getText().toString();
			if (!value.isEmpty()) {
				record.waistSize = Double.parseDouble(value);	
			}
			
			value = ((EditText)findViewById(R.id.EditTextHipSize)).getText().toString();
			if (!value.isEmpty()) {
				record.hipSize = Double.parseDouble(value);	
			}
			
			record.lunaria = ((CheckBox)findViewById(R.id.checkBox1)).isChecked();
			record.timeStamp = (Calendar)m_date.clone();
			
			DbHelper db = new DbHelper(this);
			db.addRecord(1, record);
			db.close();
		} else if (viewId == R.id.Time) {
			showDialog(1);
		} else if (viewId == R.id.Date) {
			showDialog(2);
		}
	}
    
	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                return new TimePickerDialog(this,
                        mTimeSetListener,
                        m_date.get(Calendar.HOUR_OF_DAY), 
                        m_date.get(Calendar.MINUTE), true);
            case 2:
                return new DatePickerDialog(this,
                            mDateSetListener,
                            m_date.get(Calendar.YEAR),
                            m_date.get(Calendar.MONTH),
                            m_date.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
        new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear,
                    int dayOfMonth) {
            	
            	m_date.set(Calendar.YEAR, year);
            	m_date.set(Calendar.MONTH, monthOfYear);
            	m_date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            	
            	updateTimeDisplay();
            }
        };

    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
        new TimePickerDialog.OnTimeSetListener() {

            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            	m_date.set(Calendar.HOUR_OF_DAY, hourOfDay);
            	m_date.set(Calendar.MINUTE, minute);            	
            	
            	updateTimeDisplay();
            }
        };
}
