package management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import domain.Shelf;

public class DataBaseShelfs {
	public void addShelf(Shelf shelf) {
		// Komenda SQLowska do dodania do bazy;
		String sql = """
				INSERT INTO shelfs(bookcase_id, name)
				VALUES (?, ?)
				""";

		// proba polaczenia sie z baza danych
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			// ustawiamy pola dla VALUES z sql'a, kolejnosc zgodna z kolejnoscia w insert into
			
			statement.setInt(1, shelf.getBookcaseId());
			statement.setString(2, shelf.getName());

			// execujemy sql
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Shelf> getAllShelves() {
		//lista do zwrotki
		List<Shelf> shelves = new ArrayList<>();
		
		//sql query
		String sql = """
				SELECT id, bookcase_id, name
				FROM shelfs
				ORDER BY id
				""";
		//proba polaczenia z baza
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {
			//pobieranie ksiazka po ksiazce
			while (resultSet.next()) {
				Shelf shelf = new Shelf(
						resultSet.getInt("id"), 
						resultSet.getInt("bookcase_id"),
						resultSet.getString("name"));
				shelves.add(shelf);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return shelves;
	}
	
	public void deleteShelf(int shelfId) {
		String sql = "DELETE FROM shelfs WHERE id = ?";
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, shelfId);
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateShelf(Shelf shelf) {
		String sql = """
				UPDATE shelfs
				SET bookcase_id = ?, name = ?
				WHERE id = ?
				""";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, shelf.getBookcaseId());
			statement.setString(2, shelf.getName());
			statement.setInt(3, shelf.getId());
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
