package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Loan;
import management.DataBaseLoans;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

/**
 * Klasa okna dialogowego edytowania wypożyczenia
 */
public class EditLoanDialog extends JDialog implements LanguageChangeListener {

    private Loan result = null;
    private final Loan originalLoan;
    private final DataBaseLoans dbLoans;
    private JLabel bookLabel;
    private JLabel borrowerLabel;
    private JLabel loanDateLabel;
    private JSpinner returnDateSpinner;
    private JButton ok;
    private JButton cancel;

    public EditLoanDialog(JFrame parent, Loan loan) {
        super(parent, Localization.get("dialog.edit.loan.title"), true);
        this.originalLoan = loan;
        this.dbLoans = new DataBaseLoans();
        Localization.addLanguageChangeListener(this);
        initComponents();
        loadLoanData();
        setSize(500, 250);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Book (display only)
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel(Localization.get("label.book")), gbc);
        gbc.gridx = 1;
        bookLabel = new JLabel();
        add(bookLabel, gbc);

        // Borrower (display only)
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel(Localization.get("label.borrower")), gbc);
        gbc.gridx = 1;
        borrowerLabel = new JLabel();
        add(borrowerLabel, gbc);

        // Loan Date (display only)
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel(Localization.get("label.loanDate")), gbc);
        gbc.gridx = 1;
        loanDateLabel = new JLabel();
        add(loanDateLabel, gbc);

        // Return Date
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel(Localization.get("label.returnDate")), gbc);
        gbc.gridx = 1;
        returnDateSpinner = new JSpinner(new SpinnerDateModel());
        returnDateSpinner.setEditor(new JSpinner.DateEditor(returnDateSpinner, "yyyy-MM-dd"));
        add(returnDateSpinner, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel();
        ok = new JButton(Localization.get("button.ok"));
        cancel = new JButton(Localization.get("button.cancel"));
        ok.addActionListener(e -> {
            java.util.Date selected = (java.util.Date) returnDateSpinner.getValue();
            LocalDate returnDate = new java.sql.Date(selected.getTime()).toLocalDate();
            originalLoan.setReturnDate(returnDate);
            dbLoans.updateLoan(originalLoan);
            result = originalLoan;
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

    private void loadLoanData() {
        if (originalLoan != null) {
            bookLabel.setText(originalLoan.getBook() != null ? originalLoan.getBook().getTitle() : "");
            borrowerLabel.setText(originalLoan.getBorrower() != null ?
                originalLoan.getBorrower().getFirstName() + " " + originalLoan.getBorrower().getLastName() : "");
            loanDateLabel.setText(originalLoan.getLoanDate() != null ? originalLoan.getLoanDate().toString() : "");
            LocalDate defaultDate = originalLoan.getReturnDate() != null ? originalLoan.getReturnDate() : LocalDate.now();
            returnDateSpinner.setValue(java.sql.Date.valueOf(defaultDate));
        }
    }

    public Loan showDialog() {
        setVisible(true);
        return result;
    }

    @Override
    public void onLanguageChanged() {
        setTitle(Localization.get("dialog.edit.loan.title"));
        ok.setText(Localization.get("button.ok"));
        cancel.setText(Localization.get("button.cancel"));
        revalidate();
        repaint();
    }
}
