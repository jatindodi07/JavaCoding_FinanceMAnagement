package dao;

import entity.User;
import entity.Expense;
import exception.ExpenseNotFoundException;
import exception.UserNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FinanceRepositoryImpl implements IFinanceRepository {
    private Connection conn;

    // Constructor to accept the connection once and reuse it
    public FinanceRepositoryImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean createUser(User user) {
        String sql = "INSERT INTO Users (username, password, email) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean createExpense(Expense expense) {
        String sql = "INSERT INTO Expenses (user_id, amount, category_id, date, description) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, expense.getUserId());
            pstmt.setDouble(2, expense.getAmount());
            pstmt.setInt(3, expense.getCategoryId());
            pstmt.setDate(4, new java.sql.Date(expense.getDate().getTime()));
            pstmt.setString(5, expense.getDescription());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteUser(int userId) throws UserNotFoundException {
        String sql = "DELETE FROM Users WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new UserNotFoundException("User with ID " + userId + " does not exist.");
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteExpense(int expenseId) throws ExpenseNotFoundException {
        String sql = "DELETE FROM Expenses WHERE expense_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, expenseId);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new ExpenseNotFoundException("Expense with ID " + expenseId + " does not exist.");
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Expense> getAllExpenses(int userId) {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM Expenses WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Expense expense = new Expense(
                    rs.getInt("user_id"),
                    rs.getInt("expense_id"),
                    rs.getDouble("amount"),
                    rs.getInt("category_id"),
                    rs.getDate("date"),
                    rs.getString("description")
                );
                expenses.add(expense);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }

    @Override
    public boolean updateExpense(Expense expense) throws ExpenseNotFoundException {
        String sql = "UPDATE Expenses SET amount = ?, category_id = ?, date = ?, description = ? WHERE expense_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, expense.getAmount());
            pstmt.setInt(2, expense.getCategoryId());
            pstmt.setDate(3, new java.sql.Date(expense.getDate().getTime()));
            pstmt.setString(4, expense.getDescription());
            pstmt.setInt(5, expense.getExpenseId());
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new ExpenseNotFoundException("Expense with ID " + expense.getExpenseId() + " does not exist.");
            }
            
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean authenticateUser(String username, String password) {
        String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();  // Returns true if user is found
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
