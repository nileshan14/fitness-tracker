package projects.fitnesstracker.ui;

import projects.fitnesstracker.service.AuthService;
import projects.fitnesstracker.util.Session;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

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

        Button logout = new Button("Logout");
        AuthService auth = new AuthService();
        logout.setOnAction(e -> {
            auth.logout();
            SceneManager.showLogin();
        });

        getChildren().addAll(title, welcome, logout);
    }
}