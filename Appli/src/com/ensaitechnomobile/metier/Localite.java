package com.ensaitechnomobile.metier;

/**
 * 
 * Cette classe regroupera les informations sur une localité
 * 
 * @author nicolas
 * 
 */
public class Localite {
	// attributs
	String ville = "Timbuktu";
	double latitude = 0.0;
	double longitude = 0.0;

	// constructeurs

	/**
	 * Constructeur à partir de ville TODO va chercher les lat.long de la ville
	 * par JSON
	 * 
	 * @param villeLocalite
	 */
	public Localite(String villeLocalite) {
		ville = villeLocalite;

	}

	/**
	 * Constructeur à partir de lat/long TODO va chercher la ville par JSON
	 * 
	 * @param latit
	 *            la latitude
	 * @param longit
	 *            la longitude
	 */
	public Localite(double longit, double latit) {
		latitude = latit;
		longitude = longit;
		// TODO use :
		// http://open.mapquestapi.com/nominatim/v1/search?q=48.04,-1.79&format=json
		// to get ville or something like it
		/*
		 * [{"place_id":"38395620","licence":"Data \u00a9 OpenStreetMap
		 * contributors, ODbL 1.0.
		 * http:\/\/www.openstreetmap.org\/copyright","osm_type
		 * ":"way","osm_id":"
		 * 26379829","boundingbox":["48.0376069","48.0419978","
		 * -1.7973583","-1.7929719
		 * "],"lat":"48.03980235","lon":"-1.79710009847924
		 * ","display_name":"\u00c9tang de la Sillandais, D 36, Goven, Redon,
		 * Ille-et-Vilaine, Brittany, Metropolitan
		 * France","class":"natural","type":"water","importance":0.001}] TODO
		 * écrire une fonction : JSONObject getJSONObject(String URL) dans un
		 * runnable avec des asynctasks ou des handlers et des messages
		 */

	}
	
	public Localite(String villeLocalite, double longit, double latit){
		this.setVille(villeLocalite);
		this.setLatitude(latit);
		this.setLongitude(longit);
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

	// methodes
	@Override
	public String toString() {
		return "La ville de " + ville + " se situe ici : " + latitude
				+ "E/W & " + longitude + "N/S";
	}

}
