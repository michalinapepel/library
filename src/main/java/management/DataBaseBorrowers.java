package management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import domain.Borrower;

public class DataBaseBorrowers {
	public void addBorrower(Borrower borrower) {
		// Komenda SQLowska do dodania do bazy;
		String sql = """
				INSERT INTO borrower(first_name, last_name, addresscity,addressstreet,addressnumber,addresszip,card_number)
				VALUES (?, ?, ?, ?, ?, ?, ?)
				""";

		// proba polaczenia sie z baza danych
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			// ustawiamy pola dla VALUES z sql'a, kolejnosc zgodna z kolejnoscia w insert into
			
			statement.setString(1, borrower.getFirstName());
			statement.setString(2, borrower.getLastName());
			statement.setString(3, borrower.getAddressCity());
			statement.setString(4, borrower.getAddressStreet());
			statement.setInt(5, borrower.getAddressNumber());
			statement.setInt(6, borrower.getAddressZip());
			statement.setInt(7, borrower.getCardNumber());


			// execujemy sql
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Borrower> getAllBorrowers() {
		//lista do zwrotki
		List<Borrower> borrowers = new ArrayList<>();
		
		//sql query
		String sql = """
				SELECT id, first_name, last_name, addresscity,addressstreet,addressnumber,addresszip,card_number
				FROM borrower
				ORDER BY id
				""";
		//proba polaczenia z baza
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {
			//pobieranie ksiazka po ksiazce
			while (resultSet.next()) {
				Borrower borrower = new Borrower(
						resultSet.getInt("id"), 
						resultSet.getString("first_name"),
						resultSet.getString("last_name"), 
						resultSet.getString("addresscity"),
						resultSet.getString("addressstreet"),
						resultSet.getInt("addressnumber"),
						resultSet.getInt("addresszip"),
						resultSet.getInt("card_number"));

				borrowers.add(borrower);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return borrowers;
	}
	
	 public void deleteBorrower(int borrowerId) {
		 	//usuwanie po ID
	        String sql = "DELETE FROM borrower WHERE id = ?";
	        //laczenie do bazy
	        try (Connection connection = DatabaseConnection.getConnection();
	             PreparedStatement statement = connection.prepareStatement(sql)) {

	            statement.setInt(1, borrowerId);
	            statement.executeUpdate();

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
}
