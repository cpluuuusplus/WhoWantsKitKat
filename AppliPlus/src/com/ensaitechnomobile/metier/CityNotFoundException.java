package com.ensaitechnomobile.metier;

public class CityNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Attributs
	static final String MESSAGE = "La ville n'a pas été trouvée";
    //Parameterless Constructor
    public CityNotFoundException() {}

    //Constructor that accepts a message
    public CityNotFoundException(String message)
    {
       super(message);
    }
	

}
