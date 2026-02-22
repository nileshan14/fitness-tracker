package projects.fitnesstracker.data;

import projects.fitnesstracker.model.User;

import java.sql.*;

public class UserRepository {

    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;

            User u = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password_hash"));
            u.setName(rs.getString("name"));
            return u;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User create(String username, String passwordHash) {
        String sql = "INSERT INTO users(username, password_hash) VALUES(?, ?)";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (!keys.next()) throw new RuntimeException("No generated key for user");
            int id = keys.getInt(1);

            return new User(id, username, passwordHash);
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("unique")) {
                return null; // username taken
            }
            throw new RuntimeException(e);
        }
    }
}