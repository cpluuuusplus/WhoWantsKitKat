package com.ensaitechnomobile.metier;

public class Cours {

	/* Attributs */
	private int debut, fin;
	private String nom, salle, uid;

	/* Constructeur */
	public Cours(int debut, int fin, String nom, String salle, String uid) {
		super();
		this.debut = debut;
		this.fin = fin;
		this.nom = nom;
		this.salle = salle;
		this.uid = uid;
	}

	/* Accesseurs et mutateurs */
	public int getDebut() {
		return debut;
	}

	public int getFin() {
		return fin;
	}

	public String getNom() {
		return nom;
	}

	public String getSalle() {
		return salle;
	}

	public String getUid() {
		return uid;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ("debut: " + this.debut + ", nom: " + this.nom + ", salle: "
				+ this.salle + " fin: " + this.fin);
	}

}
