package domain;

import domain.base.AbstractLocation;

/**
 * Reprezentuje regał w bibliotece.
 * <p>
 * Regał jest lokalizacją najwyższego poziomu, w której znajdują się półki.
 * Klasa rozszerza {@link AbstractLocation}, dziedzicząc identyfikator i nazwę.
 */
public class Bookcase extends AbstractLocation {

    /**
     * Tworzy nowy, pusty obiekt regału.
     */
    public Bookcase() {}

    /**
     * Tworzy nowy obiekt regału z podanym identyfikatorem i nazwą.
     *
     * @param id   unikalny identyfikator regału
     * @param name nazwa regału
     */
    public Bookcase(Integer id, String name) {
        super.init(id, name);
    }

    /**
     * Zwraca tekstową reprezentację regału, czyli jego nazwę.
     *
     * @return nazwa regału
     */
    @Override
    public String toString() { return getName(); }
}
