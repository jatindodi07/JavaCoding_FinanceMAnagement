package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnUtil {
    private static Connection connection;
    public static Connection getConnection(String propertyFileName) {
        if (connection == null) {
            try {
                String connectionString = DBPropertyUtil.getConnectionString(propertyFileName);
                connection = DriverManager.getConnection(connectionString);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
