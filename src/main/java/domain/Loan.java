package domain;

import java.time.LocalDate;

public class Loan {

    private int id;
    private Book book;
    private Borrower borrower;
    private LocalDate loanDate;
    private LocalDate dueDate;
    // null means the book has not been returned yet
    private LocalDate returnDate;

    public Loan() {}

    public Loan(int id, Book book, Borrower borrower, LocalDate loanDate, LocalDate dueDate) {
        this.id = id;
        this.book = book;
        this.borrower = borrower;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = null;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    public Borrower getBorrower() { return borrower; }
    public void setBorrower(Borrower borrower) { this.borrower = borrower; }

    public LocalDate getLoanDate() { return loanDate; }
    public void setLoanDate(LocalDate loanDate) { this.loanDate = loanDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public boolean isReturned() { return returnDate != null; }
}
