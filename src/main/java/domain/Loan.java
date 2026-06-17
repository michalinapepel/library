package domain;

import java.time.LocalDate;

/**
 * Reprezentuje wypożyczenie książki przez czytelnika.
 * Zawiera informacje o powiązanych książce, czytelniku, datach wypożyczenia i zwrotu.
 */
public class Loan {
    /**
     * Unikalny identyfikator wypożyczenia.
     */
    private int id;

    /**
     * Książka będąca przedmiotem wypożyczenia.
     */
    private Book book;

    /**
     * Czytelnik, który wypożyczył książkę.
     */
    private Borrower borrower;

    /**
     * Data wypożyczenia książki.
     */
    private LocalDate loanDate;

    /**
     * Data planowanego zwrotu książki.
     */
    private LocalDate dueDate;

    /**
     * Data faktycznego zwrotu książki.
     * Może być {@code null}, jeśli książka nie została jeszcze zwrócona.
     */
    private LocalDate returnDate;

    public Loan() {
    }

    public Loan(int id, Book book, Borrower borrower, LocalDate loanDate, LocalDate dueDate) {
        this.id = id;
        this.book = book;
        this.borrower = borrower;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Borrower getBorrower() {
        return borrower;
    }

    public void setBorrower(Borrower borrower) {
        this.borrower = borrower;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isReturned() {
        return returnDate != null;
    }
}
