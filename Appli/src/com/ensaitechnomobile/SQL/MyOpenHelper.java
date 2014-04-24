package com.ensaitechnomobile.SQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper {

	public static String NOMBASE = "EMPLOI_DU_TEMPS";
	public static int VERSION = 1;

	public MyOpenHelper(Context context) {
		super(context, NOMBASE, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String creation = "create table table_cours (debut integer, nom varchar(30), salle varchar(10),uid varchar(17) PRIMARY KEY,fin integer)";
		db.execSQL(creation);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
