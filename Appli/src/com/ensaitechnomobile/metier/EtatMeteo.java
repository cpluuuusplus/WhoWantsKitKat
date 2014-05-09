package com.ensaitechnomobile.metier;

import android.util.Log;

/**
 * Cette classe récupèrera le JSON de OpenWeatherMap et permettra d'afficher une
 * vue compacte ainsi qu' une vue détaillée de la météo
 * 
 * @author nicolas
 * 
 */
public class EtatMeteo {
	// attributs
	TypeMeteo typeMet = TypeMeteo.CAS_NON_GERE;
	double windSpeed = -1;
	double tempMin = -1;
	double tempMax = -1;
	double clouds = -1;
	double rain = -1;
	// Une prévision à 0 jours c'est la météo
	Localite loc = new Localite("Bruz");

	// constructeurs
	/**
	 * 
	 * @param wD
	 *            description de la météo
	 * @param wS
	 *            vitesse du vent
	 * @param p
	 *            pression atm
	 * @param c
	 *            nuages
	 * @param tMin
	 *            temp min
	 * @param tMax
	 *            temp max
	 * @param r
	 *            pluie
	 * @param prev
	 *            selon qu'on veuille construire une prevision ou pas
	 */
	public EtatMeteo(TypeMeteo tm, double wS, double c, double tMin,
			double tMax, double r, Localite loc) {
		typeMet = tm;
		windSpeed = wS;
		clouds = c;
		tempMin = tMin;
		tempMax = tMax;
		rain = r;
		this.loc = loc;
	}

	public TypeMeteo getTypeMet() {
		return typeMet;
	}

	public void setTypeMet(TypeMeteo typeMet) {
		this.typeMet = typeMet;
	}

	public Localite getLoc() {
		return loc;
	}

	public void setLoc(Localite loc) {
		this.loc = loc;
	}

	/**
	 * Constructeur à ne pas utiliser en prod
	 */
	EtatMeteo() {
		Log.w("AMS", "Le constructeur vide de EtatMétéo a été appelé");
	}

	public double getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}

	public double getTempMin() {
		return tempMin;
	}

	public void setTempMin(double tempMin) {
		this.tempMin = tempMin;
	}

	public double getTempMax() {
		return tempMax;
	}

	public void setTempMax(double tempMax) {
		this.tempMax = tempMax;
	}

	public double getClouds() {
		return clouds;
	}

	public void setClouds(double clouds) {
		this.clouds = clouds;
	}

	public double getRain() {
		return rain;
	}

	public void setRain(double rain) {
		this.rain = rain;
	}

	// methodes
	@Override
	public String toString() {
		String res = typeMet + "\n" + tempMin + "\n" + tempMax + "\n" + "\n"
				+ clouds + "\n" + rain + "\n" + windSpeed +"\n"
				+ loc.toString();
		return res;

	}

}
