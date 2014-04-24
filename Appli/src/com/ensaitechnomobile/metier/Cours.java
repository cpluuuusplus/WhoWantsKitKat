package com.ensaitechnomobile.metier;

import java.util.Date;

public class Cours {

	/* Attributs */
	private Date debut, fin;
	private String nom, salle, uid;

	/* Constructeur */
	public Cours(Date debut, Date fin, String nom, String salle, String uid) {
		super();
		this.debut = debut;
		this.fin = fin;
		this.nom = nom;
		this.salle = salle;
		this.uid = uid;
	}

	/* Accesseurs et mutateurs */
	public Date getDebut() {
		return debut;
	}

	public Date getFin() {
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
				+ this.salle + ", uid: " + this.uid + " fin: " + this.fin);
	}

}
