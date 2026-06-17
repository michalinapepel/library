package management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import domain.Bookcase;

public class DataBaseBookcase {
	public void addBookcase(Bookcase bookcase) {
		// Komenda SQLowska do dodania do bazy;
		String sql = """
				INSERT INTO bookcase(name)
				VALUES (?)
				""";

		// proba polaczenia sie z baza danych
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			// ustawiamy pola dla VALUES z sql'a, kolejnosc zgodna z kolejnoscia w insert into
			
			statement.setString(1, bookcase.getName());

			// execujemy sql
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Bookcase> getAllBookcases() {
		//lista do zwrotki
		List<Bookcase> bookcases = new ArrayList<>();
		
		//sql query
		String sql = """
				SELECT id, name
				FROM bookcase
				ORDER BY id
				""";
		//proba polaczenia z baza
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {
			//pobieranie ksiazka po ksiazce
			while (resultSet.next()) {
				Bookcase bookcase = new Bookcase(
						resultSet.getInt("id"), 
						resultSet.getString("name"));
				bookcases.add(bookcase);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return bookcases;
	}
	
	 public void deleteBookcase(int bookcaseId) {
	        String sql = "DELETE FROM bookcase WHERE id = ?";
	        try (Connection connection = DatabaseConnection.getConnection();
	             PreparedStatement statement = connection.prepareStatement(sql)) {

	            statement.setInt(1, bookcaseId);
	            statement.executeUpdate();

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
}
