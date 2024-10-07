package src.dao;

import src.exception.UserNotFoundException;
import src.exception.ExpenseNotFoundException;
import src.entity.User;
import src.entity.Expense;
import java.util.List;

public interface IFinanceRepository {
    boolean createUser(User user);
    boolean createExpense(Expense expense);
    boolean deleteUser(int userId) throws UserNotFoundException;
    boolean deleteExpense(int expenseId) throws ExpenseNotFoundException;  // Added exception
    List<Expense> getAllExpenses(int userId);
    boolean updateExpense(Expense expense) throws ExpenseNotFoundException;  // Added exception
    boolean authenticateUser(String username, String password);  // New method for authentication
}
