package domain;

import domain.base.AbstractPerson;

/**
 * Reprezentuje osobę wypożyczającą książki (czytelnika).
 * Zawiera dane kontaktowe (adres) oraz numer karty bibliotecznej.
 */
public class Borrower extends AbstractPerson {
    /**
     * Miasto zamieszkania wypożyczającego.
     */
    private String addressCity;

    /**
     * Ulica zamieszkania wypożyczającego.
     */
    private String addressStreet;

    /**
     * Numer domu/mieszkania (może być {@link Byte} w projekcie).
     * Może być {@code null}, jeśli niepodany.
     */
    private Byte addressNumber;

    /**
     * Kod pocztowy zamieszkania.
     */
    private String addressZip;

    /**
     * Numer karty bibliotecznej (lub numer legitymacji).
     */
    private int cardNumber;

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

    public Byte getAddressNumber() {
        return addressNumber;
    }

    public void setAddressNumber(Byte addressNumber) {
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
}
