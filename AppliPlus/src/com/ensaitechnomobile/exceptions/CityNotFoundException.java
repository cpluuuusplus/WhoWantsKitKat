package com.ensaitechnomobile.exceptions;

public class CityNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Parameterless Constructor
	public CityNotFoundException() {
	}

	// Constructor that accepts a message
	public CityNotFoundException(String message) {
		super(message);
	}

}
