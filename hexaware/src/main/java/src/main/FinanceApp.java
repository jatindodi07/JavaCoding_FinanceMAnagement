package src.main;
import src.dao.*;
import src.entity.*;
import src.util.*;
import src.exception.*;
import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class FinanceApp {
    public static void main(String[] args) {
        try (Connection conn = DBConnUtil.getConnection("db.properties")) {
            FinanceRepositoryImpl financeRepo = new FinanceRepositoryImpl(conn);
            Scanner sc = new Scanner(System.in);

            while (true) {
                System.out.println("Menu:");
                System.out.println("1. Authenticate User");
                System.out.println("2. Add User");
                System.out.println("3. Delete User");
                System.out.println("4. Exit");
                System.out.print("Choose an option: ");

                int choice = sc.nextInt();
                sc.nextLine(); // Consume the newline character
                switch (choice) {
                    case 1:
                    	//authenticate the user
                        System.out.print("Enter username: ");
                        String username = sc.nextLine();
                        System.out.print("Enter password: ");
                        String password = sc.nextLine();
                        if (financeRepo.authenticateUser(username, password)) {
                        	while(true) {
                            System.out.println("Enter a choice:");
                            System.out.println("1. Add Expense");
                            System.out.println("2. Delete Expense");
                            System.out.println("3. Update Expense");
                            System.out.println("4. Get All Expenses");
                            System.out.println("5. Exit");
                            int ch = sc.nextInt();
                            switch(ch) {                                
                            case 1:
                            	//add an expense
                            	System.out.println("Enter userId, amount, categoryId, date (yyyy-mm-dd), description:");
                                Expense expense = new Expense(sc.nextInt(),0, sc.nextDouble(), sc.nextInt(), java.sql.Date.valueOf(sc.next()), sc.next());
                                financeRepo.createExpense(expense);
                                System.out.println("Expense Created");
                                break;
                                
                            case 2:
                                // delete an expense
                                System.out.println("Enter expenseId to delete:");
                                int expenseId = sc.nextInt();
                                try {
                                    financeRepo.deleteExpense(expenseId);
                                    System.out.println("Expense deleted.");
                                } catch (ExpenseNotFoundException e) {
                                    System.out.println(e.getMessage());
                                }
                                break;

                            case 3:
                                // update an expense
                                System.out.println("Enter userId, expenseId, amount, categoryId, date (yyyy-mm-dd), description:");
                                Expense updatedExpense = new Expense(sc.nextInt(), sc.nextInt(), sc.nextDouble(), sc.nextInt(), java.sql.Date.valueOf(sc.next()), sc.next());
                                try {
                                    financeRepo.updateExpense(updatedExpense);
                                    System.out.println("Expense updated.");
                                } catch (ExpenseNotFoundException e) {
                                    System.out.println(e.getMessage());
                                }
                                break;
                            
                            case 4:
                            	//show all expenses for a certain userID
                            	System.out.println("Enter your userId to view all expenses:");
                            	int userIdForExpenses = sc.nextInt();
                            	
                                List<Expense> expenses = financeRepo.getAllExpenses(userIdForExpenses);
                                if (expenses.isEmpty()) {
                                    System.out.println("No expenses found.");
                                } else {
                                    System.out.println("Expenses:");
                                    for (Expense exp : expenses) {
                                        System.out.println("Expense ID: " + exp.getExpenseId() + 
                                                           ", Amount: " + exp.getAmount() + 
                                                           ", Category ID: " + exp.getCategoryId() + 
                                                           ", Date: " + exp.getDate() + 
                                                           ", Description: " + exp.getDescription());
                                    }
                                }
                            	
                            	
                                
                                break;
                                
                            case 5:
                            	//log out for current user
                            	System.out.println("Logging out. Exiting the app...");
                                return;
                                
                            default:
                                System.out.println("Invalid choice");
                            }
                        	}
                        } else {
                            System.out.println("Invalid username or password.");
                        }
                        break;
                        
                    case 2:
                    	//create a new user
                    	System.out.println("Enter username, password, email:");
                        User user = new User(0, sc.next(), sc.next(), sc.next());
                        financeRepo.createUser(user);
                        System.out.println("User created.");
                        break;
                        
                    case 3:
                    	//delete an existing user
                    	System.out.println("Authenticate yourself");
                    	System.out.print("Enter username: ");
                        String username2 = sc.nextLine();
                        System.out.print("Enter password: ");
                        String password2 = sc.nextLine();
                        if (financeRepo.authenticateUser(username2, password2)) {
                        	System.out.println("Enter the userId to delete:");
                            int userIdToDelete = sc.nextInt();
                            try {
                                boolean isDeleted = financeRepo.deleteUser(userIdToDelete);
                                if (isDeleted) {
                                    System.out.println("User deleted successfully.");
                                }
                            } catch (UserNotFoundException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        else {
                            System.out.println("Invalid username or password.");
                        }
                    	
                        break;
                    case 4:
                    	//exit the app
                    	System.out.println("Exiting the app...");
                        return;    
                    default:
                        System.out.println("Invalid choice");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
