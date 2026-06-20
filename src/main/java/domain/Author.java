package domain;

import domain.base.AbstractPerson;

/**
 * Reprezentuje autora książki w systemie bibliotecznym.
 * <p>
 * Klasa rozszerza {@link AbstractPerson}, dziedzicząc podstawowe dane osobowe
 * (identyfikator, imię, nazwisko), oraz dodaje informacje charakterystyczne
 * dla autora, takie jak pseudonim i narodowość.
 */
public class Author extends AbstractPerson {

    /**
     * Pseudonim autora
     */
    private String pseudonym = "";

    /**
     * Narodowość autora
     */
    private String nationality = "";

    /**
     * Tworzy nowy, pusty obiekt autora.
     */
    public Author() {}

    /**
     * Tworzy nowy obiekt autora z podanymi danymi.
     *
     * @param id          unikalny identyfikator autora
     * @param firstName   imię autora
     * @param lastName    nazwisko autora
     * @param pseudonym   pseudonim autora
     * @param nationality narodowość autora
     */
    public Author(Integer id, String firstName, String lastName, String pseudonym, String nationality) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pseudonym = pseudonym;
        this.nationality = nationality;
    }

    /**
     * Zwraca pseudonim autora.
     *
     * @return pseudonim autora
     */
    public String getPseudonym() { return pseudonym; }

    /**
     * Ustawia pseudonim autora.
     *
     * @param pseudonym pseudonim autora do ustawienia
     */
    public void setPseudonym(String pseudonym) { this.pseudonym = pseudonym; }

    /**
     * Zwraca narodowość autora.
     *
     * @return narodowość autora
     */
    public String getNationality() { return nationality; }

    /**
     * Ustawia narodowość autora.
     *
     * @param nationality narodowość autora do ustawienia
     */
    public void setNationality(String nationality) { this.nationality = nationality; }

    /**
     * Zwraca tekstową reprezentację autora w formacie „imię nazwisko”.
     *
     * @return imię i nazwisko autora oddzielone spacją
     */
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
