package domain;

import domain.base.AbstractLocation;

public class Shelf extends AbstractLocation {

    private String bookcaseName;

    public Shelf() {}

    public Shelf(Integer id, Integer bookcaseId, String name) {
        super.init(id, bookcaseId, name);
    }

    public String getBookcaseName() { return bookcaseName; }
    public void setBookcaseName(String name) { this.bookcaseName = name; }

    @Override
    public String toString() { return getName(); }
}
