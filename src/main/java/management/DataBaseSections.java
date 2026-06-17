package management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import domain.Section;

public class DataBaseSections {
	public void addSection(Section section) {
		// Komenda SQLowska do dodania do bazy;
		String sql = """
				INSERT INTO section(key)
				VALUES (?)
				""";

		// proba polaczenia sie z baza danych
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			// ustawiamy pola dla VALUES z sql'a, kolejnosc zgodna z kolejnoscia w insert into
			
			statement.setString(1, section.getKey());


			// execujemy sql
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Section> getAllSections() {
		//lista do zwrotki
		List<Section> sections = new ArrayList<>();
		
		//sql query
		String sql = """
				SELECT id, key
				FROM borrowers
				ORDER BY id
				""";
		//proba polaczenia z baza
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {
			//pobieranie ksiazka po ksiazce
			while (resultSet.next()) {
				Section section = new Section(
						resultSet.getInt("id"), 
						resultSet.getString("key"));
				sections.add(section);
				
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return sections;
	}
	
	 public void deleteSection(int sectionId) {
	 	//usuwanie po ID
        String sql = "DELETE FROM section WHERE id = ?";
        //laczenie do bazy
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, sectionId);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	public void updateSection(Section section) {
		String sql = """
				UPDATE section
				SET key = ?
				WHERE id = ?
				""";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, section.getKey());
			statement.setInt(2, section.getId());
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
