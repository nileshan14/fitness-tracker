package projects.fitnesstracker.service;

import projects.fitnesstracker.data.UserRepository;
import projects.fitnesstracker.model.User;
import projects.fitnesstracker.util.PasswordHasher;
import projects.fitnesstracker.util.Session;

public class AuthService {
    private final UserRepository users = new UserRepository();

    public String signup(String username, String password, String confirm) {
        if (username == null || username.trim().isEmpty()) return "Username required.";
        if (password == null || password.length() < 6) return "Password must be at least 6 characters.";
        if (!password.equals(confirm)) return "Passwords do not match.";

        String hash = PasswordHasher.hash(password);
        User created = users.create(username.trim(), hash);
        if (created == null) return "That username is already taken.";

        Session.setCurrentUser(created);
        return null;
    }

    public String login(String username, String password) {
        if (username == null || username.trim().isEmpty()) return "Username required.";
        if (password == null || password.isEmpty()) return "Password required.";

        User u = users.findByUsername(username.trim());
        if (u == null) return "User not found.";

        if (!PasswordHasher.verify(password, u.getPasswordHash())) return "Wrong password.";

        Session.setCurrentUser(u);
        return null;
    }

    public void logout() {
        Session.clear();
    }
}