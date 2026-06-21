package management;

import domain.Author;
import domain.Book;
import domain.Section;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.Debug;

/**
 * Zapewnia dostęp do danych książek w bazie danych.
 * <p>
 * Oprócz podstawowych operacji CRUD obsługuje również powiązania książek z
 * autorami i działami (sekcjami) w tabelach łączących.
 */
public class DataBaseBooks {

	/**
	 * Dodaje nową książkę do bazy danych wraz z jej powiązaniami z autorami i
	 * działami.
	 *
	 * @param book książka do dodania
	 */
	public void addBook(Book book) {
		String sql = "INSERT INTO book(title, publisher, publication_year, isbn, shelf_id) VALUES (?, ?, ?, ?, ?)";
		try (Connection connection = DatabaseConnection.getConnection()) {
			try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
				connection.setAutoCommit(false);
				statement.setString(1, book.getTitle());
				statement.setString(2, book.getPublisher());
				statement.setInt(3, book.getPublicationYear());
				statement.setString(4, book.getIsbn());
				if (book.getShelfId() == null) {
					statement.setNull(5, Types.INTEGER);
				} else {
					statement.setInt(5, book.getShelfId());
				}
				statement.executeUpdate();

				try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						int bookId = generatedKeys.getInt(1);

						if (book.getAuthors() != null) {
							String sqlAuthor = "INSERT INTO book_author(book_id, author_id) VALUES (?, ?)";
							try (PreparedStatement authorStmt = connection.prepareStatement(sqlAuthor)) {
								for (Author author : book.getAuthors()) {
									if (author == null)
										continue;
									authorStmt.setInt(1, bookId);
									authorStmt.setInt(2, author.getId());
									authorStmt.executeUpdate();
								}
							}
						}

						List<Section> sections = book.getSections();
						if (sections != null && !sections.isEmpty()) {
							String sqlSection = "INSERT INTO book_section(book_id, section_id) VALUES (?, ?)";
							try (PreparedStatement sectionStmt = connection.prepareStatement(sqlSection)) {
								for (Section section : sections) {
									sectionStmt.setInt(1, bookId);
									sectionStmt.setInt(2, section.getId());
									sectionStmt.executeUpdate();
								}
							}
						}
					}
				}
				connection.commit();
			} catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException rollbackException) {
					Debug.error("Błąd podczas rollback przy dodawaniu książki.", rollbackException);
				}

				Debug.error("Błąd przy dodawaniu książki: " + book.getTitle(), e);
				Debug.showErrorWindow("Wystąpił problem z bazą danych. Skontaktuj się z Administratorem");
			}

		} catch (SQLException e) {

			Debug.error("Błąd przy dodawaniu książki: ", e);
			Debug.showErrorWindow("Wystąpił problem z połączeniem z bazą danych. Skontaktuj się z Administratorem");
		}
	}

	// 3 zapytania zbiorcze zamiast 1+N+N — eliminuje problem N+1 przy ładowaniu
	// listy książek.
	/**
	 * Pobiera wszystkie książki z bazy danych wraz z ich autorami i działami.
	 * <p>
	 * Dane są wczytywane za pomocą trzech zapytań zbiorczych (książki, autorzy,
	 * działy), aby uniknąć problemu zapytań typu N+1.
	 *
	 * @return lista książek; pusta lista, jeśli brak danych
	 */
	public List<Book> getAllBooks() {
		List<Book> books = new ArrayList<>();
		Map<Integer, Book> bookMap = new HashMap<>();

		String sqlBooks = """
				SELECT id, title, publisher, publication_year, isbn, shelf_id
				FROM book
				ORDER BY id
				""";
		String sqlAuthors = """
				SELECT ba.book_id, a.id, a.first_name, a.last_name, a.pseudonym, a.nationality
				FROM book_author ba
				INNER JOIN authors a ON a.id = ba.author_id
				ORDER BY ba.book_id, a.id
				""";
		String sqlSections = """
				SELECT bs.book_id, s.id, s.key, s.description
				FROM book_section bs
				INNER JOIN section s ON s.id = bs.section_id
				ORDER BY bs.book_id, s.id
				""";

		try (Connection connection = DatabaseConnection.getConnection()) {
			try (PreparedStatement stmt = connection.prepareStatement(sqlBooks); ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int bookId = rs.getInt("id");
					Book book = new Book(bookId, rs.getString("title"), rs.getString("publisher"),
							rs.getInt("publication_year"), rs.getString("isbn"),
							rs.getObject("shelf_id") == null ? null : rs.getInt("shelf_id"));
					books.add(book);
					bookMap.put(bookId, book);
				}
			}

			Map<Integer, List<Author>> authorsMap = new HashMap<>();
			try (PreparedStatement stmt = connection.prepareStatement(sqlAuthors); ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int bookId = rs.getInt("book_id");
					authorsMap.computeIfAbsent(bookId, k -> new ArrayList<>())
							.add(new Author(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
									rs.getString("pseudonym"), rs.getString("nationality")));
				}
			}

			Map<Integer, List<Section>> sectionsMap = new HashMap<>();
			try (PreparedStatement stmt = connection.prepareStatement(sqlSections);
					ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int bookId = rs.getInt("book_id");
					sectionsMap.computeIfAbsent(bookId, k -> new ArrayList<>())
							.add(new Section(rs.getInt("id"), rs.getString("key"), rs.getString("description")));
				}
			}

			for (Book book : books) {
				List<Author> authors = authorsMap.getOrDefault(book.getId(), new ArrayList<>());
				if (!authors.isEmpty())
					book.setAuthors(authors.toArray(new Author[0]));
				book.setSections(sectionsMap.getOrDefault(book.getId(), new ArrayList<>()));
			}

		} catch (SQLException e) {
			Debug.error("Błąd przy zaciąganiu książek: ", e);
			Debug.showErrorWindow("Wystąpił problem z połączeniem z bazą danych. Skontaktuj się z Administratorem");
		}
		return books;
	}

	/**
	 * Usuwa książkę wraz z jej powiązaniami z autorami i działami.
	 * <p>
	 * Uwaga: przed wywołaniem tej metody należy usunąć powiązane wypożyczenia (np.
	 * {@code DataBaseLoans#deleteLoansByBook(int)}).
	 *
	 * @param bookId identyfikator książki do usunięcia
	 */
	public void deleteBook(int bookId) {
		String deleteAuthors = "DELETE FROM book_author WHERE book_id = ?";
		String deleteSections = "DELETE FROM book_section WHERE book_id = ?";
		String deleteBook = "DELETE FROM book WHERE id = ?";
		try (Connection connection = DatabaseConnection.getConnection()) {
			try (PreparedStatement stmt1 = connection.prepareStatement(deleteAuthors);
					PreparedStatement stmt2 = connection.prepareStatement(deleteSections);
					PreparedStatement stmt3 = connection.prepareStatement(deleteBook)) {

				stmt1.setInt(1, bookId);
				stmt1.executeUpdate();
				stmt2.setInt(1, bookId);
				stmt2.executeUpdate();
				stmt3.setInt(1, bookId);
				stmt3.executeUpdate();
			} catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException rollbackException) {
					Debug.error("Błąd podczas rollback przy usuwaniu książki.", rollbackException);
				}

				Debug.error("Błąd przy usuwaniu książki: " + bookId, e);
				Debug.showErrorWindow("Wystąpił problem z bazą danych. Skontaktuj się z Administratorem");
			}
		} catch (SQLException e) {
			Debug.error("Błąd przy usuwaniu książki: ", e);
			Debug.showErrorWindow("Wystąpił problem z połączeniem z bazą danych. Skontaktuj się z Administratorem");
		}
	}
	// Uwaga: przed wywołaniem tej metody należy usunąć powiązane wypożyczenia
	// (deleteLoansByBook).

	/**
	 * Aktualizuje dane książki oraz jej powiązania z autorami i działami.
	 * <p>
	 * Powiązania są aktualizowane tylko wted, gdy odpowiednie kolekcje w obiekcie
	 * książki nie są {@code null}.
	 *
	 * @param book książka z zaktualizowanymi danymi (musi zawierać poprawny
	 *             identyfikator)
	 */
	public void updateBook(Book book) {
		String sql = """
				UPDATE book
				SET title = ?, publisher = ?, publication_year = ?, isbn = ?, shelf_id = ?
				WHERE id = ?
				""";
		try (Connection connection = DatabaseConnection.getConnection()) {
			try (PreparedStatement statement = connection.prepareStatement(sql)) {

				statement.setString(1, book.getTitle());
				statement.setString(2, book.getPublisher());
				statement.setInt(3, book.getPublicationYear());
				statement.setString(4, book.getIsbn());
				if (book.getShelfId() == null) {
					statement.setNull(5, Types.INTEGER);
				} else {
					statement.setInt(5, book.getShelfId());
				}
				statement.setInt(6, book.getId());
				statement.executeUpdate();

				if (book.getAuthors() != null) {
					try (PreparedStatement delStmt = connection
							.prepareStatement("DELETE FROM book_author WHERE book_id = ?")) {
						delStmt.setInt(1, book.getId());
						delStmt.executeUpdate();
					}
					try (PreparedStatement insStmt = connection
							.prepareStatement("INSERT INTO book_author(book_id, author_id) VALUES (?, ?)")) {
						for (Author author : book.getAuthors()) {
							if (author == null)
								continue;
							insStmt.setInt(1, book.getId());
							insStmt.setInt(2, author.getId());
							insStmt.executeUpdate();
						}
					}
				}

				List<Section> sections = book.getSections();
				if (sections != null) {
					try (PreparedStatement delStmt = connection
							.prepareStatement("DELETE FROM book_section WHERE book_id = ?")) {
						delStmt.setInt(1, book.getId());
						delStmt.executeUpdate();
					}
					if (!sections.isEmpty()) {
						try (PreparedStatement insStmt = connection
								.prepareStatement("INSERT INTO book_section(book_id, section_id) VALUES (?, ?)")) {
							for (Section section : sections) {
								insStmt.setInt(1, book.getId());
								insStmt.setInt(2, section.getId());
								insStmt.executeUpdate();
							}
						}
					}
				}

			} catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException rollbackException) {
					Debug.error("Błąd podczas rollbacku przy aktualizowaniu książki.", rollbackException);
				}

				Debug.error("Błąd przy aktualizowaniu książki: " + book.getTitle(), e);
				Debug.showErrorWindow("Wystąpił problem z bazą danych. Skontaktuj się z Administratorem");
			}
		} catch (SQLException e) {
			Debug.error("Błąd przy aktualizowaniu książki: ", e);
			Debug.showErrorWindow("Wystąpił problem z połączeniem z bazą danych. Skontaktuj się z Administratorem");
		}
	}

	/**
	 * Pobiera autorów przypisanych do danej książki.
	 *
	 * @param bookId identyfikator książki
	 * @return lista autorów książki; pusta lista, jeśli brak powiązań
	 */
	public List<Author> getAuthorsForBook(int bookId) {
		List<Author> authors = new ArrayList<>();
		String sql = """
				SELECT a.id, a.first_name, a.last_name, a.pseudonym, a.nationality
				FROM authors a
				INNER JOIN book_author ba ON a.id = ba.author_id
				WHERE ba.book_id = ?
				ORDER BY a.id
				""";
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, bookId);
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					authors.add(new Author(resultSet.getInt("id"), resultSet.getString("first_name"),
							resultSet.getString("last_name"), resultSet.getString("pseudonym"),
							resultSet.getString("nationality")));
				}
			}
		} catch (SQLException e) {
			Debug.error("Błąd przy zaciąganiu autorów książki: ", e);
			Debug.showErrorWindow("Wystąpił problem z połączeniem z bazą danych. Skontaktuj się z Administratorem");
		}
		return authors;
	}
}
