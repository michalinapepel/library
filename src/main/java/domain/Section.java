package domain;

import app.Localization;

//działy w bibliotece
public class Section{
    private final int id;
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

