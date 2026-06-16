package management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import domain.Author;

public class DataBaseAuthors {
	public void addAuthor(Author author) {
		// Komenda SQLowska do dodania do bazy;
		String sql = """
				INSERT INTO authors(first_name, last_name, pseudonym,nationality)
				VALUES (?, ?, ?, ?)
				""";

		// proba polaczenia sie z baza danych
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			// ustawiamy pola dla VALUES z sql'a, kolejnosc zgodna z kolejnoscia w insert into
			
			statement.setString(1, author.getFirstName());
			statement.setString(2, author.getLastName());
			statement.setString(3, author.getPseudonym());
			statement.setString(4, author.getNationality());

			// execujemy sql
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Author> getAllAuthors() {
		//lista do zwrotki
		List<Author> authors = new ArrayList<>();
		
		//sql query
		String sql = """
				SELECT id, first_name, last_name, addresscity,addressstreet,addressnumber,addresszip,card_number
				FROM authors
				ORDER BY id
				""";
		//proba polaczenia z baza
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {
			//pobieranie ksiazka po ksiazce
			while (resultSet.next()) {
				Author author = new Author(
						resultSet.getInt("id"), 
						resultSet.getString("first_name"),
						resultSet.getString("last_name"), 
						resultSet.getString("pseudonym"),
						resultSet.getString("nationality"));	
				authors.add(author);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return authors;
	}
	
	 public void deleteAuthor(int authorId) {
	        String sql = "DELETE FROM authors WHERE id = ?";
	        try (Connection connection = DatabaseConnection.getConnection();
	             PreparedStatement statement = connection.prepareStatement(sql)) {

	            statement.setInt(1, authorId);
	            statement.executeUpdate();

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
}
