package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Author;
import domain.Book;
import domain.Bookcase;
import domain.Section;
import domain.Shelf;
import management.DataBaseBooks;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa okna dialogowego edytowania książki
 */
public class EditBookDialog extends JDialog implements LanguageChangeListener {

    private Book result = null;
    private final Book originalBook;
    private final Author[] authors;
    private final Shelf[] shelves;
    private final Bookcase[] bookcases;
    private final Section[] sections;
    private JTextField titleField;
    private JComboBox<Author> authorField;
    private JTextField publisherField;
    private JSpinner yearSpinner;
    private JTextField isbnField;
    private JComboBox<Bookcase> bookcaseCombo;
    private JComboBox<Shelf> shelfCombo;
    private JComboBox<Section> sectionCombo;
    private JButton ok;
    private JButton cancel;
    private final DataBaseBooks dbBooks;
    // Flaga zapobiegająca rekurencyjnemu wywoływaniu listenerów regał↔półka.
    private boolean updatingCombos = false;

    public EditBookDialog(JFrame parent, Book book, Author[] authors, Shelf[] shelves, Bookcase[] bookcases, Section[] sections) {
        super(parent, Localization.get("dialog.edit.book.title"), true);
        this.originalBook = book;
        this.dbBooks = new DataBaseBooks();
        this.authors = authors;
        this.shelves = shelves;
        this.bookcases = bookcases;
        this.sections = sections;
        Localization.addLanguageChangeListener(this);
        initComponents();
        loadBookData();
        setSize(500, 410);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel(Localization.get("label.title")), gbc);
        gbc.gridx = 1;
        titleField = new JTextField(20);
        add(titleField, gbc);

