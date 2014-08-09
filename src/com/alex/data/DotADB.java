package com.alex.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.dom4j.*;

import com.alex.dota2.MainActivity;

public class DotADB extends SQLiteOpenHelper implements Runnable{

	private MainActivity activity;
	private String account_id;


	public DotADB(Context context, String dbName, int version,MainActivity activity){
		super(context, dbName, null, version);
		this.activity = activity;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
    
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	public String getAccount_id() {
		return account_id;
	}

	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}

}
