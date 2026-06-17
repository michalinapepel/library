package domain;

/**
 * Reprezentuje książkę w systemie bibliotecznym.
 * Zawiera podstawowe metadane książki takie jak tytuł, autorzy, wydawca,
 * rok wydania i numer ISBN.
 */
public class Book {
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

    /**
     * Półka, na której przechowywana jest książka.
     */
    private Shelf shelf;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        return result.toString();
    }

    public void setAuthors(Author[] authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getYearOfPublishing() {
        return yearOfPublishing;
    }

    public void setYearOfPublishing(int yearOfPublishing) {
        this.yearOfPublishing = yearOfPublishing;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Shelf getShelf() {
        return shelf;
    }

    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }
}
