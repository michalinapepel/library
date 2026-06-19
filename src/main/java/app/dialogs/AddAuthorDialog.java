package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Author;

import javax.swing.*;
import java.awt.*;

public class AddAuthorDialog extends JDialog implements LanguageChangeListener {

    private Author result = null;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField pseudonymField;
    private JTextField nationalityField;
    private JButton ok;
    private JButton cancel;

    public AddAuthorDialog(JFrame parent) {
        super(parent, Localization.get("dialog.add.author.title"), true);
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

        // firstName
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel(Localization.get("label.firstName")), gbc);
        gbc.gridx = 1;
        firstNameField = new JTextField(20);
        add(firstNameField, gbc);

        // lastName
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel(Localization.get("label.lastName")), gbc);
        gbc.gridx = 1;
        lastNameField = new JTextField(20);
        add(lastNameField, gbc);

        // pseudonym
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel(Localization.get("label.pseudonym")), gbc);
        gbc.gridx = 1;
        pseudonymField = new JTextField(20);
        add(pseudonymField, gbc);

        // nationality
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel(Localization.get("label.nationality")), gbc);
        gbc.gridx = 1;
        nationalityField = new JTextField(20);
        add(nationalityField, gbc);

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
            // Walidacja
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();

            if (firstName.isEmpty()) {
                JOptionPane.showMessageDialog(this, Localization.get("validation.firstName.required"), Localization.get("message.validation.error"), JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (firstName.length() > 100) {
                JOptionPane.showMessageDialog(this, Localization.get("validation.firstName.maxLength"), Localization.get("message.validation.error"), JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (lastName.isEmpty()) {
                JOptionPane.showMessageDialog(this, Localization.get("validation.lastName.required"), Localization.get("message.validation.error"), JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (lastName.length() > 100) {
                JOptionPane.showMessageDialog(this, Localization.get("validation.lastName.maxLength"), Localization.get("message.validation.error"), JOptionPane.WARNING_MESSAGE);
                return;
            }

            result = new Author();
            result.setFirstName(firstName);
            result.setLastName(lastName);
            result.setPseudonym(pseudonymField.getText().trim());
            result.setNationality(nationalityField.getText().trim());
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

    public Author showDialog() {
        setVisible(true);
        return result;
    }

    @Override
    public void onLanguageChanged() {
        setTitle(Localization.get("dialog.add.author.title"));
        ok.setText(Localization.get("button.ok"));
        cancel.setText(Localization.get("button.cancel"));
        revalidate();
        repaint();
    }
}
