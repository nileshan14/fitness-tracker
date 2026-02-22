package projects.fitnesstracker.ui;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private static Stage stage;

    public static void init(Stage s) {
        stage = s;
    }

    public static void showLogin() {
        stage.setScene(new Scene(new LoginView(), 900, 600));
    }

    public static void showSignup() {
        stage.setScene(new Scene(new SignupView(), 900, 600));
    }

    public static void showDashboard() {
        stage.setScene(new Scene(new DashboardView(), 900, 600));
    }
}