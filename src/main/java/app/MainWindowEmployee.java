package app;

import app.dialogs.*;
import app.panels.ToolBar;
import domain.Author;
import domain.Book;
import domain.Borrower;
import domain.Loan;
import management.DataBaseBooks;
import management.DataBaseAuthors;
import management.DataBaseBorrowers;
import management.DataBaseLoans;
import management.DataBaseShelfs;
import management.DataBaseSections;
import management.DataBaseBookcase;
import domain.Bookcase;
import domain.Section;
import domain.Shelf;

import javax.swing.*;
import java.awt.*;

public class MainWindowEmployee extends JFrame implements LanguageChangeListener {

    private final ToolBar toolbar;
    private final DataBaseBooks dbBooks;
    private final DataBaseAuthors dbAuthors;
    private final DataBaseBorrowers dbBorrowers;
    private final DataBaseLoans dbLoans;
    private final DataBaseShelfs dbShelves;
    private final DataBaseSections dbSections;
    private final DataBaseBookcase dbBookcase;
    private final JButton booksButton;
    private final JButton addBookButton;
    private final JButton authorsButton;
    private final JButton addAuthorButton;
    private final JButton borrowersButton;
    private final JButton addBorrowerButton;
    private final JButton loansButton;
    private final JButton addLoanButton;
    private final JButton managementButton;
    private final JPanel northPanel;
    private final JPanel centerPanel;
    private final JPanel centerPanel2;
    private final JPanel southPanel;
    private final JPanel managementPanel;

