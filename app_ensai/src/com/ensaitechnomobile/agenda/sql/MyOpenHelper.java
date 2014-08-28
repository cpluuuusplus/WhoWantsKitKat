package com.ensaitechnomobile.agenda.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper {

	public static String NOMBASE = "EMPLOI_DU_TEMPS";
	public static String NOM_TABLE = "table_cours";

	public static int VERSION = 1;

	/**
	 * Constructeur
	 * 
	 * @param context
	 */
	public MyOpenHelper(Context context) {
		super(context, NOMBASE, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String creation = "create table "
				+ NOM_TABLE
				+ " (debut integer, nom varchar(30), salle varchar(10),uid varchar(17) PRIMARY KEY,fin integer)";
		db.execSQL(creation);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
}
