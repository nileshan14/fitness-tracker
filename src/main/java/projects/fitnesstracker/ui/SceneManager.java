package projects.fitnesstracker.ui;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;

public class SceneManager {
    private static Stage stage;

    public static void init(Stage s) {
        stage = s;
    }

    private static void ensureStage() {
        if (stage == null) throw new IllegalStateException("SceneManager.init(stage) was not called.");
    }

    public static void showLogin() {
        ensureStage();
        stage.setScene(new Scene(new LoginView(), 900, 600));
    }

    public static void showSignup() {
        ensureStage();
        stage.setScene(new Scene(new SignupView(), 900, 600));
    }

    public static void showDashboard() {
        ensureStage();
        stage.setScene(new Scene(new DashboardView(), 900, 600));
    }

    public static void showWorkout() {
        ensureStage();
        try {
            stage.setScene(new Scene(new WorkoutView(), 900, 600));
        } catch (Exception ex) {
            showError("Failed to open Workout screen", ex);
        }
    }

    private static void showError(String title, Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(ex.getClass().getSimpleName() + ": " + ex.getMessage());

        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        TextArea area = new TextArea(sw.toString());
        area.setEditable(false);
        area.setWrapText(false);
        area.setPrefWidth(800);
        area.setPrefHeight(300);

        alert.getDialogPane().setExpandableContent(area);
        alert.showAndWait();
    }
}