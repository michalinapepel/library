package domain;

import domain.base.AbstractPerson;

/**
 * Reprezentuje autora książek.
 * Dziedziczy po {@code AbstractPerson} i zawiera dodatkowe informacje o autorze.
 */
public class Author extends AbstractPerson {
    /**
     * Pseudonim autora.
     */
    private String pseudonym = "";
    /**
     * Narodowość autora.
     */
    private String nationality = "";

    public String getPseudonym() {
        return pseudonym;
    }

    public void setPseudonym(String pseudonym) {
        this.pseudonym = pseudonym;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
