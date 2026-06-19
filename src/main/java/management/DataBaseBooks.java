package management;

import domain.Author;
import domain.Book;
import domain.Section;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseBooks {

    public void addBook(Book book) {
        String sql = "INSERT INTO book(title, publisher, publication_year, isbn, shelf_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, book.getTitle());
            statement.setString(2, book.getPublisher());
            statement.setInt(3, book.getPublicationYear());
            statement.setString(4, book.getIsbn());
            if (book.getShelfId() == null) {
                statement.setNull(5, Types.INTEGER);
            } else {
                statement.setInt(5, book.getShelfId());
            }
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int bookId = generatedKeys.getInt(1);

                    if (book.getAuthors() != null) {
                        String sqlAuthor = "INSERT INTO book_author(book_id, author_id) VALUES (?, ?)";
                        try (PreparedStatement authorStmt = connection.prepareStatement(sqlAuthor)) {
                            for (Author author : book.getAuthors()) {
                                if (author == null) continue;
                                authorStmt.setInt(1, bookId);
                                authorStmt.setInt(2, author.getId());
                                authorStmt.executeUpdate();
                            }
                        }
                    }

                    List<Section> sections = book.getSections();
                    if (sections != null && !sections.isEmpty()) {
                        String sqlSection = "INSERT INTO book_section(book_id, section_id) VALUES (?, ?)";
                        try (PreparedStatement sectionStmt = connection.prepareStatement(sqlSection)) {
                            for (Section section : sections) {
                                sectionStmt.setInt(1, bookId);
                                sectionStmt.setInt(2, section.getId());
                                sectionStmt.executeUpdate();
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = """
                SELECT id, title, publisher, publication_year, isbn, shelf_id
                FROM book
                ORDER BY id
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int bookId = resultSet.getInt("id");
                Book book = new Book(bookId, resultSet.getString("title"),
                        resultSet.getString("publisher"), resultSet.getInt("publication_year"),
                        resultSet.getString("isbn"),
                        resultSet.getObject("shelf_id") == null ? null : resultSet.getInt("shelf_id"));

                List<Author> authors = getAuthorsForBook(bookId);
                if (!authors.isEmpty()) book.setAuthors(authors.toArray(new Author[0]));
                book.setSections(getSectionsForBook(bookId));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public void deleteBook(int bookId) {
        String sql = "DELETE FROM book WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bookId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBook(Book book) {
        String sql = """
                UPDATE book
                SET title = ?, publisher = ?, publication_year = ?, isbn = ?, shelf_id = ?
                WHERE id = ?
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, book.getTitle());
            statement.setString(2, book.getPublisher());
            statement.setInt(3, book.getPublicationYear());
            statement.setString(4, book.getIsbn());
            if (book.getShelfId() == null) {
                statement.setNull(5, Types.INTEGER);
            } else {
                statement.setInt(5, book.getShelfId());
            }
            statement.setInt(6, book.getId());
            statement.executeUpdate();

            if (book.getAuthors() != null) {
                try (PreparedStatement delStmt = connection.prepareStatement(
                        "DELETE FROM book_author WHERE book_id = ?")) {
                    delStmt.setInt(1, book.getId());
                    delStmt.executeUpdate();
                }
                try (PreparedStatement insStmt = connection.prepareStatement(
                        "INSERT INTO book_author(book_id, author_id) VALUES (?, ?)")) {
                    for (Author author : book.getAuthors()) {
                        if (author == null) continue;
                        insStmt.setInt(1, book.getId());
                        insStmt.setInt(2, author.getId());
                        insStmt.executeUpdate();
                    }
                }
            }

            List<Section> sections = book.getSections();
            if (sections != null) {
                try (PreparedStatement delStmt = connection.prepareStatement(
                        "DELETE FROM book_section WHERE book_id = ?")) {
                    delStmt.setInt(1, book.getId());
                    delStmt.executeUpdate();
                }
                if (!sections.isEmpty()) {
                    try (PreparedStatement insStmt = connection.prepareStatement(
                            "INSERT INTO book_section(book_id, section_id) VALUES (?, ?)")) {
                        for (Section section : sections) {
                            insStmt.setInt(1, book.getId());
                            insStmt.setInt(2, section.getId());
                            insStmt.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Author> getAuthorsForBook(int bookId) {
        List<Author> authors = new ArrayList<>();
        String sql = """
                SELECT a.id, a.first_name, a.last_name, a.pseudonym, a.nationality
                FROM authors a
                INNER JOIN book_author ba ON a.id = ba.author_id
                WHERE ba.book_id = ?
                ORDER BY a.id
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bookId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    authors.add(new Author(
                            resultSet.getInt("id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("pseudonym"),
                            resultSet.getString("nationality")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authors;
    }

    public List<Section> getSectionsForBook(int bookId) {
        List<Section> sections = new ArrayList<>();
        String sql = """
                SELECT s.id, s.key
                FROM section s
                INNER JOIN book_section bs ON s.id = bs.section_id
                WHERE bs.book_id = ?
                ORDER BY s.id
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bookId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    sections.add(new Section(
                            resultSet.getInt("id"),
                            resultSet.getString("key")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sections;
    }

    public void addBookAuthorRelation(int bookId, int authorId) {
        String sql = "INSERT INTO book_author(book_id, author_id) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bookId);
            statement.setInt(2, authorId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeBookAuthorRelation(int bookId, int authorId) {
        String sql = "DELETE FROM book_author WHERE book_id = ? AND author_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bookId);
            statement.setInt(2, authorId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addBookSectionRelation(int bookId, int sectionId) {
        String sql = "INSERT INTO book_section(book_id, section_id) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bookId);
            statement.setInt(2, sectionId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeBookSectionRelation(int bookId, int sectionId) {
        String sql = "DELETE FROM book_section WHERE book_id = ? AND section_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bookId);
            statement.setInt(2, sectionId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
