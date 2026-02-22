package projects.fitnesstracker.model;

public class Workout {
    private final int id;
    private final int userId;
    private final String date;          // YYYY-MM-DD
    private final String type;
    private final int durationMin;
    private final int caloriesBurned;

    public Workout(int id, int userId, String date, String type, int durationMin, int caloriesBurned) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.type = type;
        this.durationMin = durationMin;
        this.caloriesBurned = caloriesBurned;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getDate() { return date; }
    public String getType() { return type; }
    public int getDurationMin() { return durationMin; }
    public int getCaloriesBurned() { return caloriesBurned; }

    @Override
    public String toString() {
        return date + " • " + type + " • " + durationMin + " min • " + caloriesBurned + " kcal";
    }
}