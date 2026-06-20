package domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LoanTest {

    @Test
    void isReturned_zwrocona() {
        Loan loan = new Loan(1, new Book(), new Borrower(), LocalDate.now().minusDays(10), LocalDate.now().minusDays(3));
        loan.setReturnDate(LocalDate.now().minusDays(1));

        assertTrue(loan.isReturned());
    }

    @Test
    void isReturned_nieZwrocona() {
        Loan loan = new Loan(1, new Book(), new Borrower(), LocalDate.now().minusDays(5), LocalDate.now().plusDays(9));
        // returnDate domyślnie null po konstruktorze

        assertFalse(loan.isReturned());
    }

    @Test
    void setReturnDate_nullOznaczaNieZwrocona() {
        Loan loan = new Loan(1, new Book(), new Borrower(), LocalDate.now(), LocalDate.now().plusDays(14));
        loan.setReturnDate(LocalDate.now());
        loan.setReturnDate(null);

        assertFalse(loan.isReturned());
    }

    @Test
    void getLoanDate_poprawna() {
        LocalDate loanDate = LocalDate.of(2024, 1, 15);
        Loan loan = new Loan(1, new Book(), new Borrower(), loanDate, loanDate.plusDays(14));

        assertEquals(loanDate, loan.getLoanDate());
    }

    @Test
    void getDueDate_poprawna() {
        LocalDate loanDate = LocalDate.of(2024, 1, 15);
        LocalDate dueDate = loanDate.plusDays(14);
        Loan loan = new Loan(1, new Book(), new Borrower(), loanDate, dueDate);

        assertEquals(dueDate, loan.getDueDate());
    }
}
