package projects.fitnesstracker;

import projects.fitnesstracker.data.Database;
import projects.fitnesstracker.ui.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Database.init();
        SceneManager.init(stage);
        SceneManager.showLogin();

        stage.setTitle("Fitness Tracker");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}