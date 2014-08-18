package com.alex.dota2;


import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.view.View;
import android.view.View.OnClickListener;

import com.alex.listener.*;
import com.alex.data.DotADB;

public class MainActivity extends ActionBarActivity {

	private int status = 0;


	private ProgressBar bar;
	private DotADB db;
	private Button initBt;
	private EditText accountText;
	private ActionBar actionBar;
	private int mod;
	private MainTabListener mainTabListener;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 911) {
				status = ((Integer)msg.obj).intValue();	
				bar.setProgress(status);
			}
		}
	};



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		bar = (ProgressBar) findViewById(R.id.initDB);
		initBt = (Button) findViewById(R.id.commitBt);
		accountText = (EditText) findViewById(R.id.userId);
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mainTabListener = new MainTabListener();
		actionBar.addTab(actionBar.newTab().setText("英雄").setTabListener(mainTabListener));
		actionBar.addTab(actionBar.newTab().setText("比赛").setTabListener(mainTabListener));
		actionBar.addTab(actionBar.newTab().setText("记录").setTabListener(mainTabListener));
		actionBar.addTab(actionBar.newTab().setText("资料").setTabListener(mainTabListener));
 
		db = new DotADB(this, "dota", 1,this);

		initBt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				db.setAccount_id(accountText.getText().toString());
				new Thread(db).start();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

}
