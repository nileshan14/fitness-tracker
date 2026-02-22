package projects.fitnesstracker.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import projects.fitnesstracker.service.AuthService;
import projects.fitnesstracker.util.Session;

public class DashboardView extends VBox {

    public DashboardView() {
        setPadding(new Insets(30));
        setSpacing(12);

        String displayName = (Session.getCurrentUser().getName() != null)
                ? Session.getCurrentUser().getName()
                : Session.getCurrentUser().getUsername();

        Label title = new Label("Dashboard");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Label welcome = new Label("Welcome, " + displayName + " 👋");

        Button logWorkout = new Button("Log Workout");
        logWorkout.setOnAction(e -> SceneManager.showWorkout());

        // Placeholder buttons for next features
        Button logMeal = new Button("Log Meal (next)");
        Button profile = new Button("Profile (next)");

        Button logout = new Button("Logout");
        AuthService auth = new AuthService();
        logout.setOnAction(e -> {
            auth.logout();
            SceneManager.showLogin();
        });

        getChildren().addAll(title, welcome, logWorkout, logMeal, profile, logout);
    }
}