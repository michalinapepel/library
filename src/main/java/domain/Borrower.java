package domain;

import domain.base.AbstractPerson;

public class Borrower extends AbstractPerson {

    private Integer addressNumber;
    private String addressZip;
    private int cardNumber;
    private String addressCity;
    private String addressStreet;

    public Borrower() {}

    public Borrower(String firstName, String lastName, String addressCity, String addressStreet,
                    Integer addressNumber, String addressZip, Integer cardNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.addressCity = addressCity;
        this.addressStreet = addressStreet;
        this.addressNumber = addressNumber;
        this.addressZip = addressZip;
        this.cardNumber = cardNumber;
    }

    public Borrower(Integer id, String firstName, String lastName, String addressCity, String addressStreet,
                    Integer addressNumber, String addressZip, Integer cardNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.addressCity = addressCity;
        this.addressStreet = addressStreet;
        this.addressNumber = addressNumber;
        this.addressZip = addressZip;
        this.cardNumber = cardNumber;
    }

    public String getAddressCity() { return addressCity; }
    public void setAddressCity(String addressCity) { this.addressCity = addressCity; }

    public String getAddressStreet() { return addressStreet; }
    public void setAddressStreet(String addressStreet) { this.addressStreet = addressStreet; }

    public Integer getAddressNumber() { return addressNumber; }
    public void setAddressNumber(Integer addressNumber) { this.addressNumber = addressNumber; }

    public String getAddressZip() { return addressZip; }
    public void setAddressZip(String addressZip) { this.addressZip = addressZip; }

    public int getCardNumber() { return cardNumber; }
    public void setCardNumber(int cardNumber) { this.cardNumber = cardNumber; }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
