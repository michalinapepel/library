package domain;

import app.Localization;
import domain.base.AbstractLocation;

/**
 * Reprezentuje dział w bibliotece (np. literatura piękna, naukowa).
 * Nazwa działu jest przechowywana jako klucz lokalizacyjny i pobierana
 * przez klasę {@code Localization} zgodnie z aktualnym językiem.
 */
public class Section extends AbstractLocation {
    /**
     * Klucz służący do pobrania nazwy działu z zasobów lokalizacyjnych.
     * Nazwa jest zależna od aktualnego języka i pobierana przez klasę {@code Localization}.
     */
    private String key; //nazwa zgodnie z językiem przekazanym przez obiekt Localization

    public Section(int id, String key) {
        this.id = id;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        try {
            // Try to get localized name using the key
            return Localization.get(key);
        } catch (Exception ex) {
            // If key is not a localization key, return it as-is (it's a display name)
            return key;
        }
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        
    }

    @Override
    public String toString() {
        return getName(); // dzięki temu JComboBox pokaże nazwę w aktualnym języku
    }
}

