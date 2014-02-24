package org.xophiix.bodydatarecorder;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.xophiix.bodydatarecorder.model.DbHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class RecordListActivity extends Activity
	implements OnClickListener, OnItemLongClickListener, OnItemClickListener {
	DbHelper m_db;
	Calendar m_startDate, m_endDate;
	SimpleCursorAdapter m_recordListAdapter;
	int m_userId;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_list);
        
        m_userId = getIntent().getIntExtra("userId", 1);
        
        int[] buttonIds = {
    		R.id.start_date,
    		R.id.end_date
        };
            
        for (int i = 0; i < buttonIds.length; ++i) {
        	findViewById(buttonIds[i]).setOnClickListener(this);        	
        }
        
        ListView recordList = (ListView) findViewById(R.id.record_list);
        recordList.setOnItemLongClickListener(this);
        recordList.setOnItemClickListener(this);
        
        m_db = new DbHelper(this);
        m_startDate = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));               
        m_endDate = (Calendar)m_startDate.clone();
        
        // default to current month
        m_startDate.set(Calendar.DAY_OF_MONTH, 1);
        m_startDate.set(Calendar.HOUR_OF_DAY, 0);
        m_startDate.set(Calendar.MINUTE, 0);
        m_startDate.set(Calendar.SECOND, 0);
        
        m_endDate.set(Calendar.DAY_OF_MONTH, m_endDate.getActualMaximum(Calendar.DAY_OF_MONTH));
        m_endDate.set(Calendar.HOUR_OF_DAY, 23);
        m_endDate.set(Calendar.MINUTE, 59);
        m_endDate.set(Calendar.SECOND, 59);
        
        fillData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 1, 0, "查看图表");		
		return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			Intent intent = new Intent();
			intent.setClass(RecordListActivity.this, RecordGraphView.class);
			startActivity(intent);
			return true;
		}
		
		return false;
    }
    
    public void onClick(View v) {
    	int viewId = v.getId();
    	
		if (viewId == R.id.start_date) {
			showDatePickDialog(true);
		} else if (viewId == R.id.end_date) {
			showDatePickDialog(false);
		}
    }    
    
    public void onItemClick(AdapterView<?> parent, View view,
			int position, long id) {
    	int recordId = (int)id;
        Intent intent = new Intent(this, EditRecordActivity.class);
        intent.putExtra("userId", m_userId);
        intent.putExtra("recordId", recordId);
        startActivity(intent);
    }
    
    public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		final int recordId = (int)id;
		new AlertDialog.Builder(this).setTitle("提示").setMessage(
				"确定删除该记录?").setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						 m_db.deleteRecord(recordId);
						 fillData();
					}
				}).setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				}).show();

		return true;
	}
    
	private void showDatePickDialog(boolean startDate) {
		final Calendar date = startDate ? m_startDate : m_endDate;
		DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				
				date.set(Calendar.YEAR, year);
				date.set(Calendar.MONTH, monthOfYear);
				date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			}
		};
		
		final DatePickerDialog dlg = new DatePickerDialog(this,
				dateSetListener, 
				date.get(Calendar.YEAR),
				date.get(Calendar.MONTH),
				date.get(Calendar.DAY_OF_MONTH));

		dlg.show();
	}
	
	private void fillData() {
		Cursor cursor = m_db.getRecordsByDateRange(m_userId, m_startDate.getTime(), m_endDate.getTime());		
		
		if (null == m_recordListAdapter) {
			m_recordListAdapter = new SimpleCursorAdapter(this,
					R.layout.record_list_item,
					cursor, 
					new String[] { "_id", "timeStamp", "weight", "lunaria"},
					new int[] {
					R.id.record_list_itemid,
					R.id.record_list_item_date,
					R.id.record_list_item_weight, 
					R.id.record_list_item_lunaria }
			);
			
			m_recordListAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
				public boolean setViewValue(View view, Cursor cursor,
						int columnIndex) {
					if (columnIndex == 7) {
						Date date = new Date(1000L * cursor.getInt(columnIndex));
						
						String dateText = DateFormat.getDateFormat(RecordListActivity.this).format(date);
						String timeText = DateFormat.getTimeFormat(RecordListActivity.this).format(date);
						((TextView) view).setText(dateText + " " + timeText);													
					} else if (columnIndex == 2) {
						double weight = cursor.getDouble(columnIndex);
						((TextView) view).setText(String.format("%.2fkg", weight));
					} else if (columnIndex == 6) {
						int lunaria = cursor.getInt(columnIndex);
						((TextView) view).setText(lunaria > 0 ? "是" : "");
					} else {
						view.setVisibility(View.GONE);
					}
					
					return true;
				}
			});
			
			ListView recordList = (ListView) findViewById(R.id.record_list);
			recordList.setAdapter(m_recordListAdapter);
		} else {
			m_recordListAdapter.changeCursor(cursor);
			m_recordListAdapter.notifyDataSetChanged();
		}
	}
}
