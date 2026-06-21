package domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void getAuthorsAsString_jedynAutor() {
        Book book = new Book();
        book.setAuthors(new Author[]{new Author(1, "Adam", "Mickiewicz", "", "PL")});

        assertEquals("Adam Mickiewicz", book.getAuthorsAsString());
    }

    @Test
    void getAuthorsAsString_wieluAutorow() {
        Book book = new Book();
        book.setAuthors(new Author[]{
            new Author(1, "Adam", "Mickiewicz", "", "PL"),
            new Author(2, "Juliusz", "Słowacki", "", "PL")
        });

        assertEquals("Adam Mickiewicz; Juliusz Słowacki", book.getAuthorsAsString());
    }

    @Test
    void getAuthorsAsString_bezAutorow() {
        Book book = new Book();
        book.setAuthors(new Author[0]);

        assertEquals("", book.getAuthorsAsString());
    }

    @Test
    void getAuthorsAsString_authorsNull() {
        Book book = new Book();
        // authors domyślnie null – metoda powinna zwrócić pusty string bez NPE

        assertEquals("", book.getAuthorsAsString());
    }

    @Test
    void getSectionsAsString_jednaSekcja() {
        Book book = new Book();
        // Używamy klucza "-" żeby Section.getName() zwróciło klucz bezpośrednio (fallback)
        book.setSections(List.of(new Section(1, "Fantastyka", "-")));

        assertTrue(book.getSectionsAsString().contains("Fantastyka"));
    }

    @Test
    void getSectionsAsString_brakSekcji() {
        Book book = new Book();
        book.setSections(List.of());

        assertEquals("", book.getSectionsAsString());
    }

    @Test
    void toString_zwracaTytul() {
        Book book = new Book();
        book.setTitle("Pan Tadeusz");

        assertEquals("Pan Tadeusz", book.toString());
    }
}
