package org.xophiix.bodydatarecorder;

import java.util.Calendar;
import java.util.TimeZone;

import org.xophiix.bodydatarecorder.model.DbHelper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class RecordListActivity extends Activity implements OnClickListener, OnItemLongClickListener {
	DbHelper m_db;
	Calendar m_startDate, m_endDate;
	SimpleCursorAdapter m_recordListAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_list);
        
        int[] buttonIds = {
    		R.id.start_date,
    		R.id.end_date
        };
            
        for (int i = 0; i < buttonIds.length; ++i) {
        	findViewById(buttonIds[i]).setOnClickListener(this);        	
        }
        
        ListView recordList = (ListView) findViewById(R.id.record_list);
        recordList.setOnItemLongClickListener(this);
        
        m_db = new DbHelper(this);
        m_startDate = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
        m_endDate = (Calendar)m_startDate.clone();
		
		Cursor cur = m_db.getRecordsByDateRange(1, m_startDate.getTime(), m_endDate.getTime());
		String[] from = new String[] {"date", "weight", "lunaria"};
		int[] to = new int[] { R.id.record_list_header_date, R.id.record_list_weight, R.id.record_list_header_lunaria};
		m_recordListAdapter = new SimpleCursorAdapter(this, R.layout.record_list_item, cur, from, to);
		recordList.setAdapter(m_recordListAdapter);
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
			showDialog("请选择年月:", "");
		} else if (viewId == R.id.end_date) {
			showDialog("请选择年月:", "");
		}
    }
    
    public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// log.e("calllogactivity", view.tostring() + "position=" + position);
		// charsequence number = ((textview) view).gettext();
		// toast t = toast.maketext(this, number + " is long clicked",
		// toast.length_long);
		// t.show();
		
		/*_id=(int)id;
		new AlertDialog.Builder(this).setTitle("提示").setMessage(
				"确定删除该明细?").setIcon(R.drawable.quit).setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
                        //Log.v("",""+_id);
						 billdb.delBills(_id);
						 mAdapter.changeCursor(cur);
						 ((SimpleCursorAdapter) mAdapter).notifyDataSetChanged();
						// finish();
					}
				}).setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				}).show();*/

		return true;

	}
    
	private void showDialog(String title, String text) {
		/*final DatePickerDialog dia = new DatePickerDialog(this,
				mDateSetListener, mYear, mMonth-1, mDay);

		dia.show();*/
	}

	/*private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear+1;
			mDay = dayOfMonth;
			today = mYear + "-" + mMonth;

			setTitle("ColaBox-账单明细(" + today + ")");
			cur = billdb.getBills(today);
			mAdapter.changeCursor(cur);
			//lv.setAdapter(mAdapter);
			((SimpleCursorAdapter) mAdapter).notifyDataSetChanged();
		}
	};*/
}
