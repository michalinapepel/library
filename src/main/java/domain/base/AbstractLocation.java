package domain.base;

/**
 * Klasa bazowa reprezentująca lokalizację w bibliotece.
 * <p>
 * Przechowuje wspólne dane elementów odpowiadających za miejsce przechowywania
 * książek, takich jak regał czy półka (identyfikator, nazwa, identyfikator regału).
 */
abstract public class AbstractLocation {

    /**
     * Nazwa lokalizacji
     */
    protected String name;

    /**
     * Unikalny identyfikator lokalizacji
     */
    protected int id;

    /**
     * Identyfikator regału, do którego należy dana lokalizacja
     */
    protected int bookcaseId;

    /**
     * Zwraca nazwę lokalizacji.
     *
     * @return nazwa lokalizacji
     */
    public String getName() { return name; }

    /**
     * Ustawia nazwę lokalizacji.
     *
     * @param name nazwa lokalizacji
     */
    public void setName(String name) { this.name = name; }

    /**
     * Zwraca identyfikator lokalizacji.
     *
     * @return unikalny identyfikator lokalizacji
     */
    public int getId() { return id; }

    /**
     * Ustawia identyfikator lokalizacji.
     *
     * @param id unikalny identyfikator lokalizacji
     */
    public void setId(int id) { this.id = id; }

    /**
     * Zwraca identyfikator regału, do którego należy lokalizacja.
     *
     * @return identyfikator regału
     */
    public int getBookcaseId() { return bookcaseId; }

    /**
     * Ustawia identyfikator regału, do którego należy lokalizacja.
     *
     * @param bookcaseId identyfikator regału
     */
    public void setBookcaseId(int bookcaseId) { this.bookcaseId = bookcaseId; }

    /**
     * Inicjalizuje lokalizację wraz z przypisaniem do regału.
     *
     * @param id         unikalny identyfikator lokalizacji
     * @param bookcaseId identyfikator regału, do którego należy lokalizacja
     * @param name       nazwa lokalizacji
     */
    public void init(Integer id, Integer bookcaseId, String name) {
        this.id = id;
        this.bookcaseId = bookcaseId;
        this.name = name;
    }

    /**
     * Inicjalizuje lokalizację bez przypisania do regału.
     *
     * @param id   unikalny identyfikator lokalizacji
     * @param name nazwa lokalizacji
     */
    public void init(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
