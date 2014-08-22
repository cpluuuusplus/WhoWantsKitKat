package com.ensaitechnomobile.DAO;

import java.util.ArrayList;
import java.util.List;

import com.ensaitechnomobile.SQL.MyOpenHelper;
import com.ensaitechnomobile.metier.LessonItem;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoursDAO {

	public static final String TAG = "TP3";

	public CoursDAO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void insertAll(SQLiteDatabase db, List<LessonItem> ls) {
		ContentValues values = new ContentValues();
		for (LessonItem cours : ls) {
			values.put("nom", cours.getNom());
			values.put("salle", cours.getSalle());
			values.put("uid", cours.getUid());
			values.put("debut", cours.getDebut());
			values.put("fin", cours.getFin());
			db.replace(MyOpenHelper.NOM_TABLE, null, values);
		}
	}

	public ArrayList<LessonItem> getAll(SQLiteDatabase db) {
		ArrayList<LessonItem> res = new ArrayList<LessonItem>();

		Cursor curs = db.rawQuery("select debut,nom,salle,uid,fin from "
				+ MyOpenHelper.NOM_TABLE, null);
		long debut;
		String nom = null;
		String salle = null;
		String uid = null;
		long fin;
		while (curs.moveToNext()) {
			debut = curs.getLong(0);
			nom = curs.getString(1);
			salle = curs.getString(2);
			uid = curs.getString(3);
			fin = curs.getLong(4);
			res.add(new LessonItem(debut, fin, nom, salle, uid));
		}
		curs.close();
		return res;
	}

	public List<LessonItem> get(SQLiteDatabase db, String cs) {
		List<LessonItem> res = new ArrayList<LessonItem>();

		Cursor curs = db.rawQuery("select debut,nom,salle,uid,fin from "
				+ MyOpenHelper.NOM_TABLE + "where" + cs, null);
		long debut;
		String nom = null;
		String salle = null;
		String uid = null;
		long fin;
		while (curs.moveToNext()) {
			debut = curs.getLong(0);
			nom = curs.getString(1);
			salle = curs.getString(2);
			uid = curs.getString(3);
			fin = curs.getLong(4);
			res.add(new LessonItem(debut, fin, nom, salle, uid));
		}
		curs.close();
		return res;
	}

	public void removeAll(SQLiteDatabase db) {
		String query = "delete from " + MyOpenHelper.NOM_TABLE;
		db.execSQL(query);

	}
}
