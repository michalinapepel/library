package management;

import app.AppConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import domain.Borrower;

public class DataBaseBorrowers {

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
            e.printStackTrace();
        }
    }

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
            e.printStackTrace();
        }
        return borrowers;
    }

    public void deleteBorrower(int borrowerId) {
        String sql = "DELETE FROM borrower WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, borrowerId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
            e.printStackTrace();
        }
    }

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
            e.printStackTrace();
        }
        return AppConfig.MIN_CARD_NUMBER;
    }

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
            e.printStackTrace();
        }
        return null;
    }
}
