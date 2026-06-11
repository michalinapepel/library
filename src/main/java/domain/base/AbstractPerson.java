package domain.base;

/**
 * Klasa abstrakcyjna reprezentująca wspólne cechy i zachowania dla wszystkich
 * typów osób w systemie (np. wypożyczających, pracowników, autorów).
 * <p>
 * Służy jako klasa bazowa i nie może być bezpośrednio instancjonowana.
 * Każda klasa dziedzicząca po {@code AbstractPerson} musi dostarczyć
 * specyficzną dla siebie logikę biznesową.
 * </p>
 * @version 1.0
 */

abstract public class AbstractPerson {
    /**
     * Imię osoby.
     */
    protected String firstName;

    /**
     * Nazwisko osoby.
     */
    protected String lastName;
}
