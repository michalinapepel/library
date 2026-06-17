package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Book;

import javax.swing.*;
import java.awt.*;

public class AddBookDialog extends JDialog implements LanguageChangeListener {

    private Book result = null;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField publisherField;
    private JSpinner yearSpinner;
    private JTextField isbnField;
    private JTextField shelfField;
    private JButton ok;
    private JButton cancel;

    public AddBookDialog(JFrame parent) {
        super(parent, Localization.get("dialog.add.book.title"), true);
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

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel(Localization.get("label.title")), gbc);
        gbc.gridx = 1;
        titleField = new JTextField(20);
        add(titleField, gbc);

        // Authors
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel(Localization.get("label.author")), gbc);
        gbc.gridx = 1;
        authorField = new JTextField(20);
        add(authorField, gbc);

        // Publisher
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel(Localization.get("label.publisher")), gbc);
        gbc.gridx = 1;
        publisherField = new JTextField(20);
        add(publisherField, gbc);

        // Year of Publishing
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel(Localization.get("label.year")), gbc);
        gbc.gridx = 1;
        yearSpinner = new JSpinner(new SpinnerNumberModel(2024, 1000, 9999, 1));
        add(yearSpinner, gbc);

        // ISBN
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel(Localization.get("label.isbn")), gbc);
        gbc.gridx = 1;
        isbnField = new JTextField(20);
        add(isbnField, gbc);

        // Shelf
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel(Localization.get("label.shelf")), gbc);
        gbc.gridx = 1;
        shelfField = new JTextField(20);
        add(shelfField, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel();
        ok = new JButton(Localization.get("button.ok"));
        cancel = new JButton(Localization.get("button.cancel"));

        ok.addActionListener(e -> {
            result = new Book();
            result.setTitle(titleField.getText().trim());
            result.setPublisher(publisherField.getText().trim());
            result.setYearOfPublishing((Integer) yearSpinner.getValue());
            result.setIsbn(isbnField.getText().trim());
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

    public Book showDialog() {
        setVisible(true);
        return result;
    }

    @Override
    public void onLanguageChanged() {
        setTitle(Localization.get("dialog.add.book.title"));
        ok.setText(Localization.get("button.ok"));
        cancel.setText(Localization.get("button.cancel"));
        revalidate();
        repaint();
    }
}
