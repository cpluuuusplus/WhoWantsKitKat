package com.ensaitechnomobile.metier;

public class LessonItem implements Comparable<LessonItem>, Item {

	/* Attributs */
	private long debut, fin;
	private String nom, salle, uid;

	/* Constructeur */
	public LessonItem(long debut, long fin, String nom, String salle, String uid) {
		super();
		this.debut = debut;
		this.fin = fin;
		this.nom = nom;
		this.salle = salle;
		this.uid = uid;
	}

	/* Accesseurs et mutateurs */
	public long getDebut() {
		return debut;
	}

	public long getFin() {
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

	@Override
	public int compareTo(LessonItem another) {
		return (int) (this.debut - another.debut);
	}

	@Override
	public boolean isSection() {
		return false;
	}

}
