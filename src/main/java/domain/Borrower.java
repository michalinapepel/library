package domain;

import domain.base.AbstractPerson;

/**
 * Reprezentuje czytelnika (osobę wypożyczającą książki) w systemie bibliotecznym.
 * <p>
 * Klasa rozszerza {@link AbstractPerson}, dziedzicząc podstawowe dane osobowe,
 * oraz dodaje dane adresowe oraz numer karty bibliotecznej.
 */
public class Borrower extends AbstractPerson {

    /**
     * Numer budynku w adresie czytelnika
     */
    private Integer addressNumber;

    /**
     * Kod pocztowy w adresie czytelnika
     */
    private String addressZip;

    /**
     * Numer karty bibliotecznej czytelnika
     */
    private int cardNumber;

    /**
     * Miejscowość w adresie czytelnika
     */
    private String addressCity;

    /**
     * Ulica w adresie czytelnika
     */
    private String addressStreet;

    /**
     * Tworzy nowy, pusty obiekt czytelnika.
     */
    public Borrower() {}

    /**
     * Tworzy nowy obiekt czytelnika bez identyfikatora (np. przed zapisem do bazy).
     *
     * @param firstName     imię czytelnika
     * @param lastName      nazwisko czytelnika
     * @param addressCity   miejscowość
     * @param addressStreet ulica
     * @param addressNumber numer budynku
     * @param addressZip    kod pocztowy
     * @param cardNumber    numer karty bibliotecznej
     */
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

    /**
     * Tworzy nowy obiekt czytelnika z podanym identyfikatorem.
     *
     * @param id            unikalny identyfikator czytelnika
     * @param firstName     imię czytelnika
     * @param lastName      nazwisko czytelnika
     * @param addressCity   miejscowość
     * @param addressStreet ulica
     * @param addressNumber numer budynku
     * @param addressZip    kod pocztowy
     * @param cardNumber    numer karty bibliotecznej
     */
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

    /**
     * Zwraca miejscowość z adresu czytelnika.
     *
     * @return miejscowość
     */
    public String getAddressCity() { return addressCity; }

    /**
     * Ustawia miejscowość w adresie czytelnika.
     *
     * @param addressCity miejscowość
     */
    public void setAddressCity(String addressCity) { this.addressCity = addressCity; }

    /**
     * Zwraca ulicę z adresu czytelnika.
     *
     * @return ulica
     */
    public String getAddressStreet() { return addressStreet; }

    /**
     * Ustawia ulicę w adresie czytelnika.
     *
     * @param addressStreet ulica
     */
    public void setAddressStreet(String addressStreet) { this.addressStreet = addressStreet; }

    /**
     * Zwraca numer budynku z adresu czytelnika.
     *
     * @return numer budynku
     */
    public Integer getAddressNumber() { return addressNumber; }

    /**
     * Ustawia numer budynku w adresie czytelnika.
     *
     * @param addressNumber numer budynku
     */
    public void setAddressNumber(Integer addressNumber) { this.addressNumber = addressNumber; }

    /**
     * Zwraca kod pocztowy z adresu czytelnika.
     *
     * @return kod pocztowy
     */
    public String getAddressZip() { return addressZip; }

    /**
     * Ustawia kod pocztowy w adresie czytelnika.
     *
     * @param addressZip kod pocztowy
     */
    public void setAddressZip(String addressZip) { this.addressZip = addressZip; }

    /**
     * Zwraca numer karty bibliotecznej czytelnika.
     *
     * @return numer karty bibliotecznej
     */
    public int getCardNumber() { return cardNumber; }

    /**
     * Ustawia numer karty bibliotecznej czytelnika.
     *
     * @param cardNumber numer karty bibliotecznej
     */
    public void setCardNumber(int cardNumber) { this.cardNumber = cardNumber; }

    /**
     * Zwraca tekstową reprezentację czytelnika w formacie „imię nazwisko”.
     *
     * @return imię i nazwisko czytelnika oddzielone spacją
     */
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
