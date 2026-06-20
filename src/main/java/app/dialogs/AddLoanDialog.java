package app.dialogs;

import app.AppConfig;
import app.LanguageChangeListener;
import app.Localization;
import domain.Book;
import domain.Borrower;
import domain.Loan;
import management.DataBaseLoans;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class AddLoanDialog extends JDialog implements LanguageChangeListener {

    private Loan result = null;
    private final Book[] books;
    private final Borrower[] borrowers;
    private final DataBaseLoans dbLoans = new DataBaseLoans();
    private JComboBox<Book> bookCombo;
    private JComboBox<Borrower> borrowerCombo;
    private JButton ok;
    private JButton cancel;

    public AddLoanDialog(JFrame parent, Book[] books, Borrower[] borrowers) {
        super(parent, Localization.get("dialog.add.loan.title"), true);
        this.books = books;
        this.borrowers = borrowers;
        Localization.addLanguageChangeListener(this);
        initComponents();
        setSize(500, 200);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Book
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel(Localization.get("label.book")), gbc);
        gbc.gridx = 1;
        bookCombo = new JComboBox<>(books);
        add(bookCombo, gbc);

        // Borrower
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel(Localization.get("label.borrower")), gbc);
        gbc.gridx = 1;
        borrowerCombo = new JComboBox<>(borrowers);
        add(borrowerCombo, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel();
        ok = new JButton(Localization.get("button.ok"));
        cancel = new JButton(Localization.get("button.cancel"));

        ok.addActionListener(e -> {
            Book selectedBook = (Book) bookCombo.getSelectedItem();
            Borrower selectedBorrower = (Borrower) borrowerCombo.getSelectedItem();

            if (selectedBook == null) {
                JOptionPane.showMessageDialog(this, Localization.get("message.select.book"), Localization.get("message.validation.error"), JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (selectedBorrower == null) {
                JOptionPane.showMessageDialog(this, Localization.get("message.select.borrower"), Localization.get("message.validation.error"), JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (dbLoans.isBookOnActiveLoan(selectedBook.getId())) {
                JOptionPane.showMessageDialog(this,
                    "\"" + selectedBook.getTitle() + "\" " + Localization.get("message.book.already.loaned"),
                    Localization.get("message.book.unavailable.title"), JOptionPane.WARNING_MESSAGE);
                return;
            }

            result = new Loan();
            result.setBook(selectedBook);
            result.setBorrower(selectedBorrower);
            result.setLoanDate(LocalDate.now());
            result.setDueDate(LocalDate.now().plusDays(AppConfig.DEFAULT_LOAN_DAYS));
            dispose();
        });

        cancel.addActionListener(e -> {
            result = null;
            dispose();
        });

        buttonPanel.add(ok);
        buttonPanel.add(cancel);
        add(buttonPanel, gbc);
    }

    public Loan showDialog() {
        setVisible(true);
        return result;
    }

    @Override
    public void onLanguageChanged() {
        setTitle(Localization.get("dialog.add.loan.title"));
        ok.setText(Localization.get("button.ok"));
        cancel.setText(Localization.get("button.cancel"));
        revalidate();
        repaint();
    }
}
