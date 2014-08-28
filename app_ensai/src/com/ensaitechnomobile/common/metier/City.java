package com.ensaitechnomobile.common.metier;

import android.util.Log;

/**
 * 
 * Cette classe regroupera les informations sur une localit�
 * 
 * @author nicolas
 * 
 */
public class City {
	// attributs
	String ville = "..";
	double latitude = -100.0;
	double longitude = -100.0;

	// constructeurs

	/**
	 * Constructeur � partir de ville TODO va chercher les lat.long de la ville
	 * par JSON
	 * 
	 * @param villeLocalite
	 */
	public City(String villeLocalite) {
		ville = villeLocalite;
		Log.i("Meteo::Localite", "La localit� " + ville
				+ " vient  d'�tre construite, sans coordonn�es");

	}

	/**
	 * Constructeur � partir de lat/long
	 * 
	 * @param latit
	 *            la latitude
	 * @param longit
	 *            la longitude
	 */
	public City(double longit, double latit) {
		latitude = latit;
		longitude = longit;
		Log.i("Meteo::Localite", "La localit� situ�e en " + longitude + ", "
				+ latitude + " vient d'�tre construite sans ville associ�e");
	}

	public City(String villeLocalite, double longit, double latit) {
		this.setVille(villeLocalite);
		this.setLatitude(latit);
		this.setLongitude(longit);
		Log.i("Meteo::Localite", "La localit� " + villeLocalite + " situ�e en "
				+ longitude + ", " + latitude + " vient d'�tre construite.");

	}

	// getters
	public String getVille() {
		return ville;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	// setters
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setLongLat(double longit, double latit) {
		this.longitude = longit;
		this.latitude = latit;
	}

	public boolean hasVille() {
		return ville != "..";
	}

	public boolean hasLongLat() {
		return (latitude != -100 && longitude != -100);
	}

	// methodes
	@Override
	public String toString() {
		return "La ville de " + ville + " se situe ici : " + latitude
				+ "E/W & " + longitude + "N/S";
	}

}
