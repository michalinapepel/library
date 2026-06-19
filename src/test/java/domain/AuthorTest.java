package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorTest {

    @Test
    void toString_imieNazwisko() {
        Author author = new Author(1, "Adam", "Mickiewicz", "", "PL");

        assertEquals("Adam Mickiewicz", author.toString());
    }

    @Test
    void toString_pustePola() {
        Author author = new Author(1, "", "", "", "");

        assertEquals(" ", author.toString());
    }

    @Test
    void getPseudonym_zwracaPseudonim() {
        Author author = new Author(1, "Samuel", "Clemens", "Mark Twain", "US");

        assertEquals("Mark Twain", author.getPseudonym());
    }

    @Test
    void getNationality_zwracaNarodowоsc() {
        Author author = new Author(1, "Adam", "Mickiewicz", "", "PL");

        assertEquals("PL", author.getNationality());
    }

    @Test
    void settery_aktualizujaPola() {
        Author author = new Author();
        author.setFirstName("Jan");
        author.setLastName("Kowalski");
        author.setPseudonym("JK");
        author.setNationality("PL");

        assertEquals("Jan Kowalski", author.toString());
        assertEquals("JK", author.getPseudonym());
        assertEquals("PL", author.getNationality());
    }
}
