package projects.fitnesstracker.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import projects.fitnesstracker.data.ExerciseEntryRepository;
import projects.fitnesstracker.data.WorkoutRepository;
import projects.fitnesstracker.model.ExerciseEntry;
import projects.fitnesstracker.model.Workout;
import projects.fitnesstracker.util.Session;

import java.time.LocalDate;
import java.util.List;

public class WorkoutView extends VBox {

    private final WorkoutRepository workoutRepo = new WorkoutRepository();
    private final ExerciseEntryRepository entryRepo = new ExerciseEntryRepository();

    private final ObservableList<TempEntry> pendingEntries = FXCollections.observableArrayList();

    private final ListView<Workout> workoutHistory = new ListView<>();
    private final ListView<ExerciseEntry> exerciseHistory = new ListView<>();

    private final Label status = new Label();

    public WorkoutView() {
        setPadding(new Insets(24));
        setSpacing(12);

        Label title = new Label("Log Workout (Exercises)");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        DatePicker datePicker = new DatePicker(LocalDate.now());

        TextField workoutTitle = new TextField();
        workoutTitle.setPromptText("Workout title (Push Day / Legs / Upper...)");

        TextField exName = new TextField();
        exName.setPromptText("Exercise (Bench Press)");

        TextField exWeight = new TextField();
        exWeight.setPromptText("Weight (e.g., 135)");

        TextField exReps = new TextField();
        exReps.setPromptText("Reps (e.g., 8)");

        Button addEntryBtn = new Button("Add Entry");
        HBox exRow = new HBox(10, exName, exWeight, exReps, addEntryBtn);

        ListView<TempEntry> pendingList = new ListView<>(pendingEntries);
        pendingList.setPrefHeight(140);

        Button removeSelected = new Button("Remove Selected");
        Button saveWorkout = new Button("Save Workout");
        Button back = new Button("Back");
        HBox actionRow = new HBox(10, removeSelected, saveWorkout, back);

        status.setStyle("-fx-text-fill: #94a3b8;");

        Label histTitle = new Label("Saved Workouts");
        histTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        workoutHistory.setPrefHeight(170);
        exerciseHistory.setPrefHeight(170);

        // Selecting a workout loads its exercises (safe)
        workoutHistory.getSelectionModel().selectedItemProperty().addListener((obs, oldW, newW) -> {
            try {
                if (newW == null) {
                    exerciseHistory.setItems(FXCollections.observableArrayList());
                } else {
                    List<ExerciseEntry> entries = entryRepo.getEntriesForWorkout(newW.getId());
                    exerciseHistory.setItems(FXCollections.observableArrayList(entries));
                }
            } catch (Exception ex) {
                setError("Could not load entries. If you changed DB tables, delete fitness.db and re-run.");
            }
        });

        addEntryBtn.setOnAction(e -> {
            String name = exName.getText() == null ? "" : exName.getText().trim();
            if (name.isEmpty()) { setError("Exercise name is required."); return; }

            double weight;
            int reps;

            try {
                weight = Double.parseDouble(exWeight.getText().trim());
                reps = Integer.parseInt(exReps.getText().trim());
            } catch (Exception ex) {
                setError("Weight must be a number and reps must be a whole number.");
                return;
            }

            if (weight < 0) { setError("Weight must be >= 0."); return; }
            if (reps <= 0) { setError("Reps must be > 0."); return; }

            pendingEntries.add(new TempEntry(name, weight, reps));
            exName.clear();
            exWeight.clear();
            exReps.clear();
            setOk("Entry added ✅");
        });

        removeSelected.setOnAction(e -> {
            TempEntry selected = pendingList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                pendingEntries.remove(selected);
                setOk("Entry removed ✅");
            }
        });

        saveWorkout.setOnAction(e -> {
            try {
                if (Session.getCurrentUser() == null) {
                    setError("Session expired. Please log in again.");
                    SceneManager.showLogin();
                    return;
                }

                String wTitle = workoutTitle.getText() == null ? "" : workoutTitle.getText().trim();
                if (wTitle.isEmpty()) { setError("Workout title is required."); return; }
                if (pendingEntries.isEmpty()) { setError("Add at least 1 entry before saving."); return; }

                int userId = Session.getCurrentUser().getId();
                String date = datePicker.getValue().toString();

                int workoutId = workoutRepo.createWorkout(userId, date, wTitle);
                for (TempEntry te : pendingEntries) {
                    entryRepo.addEntry(workoutId, te.name, te.weight, te.reps);
                }

                pendingEntries.clear();
                workoutTitle.clear();
                setOk("Workout saved ✅");
                refreshWorkoutHistory();

            } catch (Exception ex) {
                setError("Save failed. If you changed DB tables, delete fitness.db and re-run.");
                throw ex; // so SceneManager popup will show the real error too
            }
        });

        back.setOnAction(e -> SceneManager.showDashboard());

        getChildren().addAll(
                title,
                new Label("Date"), datePicker,
                workoutTitle,
                new Separator(),
                new Label("Add exercise entries (each entry can be a set)"),
                exRow,
                new Label("Pending entries"), pendingList,
                actionRow,
                status,
                new Separator(),
                histTitle,
                new Label("Workouts (select one)"), workoutHistory,
                new Label("Entries in selected workout"), exerciseHistory
        );

        // Load history LAST, and safely
        try {
            refreshWorkoutHistory();
        } catch (Exception ex) {
            setError("Could not load workouts. If you changed DB tables, delete fitness.db and re-run.");
            throw ex; // show popup with actual error
        }
    }

    private void refreshWorkoutHistory() {
        int userId = Session.getCurrentUser().getId();
        List<Workout> workouts = workoutRepo.getWorkoutsForUser(userId);
        workoutHistory.setItems(FXCollections.observableArrayList(workouts));
        exerciseHistory.setItems(FXCollections.observableArrayList());
    }

    private void setError(String msg) {
        status.setStyle("-fx-text-fill: #ef4444;");
        status.setText(msg);
    }

    private void setOk(String msg) {
        status.setStyle("-fx-text-fill: #22c55e;");
        status.setText(msg);
    }

    private static class TempEntry {
        final String name;
        final double weight;
        final int reps;

        TempEntry(String name, double weight, int reps) {
            this.name = name;
            this.weight = weight;
            this.reps = reps;
        }

        @Override
        public String toString() {
            return name + " • " + weight + " • " + reps + " reps";
        }
    }
}