package projects.fitnesstracker.model;

public class Workout {
    private final int id;
    private final int userId;
    private final String date;   // YYYY-MM-DD
    private final String title;  // Push/Legs/etc

    public Workout(int id, int userId, String date, String title) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.title = title;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getDate() { return date; }
    public String getTitle() { return title; }

    @Override
    public String toString() {
        return date + " • " + title;
    }
}