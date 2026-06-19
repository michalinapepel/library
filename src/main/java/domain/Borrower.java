package domain;

import domain.base.AbstractPerson;

/**
 * Reprezentuje osobę wypożyczającą książki (czytelnika).
 * Zawiera dane kontaktowe (adres) oraz numer karty bibliotecznej.
 */
public class Borrower extends AbstractPerson {
  /**
     * Numer domu/mieszkania (może być {@link Byte} w projekcie).
     * Może być {@code null}, jeśli niepodany.
     */
    private Integer addressNumber;

    /**
     * Kod pocztowy zamieszkania.
     */
    private String addressZip;

    /**
     * Numer karty bibliotecznej (lub numer legitymacji).
     */
    private int cardNumber;
  
    /**
     * Miasto zamieszkania wypożyczającego.
     */
    private String addressCity;

    /**
     * Ulica zamieszkania wypożyczającego.
     */
    private String addressStreet;
    
    
    public Borrower() {
	};
	
	public Borrower(String first_name, String last_name, String addresscity, String addressstreet, Integer addressnumber, String addresszip, Integer card_number) {
		this.firstName = first_name;
		this.lastName = last_name;
		this.addressCity = addresscity;
		this.addressStreet = addressstreet;
		this.addressNumber = addressnumber;
		this.addressZip = addresszip;
		this.cardNumber = card_number;
	}

	public Borrower(Integer id, String first_name, String last_name, String addresscity, String addressstreet, Integer addressnumber, String addresszip, Integer card_number) {
		this.id = id;
		this.firstName = first_name;
		this.lastName = last_name;
		this.addressCity = addresscity;
		this.addressStreet = addressstreet;
		this.addressNumber = addressnumber;
		this.addressZip = addresszip;
		this.cardNumber = card_number;
	}
	
    
    public String getAddressCity() {
		return addressCity;
	}
	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}
	public String getAddressStreet() {
		return addressStreet;
	}
	public void setAddressStreet(String addressStreet) {
		this.addressStreet = addressStreet;
	}
	public Integer getAddressNumber() {
		return addressNumber;
	}
	public void setAddressNumber(Integer addressNumber) {
		this.addressNumber = addressNumber;
	}
	public String getAddressZip() {
		return addressZip;
	}
	public void setAddressZip(String addressZip) {
		this.addressZip = addressZip;
	}
	public int getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(int cardNumber) {
		this.cardNumber = cardNumber;
	}

	@Override
	public String toString() {
		return firstName + " " + lastName;
	}
}
