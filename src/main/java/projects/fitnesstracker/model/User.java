package projects.fitnesstracker.model;

public class User {
    private final int id;
    private final String username;
    private final String passwordHash;

    private String name;

    public User(int id, String username, String passwordHash) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}