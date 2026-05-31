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
    private int cardNumber; //numer karty bibliotecznej albo legimki jak on chcial
}
