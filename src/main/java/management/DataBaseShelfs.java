package management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import domain.Shelf;

public class DataBaseShelfs {

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
            e.printStackTrace();
        }
    }

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
            e.printStackTrace();
        }
        return shelves;
    }

    public void deleteShelf(int shelfId) {
        String nullBooks = "UPDATE book SET shelf_id = NULL WHERE shelf_id = ?";
        String deleteShelf = "DELETE FROM shelfs WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt1 = connection.prepareStatement(nullBooks);
             PreparedStatement stmt2 = connection.prepareStatement(deleteShelf)) {

            stmt1.setInt(1, shelfId);
            stmt1.executeUpdate();
            stmt2.setInt(1, shelfId);
            stmt2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
            e.printStackTrace();
        }
    }
}
