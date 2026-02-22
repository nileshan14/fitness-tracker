package projects.fitnesstracker.data;

import projects.fitnesstracker.model.ExerciseEntry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExerciseEntryRepository {

    public void addEntry(int workoutId, String name, double weight, int reps) {
        String sql = """
            INSERT INTO exercise_entries(workout_id, exercise_name, weight, reps)
            VALUES(?,?,?,?)
        """;

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, workoutId);
            ps.setString(2, name);
            ps.setDouble(3, weight);
            ps.setInt(4, reps);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to add exercise entry: " + e.getMessage(), e);
        }
    }

    public List<ExerciseEntry> getEntriesForWorkout(int workoutId) {
        String sql = """
            SELECT id, workout_id, exercise_name, weight, reps
            FROM exercise_entries
            WHERE workout_id = ?
            ORDER BY id ASC
        """;

        List<ExerciseEntry> out = new ArrayList<>();

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, workoutId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                out.add(new ExerciseEntry(
                        rs.getInt("id"),
                        rs.getInt("workout_id"),
                        rs.getString("exercise_name"),
                        rs.getDouble("weight"),
                        rs.getInt("reps")
                ));
            }
            return out;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to load exercise entries: " + e.getMessage(), e);
        }
    }
}