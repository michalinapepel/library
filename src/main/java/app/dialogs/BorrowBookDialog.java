package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Book;
import domain.Bookcase;
import domain.Shelf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowBookDialog extends JDialog implements LanguageChangeListener {

    private final List<Book> books = new ArrayList<>();
    private final List<Book> availableBooks = new ArrayList<>();
    private final Shelf[] shelves;
    private final Bookcase[] bookcases;

    private JTable booksTable;
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;
    private JButton searchButton;
    private JButton borrowButton;
    private JButton locationButton;
    private JButton cancelButton;

    private Book result = null;

    public BorrowBookDialog(JFrame parent, Shelf[] shelves, Bookcase[] bookcases) {
        super(parent, Localization.get("dialog.add.loan.title"), true);
        this.shelves = shelves;
        this.bookcases = bookcases;
        Localization.addLanguageChangeListener(this);
        initComponents();
        setSize(850, 500);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel(Localization.get("label.search")));
        searchTypeCombo = new JComboBox<>(new String[]{
                Localization.get("label.title"),
                Localization.get("label.author"),
                Localization.get("label.publisher"),
                Localization.get("label.isbn"),
                Localization.get("label.section")
        });
        searchField = new JTextField(20);
        searchButton = new JButton(Localization.get("button.search"));
        searchPanel.add(searchTypeCombo);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {
                Localization.get("label.title"),
                Localization.get("label.author"),
                Localization.get("label.publisher"),
                Localization.get("label.year"),
                Localization.get("label.isbn"),
                Localization.get("label.section")
        };
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        booksTable = new JTable(tableModel);
        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(booksTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        borrowButton = new JButton(Localization.get("button.ok"));
        locationButton = new JButton(Localization.get("label.location"));
        cancelButton = new JButton(Localization.get("button.cancel"));

        borrowButton.addActionListener(e -> borrowSelectedBook());
        locationButton.addActionListener(e -> showBookLocation());
        cancelButton.addActionListener(e -> {
            result = null;
            dispose();
        });

        buttonPanel.add(borrowButton);
        buttonPanel.add(locationButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> searchBooks());
        loadAvailableBooks();
    }

    private void borrowSelectedBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, Localization.get("message.select.book"));
            return;
        }
        result = availableBooks.get(selectedRow);
        dispose();
    }

    private void showBookLocation() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, Localization.get("message.select.book"));
            return;
        }

        Book book = availableBooks.get(selectedRow);
        Shelf foundShelf = null;
        for (Shelf shelf : shelves) {
            if (shelf.getId() == book.getShelfId()) {
                foundShelf = shelf;
                break;
            }
        }

        if (foundShelf == null) {
            JOptionPane.showMessageDialog(this,
                book.getTitle() + "\n\n" + "Brak przypisanej półki.",
                Localization.get("label.location"),
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String bookcaseName = foundShelf.getBookcaseName() != null ? foundShelf.getBookcaseName() : "—";
        String shelfName = foundShelf.getName() != null ? foundShelf.getName() : "—";

        JOptionPane.showMessageDialog(this,
            "<html><b>" + book.getTitle() + "</b><br><br>" +
            Localization.get("label.bookcase") + ": <b>" + bookcaseName + "</b><br>" +
            Localization.get("label.shelf") + ": <b>" + shelfName + "</b></html>",
            Localization.get("label.location"),
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadAvailableBooks() {
        availableBooks.clear();
        availableBooks.addAll(books);
        refreshTable();
    }

    private void searchBooks() {
        String searchText = searchField.getText().trim().toLowerCase();
        String searchType = (String) searchTypeCombo.getSelectedItem();

        availableBooks.clear();

        for (Book book : books) {
            boolean matches;
            if (searchText.isEmpty()) {
                matches = true;
            } else if (Localization.get("label.title").equals(searchType)) {
                matches = book.getTitle() != null && book.getTitle().toLowerCase().contains(searchText);
            } else if (Localization.get("label.author").equals(searchType)) {
                matches = book.getAuthorsAsString() != null && book.getAuthorsAsString().toLowerCase().contains(searchText);
            } else if (Localization.get("label.publisher").equals(searchType)) {
                matches = book.getPublisher() != null && book.getPublisher().toLowerCase().contains(searchText);
            } else if (Localization.get("label.isbn").equals(searchType)) {
                matches = book.getIsbn() != null && book.getIsbn().toLowerCase().contains(searchText);
            } else if (Localization.get("label.section").equals(searchType)) {
                matches = book.getSectionsAsString() != null && book.getSectionsAsString().toLowerCase().contains(searchText);
            } else {
                matches = false;
            }

            if (matches) availableBooks.add(book);
        }

        refreshTable();
    }

    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) booksTable.getModel();
        model.setRowCount(0);

        for (Book book : availableBooks) {
            model.addRow(new Object[]{
                    book.getTitle(),
                    book.getAuthorsAsString(),
                    book.getPublisher(),
                    book.getPublicationYear(),
                    book.getIsbn(),
                    book.getSectionsAsString()
            });
        }
    }

    public void setBooks(List<Book> books) {
        this.books.clear();
        if (books != null) this.books.addAll(books);
        loadAvailableBooks();
    }

    public Book showDialog() {
        setVisible(true);
        return result;
    }

    @Override
    public void onLanguageChanged() {
        setTitle(Localization.get("dialog.add.loan.title"));
        searchButton.setText(Localization.get("button.search"));
        borrowButton.setText(Localization.get("button.ok"));
        locationButton.setText(Localization.get("label.location"));
        cancelButton.setText(Localization.get("button.cancel"));
        revalidate();
        repaint();
    }
}
