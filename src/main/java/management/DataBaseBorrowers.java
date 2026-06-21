package management;

import app.AppConfig;
import app.Debug;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import domain.Borrower;

/**
 * Zapewnia dostęp do danych czytelników w bazie danych.
 * <p>
 * Udostępnia podstawowe operacje CRUD na tabeli czytelników oraz metody
 * związane z obsługą numerów kart bibliotecznych.
 */
public class DataBaseBorrowers {

    /**
     * Dodaje nowego czytelnika do bazy danych.
     *
     * @param borrower czytelnik do dodania
     */
    public void addBorrower(Borrower borrower) {
        String sql = """
                INSERT INTO borrower(first_name, last_name, addresscity, addressstreet, addressnumber, addresszip, card_number)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, borrower.getFirstName());
            statement.setString(2, borrower.getLastName());
            statement.setString(3, borrower.getAddressCity());
            statement.setString(4, borrower.getAddressStreet());
            statement.setInt(5, borrower.getAddressNumber());
            statement.setString(6, borrower.getAddressZip());
            statement.setInt(7, borrower.getCardNumber());
            statement.executeUpdate();
        } catch (SQLException e) {
        	Debug.error("Błąd przy dodawaniu wypożyczającego: ", e);
            Debug.showErrorWindow("Wystąpił problem z połączeniem z bazą danych. Skontaktuj się z Administratorem");
        }
    }

    /**
     * Pobiera wszystkich czytelników z bazy danych, posortowanych według identyfikatora.
     *
     * @return lista czytelników; pusta lista, jeśli brak danych
     */
    public List<Borrower> getAllBorrowers() {
        List<Borrower> borrowers = new ArrayList<>();
        String sql = """
                SELECT id, first_name, last_name, addresscity, addressstreet, addressnumber, addresszip, card_number
                FROM borrower
                ORDER BY id
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                borrowers.add(new Borrower(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("addresscity"),
                        resultSet.getString("addressstreet"),
                        resultSet.getInt("addressnumber"),
                        resultSet.getString("addresszip"),
                        resultSet.getInt("card_number")));
            }
        } catch (SQLException e) {
        	Debug.error("Błąd przy zaciąganiu wypożyczających: ", e);
            Debug.showErrorWindow("Wystąpił problem z połączeniem z bazą danych. Skontaktuj się z Administratorem");
        }
        return borrowers;
    }

    /**
     * Usuwa czytelnika z bazy danych.
     *
     * @param borrowerId identyfikator czytelnika do usunięcia
     */
    public void deleteBorrower(int borrowerId) {
        String sql = "DELETE FROM borrower WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, borrowerId);
            statement.executeUpdate();
        } catch (SQLException e) {
        	Debug.error("Błąd przy usuwaniu wypożyczającego: ", e);
            Debug.showErrorWindow("Wystąpił problem z połączeniem z bazą danych. Skontaktuj się z Administratorem");
        }
    }

    /**
     * Aktualizuje dane istniejącego czytelnika.
     *
     * @param borrower czytelnik z zaktualizowanymi danymi (musi zawierać poprawny identyfikator)
     */
    public void updateBorrower(Borrower borrower) {
        String sql = """
                UPDATE borrower
                SET first_name = ?, last_name = ?, addresscity = ?, addressstreet = ?,
                    addressnumber = ?, addresszip = ?, card_number = ?
                WHERE id = ?
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, borrower.getFirstName());
            statement.setString(2, borrower.getLastName());
            statement.setString(3, borrower.getAddressCity());
            statement.setString(4, borrower.getAddressStreet());
            statement.setInt(5, borrower.getAddressNumber());
            statement.setString(6, borrower.getAddressZip());
            statement.setInt(7, borrower.getCardNumber());
            statement.setInt(8, borrower.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
        	Debug.error("Błąd przy aktualizacji wypożyczającego: ", e);
            Debug.showErrorWindow("Wystąpił problem z połączeniem z bazą danych. Skontaktuj się z Administratorem");
        }
    }

    /**
     * Wyznacza kolejny dostępny numer karty bibliotecznej.
     * <p>
     * Numer jest o jeden większy od największego dotychczasowego numeru karty.
     * Jeśli tabela czytelników jest pusta, zwracana jest wartość początkowa
     * {@link AppConfig#MIN_CARD_NUMBER}.
     *
     * @return kolejny dostępny numer karty bibliotecznej
     */
    public int getNextCardNumber() {
        // MAX() na pustej tabeli zwraca SQL NULL – rs.getInt() zwróciłoby wtedy 0,
        // dlatego sprawdzamy wasNull() i zaczynamy numerację od 20000.
        // Rzutowanie ::integer jest konieczne, bo kolumna card_number jest typu TEXT w bazie.
        String sql = "SELECT MAX(card_number::integer) FROM borrower";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            if (rs.next()) {
                int max = rs.getInt(1);
                return rs.wasNull() ? AppConfig.MIN_CARD_NUMBER : max + 1;
            }
        } catch (SQLException e) {
        	Debug.error("Błąd przy ustalaniu kolejnego wolnego numeru karty: ", e);
            Debug.showErrorWindow("Wystąpił problem z połączeniem z bazą danych. Skontaktuj się z Administratorem");
        }
        return AppConfig.MIN_CARD_NUMBER;
    }

    /**
     * Wyszukuje czytelnika na podstawie numeru karty bibliotecznej.
     *
     * @param cardNumber numer karty bibliotecznej
     * @return znaleziony czytelnik lub {@code null}, jeśli nie istnieje
     */
    public Borrower getBorrowerByCardNumber(int cardNumber) {
        // Rzutowanie ::integer konieczne – kolumna card_number jest typu TEXT w bazie.
        String sql = """
                SELECT id, first_name, last_name, addresscity, addressstreet, addressnumber, addresszip, card_number
                FROM borrower WHERE card_number::integer = ?
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, cardNumber);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Borrower(
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("addresscity"),
                            rs.getString("addressstreet"),
                            rs.getInt("addressnumber"),
                            rs.getString("addresszip"),
                            rs.getInt("card_number"));
                }
            }
        } catch (SQLException e) {
        	Debug.error("Błąd przy zaciąganiu wypożyczającego z użyciem numeru karty: ", e);
            Debug.showErrorWindow("Wystąpił problem z połączeniem z bazą danych. Skontaktuj się z Administratorem");
        }
        return null;
    }
}
