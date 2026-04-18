import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/trip_planner?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";              // ✅ your MySQL username
    private static final String PASSWORD = "harithaishu";   // ✅ your MySQL password

    public static Connection getConnection() throws SQLException {
        try {
            // ✅ Load MySQL JDBC driver explicitly
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found! Did you add the connector JAR to classpath?", e);
        }

        // ✅ Establish connection
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
