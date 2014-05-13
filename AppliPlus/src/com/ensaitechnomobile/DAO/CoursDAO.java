package com.ensaitechnomobile.DAO;

import java.util.ArrayList;
import java.util.List;

import com.ensaitechnomobile.SQL.MyOpenHelper;
import com.ensaitechnomobile.metier.Cours;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoursDAO {

	public static final String TAG = "TP3";

	public CoursDAO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void insertAll(SQLiteDatabase db, List<Cours> ls) {
		ContentValues values = new ContentValues();
		for (Cours cours : ls) {
			values.put("nom", cours.getNom());
			values.put("salle", cours.getSalle());
			values.put("uid", cours.getUid());
			values.put("debut", cours.getDebut());
			values.put("fin", cours.getFin());
			db.replace(MyOpenHelper.NOM_TABLE, null, values);
		}
	}

	public ArrayList<Cours> getAll(SQLiteDatabase db) {
		ArrayList<Cours> res = new ArrayList<Cours>();

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
			res.add(new Cours(debut, fin, nom, salle, uid));
		}
		curs.close();
		return res;
	}

	public List<Cours> get(SQLiteDatabase db, String cs) {
		List<Cours> res = new ArrayList<Cours>();

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
			res.add(new Cours(debut, fin, nom, salle, uid));
		}
		curs.close();
		return res;
	}

	public void removeAll(SQLiteDatabase db) {
		String query = "delete from " + MyOpenHelper.NOM_TABLE;
		db.execSQL(query);

	}
}