    public MainWindowEmployee() {
        Localization.addLanguageChangeListener(this);

        dbBooks = new DataBaseBooks();
        dbAuthors = new DataBaseAuthors();
        dbBorrowers = new DataBaseBorrowers();
        dbLoans = new DataBaseLoans();
        dbShelves = new DataBaseShelfs();
        dbSections = new DataBaseSections();
        dbBookcase = new DataBaseBookcase();

        setTitle(Localization.get("app.title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1, 50, 10));

        booksButton = new JButton(Localization.get("button.books"));
        booksButton.addActionListener(e -> showListBooksDialog());
        booksButton.setPreferredSize(new Dimension(300, 50));
        addBookButton = new JButton(Localization.get("button.book.add"));
        addBookButton.addActionListener(e -> showAddBookDialog());
        addBookButton.setPreferredSize(new Dimension(50, 20));
        addBookButton.setForeground(new Color(0,153,76));
        addBookButton.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 25));

        authorsButton = new JButton(Localization.get("button.authors"));
        authorsButton.addActionListener(e -> showAuthorsDialog());
        authorsButton.setPreferredSize(new Dimension(300, 50));
        addAuthorButton = new JButton(Localization.get("button.author.add"));
        addAuthorButton.addActionListener(e -> showAddAuthorDialog());
        addAuthorButton.setPreferredSize(new Dimension(50, 20));
        addAuthorButton.setForeground(new Color(0,153,76));
        addAuthorButton.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 25));

        borrowersButton = new JButton(Localization.get("button.borrowers"));
        borrowersButton.addActionListener(e -> showListBorrowersDialog());
        borrowersButton.setPreferredSize(new Dimension(300, 50));
        addBorrowerButton = new JButton(Localization.get("button.borrower.add"));
        addBorrowerButton.addActionListener(e -> showAddBorrowerDialog());
        addBorrowerButton.setPreferredSize(new Dimension(50, 20));
        addBorrowerButton.setForeground(new Color(0,153,76));
        addBorrowerButton.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 25));

        loansButton = new JButton(Localization.get("button.loans"));
        loansButton.addActionListener(e -> showListLoansDialog());
        loansButton.setPreferredSize(new Dimension(300, 50));
        addLoanButton = new JButton(Localization.get("button.loan.add"));
        addLoanButton.addActionListener(e -> showAddLoanDialog());
        addLoanButton.setPreferredSize(new Dimension(50, 20));
        addLoanButton.setForeground(new Color(0,153,76));
        addLoanButton.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 25));

        managementButton = new JButton(Localization.get("button.management"));
        managementButton.addActionListener(e -> {
            ManagementWindow window = new ManagementWindow();
            window.setVisible(true);
        });
        managementButton.setPreferredSize(new Dimension(500, 50));

        northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        northPanel.add(booksButton, BorderLayout.WEST);
        northPanel.add(addBookButton, BorderLayout.CENTER);

        centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(authorsButton, BorderLayout.WEST);
        centerPanel.add(addAuthorButton, BorderLayout.CENTER);

        centerPanel2 = new JPanel();
        centerPanel2.setLayout(new BorderLayout());
        centerPanel2.add(loansButton, BorderLayout.WEST);
        centerPanel2.add(addLoanButton, BorderLayout.CENTER);

        southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.add(borrowersButton, BorderLayout.WEST);
        southPanel.add(addBorrowerButton, BorderLayout.CENTER);

        managementPanel = new JPanel();
        managementPanel.setLayout(new BorderLayout());
        managementPanel.add(managementButton, BorderLayout.SOUTH);


        toolbar = new ToolBar(true);
        add(toolbar);
        add(northPanel);
        add(centerPanel);
        add(centerPanel2);
        add(southPanel);
        add(managementPanel);


        setSize(600, 350);
        setLocationRelativeTo(null);
    }

    private void showListBooksDialog() {
        Shelf[] shelves = dbShelves.getAllShelves().toArray(new Shelf[0]);
        Bookcase[] bookcases = dbBookcase.getAllBookcases().toArray(new Bookcase[0]);
        Section[] sections = dbSections.getAllSections().toArray(new Section[0]);
        ListBooksDialog dialog = new ListBooksDialog(this, dbAuthors.getAllAuthors().toArray(new Author[0]), shelves, bookcases, sections);
        dialog.setBooks(dbBooks.getAllBooks());
        dialog.showDialog();
    }

    private void showAddBookDialog() {
        Shelf[] shelves = dbShelves.getAllShelves().toArray(new Shelf[0]);
        Bookcase[] bookcases = dbBookcase.getAllBookcases().toArray(new Bookcase[0]);
        Section[] sections = dbSections.getAllSections().toArray(new Section[0]);
        AddBookDialog dialog = new AddBookDialog(this, dbAuthors.getAllAuthors().toArray(new Author[0]), shelves, bookcases, sections);
        Book newBook = dialog.showDialog();

        if (newBook != null) {
            dbBooks.addBook(newBook);
            JOptionPane.showMessageDialog(this, Localization.get("message.add.success.book"));
        }
    }

    private void showAddAuthorDialog() {
        AddAuthorDialog dialog = new AddAuthorDialog(this);
        Author newAuthor = dialog.showDialog();

        if (newAuthor != null) {
            dbAuthors.addAuthor(newAuthor);
            JOptionPane.showMessageDialog(this, Localization.get("message.add.success.author"));
        }
    }

    private void showAuthorsDialog() {
        ListAuthorsDialog dialog = new ListAuthorsDialog(this);
        dialog.setAuthors(dbAuthors.getAllAuthors());
        dialog.showDialog();
    }

    private void showListBorrowersDialog() {
        ListBorrowersDialog dialog = new ListBorrowersDialog(this);
        dialog.setBorrowers(dbBorrowers.getAllBorrowers());
        dialog.showDialog();
    }

    private void showAddBorrowerDialog() {
        int nextCardNumber = dbBorrowers.getNextCardNumber();
        AddBorrowerDialog dialog = new AddBorrowerDialog(this, nextCardNumber);
        Borrower newBorrower = dialog.showDialog();

        if (newBorrower != null) {
            dbBorrowers.addBorrower(newBorrower);
            JOptionPane.showMessageDialog(this, Localization.get("message.add.success.borrower"));
        }
    }

    private void showListLoansDialog() {
        ListLoansDialog dialog = new ListLoansDialog(this);
        dialog.setLoans(dbLoans.getAllLoans());
        dialog.showDialog();
    }

    private void showAddLoanDialog() {
        java.util.Set<Integer> onLoan = dbLoans.getActiveLoanBookIds();
        Book[] books = dbBooks.getAllBooks().stream()
                .filter(b -> !onLoan.contains(b.getId()))
                .toArray(Book[]::new);
        Borrower[] borrowers = dbBorrowers.getAllBorrowers().toArray(new Borrower[0]);
        AddLoanDialog dialog = new AddLoanDialog(this, books, borrowers);
        Loan newLoan = dialog.showDialog();

        if (newLoan != null) {
            dbLoans.addLoan(newLoan);
            JOptionPane.showMessageDialog(this, Localization.get("message.add.success.loan"));
        }
    }

    @Override
    public void onLanguageChanged() {
        setTitle(Localization.get("app.title"));
        booksButton.setText(Localization.get("button.books"));
        authorsButton.setText(Localization.get("button.authors"));
        loansButton.setText(Localization.get("button.loans"));
        borrowersButton.setText(Localization.get("button.borrowers"));
        managementButton.setText(Localization.get("button.management"));
        addBookButton.setText(Localization.get("button.book.add"));
        addAuthorButton.setText(Localization.get("button.author.add"));
        addBorrowerButton.setText(Localization.get("button.borrower.add"));
        addLoanButton.setText(Localization.get("button.loan.add"));
        revalidate();
        repaint();
    }
}

