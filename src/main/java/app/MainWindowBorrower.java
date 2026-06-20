package app;

import app.dialogs.BorrowBookDialog;
import app.panels.ToolBar;
import domain.Book;
import domain.Bookcase;
import domain.Borrower;
import domain.Loan;
import domain.Shelf;
import management.DataBaseBookcase;
import management.DataBaseBooks;
import management.DataBaseLoans;
import management.DataBaseShelfs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Okno główne do obsługi dla wypożyczającego
 */
public class MainWindowBorrower extends JFrame implements LanguageChangeListener {
    /** Pasek narzędziowy ze zmianą języka i wylogowaniem. */
    private final ToolBar toolbar;
    /** Tabela prezentująca wypożyczenia czytelnika. */
    private final JTable loansTable;
    /** Model danych tabeli wypożyczeń. */
    private final DefaultTableModel loansTableModel;
    /** Przycisk otwierający okno wypożyczenia książki. */
    private final JButton borrowButton;
    /** Etykieta z numerem karty bibliotecznej czytelnika. */
    private final JLabel cardLabel;
    /** Zalogowany czytelnik, którego dane są wyświetlane w oknie. */
    private final Borrower borrower;
    private final DataBaseLoans dbLoans = new DataBaseLoans();
    private final DataBaseBooks dbBooks = new DataBaseBooks();
    private final DataBaseShelfs dbShelves = new DataBaseShelfs();
    private final DataBaseBookcase dbBookcase = new DataBaseBookcase();

    /**
     * Tworzy okno główne czytelnika i wczytuje jego wypożyczenia.
     *
     * @param borrower zalogowany czytelnik
     */
    public MainWindowBorrower(Borrower borrower) {
        this.borrower = borrower;
        Localization.addLanguageChangeListener(this);

        setTitle(Localization.get("app.title") + " — " + borrower.getFirstName() + " " + borrower.getLastName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        toolbar = new ToolBar(true);

        // Borrower info — same row as toolbar icons
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        JLabel nameLabel = new JLabel(borrower.getFirstName() + " " + borrower.getLastName());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 16f));
        cardLabel = new JLabel(Localization.get("label.cardNumber") + ": " + borrower.getCardNumber());
        cardLabel.setFont(cardLabel.getFont().deriveFont(14f));
        infoPanel.add(nameLabel);
        infoPanel.add(new JLabel(" | "));
        infoPanel.add(cardLabel);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(infoPanel, BorderLayout.WEST);
        headerPanel.add(toolbar, BorderLayout.EAST);

        // Loans table
        String[] columnNames = {
                Localization.get("label.book"),
                Localization.get("label.loanDate"),
                Localization.get("label.dueDate"),
                Localization.get("label.returnDate"),
                Localization.get("label.status"),
        };
        loansTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        loansTable = new JTable(loansTableModel);
        loansTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(loansTable);

        borrowButton = new JButton(Localization.get("button.borrow"));
        borrowButton.addActionListener(e -> showBorrowDialog());
        borrowButton.setPreferredSize(new Dimension(500, 50));

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(borrowButton, BorderLayout.SOUTH);

        setSize(750, 450);
        setLocationRelativeTo(null);

        loadLoans();
    }

    /**
     * Odświeża tabelę wypożyczeń aktualnymi danymi czytelnika z bazy danych.
     */
    private void loadLoans() {
        loansTableModel.setRowCount(0);
        List<Loan> loans = dbLoans.getAllLoansByBorrower(borrower.getId());
        for (Loan loan : loans) {
            String bookTitle = loan.getBook() != null ? loan.getBook().getTitle() : "—";
            String status = loan.isReturned()
                    ? Localization.get("label.returned")
                    : Localization.get("label.active");
            loansTableModel.addRow(new Object[]{
                    bookTitle,
                    loan.getLoanDate(),
                    loan.getDueDate(),
                    loan.getReturnDate() != null ? loan.getReturnDate() : "—",
                    status
            });
        }
    }

    /**
     * Otwiera okno wypożyczenia książki i zapisuje nowe wypożyczenie,
     * jeśli czytelnik wybierze dostępną książkę.
     */
    private void showBorrowDialog() {
        Set<Integer> onLoan = dbLoans.getActiveLoanBookIds();
        List<Book> available = dbBooks.getAllBooks().stream()
                .filter(b -> !onLoan.contains(b.getId()))
                .collect(Collectors.toList());

        Shelf[] shelves = dbShelves.getAllShelves().toArray(new Shelf[0]);
        Bookcase[] bookcases = dbBookcase.getAllBookcases().toArray(new Bookcase[0]);

        BorrowBookDialog dialog = new BorrowBookDialog(this, shelves, bookcases);
        dialog.setBooks(available);
        Book selected = dialog.showDialog();

        if (selected != null) {
            Loan loan = new Loan();
            loan.setBook(selected);
            loan.setBorrower(borrower);
            loan.setLoanDate(LocalDate.now());
            loan.setDueDate(LocalDate.now().plusDays(14));
            dbLoans.addLoan(loan);
            loadLoans();
        }
    }

    /**
     * Aktualizuje teksty okna (tytuł, etykiety, nagłówki tabeli) po zmianie języka.
     */
    @Override
    public void onLanguageChanged() {
        setTitle(Localization.get("app.title") + " — " + borrower.getFirstName() + " " + borrower.getLastName());
        borrowButton.setText(Localization.get("button.borrow"));
        cardLabel.setText(Localization.get("label.cardNumber") + ": " + borrower.getCardNumber());
        loansTableModel.setColumnIdentifiers(new Object[]{
                Localization.get("label.book"),
                Localization.get("label.loanDate"),
                Localization.get("label.dueDate"),
                Localization.get("label.returnDate"),
                Localization.get("label.status"),
        });
        loadLoans();
        revalidate();
        repaint();
    }
}
