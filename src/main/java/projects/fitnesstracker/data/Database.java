package projects.fitnesstracker.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    // This creates a file called fitness.db in your PROJECT ROOT (same level as pom.xml)
    private static final String URL = "jdbc:sqlite:fitness.db";

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);

        // SQLite needs this ON each connection for foreign keys to actually enforce
        try (Statement st = conn.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON;");
        }

        return conn;
    }

    public static void init() {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {

            // USERS table
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  username TEXT UNIQUE NOT NULL,
                  password_hash TEXT NOT NULL,
                  name TEXT,
                  age INTEGER,
                  height_cm REAL,
                  weight_kg REAL,
                  goal_calories INTEGER
                );
            """);

            // WORKOUTS table
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS workouts (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  user_id INTEGER NOT NULL,
                  date TEXT NOT NULL,                 -- YYYY-MM-DD
                  type TEXT NOT NULL,
                  duration_min INTEGER NOT NULL CHECK(duration_min > 0),
                  calories_burned INTEGER NOT NULL CHECK(calories_burned >= 0),
                  FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
                );
            """);

            // MEALS table
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS meals (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  user_id INTEGER NOT NULL,
                  date TEXT NOT NULL,                 -- YYYY-MM-DD
                  name TEXT NOT NULL,
                  calories INTEGER NOT NULL CHECK(calories >= 0),
                  FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
                );
            """);

        } catch (SQLException e) {
            throw new RuntimeException("Database init failed: " + e.getMessage(), e);
        }
    }
}