        // Author
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel(Localization.get("label.author")), gbc);
        gbc.gridx = 1;
        authorField = new JComboBox<>(authors);
        add(authorField, gbc);

        // Publisher
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel(Localization.get("label.publisher")), gbc);
        gbc.gridx = 1;
        publisherField = new JTextField(20);
        add(publisherField, gbc);

        // Year
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel(Localization.get("label.year")), gbc);
        gbc.gridx = 1;
        int currentYear = java.time.Year.now().getValue();
        yearSpinner = new JSpinner(new SpinnerNumberModel(currentYear, 1000, currentYear, 1));
        add(yearSpinner, gbc);

        // ISBN
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel(Localization.get("label.isbn")), gbc);
        gbc.gridx = 1;
        isbnField = new JTextField(20);
        add(isbnField, gbc);

        // Bookcase
        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel(Localization.get("label.bookcase")), gbc);
        gbc.gridx = 1;
        bookcaseCombo = new JComboBox<>(buildBookcasesWithNone());
        add(bookcaseCombo, gbc);

        // Shelf (filtered by bookcase)
        gbc.gridx = 0; gbc.gridy = 6;
        add(new JLabel(Localization.get("label.shelf")), gbc);
        gbc.gridx = 1;
        shelfCombo = new JComboBox<>();
        filterShelvesByBookcase();
        add(shelfCombo, gbc);

        bookcaseCombo.addActionListener(e -> {
            if (!updatingCombos) {
                updatingCombos = true;
                filterShelvesByBookcase();
                updatingCombos = false;
            }
        });
        shelfCombo.addActionListener(e -> {
            if (!updatingCombos) {
                Shelf selected = (Shelf) shelfCombo.getSelectedItem();
                if (selected != null) {
                    updatingCombos = true;
                    for (int i = 0; i < bookcaseCombo.getItemCount(); i++) {
                        if (bookcaseCombo.getItemAt(i).getId() == selected.getBookcaseId()) {
                            bookcaseCombo.setSelectedIndex(i);
                            break;
                        }
                    }
                    updatingCombos = false;
                }
            }
        });

        // Section
        gbc.gridx = 0; gbc.gridy = 7;
        add(new JLabel(Localization.get("label.section")), gbc);
        gbc.gridx = 1;
        sectionCombo = new JComboBox<>(buildSectionsWithNone());
        add(sectionCombo, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel();
        ok = new JButton(Localization.get("button.ok"));
        cancel = new JButton(Localization.get("button.cancel"));

        ok.addActionListener(e -> {
            String title = titleField.getText().trim();
            String publisher = publisherField.getText().trim();
            String isbn = isbnField.getText().trim();

            if (title.isEmpty() || publisher.isEmpty() || isbn.isEmpty()) {
                JOptionPane.showMessageDialog(this, Localization.get("validation.all.required"), Localization.get("message.validation.error"), JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!isbn.matches("\\d{10}|\\d{13}")) {
                JOptionPane.showMessageDialog(this, Localization.get("validation.isbn.format"), Localization.get("message.validation.error"), JOptionPane.WARNING_MESSAGE);
                return;
            }

            result = originalBook;
            result.setTitle(title);
            result.setPublisher(publisher);
            result.setYearOfPublishing((Integer) yearSpinner.getValue());
            result.setIsbn(isbn);

            Author selectedAuthor = (Author) authorField.getSelectedItem();
            if (selectedAuthor == null) {
                int confirm = JOptionPane.showConfirmDialog(this,
                    Localization.get("message.no.author.edit"), Localization.get("dialog.no.author.title"),
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (confirm != JOptionPane.YES_OPTION) return;
                result.setAuthors(new Author[0]);
            } else {
                result.setAuthors(new Author[]{selectedAuthor});
            }

            Shelf selectedShelf = (Shelf) shelfCombo.getSelectedItem();
            result.setShelfId(selectedShelf != null ? selectedShelf.getId() : null);

            Section selectedSection = (Section) sectionCombo.getSelectedItem();
            List<Section> selectedSections = new ArrayList<>();
            if (selectedSection != null && selectedSection.getId() > 0) {
                selectedSections.add(selectedSection);
            }
            result.setSections(selectedSections);

            dbBooks.updateBook(result);
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

    private void filterShelvesByBookcase() {
        Bookcase selected = (Bookcase) bookcaseCombo.getSelectedItem();
        Shelf currentShelf = (Shelf) shelfCombo.getSelectedItem();
        DefaultComboBoxModel<Shelf> model = new DefaultComboBoxModel<>();
        for (Shelf shelf : shelves) {
            if (selected == null || selected.getId() == 0 || shelf.getBookcaseId() == selected.getId()) {
                model.addElement(shelf);
            }
        }
        shelfCombo.setModel(model);
        // Restore previous shelf selection if it's still in the filtered list
        if (currentShelf != null) {
            for (int i = 0; i < shelfCombo.getItemCount(); i++) {
                if (shelfCombo.getItemAt(i).getId() == currentShelf.getId()) {
                    shelfCombo.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private Bookcase[] buildBookcasesWithNone() {
        Bookcase[] result = new Bookcase[bookcases.length + 1];
        result[0] = new Bookcase(0, "—");
        System.arraycopy(bookcases, 0, result, 1, bookcases.length);
        return result;
    }

    private Section[] buildSectionsWithNone() {
        Section[] result = new Section[sections.length + 1];
        result[0] = new Section(0, "-", "-");
        System.arraycopy(sections, 0, result, 1, sections.length);
        return result;
    }

    private void loadBookData() {
        if (originalBook == null) return;

        titleField.setText(originalBook.getTitle() != null ? originalBook.getTitle() : "");
        publisherField.setText(originalBook.getPublisher() != null ? originalBook.getPublisher() : "");
        yearSpinner.setValue(originalBook.getPublicationYear());
        isbnField.setText(originalBook.getIsbn() != null ? originalBook.getIsbn() : "");

        // Find current shelf, derive bookcase from it
        int bookShelfId = originalBook.getShelfId() != null ? originalBook.getShelfId() : 0;
        Shelf currentShelf = null;
        for (Shelf shelf : shelves) {
            if (shelf.getId() == bookShelfId) {
                currentShelf = shelf;
                break;
            }
        }

        // Block listeners while we set both combos manually
        updatingCombos = true;

        if (currentShelf != null) {
            // Select the bookcase
            int bookcaseId = currentShelf.getBookcaseId();
            for (int i = 0; i < bookcaseCombo.getItemCount(); i++) {
                if (bookcaseCombo.getItemAt(i).getId() == bookcaseId) {
                    bookcaseCombo.setSelectedIndex(i);
                    break;
                }
            }
            // Rebuild shelf list for that bookcase, then select the right shelf
            filterShelvesByBookcase();
            for (int i = 0; i < shelfCombo.getItemCount(); i++) {
                if (shelfCombo.getItemAt(i).getId() == currentShelf.getId()) {
                    shelfCombo.setSelectedIndex(i);
                    break;
                }
            }
        }

        updatingCombos = false;

        // Author
        if (originalBook.getId() > 0) {
            List<Author> bookAuthors = dbBooks.getAuthorsForBook(originalBook.getId());
            authorField.setSelectedItem(!bookAuthors.isEmpty() ? bookAuthors.get(0) : null);
        }

        // Section
        List<Section> bookSections = originalBook.getSections();
        if (!bookSections.isEmpty()) {
            int sectionId = bookSections.get(0).getId();
            for (int i = 0; i < sectionCombo.getItemCount(); i++) {
                if (sectionCombo.getItemAt(i).getId() == sectionId) {
                    sectionCombo.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    public Book showDialog() {
        setVisible(true);
        return result;
    }

    @Override
    public void onLanguageChanged() {
        setTitle(Localization.get("dialog.edit.book.title"));
        ok.setText(Localization.get("button.ok"));
        cancel.setText(Localization.get("button.cancel"));
        revalidate();
        repaint();
    }
}
