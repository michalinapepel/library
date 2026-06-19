package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BorrowerTest {

    @Test
    void toString_imieNazwisko() {
        Borrower borrower = new Borrower("Jan", "Kowalski", "Warszawa", "Marszałkowska", 1, "00-001", 20000);

        assertEquals("Jan Kowalski", borrower.toString());
    }

    @Test
    void getCardNumber_zwracaNumerKarty() {
        Borrower borrower = new Borrower("Jan", "Kowalski", "Warszawa", "Marszałkowska", 1, "00-001", 20000);

        assertEquals(20000, borrower.getCardNumber());
    }

    @Test
    void setCardNumber_aktualizujeNumer() {
        Borrower borrower = new Borrower();
        borrower.setCardNumber(20042);

        assertEquals(20042, borrower.getCardNumber());
    }

    @Test
    void konstruktorZId_ustawiaId() {
        Borrower borrower = new Borrower(5, "Anna", "Nowak", "Kraków", "Floriańska", 3, "31-021", 20001);

        assertEquals(5, borrower.getId());
        assertEquals("Anna", borrower.getFirstName());
        assertEquals("Nowak", borrower.getLastName());
    }

    @Test
    void adres_poprawnePola() {
        Borrower borrower = new Borrower("Jan", "Kowalski", "Gdańsk", "Długa", 7, "80-001", 20005);

        assertEquals("Gdańsk", borrower.getAddressCity());
        assertEquals("Długa", borrower.getAddressStreet());
        assertEquals(7, borrower.getAddressNumber());
        assertEquals("80-001", borrower.getAddressZip());
    }
}
