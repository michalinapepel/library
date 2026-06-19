package domain.base;

abstract public class AbstractPerson {

    protected Integer id;
    protected String firstName;
    protected String lastName;

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
}
