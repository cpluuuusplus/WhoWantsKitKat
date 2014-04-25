package com.ensaitechnomobile.meteolocale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Cette classe renverra un ou plusieurs objets EtatMeteo à partir d'un JSON
 * 
 * @author nicolas
 * 
 */
public class MeteoJSON {

	/**
	 * Constructeur sans rien
	 */
	MeteoJSON() {
	}

	/**
	 * 
	 * Initialisateur à partir du JSON de la météo du jour même
	 * 
	 * @param json
	 * @throws JSONException
	 *             Si l'objet n'est pas bon
	 * 
	 * @return un objet EtatmMétéo
	 */
	EtatMeteo construireEtatMeteoActuel(JSONObject json) throws JSONException {

		JSONArray tableWeather;
		JSONObject objWeather, objMain, objWind, objClouds, objRain;
		int idMeteo, switchMeteo;
		TypeMeteo tm;
		double tempMin, tempMax, pressure, windSpeed, clouds, rain;

		tableWeather = json.getJSONArray("weather");
		objWeather = tableWeather.getJSONObject(0);
		idMeteo =  objWeather.getInt("id");
		objMain = json.getJSONObject("main");
		tempMin = objMain.getDouble("temp_min");
		tempMax = objMain.getDouble("temp_max");
		pressure = objMain.getInt("pressure");
		objWind = json.getJSONObject("wind");
		windSpeed = objWind.getDouble("speed");
		objClouds = json.getJSONObject("clouds");
		clouds = objClouds.getDouble("all");
		objRain = json.getJSONObject("rain");
		rain = objRain.getDouble("3h");


		
		switchMeteo = Integer.parseInt((""+idMeteo).substring(0, 1));
		switch (switchMeteo) {
		case 2:
			tm = TypeMeteo.ORAGE;
			break;
		case 3:
			tm = TypeMeteo.CRACHATS;
			break;
		case 5:
			tm = TypeMeteo.PLUIE;
			break;
		case 6:
			tm = TypeMeteo.NEIGE;
			break;
		case 7:
			tm = TypeMeteo.BROUILLARD;
			break;
		case 8:
			tm = TypeMeteo.NUAGES;
			break;
		case 9:
			tm = TypeMeteo.EXTREME;
			break;
		default:
			tm = TypeMeteo.CAS_NON_GERE;
			break;
		}

		return new EtatMeteo(tm, windSpeed, pressure, clouds, tempMin, tempMax,
				rain, 0);

	}

}
