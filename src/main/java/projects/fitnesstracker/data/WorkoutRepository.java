package projects.fitnesstracker.data;

import projects.fitnesstracker.model.Workout;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkoutRepository {

    public void addWorkout(int userId, String date, String type, int durationMin, int caloriesBurned) {
        String sql = """
            INSERT INTO workouts(user_id, date, type, duration_min, calories_burned)
            VALUES(?,?,?,?,?)
        """;

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, date);
            ps.setString(3, type);
            ps.setInt(4, durationMin);
            ps.setInt(5, caloriesBurned);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to add workout: " + e.getMessage(), e);
        }
    }

    public List<Workout> getWorkoutsForUser(int userId) {
        String sql = """
            SELECT id, user_id, date, type, duration_min, calories_burned
            FROM workouts
            WHERE user_id = ?
            ORDER BY date DESC, id DESC
        """;

        List<Workout> out = new ArrayList<>();

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                out.add(new Workout(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("date"),
                        rs.getString("type"),
                        rs.getInt("duration_min"),
                        rs.getInt("calories_burned")
                ));
            }

            return out;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load workouts: " + e.getMessage(), e);
        }
    }
}