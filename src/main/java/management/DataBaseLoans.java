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

    public List<Loan> getAllLoans() {
        List<Loan> loans = new ArrayList<>();

        String sql = """
                SELECT id, book_id, borrower_id, loan_date, due_date, return_date
                FROM loan
                ORDER BY id DESC
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            DataBaseBooks dbBooks = new DataBaseBooks();
            DataBaseBorrowers dbBorrowers = new DataBaseBorrowers();
            List<Book> books = dbBooks.getAllBooks();
            List<Borrower> borrowers = dbBorrowers.getAllBorrowers();

            while (resultSet.next()) {
                int loanId = resultSet.getInt("id");
                int bookId = resultSet.getInt("book_id");
                int borrowerId = resultSet.getInt("borrower_id");
                LocalDate loanDate = resultSet.getDate("loan_date").toLocalDate();
                LocalDate dueDate = resultSet.getDate("due_date").toLocalDate();
                LocalDate returnDate = resultSet.getDate("return_date") != null ?
                    resultSet.getDate("return_date").toLocalDate() : null;

                Book book = books.stream().filter(b -> b.getId() == bookId).findFirst().orElse(null);
                Borrower borrower = borrowers.stream().filter(b -> b.getId() == borrowerId).findFirst().orElse(null);

                Loan loan = new Loan(loanId, book, borrower, loanDate, dueDate);
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
                SELECT id, book_id, borrower_id, loan_date, due_date, return_date
                FROM loan
                WHERE borrower_id = ?
                ORDER BY loan_date DESC
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, borrowerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                DataBaseBooks dbBooks = new DataBaseBooks();
                DataBaseBorrowers dbBorrowers = new DataBaseBorrowers();
                List<Book> books = dbBooks.getAllBooks();
                List<Borrower> borrowers = dbBorrowers.getAllBorrowers();
                while (resultSet.next()) {
                    int loanId = resultSet.getInt("id");
                    int bookId = resultSet.getInt("book_id");
                    LocalDate loanDate = resultSet.getDate("loan_date").toLocalDate();
                    LocalDate dueDate = resultSet.getDate("due_date").toLocalDate();
                    LocalDate returnDate = resultSet.getDate("return_date") != null ?
                        resultSet.getDate("return_date").toLocalDate() : null;
                    Book book = books.stream().filter(b -> b.getId() == bookId).findFirst().orElse(null);
                    Borrower borrower = borrowers.stream().filter(b -> b.getId() == borrowerId).findFirst().orElse(null);
                    Loan loan = new Loan(loanId, book, borrower, loanDate, dueDate);
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
                SELECT id, book_id, borrower_id, loan_date, due_date, return_date
                FROM loan
                WHERE borrower_id = ? AND return_date IS NULL
                ORDER BY due_date
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, borrowerId);

            try (ResultSet resultSet = statement.executeQuery()) {
                DataBaseBooks dbBooks = new DataBaseBooks();
                DataBaseBorrowers dbBorrowers = new DataBaseBorrowers();
                List<Book> books = dbBooks.getAllBooks();
                List<Borrower> borrowers = dbBorrowers.getAllBorrowers();

                while (resultSet.next()) {
                    int loanId = resultSet.getInt("id");
                    int bookId = resultSet.getInt("book_id");
                    LocalDate loanDate = resultSet.getDate("loan_date").toLocalDate();
                    LocalDate dueDate = resultSet.getDate("due_date").toLocalDate();

                    Book book = books.stream().filter(b -> b.getId() == bookId).findFirst().orElse(null);
                    Borrower borrower = borrowers.stream().filter(b -> b.getId() == borrowerId).findFirst().orElse(null);

                    Loan loan = new Loan(loanId, book, borrower, loanDate, dueDate);
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

