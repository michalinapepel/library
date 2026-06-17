package app;

import app.dialogs.*;
import app.panels.ToolBar;
import domain.Author;
import domain.Book;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainWindowEmployee extends JFrame implements LanguageChangeListener {

    private final ToolBar toolbar;
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
        //editLoanButton = new JButton(Localization.get("button.loan.edit"));
        //editLoanButton.addActionListener(e -> EditLoanDialog());
        //editLoanButton.setPreferredSize(new Dimension(150, 20));

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
        //centerPanel.add(editLoanButton, BorderLayout.EAST);

        centerPanel2 = new JPanel();
        centerPanel2.setLayout(new BorderLayout());
        centerPanel2.add(loansButton, BorderLayout.WEST);
        centerPanel2.add(addLoanButton, BorderLayout.CENTER);
        //centerPanel2.add(editLoanButton, BorderLayout.EAST);

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


        setSize(600, 300);
        setLocationRelativeTo(null);
    }

    private void showListBooksDialog() {
        ListBooksDialog dialog = new ListBooksDialog(this);
        ArrayList<Book> books = new ArrayList<>();

        Book book1 = new Book();
        book1.setTitle("The Great Gatsby");
        book1.setAuthors(new Author[] {new Author()});
        book1.setPublisher("HarperCollins");

        books.add(book1);

        dialog.setBooks(books);
        dialog.showDialog();
    }

    private void showAddBookDialog() {
        AddBookDialog dialog = new AddBookDialog(this);
        dialog.showDialog();
    }

    private void showAddAuthorDialog() {
        AddAuthorDialog dialog = new AddAuthorDialog(this);
        dialog.showDialog();
    }

    private void showAuthorsDialog() {
        ListAuthorsDialog dialog = new ListAuthorsDialog(this);
        dialog.showDialog();
    }

    private void showListBorrowersDialog() {
        ListBorrowersDialog dialog = new ListBorrowersDialog(this);
        dialog.setBorrowers(new ArrayList<>());
        dialog.showDialog();
    }

    private void showAddBorrowerDialog() {
        AddBorrowerDialog dialog = new AddBorrowerDialog(this);
        dialog.showDialog();
    }

    private void showListLoansDialog() {
        ListLoansDialog dialog = new ListLoansDialog(this);
        dialog.setLoans(new ArrayList<>());
        dialog.showDialog();
    }

    private void showAddLoanDialog() {
        AddLoanDialog dialog = new AddLoanDialog(this);
        dialog.showDialog();
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

