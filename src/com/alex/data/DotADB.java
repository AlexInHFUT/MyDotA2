package com.alex.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.dom4j.*;

public class DotADB extends SQLiteOpenHelper {

	public DotADB(Context context, String dbName, int version){
		super(context, dbName, null, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
    
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	public void initData(String account_id){
		
	}

}
