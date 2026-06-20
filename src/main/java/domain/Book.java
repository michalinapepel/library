package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Reprezentuje książkę przechowywaną w bibliotece.
 * <p>
 * Klasa przechowuje podstawowe dane bibliograficzne książki (tytuł, autorzy,
 * wydawnictwo, rok wydania, numer ISBN) oraz informacje o jej lokalizacji
 * (półka) i przynależności do działów (sekcji).
 */
public class Book {

    /**
     * Unikalny identyfikator książki
     */
    private int id;

    /**
     * Identyfikator półki, na której znajduje się dana książka
     */
    private Integer shelfId;

    /**
     * Tytuł książki
     */
    private String title = "";

    /**
     * Tablica autorów książki
     */
    private Author[] authors = null;

    /**
     * Nazwa wydawnictwa, które opublikowało książkę
     */
    private String publisher = "";

    /**
     * Rok wydania książki
     */
    private int yearOfPublishing = 0;

    /**
     * Numer ISBN książki
     */
    private String isbn = "";

    /**
     * Lista działów (sekcji), do których przypisana jest książka
     */
    private List<Section> sections = new ArrayList<>();

    /**
     * Tworzy nowy, pusty obiekt książki.
     */
    public Book() {}

    /**
     * Tworzy nowy obiekt książki bez identyfikatora (np. przed zapisem do bazy).
     *
     * @param title           tytuł książki
     * @param publisher       nazwa wydawnictwa
     * @param publicationYear rok wydania
     * @param isbn            numer ISBN
     * @param shelfId         identyfikator półki, na której znajduje się książka
     */
    public Book(String title, String publisher, int publicationYear, String isbn, Integer shelfId) {
        this.title = title;
        this.publisher = publisher;
        this.yearOfPublishing = publicationYear;
        this.isbn = isbn;
        this.shelfId = shelfId;
    }

    /**
     * Tworzy nowy obiekt książki z podanym identyfikatorem.
     *
     * @param id              unikalny identyfikator książki
     * @param title           tytuł książki
     * @param publisher       nazwa wydawnictwa
     * @param publicationYear rok wydania
     * @param isbn            numer ISBN
     * @param shelfId         identyfikator półki, na której znajduje się książka
     */
    public Book(int id, String title, String publisher, int publicationYear, String isbn, Integer shelfId) {
        this.id = id;
        this.title = title;
        this.publisher = publisher;
        this.yearOfPublishing = publicationYear;
        this.isbn = isbn;
        this.shelfId = shelfId;
    }

    /**
     * Zwraca identyfikator książki.
     *
     * @return unikalny identyfikator książki
     */
    public int getId() { return id; }
    /**
     * Ustawia identyfikator książki.
     *
     * @param id unikalny identyfikator książki
     */
    public void setId(int id) { this.id = id; }

    /**
     * Zwraca tytuł książki.
     *
     * @return tytuł książki
     */
    public String getTitle() { return title; }
    /**
     * Ustawia tytuł książki.
     *
     * @param title tytuł książki
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Zwraca nazwę wydawnictwa.
     *
     * @return nazwa wydawnictwa
     */
    public String getPublisher() { return publisher; }
    /**
     * Ustawia nazwę wydawnictwa.
     *
     * @param publisher nazwa wydawnictwa
     */
    public void setPublisher(String publisher) { this.publisher = publisher; }

    /**
     * Zwraca rok wydania książki.
     *
     * @return rok wydania
     */
    public int getPublicationYear() { return yearOfPublishing; }
    /**
     * Ustawia rok wydania książki.
     *
     * @param value rok wydania
     */
    public void setYearOfPublishing(Integer value) { this.yearOfPublishing = value; }

    /**
     * Zwraca numer ISBN książki.
     *
     * @return numer ISBN
     */
    public String getIsbn() { return isbn; }
    /**
     * Ustawia numer ISBN książki.
     *
     * @param isbn numer ISBN
     */
    public void setIsbn(String isbn) { this.isbn = isbn; }

    /**
     * Zwraca identyfikator półki, na której znajduje się książka.
     *
     * @return identyfikator półki
     */
    public Integer getShelfId() { return shelfId; }
    /**
     * Ustawia identyfikator półki, na której znajduje się książka.
     *
     * @param shelfId identyfikator półki
     */
    public void setShelfId(Integer shelfId) { this.shelfId = shelfId; }

    /**
     * Zwraca tablicę autorów książki.
     *
     * @return tablica autorów
     */
    public Author[] getAuthors() { return authors; }
    /**
     * Ustawia tablicę autorów książki.
     *
     * @param authors tablica autorów
     */
    public void setAuthors(Author[] authors) { this.authors = authors; }

    /**
     * Zwraca autorów książki w postaci jednego łańcucha znaków.
     * <p>
     * Poszczególni autorzy (imię i nazwisko) są oddzieleni średnikiem.
     *
     * @return tekstowa lista autorów lub pusty łańcuch, jeśli brak autorów
     */
    public String getAuthorsAsString() {
        StringBuilder result = new StringBuilder();
        if (authors != null) {
            for (Author author : authors) {
                result.append(author.getFirstName()).append(" ").append(author.getLastName()).append("; ");
            }
        }
        if (result.length() > 2) result.setLength(result.length() - 2);
        return result.toString();
    }

    /**
     * Zwraca listę działów (sekcji), do których przypisana jest książka.
     *
     * @return lista działów
     */
    public List<Section> getSections() { return sections; }
    /**
     * Ustawia listę działów (sekcji), do których przypisana jest książka.
     * <p>
     * Jeśli przekazana zostanie wartość {@code null}, ustawiana jest pusta lista.
     *
     * @param sections lista działów
     */
    public void setSections(List<Section> sections) { this.sections = sections != null ? sections : new ArrayList<>(); }

    /**
     * Zwraca nazwy działów (sekcji) książki w postaci jednego łańcucha znaków.
     * <p>
     * Poszczególne nazwy są oddzielone przecinkiem.
     *
     * @return tekstowa lista nazw działów lub pusty łańcuch, jeśli brak działów
     */
    public String getSectionsAsString() {
        StringBuilder sb = new StringBuilder();
        for (Section section : sections) {
            sb.append(section.getName()).append(", ");
        }
        if (sb.length() > 2) sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    /**
     * Zwraca tekstową reprezentację książki, czyli jej tytuł.
     *
     * @return tytuł książki
     */
    @Override
    public String toString() { return title; }
}
