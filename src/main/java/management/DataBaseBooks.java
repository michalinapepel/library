package management;

import domain.Author;
import domain.Book;
import domain.Section;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // 3 zapytania zbiorcze zamiast 1+N+N — eliminuje problem N+1 przy ładowaniu listy książek.
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        Map<Integer, Book> bookMap = new HashMap<>();

        String sqlBooks = """
                SELECT id, title, publisher, publication_year, isbn, shelf_id
                FROM book
                ORDER BY id
                """;
        String sqlAuthors = """
                SELECT ba.book_id, a.id, a.first_name, a.last_name, a.pseudonym, a.nationality
                FROM book_author ba
                INNER JOIN authors a ON a.id = ba.author_id
                ORDER BY ba.book_id, a.id
                """;
        String sqlSections = """
                SELECT bs.book_id, s.id, s.key
                FROM book_section bs
                INNER JOIN section s ON s.id = bs.section_id
                ORDER BY bs.book_id, s.id
                """;

        try (Connection connection = DatabaseConnection.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(sqlBooks);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int bookId = rs.getInt("id");
                    Book book = new Book(bookId, rs.getString("title"),
                            rs.getString("publisher"), rs.getInt("publication_year"),
                            rs.getString("isbn"),
                            rs.getObject("shelf_id") == null ? null : rs.getInt("shelf_id"));
                    books.add(book);
                    bookMap.put(bookId, book);
                }
            }

            Map<Integer, List<Author>> authorsMap = new HashMap<>();
            try (PreparedStatement stmt = connection.prepareStatement(sqlAuthors);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int bookId = rs.getInt("book_id");
                    authorsMap.computeIfAbsent(bookId, k -> new ArrayList<>())
                              .add(new Author(rs.getInt("id"), rs.getString("first_name"),
                                             rs.getString("last_name"), rs.getString("pseudonym"),
                                             rs.getString("nationality")));
                }
            }

            Map<Integer, List<Section>> sectionsMap = new HashMap<>();
            try (PreparedStatement stmt = connection.prepareStatement(sqlSections);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int bookId = rs.getInt("book_id");
                    sectionsMap.computeIfAbsent(bookId, k -> new ArrayList<>())
                               .add(new Section(rs.getInt("id"), rs.getString("key")));
                }
            }

            for (Book book : books) {
                List<Author> authors = authorsMap.getOrDefault(book.getId(), new ArrayList<>());
                if (!authors.isEmpty()) book.setAuthors(authors.toArray(new Author[0]));
                book.setSections(sectionsMap.getOrDefault(book.getId(), new ArrayList<>()));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public void deleteBook(int bookId) {
        String deleteAuthors = "DELETE FROM book_author WHERE book_id = ?";
        String deleteSections = "DELETE FROM book_section WHERE book_id = ?";
        String deleteBook = "DELETE FROM book WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt1 = connection.prepareStatement(deleteAuthors);
             PreparedStatement stmt2 = connection.prepareStatement(deleteSections);
             PreparedStatement stmt3 = connection.prepareStatement(deleteBook)) {

            stmt1.setInt(1, bookId);
            stmt1.executeUpdate();
            stmt2.setInt(1, bookId);
            stmt2.executeUpdate();
            stmt3.setInt(1, bookId);
            stmt3.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Uwaga: przed wywołaniem tej metody należy usunąć powiązane wypożyczenia (deleteLoansByBook).

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
