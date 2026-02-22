package projects.fitnesstracker.data;

import projects.fitnesstracker.model.Workout;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkoutRepository {

    public int createWorkout(int userId, String date, String title) {
        String sql = "INSERT INTO workouts(user_id, date, title) VALUES(?,?,?)";

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userId);
            ps.setString(2, date);
            ps.setString(3, title);
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (!keys.next()) throw new RuntimeException("No generated key for workout");
            return keys.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create workout: " + e.getMessage(), e);
        }
    }

    public List<Workout> getWorkoutsForUser(int userId) {
        String sql = """
            SELECT id, user_id, date, title
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
                        rs.getString("title")
                ));
            }

            return out;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to load workouts: " + e.getMessage(), e);
        }
    }
}