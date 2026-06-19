package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Reprezentuje książkę w systemie bibliotecznym.
 * Zawiera podstawowe metadane książki takie jak tytuł, autorzy, wydawca,
 * rok wydania i numer ISBN.
 */
public class Book {

    private int id;
    private int shelfId;
    private List<Section> sections = new ArrayList<>();

    /**
     * Tytuł książki.
     * Domyślnie pusty łańcuch, może być {@code null} jeśli zmienione.
     */
    private String title = "";

    /**
     * Tablica autorów książki.
     * Może być {@code null} lub pusta, jeśli autorzy nie są przypisani.
     */
    private Author[] authors = null;

    /**
     * Nazwa wydawcy książki.
     */
    private String publisher = "";

    /**
     * Rok wydania książki.
     * Wartość domyślna to 0 jeśli rok nie został podany.
     */
    private int yearOfPublishing = 0;

    /**
     * Numer ISBN książki.
     */
    private String isbn = "";

    public Book() {
    }

    ;

    public Book(String title, String publisher, int publicationYear, String isbn, Integer shelfId) {
        this.title = title;
        this.publisher = publisher;
        this.yearOfPublishing = publicationYear;
        this.isbn = isbn;
        this.shelfId = shelfId;
    }

    public Book(int id, String title, String publisher, int publicationYear, String isbn, Integer shelfId) {
        this.id = id;
        this.title = title;
        this.publisher = publisher;
        this.yearOfPublishing = publicationYear;
        this.isbn = isbn;
        this.shelfId = shelfId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getPublicationYear() {
        return yearOfPublishing;
    }

    public String getIsbn() {
        return isbn;
    }

    public Integer getShelfId() {
        return shelfId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setShelfId(Integer shelfId) {
        this.shelfId = shelfId;
    }

    public Author[] getAuthors() {
        return authors;
    }

    public String getAuthorsAsString() {
        StringBuilder result = new StringBuilder();
        if (authors != null) {
            for (Author author : authors) {
                result.append(author.getFirstName()).append(" ").append(author.getLastName()).append("; ");
            }
        }

        // Usuwamy ostatnie dwa znaki ("; "), jeśli string nie jest pusty
        if (result.length() > 2) {
            result.setLength(result.length() - 2);
        }

        return result.toString();
    }

    public void setAuthors(Author[] authors) {
        this.authors = authors;
    }

    public void setYearOfPublishing(Integer value) {
        this.yearOfPublishing = value;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections != null ? sections : new ArrayList<>();
    }

    public String getSectionsAsString() {
        StringBuilder sb = new StringBuilder();
        for (Section section : sections) {
            sb.append(section.getName()).append(", ");
        }
        if (sb.length() > 2) sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    @Override
    public String toString() {
        return title;
    }
}
