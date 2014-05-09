package com.ensaitechnomobile.meteolocale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ensaitechnomobile.metier.CityNotFoundException;
import com.ensaitechnomobile.metier.EtatMeteo;
import com.ensaitechnomobile.metier.Localite;
import com.ensaitechnomobile.metier.TypeMeteo;

/**
 * Cette classe renverra un ou plusieurs objets EtatMeteo � partir d'un JSON
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
	 * Initialisateur � partir du JSON de la m�t�o du jour m�me
	 * 
	 * @param json
	 * @throws JSONException
	 *             Si l'objet JSON est invalide
	 * 
	 * @return un objet EtatM�t�o
	 * @throws CityNotFoundException
	 *             Si la ville saisie est incorrecte
	 */
	EtatMeteo construireEtatMeteoActuel(JSONObject json) throws JSONException,
			CityNotFoundException {

		// D�clarations
		JSONArray tableWeather;
		JSONObject objWeather, objMain, objWind, objClouds, objRain;
		int idMeteo, switchMeteo;
		TypeMeteo tm;
		double tempMin, tempMax, windSpeed, clouds, rain1, rain3, longLocalite, latLocalite;
		String nomLocalite;

		// R�cup�rations JSON
		try {
			if (json.getInt("cod") == 404) {
				// La ville n'a pas �t� trouv�e
				throw new CityNotFoundException("La ville n'a pas �t� trouv�e");
			}
		} catch (JSONException e) {

		}
		tableWeather = json.getJSONArray("weather");
		objWeather = tableWeather.getJSONObject(0);
		idMeteo = objWeather.getInt("id");
		objMain = json.getJSONObject("main");
		tempMin = objMain.getDouble("temp_min");
		tempMax = objMain.getDouble("temp_max");
		objWind = json.getJSONObject("wind");
		windSpeed = objWind.getDouble("speed");
		objClouds = json.getJSONObject("clouds");
		clouds = objClouds.getDouble("all");
		try {
			// Si il n'y a pas de pluie, il n'y a pas d'objet "rain"
			// Il faudrait en th�orie faire selon les valeurs de l'objet
			// objWeather
			objRain = json.getJSONObject("rain");
			rain3 = objRain.getDouble("3h");

		} catch (JSONException e) {
			rain3 = 0.0;
		}
		try {
			objRain = json.getJSONObject("rain");
			rain1 = objRain.getDouble("1h");
		} catch (JSONException e) {
			rain1 = 0.0;
		}

		longLocalite = json.getJSONObject("coord").getDouble("lon");
		latLocalite = json.getJSONObject("coord").getDouble("lat");
		nomLocalite = json.getString("name");

		switchMeteo = Integer.parseInt(("" + idMeteo).substring(0, 1));
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

		return new EtatMeteo(tm, windSpeed, clouds, tempMin, tempMax, rain1,
				rain3, new Localite(nomLocalite, longLocalite, latLocalite));

	}
}
