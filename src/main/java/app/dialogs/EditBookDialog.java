package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Book;

import javax.swing.*;
import java.awt.*;

public class EditBookDialog extends JDialog implements LanguageChangeListener {

    private Book result = null;
    private final Book originalBook;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField publisherField;
    private JSpinner yearSpinner;
    private JTextField isbnField;
    private JButton ok;
    private JButton cancel;
    private JButton delete;
    private boolean deleted = false;

    public EditBookDialog(JFrame parent, Book book) {
        super(parent, Localization.get("dialog.edit.book.title"), true);
        this.originalBook = book;
        Localization.addLanguageChangeListener(this);
        initComponents();
        loadBookData();
        setSize(500, 300);
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

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel();
        ok = new JButton(Localization.get("button.ok"));
        cancel = new JButton(Localization.get("button.cancel"));
        delete = new JButton(Localization.get("button.delete"));
        delete.setForeground(Color.RED);

        ok.addActionListener(e -> {
            result = originalBook;
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

        delete.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, Localization.get("message.confirm.delete"));
            if (confirm == JOptionPane.YES_OPTION) {
                deleted = true;
                dispose();
            }
        });

        buttonPanel.add(ok);
        buttonPanel.add(cancel);
        buttonPanel.add(delete);
        add(buttonPanel, gbc);
    }

    private void loadBookData() {
        if (originalBook != null) {
            titleField.setText(originalBook.getTitle() != null ? originalBook.getTitle() : "");
            publisherField.setText(originalBook.getPublisher() != null ? originalBook.getPublisher() : "");
            yearSpinner.setValue(originalBook.getYearOfPublishing());
            isbnField.setText(originalBook.getIsbn() != null ? originalBook.getIsbn() : "");
        }
    }

    public Book showDialog() {
        setVisible(true);
        return result;
    }

    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void onLanguageChanged() {
        setTitle(Localization.get("dialog.edit.book.title"));
        ok.setText(Localization.get("button.ok"));
        cancel.setText(Localization.get("button.cancel"));
        delete.setText(Localization.get("button.delete"));
        revalidate();
        repaint();
    }
}
