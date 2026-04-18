import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DatabaseInitializer - Automatically creates database, tables, and populates data
 * This runs when the application starts to ensure the database is ready
 */
public class DatabaseInitializer {
    
    /**
     * Initialize the database with all required tables and data
     */
    public static void initializeDatabase() {
        try {
            System.out.println("Checking database setup...");
            
            // First, create the database if it doesn't exist
            createDatabase();
            
            // Then create tables and populate data
            createTablesAndPopulateData();
            
            System.out.println("Database initialization complete!");
            
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Create the trip_planner database if it doesn't exist
     */
    private static void createDatabase() {
        String url = "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String user = "root";
        String password = "harithaishu";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = java.sql.DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();
            
            // Create database
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS YOUR_DATABASE_NAME");
            System.out.println("✓ Database 'YOUR_DATABASE_NAME' is ready");
            
            stmt.close();
            conn.close();
            
        } catch (Exception e) {
            System.err.println("Error creating database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Create tables and populate with data
     */
    private static void createTablesAndPopulateData() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Create hotels table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS hotels (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL, " +
                "destination VARCHAR(255) NOT NULL, " +
                "price_per_night DECIMAL(10,2) NOT NULL)"
            );
            System.out.println("✓ Hotels table created");
            
            // Create tourist_spots table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS tourist_spots (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL, " +
                "destination VARCHAR(255) NOT NULL)"
            );
            System.out.println("✓ Tourist spots table created");
            
            // Create food_spots table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS food_spots (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL, " +
                "destination VARCHAR(255) NOT NULL)"
            );
            System.out.println("✓ Food spots table created");
            
            // Check if data already exists
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM hotels");
            rs.next();
            int hotelCount = rs.getInt("count");
            
            if (hotelCount == 0) {
                System.out.println("Tables created but no sample data populated (using your existing data)...");
                // Removed sample data population
            } else {
                System.out.println("✓ Database already contains data");
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Populate hotels table with sample data
     */
    private static void populateHotels(Statement stmt) throws SQLException {
        String[] hotels = {
            "INSERT INTO hotels (name, destination, price_per_night) VALUES ('Hotel Paradise', 'Munnar', 800.00)",
            "INSERT INTO hotels (name, destination, price_per_night) VALUES ('Green Valley Resort', 'Munnar', 1000.00)",
            "INSERT INTO hotels (name, destination, price_per_night) VALUES ('Tea Garden Lodge', 'Munnar', 900.00)",
            "INSERT INTO hotels (name, destination, price_per_night) VALUES ('Hill View Hotel', 'Munnar', 700.00)",
            "INSERT INTO hotels (name, destination, price_per_night) VALUES ('Spice Mountain Resort', 'Munnar', 1200.00)",
            
            "INSERT INTO hotels (name, destination, price_per_night) VALUES ('City Center Hotel', 'Bangalore', 900.00)",
            "INSERT INTO hotels (name, destination, price_per_night) VALUES ('Tech Hub Inn', 'Bangalore', 850.00)",
            "INSERT INTO hotels (name, destination, price_per_night) VALUES ('Garden City Resort', 'Bangalore', 1100.00)",
            "INSERT INTO hotels (name, destination, price_per_night) VALUES ('Business Hotel', 'Bangalore', 750.00)",
            "INSERT INTO hotels (name, destination, price_per_night) VALUES ('Silicon Valley Lodge', 'Bangalore', 1000.00)",
            "INSERT INTO hotels (name, destination, price_per_night) VALUES ('Royal Orchid', 'Bangalore', 1300.00)",
            
            "INSERT INTO hotels (name, destination, price_per_night) VALUES ('Beach View Resort', 'Pondicherry', 1000.00)",
            "INSERT INTO hotels (name, destination, price_per_night) VALUES ('French Quarter Hotel', 'Pondicherry', 1200.00)",
            "INSERT INTO hotels (name, destination, price_per_night) VALUES ('Seaside Villa', 'Pondicherry', 1100.00)",
            "INSERT INTO hotels (name, destination, price_per_night) VALUES ('Colonial Inn', 'Pondicherry', 950.00)",
            "INSERT INTO hotels (name, destination, price_per_night) VALUES ('Ocean Breeze Resort', 'Pondicherry', 1300.00)"
        };
        
        for (String sql : hotels) {
            stmt.executeUpdate(sql);
        }
    }
    
    /**
     * Populate tourist_spots table with sample data
     */
    private static void populateTouristSpots(Statement stmt) throws SQLException {
        String[] spots = {
            "INSERT INTO tourist_spots (name, destination) VALUES ('Tea Gardens', 'Munnar')",
            "INSERT INTO tourist_spots (name, destination) VALUES ('Eravikulam National Park', 'Munnar')",
            "INSERT INTO tourist_spots (name, destination) VALUES ('Mattupetty Dam', 'Munnar')",
            "INSERT INTO tourist_spots (name, destination) VALUES ('Top Station', 'Munnar')",
            "INSERT INTO tourist_spots (name, destination) VALUES ('Anamudi Peak', 'Munnar')",
            "INSERT INTO tourist_spots (name, destination) VALUES ('Kundala Lake', 'Munnar')",
            "INSERT INTO tourist_spots (name, destination) VALUES ('Rose Garden', 'Munnar')",
            
            "INSERT INTO tourist_spots (name, destination) VALUES ('Lalbagh Botanical Garden', 'Bangalore')",
            "INSERT INTO tourist_spots (name, destination) VALUES ('Bangalore Palace', 'Bangalore')",
            "INSERT INTO tourist_spots (name, destination) VALUES ('Cubbon Park', 'Bangalore')",
            "INSERT INTO tourist_spots (name, destination) VALUES ('Vidhana Soudha', 'Bangalore')",
            "INSERT INTO tourist_spots (name, destination) VALUES ('ISKCON Temple', 'Bangalore')",
            "INSERT INTO tourist_spots (name, destination) VALUES ('UB City Mall', 'Bangalore')",
            "INSERT INTO tourist_spots (name, destination) VALUES ('Nandi Hills', 'Bangalore')",
            "INSERT INTO tourist_spots (name, destination) VALUES ('Tipu Sultan Palace', 'Bangalore')",
            
            "INSERT INTO tourist_spots (name, destination) VALUES ('Promenade Beach', 'Pondicherry')",
            "INSERT INTO tourist_spots (name, destination) VALUES ('Auroville', 'Pondicherry')",
            "INSERT INTO tourist_spots (name, destination) VALUES ('French Quarter', 'Pondicherry')",
            "INSERT INTO tourist_spots (name, destination) VALUES ('Paradise Beach', 'Pondicherry')",
            "INSERT INTO tourist_spots (name, destination) VALUES ('Sri Aurobindo Ashram', 'Pondicherry')",
            "INSERT INTO tourist_spots (name, destination) VALUES ('Pondicherry Museum', 'Pondicherry')",
            "INSERT INTO tourist_spots (name, destination) VALUES ('Rock Beach', 'Pondicherry')",
            "INSERT INTO tourist_spots (name, destination) VALUES ('Botanical Garden', 'Pondicherry')"
        };
        
        for (String sql : spots) {
            stmt.executeUpdate(sql);
        }
    }
    
    /**
     * Populate food_spots table with sample data
     */
    private static void populateFoodSpots(Statement stmt) throws SQLException {
        String[] foods = {
            "INSERT INTO food_spots (name, destination) VALUES ('Spice Garden Restaurant', 'Munnar')",
            "INSERT INTO food_spots (name, destination) VALUES ('Hill View Cafe', 'Munnar')",
            "INSERT INTO food_spots (name, destination) VALUES ('Tea House Restaurant', 'Munnar')",
            "INSERT INTO food_spots (name, destination) VALUES ('Mountain Spice', 'Munnar')",
            "INSERT INTO food_spots (name, destination) VALUES ('Cardamom County', 'Munnar')",
            "INSERT INTO food_spots (name, destination) VALUES ('Copper Castle', 'Munnar')",
            
            "INSERT INTO food_spots (name, destination) VALUES ('MTR Restaurant', 'Bangalore')",
            "INSERT INTO food_spots (name, destination) VALUES ('Koshy\\'s Restaurant', 'Bangalore')",
            "INSERT INTO food_spots (name, destination) VALUES ('Vidyarthi Bhavan', 'Bangalore')",
            "INSERT INTO food_spots (name, destination) VALUES ('Cafe Coffee Day', 'Bangalore')",
            "INSERT INTO food_spots (name, destination) VALUES ('Toit Brewpub', 'Bangalore')",
            "INSERT INTO food_spots (name, destination) VALUES ('The Only Place', 'Bangalore')",
            "INSERT INTO food_spots (name, destination) VALUES ('Mavalli Tiffin Room', 'Bangalore')",
            "INSERT INTO food_spots (name, destination) VALUES ('Corner House Ice Cream', 'Bangalore')",
            
            "INSERT INTO food_spots (name, destination) VALUES ('Cafe des Arts', 'Pondicherry')",
            "INSERT INTO food_spots (name, destination) VALUES ('Villa Shanti', 'Pondicherry')",
            "INSERT INTO food_spots (name, destination) VALUES ('Le Dupleix', 'Pondicherry')",
            "INSERT INTO food_spots (name, destination) VALUES ('Surguru Restaurant', 'Pondicherry')",
            "INSERT INTO food_spots (name, destination) VALUES ('Baker Street Bistro', 'Pondicherry')",
            "INSERT INTO food_spots (name, destination) VALUES ('Carte Blanche', 'Pondicherry')",
            "INSERT INTO food_spots (name, destination) VALUES ('Tanto Pizzeria', 'Pondicherry')"
        };
        
        for (String sql : foods) {
            stmt.executeUpdate(sql);
        }
    }
}
