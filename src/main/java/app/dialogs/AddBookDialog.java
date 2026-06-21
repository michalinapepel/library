package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Author;
import domain.Book;
import domain.Bookcase;
import domain.Section;
import domain.Shelf;
import management.DataBaseAuthors;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa okna dialogowego dodawania książki
 */
public class AddBookDialog extends JDialog implements LanguageChangeListener {

    private final Author[] authors;
    private final Shelf[] shelves;
    private final Bookcase[] bookcases;
    private final Section[] sections;
    private final DataBaseAuthors dbAuthors = new DataBaseAuthors();
    // Flaga zapobiegająca rekurencyjnemu wywoływaniu listenerów regał↔półka.
    // Gdy jeden combo zmienia drugi programowo, flaga blokuje listener drugiego.
    private boolean updatingCombos = false;
    private Book result = null;
    private JTextField titleField;
    private JComboBox<Author> authorField;
    private JButton addAuthorBtn;
    private JTextField publisherField;
    private JSpinner yearSpinner;
    private JTextField isbnField;
    private JComboBox<Bookcase> bookcaseCombo;
    private JComboBox<Shelf> shelfCombo;
    private JComboBox<Section> sectionCombo;
    private JButton ok;
    private JButton cancel;

    public AddBookDialog(JFrame parent, Author[] authors, Shelf[] shelves, Bookcase[] bookcases, Section[] sections) {
        super(parent, Localization.get("dialog.add.book.title"), true);
        this.authors = authors;
        this.shelves = shelves;
        this.bookcases = bookcases;
        this.sections = sections;
        Localization.addLanguageChangeListener(this);
        initComponents();
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

        // Author + add button
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel(Localization.get("label.author")), gbc);
        gbc.gridx = 1;
        authorField = new JComboBox<>(authors);
        addAuthorBtn = new JButton("+");
        addAuthorBtn.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        addAuthorBtn.setMargin(new Insets(2, 8, 2, 8));
        addAuthorBtn.setToolTipText(Localization.get("tooltip.author.add"));
        addAuthorBtn.addActionListener(e -> {
            AddAuthorDialog authorDialog = new AddAuthorDialog((JFrame) SwingUtilities.getWindowAncestor(this));
            Author newAuthor = authorDialog.showDialog();
            if (newAuthor != null) {
                dbAuthors.addAuthor(newAuthor);
                authorField.addItem(newAuthor);
                authorField.setSelectedItem(newAuthor);
            }
        });
        JPanel authorPanel = new JPanel(new BorderLayout(3, 0));
        authorPanel.add(authorField, BorderLayout.CENTER);
        authorPanel.add(addAuthorBtn, BorderLayout.EAST);
        add(authorPanel, gbc);

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
        Bookcase[] bookcasesWithNone = buildBookcasesWithNone();
        bookcaseCombo = new JComboBox<>(bookcasesWithNone);
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

            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, Localization.get("validation.title.required"), Localization.get("message.validation.error"), JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (title.length() > 255) {
                JOptionPane.showMessageDialog(this, Localization.get("validation.title.maxLength"), Localization.get("message.validation.error"), JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (publisher.isEmpty()) {
                JOptionPane.showMessageDialog(this, Localization.get("validation.publisher.required"), Localization.get("message.validation.error"), JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (publisher.length() > 255) {
                JOptionPane.showMessageDialog(this, Localization.get("validation.publisher.maxLength"), Localization.get("message.validation.error"), JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (isbn.isEmpty()) {
                JOptionPane.showMessageDialog(this, Localization.get("validation.isbn.required"), Localization.get("message.validation.error"), JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!isbn.matches("\\d{10}|\\d{13}")) {
                JOptionPane.showMessageDialog(this, Localization.get("validation.isbn.format"), Localization.get("message.validation.error"), JOptionPane.WARNING_MESSAGE);
                return;
            }

            result = new Book();
            result.setTitle(title);

            Author selectedAuthor = (Author) authorField.getSelectedItem();
            if (selectedAuthor == null) {
                int confirm = JOptionPane.showConfirmDialog(this,
                    Localization.get("message.no.author.add"), Localization.get("dialog.no.author.title"),
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (confirm != JOptionPane.YES_OPTION) return;
                result.setAuthors(new Author[0]);
            } else {
                result.setAuthors(new Author[]{selectedAuthor});
            }

            result.setPublisher(publisher);
            result.setYearOfPublishing((Integer) yearSpinner.getValue());
            result.setIsbn(isbn);

            Shelf selectedShelf = (Shelf) shelfCombo.getSelectedItem();
            if (selectedShelf != null) {
                result.setShelfId(selectedShelf.getId());
            }

            Section selectedSection = (Section) sectionCombo.getSelectedItem();
            if (selectedSection != null && selectedSection.getId() > 0) {
                List<Section> sel = new ArrayList<>();
                sel.add(selectedSection);
                result.setSections(sel);
            }

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
        DefaultComboBoxModel<Shelf> model = new DefaultComboBoxModel<>();
        for (Shelf shelf : shelves) {
            if (selected == null || selected.getId() == 0 || shelf.getBookcaseId() == selected.getId()) {
                model.addElement(shelf);
            }
        }
        shelfCombo.setModel(model);
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

    public Book showDialog() {
        setVisible(true);
        return result;
    }

    @Override
    public void onLanguageChanged() {
        setTitle(Localization.get("dialog.add.book.title"));
        ok.setText(Localization.get("button.ok"));
        cancel.setText(Localization.get("button.cancel"));
        addAuthorBtn.setToolTipText(Localization.get("tooltip.author.add"));
        revalidate();
        repaint();
    }
}
