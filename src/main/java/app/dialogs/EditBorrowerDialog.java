package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Borrower;
import management.DataBaseBorrowers;

import javax.swing.*;
import java.awt.*;

/**
 * Klasa okna dialogowego edytowania wypożyczającego
 */
public class EditBorrowerDialog extends JDialog implements LanguageChangeListener {

    private Borrower result = null;
    private final Borrower originalBorrower;
    private final DataBaseBorrowers dbBorrowers;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField addressCityField;
    private JTextField addressStreetField;
    private JSpinner addressNumberSpinner;
    private JTextField addressZipField;
    private JSpinner cardNumberSpinner;
    private JButton ok;
    private JButton cancel;

    public EditBorrowerDialog(JFrame parent, Borrower borrower) {
        super(parent, Localization.get("dialog.edit.borrower.title"), true);
        this.originalBorrower = borrower;
        this.dbBorrowers = new DataBaseBorrowers();
        Localization.addLanguageChangeListener(this);
        initComponents();
        loadBorrowerData();
        setSize(500, 350);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // First Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel(Localization.get("label.firstName")), gbc);
        gbc.gridx = 1;
        firstNameField = new JTextField(20);
        add(firstNameField, gbc);

        // Last Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel(Localization.get("label.lastName")), gbc);
        gbc.gridx = 1;
        lastNameField = new JTextField(20);
        add(lastNameField, gbc);

        // Address City
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel(Localization.get("label.city")), gbc);
        gbc.gridx = 1;
        addressCityField = new JTextField(20);
        add(addressCityField, gbc);

        // Address Street
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel(Localization.get("label.street")), gbc);
        gbc.gridx = 1;
        addressStreetField = new JTextField(20);
        add(addressStreetField, gbc);

        // Address Number
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel(Localization.get("label.number")), gbc);
        gbc.gridx = 1;
        addressNumberSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        add(addressNumberSpinner, gbc);

        // Address Zip
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel(Localization.get("label.zip")), gbc);
        gbc.gridx = 1;
        addressZipField = new JTextField(20);
        add(addressZipField, gbc);

        // Card Number
        gbc.gridx = 0;
        gbc.gridy = 6;
        add(new JLabel(Localization.get("label.cardNumber")), gbc);
        gbc.gridx = 1;
        cardNumberSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999999, 1));
        add(cardNumberSpinner, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel();
        ok = new JButton(Localization.get("button.ok"));
        cancel = new JButton(Localization.get("button.cancel"));
        ok.addActionListener(e -> {
            result = originalBorrower;
            result.setFirstName(firstNameField.getText().trim());
            result.setLastName(lastNameField.getText().trim());
            result.setAddressCity(addressCityField.getText().trim());
            result.setAddressStreet(addressStreetField.getText().trim());
            result.setAddressNumber(((Integer) addressNumberSpinner.getValue()));
            result.setAddressZip(addressZipField.getText().trim());
            result.setCardNumber((Integer) cardNumberSpinner.getValue());
            dbBorrowers.updateBorrower(result);
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

    private void loadBorrowerData() {
        if (originalBorrower != null) {
            firstNameField.setText(originalBorrower.getFirstName() != null ? originalBorrower.getFirstName() : "");
            lastNameField.setText(originalBorrower.getLastName() != null ? originalBorrower.getLastName() : "");
            addressCityField.setText(originalBorrower.getAddressCity() != null ? originalBorrower.getAddressCity() : "");
            addressStreetField.setText(originalBorrower.getAddressStreet() != null ? originalBorrower.getAddressStreet() : "");
            if (originalBorrower.getAddressNumber() != null) {
                addressNumberSpinner.setValue(originalBorrower.getAddressNumber().intValue());
            }
            addressZipField.setText(originalBorrower.getAddressZip() != null ? originalBorrower.getAddressZip() : "");
            cardNumberSpinner.setValue(originalBorrower.getCardNumber());
        }
    }

    public Borrower showDialog() {
        setVisible(true);
        return result;
    }

    @Override
    public void onLanguageChanged() {
        setTitle(Localization.get("dialog.edit.borrower.title"));
        ok.setText(Localization.get("button.ok"));
        cancel.setText(Localization.get("button.cancel"));
        revalidate();
        repaint();
    }
}
