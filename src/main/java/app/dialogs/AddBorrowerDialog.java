package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Borrower;

import javax.swing.*;
import java.awt.*;

public class AddBorrowerDialog extends JDialog implements LanguageChangeListener {

    private Borrower result = null;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField addressCityField;
    private JTextField addressStreetField;
    private JSpinner addressNumberSpinner;
    private JTextField addressZipField;
    private JSpinner cardNumberSpinner;
    private JButton ok;
    private JButton cancel;

    private final int nextCardNumber;

    public AddBorrowerDialog(JFrame parent, int nextCardNumber) {
        super(parent, Localization.get("dialog.add.borrower.title"), true);
        this.nextCardNumber = nextCardNumber;
        Localization.addLanguageChangeListener(this);
        initComponents();
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
        cardNumberSpinner = new JSpinner(new SpinnerNumberModel(nextCardNumber, 1, 999999, 1));
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
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String city = addressCityField.getText().trim();
            String street = addressStreetField.getText().trim();
            String zip = addressZipField.getText().trim();
            Integer cardNumber = (Integer) cardNumberSpinner.getValue();

            if (firstName.isEmpty()) {
                JOptionPane.showMessageDialog(this, Localization.get("validation.firstName.required"), Localization.get("message.validation.error"), JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (lastName.isEmpty()) {
                JOptionPane.showMessageDialog(this, Localization.get("validation.lastName.required"), Localization.get("message.validation.error"), JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (city.isEmpty()) {
                JOptionPane.showMessageDialog(this, Localization.get("validation.city.required"), Localization.get("message.validation.error"), JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (street.isEmpty()) {
                JOptionPane.showMessageDialog(this, Localization.get("validation.street.required"), Localization.get("message.validation.error"), JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (zip.isEmpty()) {
                JOptionPane.showMessageDialog(this, Localization.get("validation.zip.required"), Localization.get("message.validation.error"), JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!zip.matches("\\d{2}-\\d{3}|\\d{5}")) {
                JOptionPane.showMessageDialog(this, Localization.get("validation.zip.format"), Localization.get("message.validation.error"), JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (cardNumber <= 0) {
                JOptionPane.showMessageDialog(this, Localization.get("validation.cardNumber.positive"), Localization.get("message.validation.error"), JOptionPane.WARNING_MESSAGE);
                return;
            }

            result = new Borrower();
            result.setFirstName(firstName);
            result.setLastName(lastName);
            result.setAddressCity(city);
            result.setAddressStreet(street);
            result.setAddressNumber(((Integer) addressNumberSpinner.getValue()));
            result.setAddressZip(zip);
            result.setCardNumber(cardNumber);
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

    public Borrower showDialog() {
        setVisible(true);
        return result;
    }

    @Override
    public void onLanguageChanged() {
        setTitle(Localization.get("dialog.add.borrower.title"));
        ok.setText(Localization.get("button.ok"));
        cancel.setText(Localization.get("button.cancel"));
        revalidate();
        repaint();
    }
}
