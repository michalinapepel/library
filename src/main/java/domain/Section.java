package domain;

import app.Localization;

/**
 * Reprezentuje dział w bibliotece (np. literatura piękna, naukowa).
 * Nazwa działu jest przechowywana jako klucz lokalizacyjny i pobierana
 * przez klasę {@code Localization} zgodnie z aktualnym językiem.
 */
public class Section{
    /**
     * Unikalny identyfikator działu.
     */
    private final int id;

    /**
     * Klucz służący do pobrania nazwy działu z zasobów lokalizacyjnych.
     * Nazwa jest zależna od aktualnego języka i pobierana przez klasę {@code Localization}.
     */
    private final String key; //nazwa zgodnie z językiem przekazanym przez obiekt Localization

    public Section(int id, String key) {
        this.id = id;
        this.key = key;
    }

    public int getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return Localization.get(key);
    }

    @Override
    public String toString() {
        return getName(); // dzięki temu JComboBox pokaże nazwę w aktualnym języku
    }
}

