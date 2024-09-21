
package qpms1;

import java.sql.*;

public class DatabaseManager {
    // Update the database URL, username, and password with your MySQL connection details
    public static final String JDBC_URL = "jdbc:mysql://localhost:3306/QPMS";
    public static final String JDBC_USERNAME = "root";
    public static final String JDBC_PASSWORD = "aghar123";

    public static boolean authenticate(String username, String password) throws SQLException {
    String query = "SELECT * FROM users WHERE username = ? AND password = ?";
    try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            return resultSet.next(); // If a record is found, authentication is successful
        }
    } catch (SQLException e) {
        e.printStackTrace(); // Log the exception for debugging purposes
        throw e; // Re-throw the exception to be handled by the calling code
    }
}

}
