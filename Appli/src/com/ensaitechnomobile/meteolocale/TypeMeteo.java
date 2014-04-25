package com.ensaitechnomobile.meteolocale;

public enum TypeMeteo {
	ORAGE("Orage!"),
	CRACHATS("Pluie légère"),
	PLUIE("Pluie"),
	NEIGE("Neige"),
	BROUILLARD("Brouillard"),
	NUAGES("Nuages"),
	EXTREME("Extreme"),
	CAS_NON_GERE("A mettre en place"),;
	
	private String name="";
	// Constructeur
	TypeMeteo(String name){
		this.name=name;
	}
	public String toString(){
		return name;
	}
}
