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
        String sql = "INSERT INTO bookcase(name) VALUES (?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, bookcase.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
