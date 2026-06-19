package domain;

import domain.base.AbstractLocation;

public class Bookcase extends AbstractLocation {

    public Bookcase() {}

    public Bookcase(Integer id, String name) {
        super.init(id, name);
    }

    @Override
    public String toString() { return getName(); }
}
