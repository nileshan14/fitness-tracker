package projects.fitnesstracker.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String URL = "jdbc:sqlite:fitness.db";

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        try (Statement st = conn.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }

    public static void init() {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {

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

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS workouts (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  user_id INTEGER NOT NULL,
                  date TEXT NOT NULL,
                  title TEXT NOT NULL,
                  FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
                );
            """);

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS exercise_entries (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  workout_id INTEGER NOT NULL,
                  exercise_name TEXT NOT NULL,
                  weight REAL NOT NULL CHECK(weight >= 0),
                  reps INTEGER NOT NULL CHECK(reps > 0),
                  FOREIGN KEY(workout_id) REFERENCES workouts(id) ON DELETE CASCADE
                );
            """);

        } catch (SQLException e) {
            throw new RuntimeException("Database init failed: " + e.getMessage(), e);
        }
    }
}