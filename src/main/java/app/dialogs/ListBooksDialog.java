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
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ListBooksDialog extends JDialog implements LanguageChangeListener {

    private final List<Book> books = new ArrayList<>();
    private final List<Book> filteredBooks = new ArrayList<>();
    private final Author[] authors;
    private final Shelf[] shelves;
    private final Bookcase[] bookcases;
    private final Section[] sections;
    private final DataBaseBooks dbBooks = new DataBaseBooks();
    private JTable booksTable;
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;
    private JButton search;
    private JButton close;
    private JButton edit;
    private JButton delete;
    private JButton location;

    public ListBooksDialog(JFrame parent, Author[] authors, Shelf[] shelves, Bookcase[] bookcases, Section[] sections) {
        super(parent, Localization.get("dialog.list.books.title"), true);
        this.authors = authors;
        this.shelves = shelves;
        this.bookcases = bookcases;
        this.sections = sections;
        Localization.addLanguageChangeListener(this);
        initComponents();
        setSize(800, 500);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

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
        search = new JButton(Localization.get("button.search"));
        searchPanel.add(searchTypeCombo);
        searchPanel.add(searchField);
        searchPanel.add(search);
        add(searchPanel, BorderLayout.NORTH);

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
        booksTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(booksTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        edit = new JButton(Localization.get("button.edit"));
        close = new JButton(Localization.get("button.close"));

        location = new JButton(Localization.get("label.location"));
        delete = new JButton(Localization.get("button.delete"));
        delete.setForeground(Color.RED);
        edit.addActionListener(e -> editSelectedBook());
        location.addActionListener(e -> showBookLocation());
        delete.addActionListener(e -> deleteSelectedBooks());
        close.addActionListener(e -> dispose());

        buttonPanel.add(edit);
        buttonPanel.add(location);
        buttonPanel.add(delete);
        buttonPanel.add(close);
        add(buttonPanel, BorderLayout.SOUTH);

        search.addActionListener(e -> searchBooks());
        loadAllBooks();
    }

    private void showBookLocation() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, Localization.get("message.select.book"));
            return;
        }

        Book book = filteredBooks.get(selectedRow);
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

    private void editSelectedBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, Localization.get("message.select.book"));
            return;
        }

        Book selectedBook = filteredBooks.get(selectedRow);
        EditBookDialog dialog = new EditBookDialog((JFrame) SwingUtilities.getWindowAncestor(this), selectedBook, authors, shelves, bookcases, sections);
        Book editedBook = dialog.showDialog();

        if (editedBook != null) {
            refreshTable();
        }
    }

    private void deleteSelectedBooks() {
        int[] selectedRows = booksTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, Localization.get("message.select.book"));
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Czy na pewno chcesz usunąć zaznaczone pozycje?",
            "Potwierdź usunięcie",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            List<Book> toDelete = new ArrayList<>();
            for (int row : selectedRows) toDelete.add(filteredBooks.get(row));
            for (Book book : toDelete) {
                dbBooks.deleteBook(book.getId());
                books.remove(book);
            }
            loadAllBooks();
        }
    }

    private void loadAllBooks() {
        filteredBooks.clear();
        filteredBooks.addAll(books);
        refreshTable();
    }

    private void searchBooks() {
        String searchText = searchField.getText().trim().toLowerCase();
        String searchType = (String) searchTypeCombo.getSelectedItem();

        filteredBooks.clear();

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

            if (matches) filteredBooks.add(book);
        }

        refreshTable();
    }

    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) booksTable.getModel();
        model.setRowCount(0);

        for (Book book : filteredBooks) {
            Object[] row = {
                book.getTitle(),
                book.getAuthorsAsString(),
                book.getPublisher(),
                book.getPublicationYear(),
                book.getIsbn(),
                book.getSectionsAsString()
            };
            model.addRow(row);
        }
    }

    public void setBooks(List<Book> books) {
        this.books.clear();
        this.books.addAll(books);
        loadAllBooks();
    }

    public void showDialog() {
        setVisible(true);
    }

    @Override
    public void onLanguageChanged() {
        setTitle(Localization.get("dialog.list.books.title"));
        search.setText(Localization.get("button.search"));
        edit.setText(Localization.get("button.edit"));
        location.setText(Localization.get("label.location"));
        delete.setText(Localization.get("button.delete"));
        close.setText(Localization.get("button.close"));
        revalidate();
        repaint();
    }
}
