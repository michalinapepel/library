package management;

import domain.Book;
import domain.Borrower;
import domain.Loan;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataBaseLoans {

    public void addLoan(Loan loan) {
        String sql = """
                INSERT INTO loan(book_id, borrower_id, loan_date, due_date, return_date)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, loan.getBook().getId());
            statement.setInt(2, loan.getBorrower().getId());
            statement.setDate(3, java.sql.Date.valueOf(loan.getLoanDate()));
            statement.setDate(4, java.sql.Date.valueOf(loan.getDueDate()));

            if (loan.getReturnDate() == null) {
                statement.setNull(5, Types.DATE);
            } else {
                statement.setDate(5, java.sql.Date.valueOf(loan.getReturnDate()));
            }

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Jedno zapytanie JOIN zamiast ładowania wszystkich książek i czytelników osobno.
    public List<Loan> getAllLoans() {
        List<Loan> loans = new ArrayList<>();
        String sql = """
                SELECT l.id, l.loan_date, l.due_date, l.return_date,
                       b.id AS book_id, b.title, b.publisher, b.publication_year, b.isbn, b.shelf_id,
                       br.id AS borrower_id, br.first_name, br.last_name,
                       br.addresscity, br.addressstreet, br.addressnumber, br.addresszip, br.card_number
                FROM loan l
                INNER JOIN book b ON l.book_id = b.id
                INNER JOIN borrower br ON l.borrower_id = br.id
                ORDER BY l.id DESC
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Book book = new Book(rs.getInt("book_id"), rs.getString("title"),
                        rs.getString("publisher"), rs.getInt("publication_year"),
                        rs.getString("isbn"),
                        rs.getObject("shelf_id") == null ? null : rs.getInt("shelf_id"));
                Borrower borrower = new Borrower(rs.getInt("borrower_id"),
                        rs.getString("first_name"), rs.getString("last_name"),
                        rs.getString("addresscity"), rs.getString("addressstreet"),
                        rs.getInt("addressnumber"), rs.getString("addresszip"),
                        rs.getInt("card_number"));
                LocalDate returnDate = rs.getDate("return_date") != null ?
                        rs.getDate("return_date").toLocalDate() : null;
                Loan loan = new Loan(rs.getInt("id"), book, borrower,
                        rs.getDate("loan_date").toLocalDate(),
                        rs.getDate("due_date").toLocalDate());
                loan.setReturnDate(returnDate);
                loans.add(loan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }

    public List<Loan> getAllLoansByBorrower(int borrowerId) {
        List<Loan> loans = new ArrayList<>();
        String sql = """
                SELECT l.id, l.loan_date, l.due_date, l.return_date,
                       b.id AS book_id, b.title, b.publisher, b.publication_year, b.isbn, b.shelf_id,
                       br.id AS borrower_id, br.first_name, br.last_name,
                       br.addresscity, br.addressstreet, br.addressnumber, br.addresszip, br.card_number
                FROM loan l
                INNER JOIN book b ON l.book_id = b.id
                INNER JOIN borrower br ON l.borrower_id = br.id
                WHERE l.borrower_id = ?
                ORDER BY l.loan_date DESC
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, borrowerId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book(rs.getInt("book_id"), rs.getString("title"),
                            rs.getString("publisher"), rs.getInt("publication_year"),
                            rs.getString("isbn"),
                            rs.getObject("shelf_id") == null ? null : rs.getInt("shelf_id"));
                    Borrower borrower = new Borrower(rs.getInt("borrower_id"),
                            rs.getString("first_name"), rs.getString("last_name"),
                            rs.getString("addresscity"), rs.getString("addressstreet"),
                            rs.getInt("addressnumber"), rs.getString("addresszip"),
                            rs.getInt("card_number"));
                    LocalDate returnDate = rs.getDate("return_date") != null ?
                            rs.getDate("return_date").toLocalDate() : null;
                    Loan loan = new Loan(rs.getInt("id"), book, borrower,
                            rs.getDate("loan_date").toLocalDate(),
                            rs.getDate("due_date").toLocalDate());
                    loan.setReturnDate(returnDate);
                    loans.add(loan);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }

    public List<Loan> getActiveLoansByBorrower(int borrowerId) {
        List<Loan> loans = new ArrayList<>();
        String sql = """
                SELECT l.id, l.loan_date, l.due_date,
                       b.id AS book_id, b.title, b.publisher, b.publication_year, b.isbn, b.shelf_id,
                       br.id AS borrower_id, br.first_name, br.last_name,
                       br.addresscity, br.addressstreet, br.addressnumber, br.addresszip, br.card_number
                FROM loan l
                INNER JOIN book b ON l.book_id = b.id
                INNER JOIN borrower br ON l.borrower_id = br.id
                WHERE l.borrower_id = ? AND l.return_date IS NULL
                ORDER BY l.due_date
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, borrowerId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book(rs.getInt("book_id"), rs.getString("title"),
                            rs.getString("publisher"), rs.getInt("publication_year"),
                            rs.getString("isbn"),
                            rs.getObject("shelf_id") == null ? null : rs.getInt("shelf_id"));
                    Borrower borrower = new Borrower(rs.getInt("borrower_id"),
                            rs.getString("first_name"), rs.getString("last_name"),
                            rs.getString("addresscity"), rs.getString("addressstreet"),
                            rs.getInt("addressnumber"), rs.getString("addresszip"),
                            rs.getInt("card_number"));
                    Loan loan = new Loan(rs.getInt("id"), book, borrower,
                            rs.getDate("loan_date").toLocalDate(),
                            rs.getDate("due_date").toLocalDate());
                    loans.add(loan);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }

    public void updateLoan(Loan loan) {
        String sql = """
                UPDATE loan
                SET book_id = ?, borrower_id = ?, loan_date = ?, due_date = ?, return_date = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, loan.getBook().getId());
            statement.setInt(2, loan.getBorrower().getId());
            statement.setDate(3, java.sql.Date.valueOf(loan.getLoanDate()));
            statement.setDate(4, java.sql.Date.valueOf(loan.getDueDate()));

            if (loan.getReturnDate() == null) {
                statement.setNull(5, Types.DATE);
            } else {
                statement.setDate(5, java.sql.Date.valueOf(loan.getReturnDate()));
            }

            statement.setInt(6, loan.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void returnBook(int loanId) {
        String sql = "UPDATE loan SET return_date = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            statement.setInt(2, loanId);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean hasActiveLoans(int borrowerId) {
        String sql = "SELECT COUNT(*) FROM loan WHERE borrower_id = ? AND return_date IS NULL";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, borrowerId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean hasAnyLoans(int borrowerId) {
        String sql = "SELECT COUNT(*) FROM loan WHERE borrower_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, borrowerId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Usuwa wszystkie wypożyczenia czytelnika przed usunięciem jego konta (wymagane przez FK).
    public void deleteLoansByBorrower(int borrowerId) {
        String sql = "DELETE FROM loan WHERE borrower_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, borrowerId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean hasAnyLoanForBook(int bookId) {
        String sql = "SELECT COUNT(*) FROM loan WHERE book_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Usuwa wszystkie wypożyczenia książki przed usunięciem jej z bazy (wymagane przez FK).
    public void deleteLoansByBook(int bookId) {
        String sql = "DELETE FROM loan WHERE book_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isBookOnActiveLoan(int bookId) {
        String sql = "SELECT COUNT(*) FROM loan WHERE book_id = ? AND return_date IS NULL";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public java.util.Set<Integer> getActiveLoanBookIds() {
        java.util.Set<Integer> ids = new java.util.HashSet<>();
        String sql = "SELECT book_id FROM loan WHERE return_date IS NULL";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) ids.add(rs.getInt("book_id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }

    public void deleteLoan(int loanId) {
        String sql = "DELETE FROM loan WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, loanId);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

