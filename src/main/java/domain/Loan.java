package domain;

import java.time.LocalDate;

/**
 * Reprezentuje wypożyczenie książki przez czytelnika.
 * <p>
 * Przechowuje informacje o wypożyczonej książce, czytelniku oraz datach:
 * wypożyczenia, planowanego zwrotu i faktycznego zwrotu.
 */
public class Loan {

    /**
     * Unikalny identyfikator wypożyczenia
     */
    private int id;

    /**
     * Wypożyczona książka
     */
    private Book book;

    /**
     * Czytelnik, który wypożyczył książkę
     */
    private Borrower borrower;

    /**
     * Data wypożyczenia książki
     */
    private LocalDate loanDate;

    /**
     * Planowana data zwrotu książki
     */
    private LocalDate dueDate;

    /**
     * Faktyczna data zwrotu książki; wartość {@code null} oznacza,
     * że książka nie została jeszcze zwrócona
     */
    private LocalDate returnDate;

    /**
     * Tworzy nowy, pusty obiekt wypożyczenia.
     */
    public Loan() {}

    /**
     * Tworzy nowy obiekt wypożyczenia. Data zwrotu jest początkowo ustawiona
     * na {@code null} (książka niezwrócona).
     *
     * @param id       unikalny identyfikator wypożyczenia
     * @param book     wypożyczona książka
     * @param borrower czytelnik wypożyczający książkę
     * @param loanDate data wypożyczenia
     * @param dueDate  planowana data zwrotu
     */
    public Loan(int id, Book book, Borrower borrower, LocalDate loanDate, LocalDate dueDate) {
        this.id = id;
        this.book = book;
        this.borrower = borrower;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = null;
    }

    /**
     * Zwraca identyfikator wypożyczenia.
     *
     * @return unikalny identyfikator wypożyczenia
     */
    public int getId() { return id; }

    /**
     * Ustawia identyfikator wypożyczenia.
     *
     * @param id unikalny identyfikator wypożyczenia
     */
    public void setId(int id) { this.id = id; }

    /**
     * Zwraca wypożyczoną książkę.
     *
     * @return wypożyczona książka
     */
    public Book getBook() { return book; }

    /**
     * Ustawia wypożyczoną książkę.
     *
     * @param book wypożyczona książka
     */
    public void setBook(Book book) { this.book = book; }

    /**
     * Zwraca czytelnika, który wypożyczył książkę.
     *
     * @return czytelnik wypożyczający książkę
     */
    public Borrower getBorrower() { return borrower; }

    /**
     * Ustawia czytelnika, który wypożyczył książkę.
     *
     * @param borrower czytelnik wypożyczający książkę
     */
    public void setBorrower(Borrower borrower) { this.borrower = borrower; }

    /**
     * Zwraca datę wypożyczenia książki.
     *
     * @return data wypożyczenia
     */
    public LocalDate getLoanDate() { return loanDate; }

    /**
     * Ustawia datę wypożyczenia książki.
     *
     * @param loanDate data wypożyczenia
     */
    public void setLoanDate(LocalDate loanDate) { this.loanDate = loanDate; }

    /**
     * Zwraca planowaną datę zwrotu książki.
     *
     * @return planowana data zwrotu
     */
    public LocalDate getDueDate() { return dueDate; }

    /**
     * Ustawia planowaną datę zwrotu książki.
     *
     * @param dueDate planowana data zwrotu
     */
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    /**
     * Zwraca faktyczną datę zwrotu książki.
     *
     * @return data zwrotu lub {@code null}, jeśli książka nie została zwrócona
     */
    public LocalDate getReturnDate() { return returnDate; }

    /**
     * Ustawia faktyczną datę zwrotu książki.
     *
     * @param returnDate data zwrotu
     */
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    /**
     * Sprawdza, czy książka została już zwrócona.
     *
     * @return {@code true}, jeśli data zwrotu jest ustawiona; w przeciwnym razie {@code false}
     */
    public boolean isReturned() { return returnDate != null; }
}
