package domain;

import app.Localization;

//działy w bibliotece
public class Section{
    private int id;
    private String key; //nazwa zgodnie z językiem przekazanym przez obiekt Localization

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

