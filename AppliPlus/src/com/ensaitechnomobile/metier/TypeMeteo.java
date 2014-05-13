package com.ensaitechnomobile.metier;

public enum TypeMeteo {
	ORAGE("il fait orage !"),
	CRACHATS("il pleut un peu."),
	PLUIE("il pleut."),
	NEIGE("il neige !"),
	BROUILLARD("on voit rien."),
	NUAGES("le ciel est nuageux."),
	EXTREME("le ciel nous tombe sur la t�te !"),
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
