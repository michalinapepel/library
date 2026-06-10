package management;

import domain.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseBooks {
	public void addBook(Book book) {
		// Komenda SQLowska do dodania do bazy;
		String sql = """
				INSERT INTO book(title, publisher, publication_year,isbn,shelf_id)
				VALUES (?, ?, ?, ?, ?)
				""";

		// proba polaczenia sie z baza danych
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			// ustawiamy pola dla VALUES z sql'a, kolejnosc zgodna z kolejnoscia w insert into
			
			statement.setString(1, book.getTitle());
			statement.setString(2, book.getPublisher());
			statement.setInt(3, book.getPublicationYear());
			statement.setString(4, book.getIsbn());

			// jesli nie ma podanego id to wrzucilem nulla, ale ngl i tak sra wtedy bledem XD
			if (book.getShelfId() == null) {
				statement.setNull(5, Types.INTEGER);
			} else {
				statement.setInt(5, book.getShelfId());
			}

			// execujemy sql
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Book> getAllBooks() {
		//lista do zwrotki
		List<Book> books = new ArrayList<>();
		
		//sql query
		String sql = """
				SELECT id, title, publisher, publication_year, isbn, shelf_id
				FROM book
				ORDER BY id
				""";
		//proba polaczenia z baza
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {
			//pobieranie ksiazka po ksiazce
			while (resultSet.next()) {
				Book book = new Book(resultSet.getInt("id"), resultSet.getString("title"),
						resultSet.getString("publisher"), resultSet.getInt("publication_year"),
						resultSet.getString("isbn"),
						resultSet.getObject("shelf_id") == null ? null : resultSet.getInt("shelf_id"));

				books.add(book);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return books;
	}
	
	 public void deleteBook(int bookId) {
		 	//usuwanie po ID
	        String sql = "DELETE FROM book WHERE id = ?";
	        //laczenie do bazy
	        try (Connection connection = DatabaseConnection.getConnection();
	             PreparedStatement statement = connection.prepareStatement(sql)) {

	            statement.setInt(1, bookId);
	            statement.executeUpdate();

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
}
