package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Author;
import management.DataBaseAuthors;

import javax.swing.*;
import java.awt.*;

public class EditAuthorDialog extends JDialog implements LanguageChangeListener {

    private Author result = null;
    private final Author originalAuthor;
    private final DataBaseAuthors dbAuthors;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField pseudonymField;
    private JTextField nationalityField;
    private JButton ok;
    private JButton cancel;

    public EditAuthorDialog(JFrame parent, Author author) {
        super(parent, Localization.get("dialog.edit.author.title"), true);
        this.originalAuthor = author;
        this.dbAuthors = new DataBaseAuthors();
        Localization.addLanguageChangeListener(this);
        initComponents();
        loadAuthorData();
        setSize(500, 250);
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

        // Pseudonym
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel(Localization.get("label.pseudonym")), gbc);
        gbc.gridx = 1;
        pseudonymField = new JTextField(20);
        add(pseudonymField, gbc);

        // Nationality
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
            result = originalAuthor;
            result.setFirstName(firstNameField.getText().trim());
            result.setLastName(lastNameField.getText().trim());
            result.setPseudonym(pseudonymField.getText().trim());
            result.setNationality(nationalityField.getText().trim());
            dbAuthors.updateAuthor(result);
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

    private void loadAuthorData() {
        if (originalAuthor != null) {
            firstNameField.setText(originalAuthor.getFirstName() != null ? originalAuthor.getFirstName() : "");
            lastNameField.setText(originalAuthor.getLastName() != null ? originalAuthor.getLastName() : "");
            pseudonymField.setText(originalAuthor.getPseudonym() != null ? originalAuthor.getPseudonym() : "");
            nationalityField.setText(originalAuthor.getNationality() != null ? originalAuthor.getNationality() : "");
        }
    }

    public Author showDialog() {
        setVisible(true);
        return result;
    }

    @Override
    public void onLanguageChanged() {
        setTitle(Localization.get("dialog.edit.author.title"));
        ok.setText(Localization.get("button.ok"));
        cancel.setText(Localization.get("button.cancel"));
        revalidate();
        repaint();
    }
}

