package org.xophiix.bodydatarecorder.model;

import java.util.Date;

import org.xophiix.bodydatarecorder.R;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;

public class DbHelper {	
	private static final String LOG_TAG = "BodyDB";
	private static final String DATABASE_NAME = "body.db";

	private static final int DB_VERSION = 0;    

	SQLiteDatabase m_db;
	Context m_context;

	public DbHelper(Context context) {
		m_context = context;
		m_db = context.openOrCreateDatabase(DATABASE_NAME, 0, null); 
		Log.v(LOG_TAG,"db path=" + m_db.getPath());
	}

	public void checkDBVersion() {

	}

	public int getCurrentUser() {
		return 0;
	}

	public void close() {
		m_db.close();
	}

	public void createNewDB() {
		try {
			String col[] = {"type", "name"};
			Cursor c = m_db.query("sqlite_master", col, "name='body_record_config'", null, null, null, null);	    	
			if (c.getCount() == 0) {
				createTables();
			}	    	
		} catch (Exception e) {
			Log.v(LOG_TAG, e.getMessage());
		}
	}

	void createTables() {
		try {
			Log.v(LOG_TAG, "Creating body_record_config Table");
			m_db.execSQL("CREATE TABLE body_record_config ("
					+ "_id INTEGER primary key,"
					+ "current_user INTEGER default 1,"
					+ "version INTEGER  default 0"
					+ ");");

			Log.v(LOG_TAG, "Creating records Table");
			m_db.execSQL("CREATE TABLE records ("
					+ "_id INTEGER primary key autoincrement,"
					+ "uid integer,"
					+ "weight REAL,"
					+ "breastSize REAL,"
					+ "waistSize REAL,"
					+ "hipSize REAL,"
					+ "lunaria INTEGER,"
					+ "date TEXT,"
					+ "time TEXT"
					+ ");");                                                                                                                                                                                                                                                                

			Log.v(LOG_TAG, "Creating users Table");
			m_db.execSQL("CREATE TABLE users ("
					+ "_id INTEGER primary key autoincrement,"
					+ "name TEXT"	                               
					+ ");");

			m_db.execSQL("insert into users values (null,'amy')");	      	       
		} catch(Exception e) {
			Log.v(LOG_TAG,"Create Table err" + e.getMessage());
		}
	}

	public void addRecord(int userId, BodyDataRecord record) {

		CharSequence dateText = DateFormat.format("yyyy/MM/dd", record.timeStamp);
		CharSequence timeText = DateFormat.format("kk:mm", record.timeStamp);

		String sql="insert into records values(null,"+userId+","
				+ record.weight+","
				+ record.breastSize+","
				+ record.waistSize+","
				+ record.hipSize+","
				+ (record.lunaria ? 1 : 0) + ",'"
				+ dateText +"','"
				+ timeText +"'"
				+")";

		m_db.execSQL(sql);	                
	}

	public void modifyRecord(int userId, BodyDataRecord modifiedRecord) {

	}

	public void deleteRecord(int recordId) {

	}

	public Cursor getRecordsByDate(int userId, int year, int month, int day) {
		return m_db.query("records", null, "uid=" + userId, null, null, null, "_id");
	}
	
	public Cursor getRecordsByDateRange(int userId, Date startDate, Date endDate) {
		return m_db.query("records", null, "uid=" + userId, null, null, null, "_id");
	}

	public int addUser(String name) {
		int newUserId = 0;
		return newUserId;
	}

	public void deleteUser(int userId) {

	}

	public boolean changeUser(int userId) {
		return true;
	}
}
