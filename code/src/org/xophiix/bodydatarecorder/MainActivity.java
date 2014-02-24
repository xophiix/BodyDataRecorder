package org.xophiix.bodydatarecorder;

import org.xophiix.bodydatarecorder.model.DbHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        DbHelper db = new DbHelper(this);
        int userId = db.getCurrentUser();
        
        Intent intent = new Intent(this, EditRecordActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
        
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
