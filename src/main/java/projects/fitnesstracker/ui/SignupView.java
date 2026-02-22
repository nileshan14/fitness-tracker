package projects.fitnesstracker.ui;

import projects.fitnesstracker.service.AuthService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class SignupView extends VBox {

    public SignupView() {
        setPadding(new Insets(30));
        setSpacing(12);

        Label title = new Label("Sign Up");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextField username = new TextField();
        username.setPromptText("Username");

        PasswordField password = new PasswordField();
        password.setPromptText("Password (min 6 chars)");

        PasswordField confirm = new PasswordField();
        confirm.setPromptText("Confirm password");

        Label error = new Label();
        error.setStyle("-fx-text-fill: #ef4444;");

        Button createBtn = new Button("Create Account");
        Hyperlink backLogin = new Hyperlink("Back to login");

        AuthService auth = new AuthService();

        createBtn.setOnAction(e -> {
            String msg = auth.signup(username.getText(), password.getText(), confirm.getText());
            if (msg == null) {
                SceneManager.showDashboard();
            } else {
                error.setText(msg);
            }
        });

        backLogin.setOnAction(e -> SceneManager.showLogin());

        getChildren().addAll(title, username, password, confirm, createBtn, backLogin, error);
    }
}