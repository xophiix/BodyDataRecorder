package org.xophiix.bodydatarecorder.model;

import java.util.Date;

import org.xophiix.bodydatarecorder.R;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;

public class DbHelper {	
	private static final String LOG_TAG = "BodyDB";
	private static final String DATABASE_NAME = "body";

	private static final int DB_VERSION = 1;    

	private SQLiteOpenHelper m_sqliteHelper;
	SQLiteDatabase m_db;
	Context m_context;

	public DbHelper(Context context) {
		m_context = context;
		m_sqliteHelper = new SQLiteOpenHelper(context, DATABASE_NAME, null, DB_VERSION) {
			@Override
			public void onCreate(SQLiteDatabase db) {
				createTables(db);			    
			}

			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			}
		};
		
		m_db = m_sqliteHelper.getWritableDatabase(); 
		Log.v(LOG_TAG,"db path=" + m_db.getPath());
	}

	public void checkDBVersion() {

	}

	public int getCurrentUser() {
		return 1;
	}

	public void close() {
		m_db.close();
	}

	void createTables(SQLiteDatabase db) {
		Log.v(LOG_TAG, "Creating body_record_config Table");
		db.execSQL("CREATE TABLE body_record_config ("
				+ "_id INTEGER primary key,"
				+ "current_user INTEGER default 1,"
				+ "version INTEGER  default 0"
				+ ");");

		Log.v(LOG_TAG, "Creating records Table");
		db.execSQL("CREATE TABLE records ("
				+ "_id INTEGER primary key autoincrement,"
				+ "uid integer,"
				+ "weight REAL,"
				+ "breastSize REAL,"
				+ "waistSize REAL,"
				+ "hipSize REAL,"
				+ "lunaria INTEGER,"
				+ "timeStamp"				
				+ ");");                                                                                                                                                                                                                                                                

		db.execSQL("CREATE INDEX record_created_at ON records (timeStamp)");
		
		Log.v(LOG_TAG, "Creating users Table");
		db.execSQL("CREATE TABLE users ("
				+ "_id INTEGER primary key autoincrement,"
				+ "name TEXT"	                               
				+ ");");

		db.execSQL("insert into users values (null,'amy')");	      	       
	}

	public void addRecord(int userId, BodyDataRecord record) {
		String sql="insert into records values(null,"
				+ userId+","
				+ record.weight+","
				+ record.breastSize+","
				+ record.waistSize+","
				+ record.hipSize+","
				+ (record.lunaria ? 1 : 0) + ","
				+ (record.timeStamp.getTimeInMillis() / 1000)				
				+")";

		m_db.execSQL(sql);	                
	}

	public void modifyRecord(int userId, BodyDataRecord modifiedRecord) {

	}

	public void deleteRecord(int recordId) {
		exec("delete from records where _id=?", new Object[]{recordId});
	}
	
	public Cursor getRecordsByDateRange(int userId, Date startDate, Date endDate) {
		long startTime = startDate.getTime() / 1000;
		long endTime = endDate.getTime() / 1000;
		if (endTime < startTime) {
			endTime = startTime;
		}
		
		return query("select * from records where timeStamp >=" + startTime + " and timeStamp <=" + endTime);
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
	
    public void beginTransaction() {
    	m_db.beginTransaction();
    }

    public void endTransaction() {
    	m_db.endTransaction();
    }

    public void setTransactionSuccessful() {
    	m_db.setTransactionSuccessful();
    }

    public void exec(String query) {
    	m_db.execSQL(query);
    }

    public void exec(String query, Object[] values) {
    	m_db.execSQL(query, values);
    }

    public Cursor query(String query) {
    	return m_db.rawQuery(query, null);
    }
}
