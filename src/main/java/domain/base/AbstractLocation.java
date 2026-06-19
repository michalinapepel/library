package domain.base;

abstract public class AbstractLocation {

    protected String name;
    protected int id;
    protected int bookcaseId;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBookcaseId() { return bookcaseId; }
    public void setBookcaseId(int bookcaseId) { this.bookcaseId = bookcaseId; }

    public void init(Integer id, Integer bookcaseId, String name) {
        this.id = id;
        this.bookcaseId = bookcaseId;
        this.name = name;
    }

    public void init(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
