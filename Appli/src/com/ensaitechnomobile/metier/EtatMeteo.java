package com.ensaitechnomobile.metier;

import android.util.Log;

/**
 * Cette classe r�cup�rera le JSON de OpenWeatherMap et permettra d'afficher une
 * vue compacte ainsi qu' une vue d�taill�e de la m�t�o
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
	double rain1 = -1;
	double rain3;
	// Une pr�vision � 0 jours c'est la m�t�o
	Localite loc = new Localite("..");

	// constructeurs
	/**
	 * 
	 * @param wD
	 *            description de la m�t�o
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
			double tMax, double r1, double r3, Localite loc) {
		typeMet = tm;
		windSpeed = wS;
		clouds = c;
		tempMin = tMin;
		tempMax = tMax;
		rain1 = r1;
		rain3 = r3;
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
	 * Constructeur � ne pas utiliser en prod
	 */
	EtatMeteo() {
		Log.w("AMS", "Le constructeur vide de EtatM�t�o a �t� appel�");
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

	public double getRain1() {
		return rain1;
	}

	public void setRain1(double rain) {
		this.rain1 = rain;
	}
	
	public double getRain3() {
		return rain3;
	}

	public void setRain3(double rain) {
		this.rain3 = rain;
	}

	// methodes
	@Override
	public String toString() {
		String res = typeMet + "\n Tmin" + tempMin + "\n Tmax" + tempMax + "\n Nuages" 
				+ clouds + "\n Pluie 1H" + rain1 + "\n Pluie 3H"+ rain3+ "\n" + windSpeed +"\n"
				+ loc.toString();
		return res;

	}

}
