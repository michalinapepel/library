package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Book;
import domain.Author;
import domain.Shelf;
import management.DataBaseBooks;

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
    private JSpinner shelfField;
    private JButton ok;
    private JButton cancel;
    private JButton delete;
    private boolean deleted = false;
    private final DataBaseBooks dbBooks;

    public EditBookDialog(JFrame parent, Book book) {
        super(parent, Localization.get("dialog.edit.book.title"), true);
        this.originalBook = book;
        this.dbBooks = new DataBaseBooks();
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

        // Authors (read-only - pokazuje autorów z bazy)
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel(Localization.get("label.author")), gbc);
        gbc.gridx = 1;
        authorField = new JTextField(20);
        authorField.setEditable(false);
        authorField.setBackground(Color.LIGHT_GRAY);
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
        shelfField = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
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
        delete = new JButton(Localization.get("button.delete"));
        delete.setForeground(Color.RED);

        ok.addActionListener(e -> {
            // Walidacja
            String title = titleField.getText().trim();
            String publisher = publisherField.getText().trim();
            String isbn = isbnField.getText().trim();

            if (title.isEmpty() || publisher.isEmpty() || isbn.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Wszystkie pola są wymagane!", "Błąd walidacji", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!isbn.matches("\\d{10}|\\d{13}")) {
                JOptionPane.showMessageDialog(this, "ISBN musi mieć 10 lub 13 cyfr!", "Błąd walidacji", JOptionPane.WARNING_MESSAGE);
                return;
            }

            result = originalBook;
            result.setTitle(title);
            result.setPublisher(publisher);
            result.setYearOfPublishing((Integer) yearSpinner.getValue());
            result.setIsbn(isbn);
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
            yearSpinner.setValue(originalBook.getPublicationYear());
            isbnField.setText(originalBook.getIsbn() != null ? originalBook.getIsbn() : "");

            // Pobierz i wyświetl autorów z bazy danych
            if (originalBook.getId() > 0) {
                java.util.List<Author> authors = dbBooks.getAuthorsForBook(originalBook.getId());
                if (!authors.isEmpty()) {
                    StringBuilder authorsStr = new StringBuilder();
                    for (Author author : authors) {
                        if (authorsStr.length() > 0) authorsStr.append("; ");
                        authorsStr.append(author.getFirstName()).append(" ").append(author.getLastName());
                    }
                    authorField.setText(authorsStr.toString());
                } else {
                    authorField.setText("Brak przypisanych autorów");
                }
            }
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


