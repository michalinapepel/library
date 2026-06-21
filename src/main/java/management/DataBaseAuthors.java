package management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import domain.Author;

/**
 * Zapewnia dostęp do danych autorów w bazie danych.
 * <p>
 * Udostępnia podstawowe operacje CRUD (dodawanie, odczyt, aktualizacja, usuwanie)
 * na tabeli autorów.
 */
public class DataBaseAuthors {

    /**
     * Dodaje nowego autora do bazy danych i ustawia w przekazanym obiekcie
     * wygenerowany identyfikator.
     *
     * @param author autor do dodania
     */
    public void addAuthor(Author author) {
        String sql = """
                INSERT INTO authors(first_name, last_name, pseudonym, nationality)
                VALUES (?, ?, ?, ?)
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, author.getFirstName());
            statement.setString(2, author.getLastName());
            statement.setString(3, author.getPseudonym());
            statement.setString(4, author.getNationality());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) author.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pobiera wszystkich autorów z bazy danych, posortowanych według identyfikatora.
     *
     * @return lista autorów; pusta lista, jeśli brak danych
     */
    public List<Author> getAllAuthors() {
        List<Author> authors = new ArrayList<>();
        String sql = """
                SELECT id, first_name, last_name, pseudonym, nationality
                FROM authors
                ORDER BY id
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                authors.add(new Author(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("pseudonym"),
                        resultSet.getString("nationality")));
            }
        } catch (SQLException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        return authors;
    }

    /**
     * Usuwa autora wraz z jego powiązaniami z książkami.
     *
     * @param authorId identyfikator autora do usunięcia
     */
    public void deleteAuthor(int authorId) {
        String deleteRelations = "DELETE FROM book_author WHERE author_id = ?";
        String deleteAuthor = "DELETE FROM authors WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt1 = connection.prepareStatement(deleteRelations);
             PreparedStatement stmt2 = connection.prepareStatement(deleteAuthor)) {

            stmt1.setInt(1, authorId);
            stmt1.executeUpdate();
            stmt2.setInt(1, authorId);
            stmt2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Aktualizuje dane istniejącego autora.
     *
     * @param author autor z zaktualizowanymi danymi (musi zawierać poprawny identyfikator)
     */
    public void updateAuthor(Author author) {
        String sql = """
                UPDATE authors
                SET first_name = ?, last_name = ?, pseudonym = ?, nationality = ?
                WHERE id = ?
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, author.getFirstName());
            statement.setString(2, author.getLastName());
            statement.setString(3, author.getPseudonym());
            statement.setString(4, author.getNationality());
            statement.setInt(5, author.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
