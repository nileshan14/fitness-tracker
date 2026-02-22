package projects.fitnesstracker.ui;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import projects.fitnesstracker.data.WorkoutRepository;
import projects.fitnesstracker.model.Workout;
import projects.fitnesstracker.util.Session;

import java.time.LocalDate;
import java.util.List;

public class WorkoutView extends VBox {

    private final WorkoutRepository repo = new WorkoutRepository();
    private final ListView<Workout> historyList = new ListView<>();
    private final Label status = new Label();

    public WorkoutView() {
        setPadding(new Insets(30));
        setSpacing(14);

        Label title = new Label("Log Workout");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Form controls
        DatePicker datePicker = new DatePicker(LocalDate.now());

        TextField typeField = new TextField();
        typeField.setPromptText("Workout type (Run, Lift, Bike...)");

        TextField durationField = new TextField();
        durationField.setPromptText("Duration (minutes)");

        TextField caloriesField = new TextField();
        caloriesField.setPromptText("Calories burned");

        Button saveBtn = new Button("Save Workout");
        Button backBtn = new Button("Back to Dashboard");

        HBox buttonRow = new HBox(10, saveBtn, backBtn);

        status.setStyle("-fx-text-fill: #e5e7eb;");

        // History section
        Label historyTitle = new Label("Workout History");
        historyTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        historyList.setPrefHeight(260);

        // Actions
        saveBtn.setOnAction(e -> {
            String type = typeField.getText() == null ? "" : typeField.getText().trim();
            if (type.isEmpty()) {
                setError("Type is required.");
                return;
            }

            int duration;
            int calories;
            try {
                duration = Integer.parseInt(durationField.getText().trim());
                calories = Integer.parseInt(caloriesField.getText().trim());
            } catch (Exception ex) {
                setError("Duration and calories must be valid whole numbers.");
                return;
            }

            if (duration <= 0) {
                setError("Duration must be > 0.");
                return;
            }
            if (calories < 0) {
                setError("Calories must be >= 0.");
                return;
            }

            int userId = Session.getCurrentUser().getId();
            String date = datePicker.getValue().toString();

            repo.addWorkout(userId, date, type, duration, calories);

            // Clear fields + refresh list
            typeField.clear();
            durationField.clear();
            caloriesField.clear();

            setOk("Saved ✅");
            refreshHistory();
        });

        backBtn.setOnAction(e -> SceneManager.showDashboard());

        // Initial load
        refreshHistory();

        getChildren().addAll(
                title,
                new Label("Date"), datePicker,
                typeField, durationField, caloriesField,
                buttonRow,
                status,
                new Separator(),
                historyTitle,
                historyList
        );
    }

    private void refreshHistory() {
        int userId = Session.getCurrentUser().getId();
        List<Workout> workouts = repo.getWorkoutsForUser(userId);
        historyList.setItems(FXCollections.observableArrayList(workouts));
    }

    private void setError(String msg) {
        status.setStyle("-fx-text-fill: #ef4444;");
        status.setText(msg);
    }

    private void setOk(String msg) {
        status.setStyle("-fx-text-fill: #22c55e;");
        status.setText(msg);
    }
}