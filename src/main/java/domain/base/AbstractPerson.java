package domain.base;

/**
 * Klasa bazowa reprezentująca osobę w systemie bibliotecznym.
 * <p>
 * Przechowuje wspólne dane osobowe (identyfikator, imię, nazwisko)
 * współdzielone przez konkretne typy osób, takie jak autor czy czytelnik.
 */
abstract public class AbstractPerson {

    /**
     * Unikalny identyfikator osoby
     */
    protected Integer id;

    /**
     * Imię osoby
     */
    protected String firstName;

    /**
     * Nazwisko osoby
     */
    protected String lastName;

    /**
     * Zwraca imię osoby.
     *
     * @return imię osoby
     */
    public String getFirstName() { return firstName; }

    /**
     * Ustawia imię osoby.
     *
     * @param firstName imię osoby
     */
    public void setFirstName(String firstName) { this.firstName = firstName; }

    /**
     * Zwraca nazwisko osoby.
     *
     * @return nazwisko osoby
     */
    public String getLastName() { return lastName; }

    /**
     * Ustawia nazwisko osoby.
     *
     * @param lastName nazwisko osoby
     */
    public void setLastName(String lastName) { this.lastName = lastName; }

    /**
     * Zwraca identyfikator osoby.
     *
     * @return unikalny identyfikator osoby
     */
    public Integer getId() { return id; }

    /**
     * Ustawia identyfikator osoby.
     *
     * @param id unikalny identyfikator osoby
     */
    public void setId(Integer id) { this.id = id; }
}
