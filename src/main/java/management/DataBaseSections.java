package management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import domain.Section;

/**
 * Zapewnia dostęp do danych działów (sekcji) w bazie danych.
 * <p>
 * Udostępnia podstawowe operacje CRUD na tabeli działów.
 */
public class DataBaseSections {

    /**
     * Dodaje nowy dział do bazy danych.
     *
     * @param section dział do dodania
     */
    public void addSection(Section section) {
        String sql = "INSERT INTO section(key) VALUES (?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, section.getKey());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pobiera wszystkie działy z bazy danych, posortowane według identyfikatora.
     *
     * @return lista działów; pusta lista, jeśli brak danych
     */
    public List<Section> getAllSections() {
        List<Section> sections = new ArrayList<>();
        String sql = """
                SELECT id, key
                FROM section
                ORDER BY id
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                sections.add(new Section(
                        resultSet.getInt("id"),
                        resultSet.getString("key")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sections;
    }

    /**
     * Usuwa dział z bazy danych.
     *
     * @param sectionId identyfikator działu do usunięcia
     */
    public void deleteSection(int sectionId) {
        String sql = "DELETE FROM section WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, sectionId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Aktualizuje dane istniejącego działu.
     *
     * @param section dział z zaktualizowanymi danymi (musi zawierać poprawny identyfikator)
     */
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
