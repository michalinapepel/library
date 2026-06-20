package management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import domain.Bookcase;

/**
 * Zapewnia dostęp do danych regałów w bazie danych.
 * <p>
 * Udostępnia podstawowe operacje CRUD na tabeli regałów oraz metody pomocnicze
 * sprawdzające powiązania z książkami.
 */
public class DataBaseBookcase {

    /**
     * Dodaje nowy regał do bazy danych.
     *
     * @param bookcase regał do dodania
     */
    public void addBookcase(Bookcase bookcase) {
        String sql = "INSERT INTO bookcase(name) VALUES (?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, bookcase.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pobiera wszystkie regały z bazy danych, posortowane według identyfikatora.
     *
     * @return lista regałów; pusta lista, jeśli brak danych
     */
    public List<Bookcase> getAllBookcases() {
        List<Bookcase> bookcases = new ArrayList<>();
        String sql = """
                SELECT id, name
                FROM bookcase
                ORDER BY id
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                bookcases.add(new Bookcase(
                        resultSet.getInt("id"),
                        resultSet.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookcases;
    }

    /**
     * Sprawdza, czy na którejkolwiek półce danego regału znajdują się książki.
     *
     * @param bookcaseId identyfikator regału
     * @return {@code true}, jeśli regał zawiera książki; w przeciwnym razie {@code false}
     */
    public boolean hasBooksOnAnyShelff(int bookcaseId) {
        String sql = """
                SELECT COUNT(*) FROM book b
                INNER JOIN shelfs s ON b.shelf_id = s.id
                WHERE s.bookcase_id = ?
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookcaseId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Usuwa regał wraz z należącymi do niego (pustymi) półkami.
     * <p>
     * Wywołanie powinno być poprzedzone sprawdzeniem {@link #hasBooksOnAnyShelff(int)}.
     *
     * @param bookcaseId identyfikator regału do usunięcia
     */
    public void deleteBookcase(int bookcaseId) {
        String deleteShelves = "DELETE FROM shelfs WHERE bookcase_id = ?";
        String deleteBookcase = "DELETE FROM bookcase WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt1 = connection.prepareStatement(deleteShelves);
             PreparedStatement stmt2 = connection.prepareStatement(deleteBookcase)) {

            stmt1.setInt(1, bookcaseId);
            stmt1.executeUpdate();
            stmt2.setInt(1, bookcaseId);
            stmt2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Aktualizuje dane istniejącego regału.
     *
     * @param bookcase regał z zaktualizowanymi danymi (musi zawierać poprawny identyfikator)
     */
    public void updateBookcase(Bookcase bookcase) {
        String sql = """
                UPDATE bookcase
                SET name = ?
                WHERE id = ?
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, bookcase.getName());
            statement.setInt(2, bookcase.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
