package com.ensaitechnomobile.menuprincipal;

import android.annotation.SuppressLint;
import java.util.Locale;

public enum Services {
	PAMPLEMOUSSE,
	METEO,
	GEOLOC;
	
	 @SuppressLint("DefaultLocale")
	@Override public String toString() {
		   //only capitalize the first letter
		   String s = super.toString();
		   return s.substring(0, 1) + s.substring(1).toLowerCase(Locale.FRANCE);
	}	
}
