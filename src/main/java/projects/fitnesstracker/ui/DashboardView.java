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

        String displayName = (Session.getCurrentUser() != null && Session.getCurrentUser().getName() != null)
                ? Session.getCurrentUser().getName()
                : (Session.getCurrentUser() != null ? Session.getCurrentUser().getUsername() : "User");

        Label title = new Label("Dashboard");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label welcome = new Label("Welcome, " + displayName + " 👋");

        Button logWorkout = new Button("Log Workout (Exercises)");
        logWorkout.setPrefWidth(250);
        logWorkout.setOnAction(e -> SceneManager.showWorkout());

        Button logout = new Button("Logout");
        logout.setPrefWidth(250);
        AuthService auth = new AuthService();
        logout.setOnAction(e -> {
            auth.logout();
            SceneManager.showLogin();
        });

        getChildren().addAll(title, welcome, logWorkout, logout);
    }
}