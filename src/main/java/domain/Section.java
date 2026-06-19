package domain;

import app.Localization;
import domain.base.AbstractLocation;

/**
 * Section key is a localization key (e.g. "section.fantasy") resolved at runtime
 * to the display name in the current language.
 */
public class Section extends AbstractLocation {

    private String key;

    public Section(int id, String key) {
        this.id = id;
        this.key = key;
    }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getName() {
        try {
            return Localization.get(key);
        } catch (Exception ex) {
            return key;
        }
    }

    public void setId(int id) { this.id = id; }

    @Override
    public String toString() { return getName(); }
}
