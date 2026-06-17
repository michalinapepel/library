package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Loan;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class AddLoanDialog extends JDialog implements LanguageChangeListener {

    private Loan result = null;
    private JComboBox<String> bookCombo;
    private JComboBox<String> borrowerCombo;
    private JSpinner loanDateSpinner;
    private JSpinner dueDateSpinner;
    private JButton ok;
    private JButton cancel;

    public AddLoanDialog(JFrame parent) {
        super(parent, Localization.get("dialog.add.loan.title"), true);
        Localization.addLanguageChangeListener(this);
        initComponents();
        setSize(500, 250);
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
        bookCombo = new JComboBox<>();
        add(bookCombo, gbc);

        // Borrower
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel(Localization.get("label.borrower")), gbc);
        gbc.gridx = 1;
        borrowerCombo = new JComboBox<>();
        add(borrowerCombo, gbc);

        // Loan Date
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel(Localization.get("label.loanDate")), gbc);
        gbc.gridx = 1;
        loanDateSpinner = new JSpinner(new SpinnerDateModel());
        add(loanDateSpinner, gbc);

        // Due Date
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel(Localization.get("label.dueDate")), gbc);
        gbc.gridx = 1;
        dueDateSpinner = new JSpinner(new SpinnerDateModel());
        add(dueDateSpinner, gbc);

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
            result = new Loan();
            result.setLoanDate(LocalDate.now());
            result.setDueDate(LocalDate.now().plusDays(14));
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
