package projects.fitnesstracker.data;

import java.sql.*;

public class Database {
    private static final String URL = "jdbc:sqlite:fitness.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void init() {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  username TEXT UNIQUE NOT NULL,
                  password_hash TEXT NOT NULL,
                  name TEXT
                );
            """);

        } catch (SQLException e) {
            throw new RuntimeException("DB init failed: " + e.getMessage(), e);
        }
    }
}