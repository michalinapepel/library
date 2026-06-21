package management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.Debug;
import domain.Shelf;

/**
 * Zapewnia dostęp do danych półek w bazie danych.
 * <p>
 * Udostępnia podstawowe operacje CRUD na tabeli półek oraz metody pomocnicze
 * sprawdzające powiązania z książkami.
 */
public class DataBaseShelfs {

    /**
     * Dodaje nową półkę do bazy danych.
     *
     * @param shelf półka do dodania
     */
    public void addShelf(Shelf shelf) {
        String sql = """
                INSERT INTO shelfs(bookcase_id, name)
                VALUES (?, ?)
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, shelf.getBookcaseId());
            statement.setString(2, shelf.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
        	Debug.error("Błąd przy dodawaniu półki: ", e);
            Debug.showErrorWindow("Wystąpił problem z połączeniem z bazą danych. Skontaktuj się z Administratorem");
        }
    }

    /**
     * Pobiera wszystkie półki z bazy danych wraz z nazwą regału, do którego należą.
     *
     * @return lista półek; pusta lista, jeśli brak danych
     */
    public List<Shelf> getAllShelves() {
        List<Shelf> shelves = new ArrayList<>();
        String sql = """
                SELECT s.id, s.bookcase_id, s.name, b.name AS bookcase_name
                FROM shelfs s
                LEFT JOIN bookcase b ON s.bookcase_id = b.id
                ORDER BY s.id
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Shelf shelf = new Shelf(
                        resultSet.getInt("id"),
                        resultSet.getInt("bookcase_id"),
                        resultSet.getString("name"));
                shelf.setBookcaseName(resultSet.getString("bookcase_name"));
                shelves.add(shelf);
            }
        } catch (SQLException e) {
        	Debug.error("Błąd przy zaciąganiu półek: ", e);
            Debug.showErrorWindow("Wystąpił problem z połączeniem z bazą danych. Skontaktuj się z Administratorem");
        }
        return shelves;
    }

    /**
     * Sprawdza, czy do danej półki przypisane są jakiekolwiek książki.
     *
     * @param shelfId identyfikator półki
     * @return {@code true}, jeśli półka zawiera książki; w przeciwnym razie {@code false}
     */
    public boolean hasBooksAssigned(int shelfId) {
        String sql = "SELECT COUNT(*) FROM book WHERE shelf_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, shelfId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
        	Debug.error("Błąd przy zaciąganiu książek z półki: ", e);
            Debug.showErrorWindow("Wystąpił problem z połączeniem z bazą danych. Skontaktuj się z Administratorem");
        }
        return false;
    }

    /**
     * Usuwa półkę z bazy danych.
     *
     * @param shelfId identyfikator półki do usunięcia
     */
    public void deleteShelf(int shelfId) {
        String sql = "DELETE FROM shelfs WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, shelfId);
            statement.executeUpdate();
        } catch (SQLException e) {
        	Debug.error("Błąd przy usuwaniu półki: ", e);
            Debug.showErrorWindow("Wystąpił problem z połączeniem z bazą danych. Skontaktuj się z Administratorem");
        }
    }

    /**
     * Aktualizuje dane istniejącej półki.
     *
     * @param shelf półka z zaktualizowanymi danymi (musi zawierać poprawny identyfikator)
     */
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
        	Debug.error("Błąd przy aktualizowaniu półki: ", e);
            Debug.showErrorWindow("Wystąpił problem z połączeniem z bazą danych. Skontaktuj się z Administratorem");
        }
    }
}
