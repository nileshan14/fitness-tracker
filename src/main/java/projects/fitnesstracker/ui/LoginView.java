package projects.fitnesstracker.ui;

import projects.fitnesstracker.service.AuthService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class LoginView extends VBox {

    public LoginView() {
        setPadding(new Insets(30));
        setSpacing(12);

        Label title = new Label("Login");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextField username = new TextField();
        username.setPromptText("Username");

        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        Label error = new Label();
        error.setStyle("-fx-text-fill: #ef4444;");

        Button loginBtn = new Button("Login");
        Hyperlink goSignup = new Hyperlink("Create an account");

        AuthService auth = new AuthService();

        loginBtn.setOnAction(e -> {
            String msg = auth.login(username.getText(), password.getText());
            if (msg == null) SceneManager.showDashboard();
            else error.setText(msg);
        });

        goSignup.setOnAction(e -> SceneManager.showSignup());

        getChildren().addAll(title, username, password, loginBtn, goSignup, error);
    }
}