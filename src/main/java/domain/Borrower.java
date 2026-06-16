package domain;

import domain.base.AbstractPerson;

public class Borrower extends AbstractPerson {
    private String addressCity;
    private String addressStreet;
    private Integer addressNumber;
    private Integer addressZip;
    
    private int cardNumber;
    
    
    public Borrower() {
	};
	
	public Borrower(String first_name, String last_name, String addresscity, String addressstreet, Integer addressnumber, Integer addresszip, Integer card_number) {
		this.firstName = first_name;
		this.lastName = last_name;
		this.addressCity = addresscity;
		this.addressStreet = addressstreet;
		this.addressNumber = addressnumber;
		this.addressZip = addresszip;
		this.cardNumber = card_number;
	}

	public Borrower(Integer id, String first_name, String last_name, String addresscity, String addressstreet, Integer addressnumber, Integer addresszip, Integer card_number) {
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
	public Integer getAddressZip() {
		return addressZip;
	}
	public void setAddressZip(Integer addressZip) {
		this.addressZip = addressZip;
	}
	public int getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(int cardNumber) {
		this.cardNumber = cardNumber;
	}

}
