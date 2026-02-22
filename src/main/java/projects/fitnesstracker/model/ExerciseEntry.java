package projects.fitnesstracker.model;

public class ExerciseEntry {
    private final int id;
    private final int workoutId;
    private final String exerciseName;
    private final double weight;
    private final int reps;

    public ExerciseEntry(int id, int workoutId, String exerciseName, double weight, int reps) {
        this.id = id;
        this.workoutId = workoutId;
        this.exerciseName = exerciseName;
        this.weight = weight;
        this.reps = reps;
    }

    public int getId() { return id; }
    public int getWorkoutId() { return workoutId; }
    public String getExerciseName() { return exerciseName; }
    public double getWeight() { return weight; }
    public int getReps() { return reps; }

    @Override
    public String toString() {
        return exerciseName + " • " + weight + " • " + reps + " reps";
    }
}