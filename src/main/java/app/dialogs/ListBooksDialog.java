package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ListBooksDialog extends JDialog implements LanguageChangeListener {

    private final List<Book> books = new ArrayList<>();
    private final List<Book> filteredBooks = new ArrayList<>();
    private JTable booksTable;
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;
    private JButton search;
    private JButton close;
    private JButton edit;

    public ListBooksDialog(JFrame parent) {
        super(parent, Localization.get("dialog.list.books.title"), true);
        Localization.addLanguageChangeListener(this);
        initComponents();
        setSize(700, 500);
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
            Localization.get("label.isbn")
        });
        searchField = new JTextField(20);
        search = new JButton(Localization.get("button.search"));
        searchPanel.add(searchTypeCombo);
        searchPanel.add(searchField);
        searchPanel.add(search);
        add(searchPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {
            Localization.get("label.title"),
            Localization.get("label.author"),
            Localization.get("label.publisher"),
            Localization.get("label.year"),
            Localization.get("label.isbn"),
                Localization.get("label.shelf")
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
         edit = new JButton(Localization.get("button.edit"));
         close = new JButton(Localization.get("button.close"));

         edit.addActionListener(e -> editSelectedBook());
         close.addActionListener(e -> dispose());

         buttonPanel.add(edit);
         buttonPanel.add(close);
         add(buttonPanel, BorderLayout.SOUTH);

        search.addActionListener(e -> searchBooks());
         loadAllBooks();
     }

     private void editSelectedBook() {
         int selectedRow = booksTable.getSelectedRow();
         if (selectedRow == -1) {
             JOptionPane.showMessageDialog(this, Localization.get("message.select.book"));
             return;
         }

         Book selectedBook = filteredBooks.get(selectedRow);
          EditBookDialog dialog = new EditBookDialog((JFrame) SwingUtilities.getWindowAncestor(this), selectedBook);
          Book editedBook = dialog.showDialog();

          if (dialog.isDeleted()) {
              books.remove(selectedBook);
              loadAllBooks();
          } else if (editedBook != null) {
              refreshTable();
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
            boolean matches = false;
            
            if (searchText.isEmpty()) {
                matches = true;
            } else if (Localization.get("label.title").equals(searchType)) {
                matches = book.getTitle() != null && book.getTitle().toLowerCase().contains(searchText);
            } else if (Localization.get("label.title").equals(searchType)) {
                matches = book.getAuthorsAsString() != null && book.getAuthorsAsString().toLowerCase().contains(searchText);
            } else if (Localization.get("label.publisher").equals(searchType)) {
                matches = book.getPublisher() != null && book.getPublisher().toLowerCase().contains(searchText);
            } else if (Localization.get("label.isbn").equals(searchType)) {
                matches = book.getIsbn() != null && book.getIsbn().toLowerCase().contains(searchText);
            }
            
            if (matches) {
                filteredBooks.add(book);
            }
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
                    book.getShelfId()
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
         close.setText(Localization.get("button.close"));
         revalidate();
         repaint();
     }
}
