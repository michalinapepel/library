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
    //przechowywanie jak???

}
