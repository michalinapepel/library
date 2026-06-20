package domain;

import domain.base.AbstractLocation;

/**
 * Reprezentuje półkę znajdującą się w regale.
 * <p>
 * Półka jest konkretną lokalizacją, w której przechowywane są książki.
 * Klasa rozszerza {@link AbstractLocation}, dziedzicząc identyfikator, nazwę
 * oraz identyfikator regału, i dodaje nazwę regału na potrzeby prezentacji.
 */
public class Shelf extends AbstractLocation {

    /**
     * Nazwa regału, do którego należy półka
     */
    private String bookcaseName;

    /**
     * Tworzy nowy, pusty obiekt półki.
     */
    public Shelf() {}

    /**
     * Tworzy nowy obiekt półki z podanymi danymi.
     *
     * @param id         unikalny identyfikator półki
     * @param bookcaseId identyfikator regału, do którego należy półka
     * @param name       nazwa półki
     */
    public Shelf(Integer id, Integer bookcaseId, String name) {
        super.init(id, bookcaseId, name);
    }

    /**
     * Zwraca nazwę regału, do którego należy półka.
     *
     * @return nazwa regału
     */
    public String getBookcaseName() { return bookcaseName; }

    /**
     * Ustawia nazwę regału, do którego należy półka.
     *
     * @param name nazwa regału
     */
    public void setBookcaseName(String name) { this.bookcaseName = name; }

    /**
     * Zwraca tekstową reprezentację półki, czyli jej nazwę.
     *
     * @return nazwa półki
     */
    @Override
    public String toString() { return getName(); }
}
