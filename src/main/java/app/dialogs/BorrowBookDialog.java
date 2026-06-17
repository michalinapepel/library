package app.dialogs;

import app.LanguageChangeListener;
import app.Localization;
import domain.Book;
import domain.Loan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Dialog dla wypożyczającego — umożliwia wybranie książki, którą chce wypożyczyć.
 * Pokazywane są tylko książki dostępne (nieobjęte aktywnym wypożyczeniem).
 */
public class BorrowBookDialog extends JDialog implements LanguageChangeListener {

	private final List<Book> books = new ArrayList<>();
	private final List<Loan> loans = new ArrayList<>();
	private final List<Book> availableBooks = new ArrayList<>();

	private JTable booksTable;
	private JTextField searchField;
	private JComboBox<String> searchTypeCombo;
	private JButton searchButton;
	private JButton borrowButton;
	private JButton cancelButton;

	private Book result = null;

	public BorrowBookDialog(JFrame parent) {
		super(parent, Localization.get("dialog.add.loan.title"), true);
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
		borrowButton = new JButton(Localization.get("button.ok"));
		cancelButton = new JButton(Localization.get("button.cancel"));

		borrowButton.addActionListener(e -> borrowSelectedBook());
		cancelButton.addActionListener(e -> {
			result = null;
			dispose();
		});

		buttonPanel.add(borrowButton);
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

	private void loadAvailableBooks() {
		availableBooks.clear();
		for (Book b : books) {
			if (isBookAvailable(b)) {
				availableBooks.add(b);
			}
		}
		refreshTable();
	}

	private boolean isBookAvailable(Book book) {
		for (Loan loan : loans) {
			Book loanBook = loan.getBook();
			if (loanBook == null) continue;
			// Porównujemy referencję jeśli to ta sama instancja lub ISBN gdy obiekty mogą być różne
			boolean sameBook = loanBook == book;
			if (!sameBook) {
				String isbn1 = book.getIsbn();
				String isbn2 = loanBook.getIsbn();
				sameBook = isbn1 != null && !isbn1.isEmpty() && isbn1.equals(isbn2);
			}

			if (sameBook && !loan.isReturned()) {
				return false;
			}
		}
		return true;
	}

	private void searchBooks() {
		String searchText = searchField.getText().trim().toLowerCase();
		String searchType = (String) searchTypeCombo.getSelectedItem();

		availableBooks.clear();

		for (Book book : books) {
			if (!isBookAvailable(book)) continue;

			boolean matches = false;
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
			}

			if (matches) {
				availableBooks.add(book);
			}
		}

		refreshTable();
	}

	private void refreshTable() {
		DefaultTableModel model = (DefaultTableModel) booksTable.getModel();
		model.setRowCount(0);

		for (Book book : availableBooks) {
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
		if (books != null) this.books.addAll(books);
		loadAvailableBooks();
	}

	public void setLoans(List<Loan> loans) {
		this.loans.clear();
		if (loans != null) this.loans.addAll(loans);
		loadAvailableBooks();
	}

	/**
	 * Pokazuje dialog i zwraca wybraną książkę lub null gdy anulowano.
	 */
	public Book showDialog() {
		setVisible(true);
		return result;
	}

	@Override
	public void onLanguageChanged() {
		setTitle(Localization.get("dialog.add.loan.title"));
		searchButton.setText(Localization.get("button.search"));
		borrowButton.setText(Localization.get("button.ok"));
		cancelButton.setText(Localization.get("button.cancel"));
		revalidate();
		repaint();
	}
}
