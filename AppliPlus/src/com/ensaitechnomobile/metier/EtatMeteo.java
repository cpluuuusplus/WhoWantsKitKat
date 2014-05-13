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
	int tempMin = -1;
	int tempMax = -1;
	double clouds = -1;
	double rain1 = -1;
	double rain3;
	String country;
	long sunrise, sunset, pressure;

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
	public EtatMeteo(TypeMeteo tm, double wS, double c, int tMin, int tMax,
			double r1, double r3, Localite loc) {
		typeMet = tm;
		windSpeed = wS;
		clouds = c;
		tempMin = tMin;
		tempMax = tMax;
		rain1 = r1;
		rain3 = r3;
		this.loc = loc;
	}

	public EtatMeteo(TypeMeteo typeMet, double windSpeed, int tempMin,
			int tempMax, double clouds, double rain1, double rain3,
			String country, long sunrise, long sunset, long pressure,
			Localite loc) {
		super();
		this.typeMet = typeMet;
		this.windSpeed = windSpeed;
		this.tempMin = tempMin;
		this.tempMax = tempMax;
		this.clouds = clouds;
		this.rain1 = rain1;
		this.rain3 = rain3;
		this.country = country;
		this.sunrise = sunrise * 1000;
		this.sunset = sunset * 1000;
		this.pressure = pressure;
		this.loc = loc;
	}

	public TypeMeteo getTypeMet() {
		return typeMet;
	}

	public void setTypeMet(TypeMeteo typeMet) {
		this.typeMet = typeMet;
	}

	public long getPressure() {
		return pressure;
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

	public int getTempMin() {
		return tempMin;
	}

	public void setTempMin(int tempMin) {
		this.tempMin = tempMin;
	}

	public int getTempMax() {
		return tempMax;
	}

	public void setTempMax(int tempMax) {
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

	public String getCountry() {
		return country;
	}

	public long getSunrise() {
		return sunrise;
	}

	public long getSunset() {
		return sunset;
	}

	// methodes
	@Override
	public String toString() {
		String res = typeMet + "\n Tmin" + tempMin + "\n Tmax" + tempMax
				+ "\n Nuages" + clouds + "\n Pluie 1H" + rain1 + "\n Pluie 3H"
				+ rain3 + "\n" + windSpeed + "\n" + loc.toString();
		return res;

	}

}
