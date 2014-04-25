package com.ensaitechnomobile.metier;

import java.util.Date;

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
	double pressure = -1;
	double clouds = -1;
	double rain = -1;
	Date jour;
	Localite loc;
	
	// Une prévision à 0 jours c'est la météo
	int prevision = 0;

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
	public EtatMeteo(TypeMeteo tm, double wS, double p, double c, double tMin,
			double tMax, double r, int prev, Localite l) {
		typeMet = tm;
		windSpeed = wS;
		pressure = p;
		clouds = c;
		tempMin=tMin;
		tempMax=tMax;
		rain = r;
		prevision = prev;
		loc=l;
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

	public double getPressure() {
		return pressure;
	}

	public void setPressure(double pressure) {
		this.pressure = pressure;
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

	public int getPrevision() {
		return prevision;
	}

	public void setPrevision(int prevision) {
		this.prevision = prevision;
	}

	// methodes
	@Override
	public String toString() {
		String res = typeMet + "\n" + tempMin + "\n" + tempMax + "\n"
				+ pressure + "\n" + clouds + "\n" + rain + "\n" + prevision
				+ "\n";
		return res;

	}

}
