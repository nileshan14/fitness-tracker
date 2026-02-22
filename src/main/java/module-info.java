module projects.fitnesstracker {
    requires javafx.controls;
    requires javafx.fxml;


    opens projects.fitnesstracker to javafx.fxml;
    exports projects.fitnesstracker;
}