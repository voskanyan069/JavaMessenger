package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;

public class SignupController {
    @FXML
    private TextField usernameInput;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private Button loginBtn;

    @FXML
    private Button signupBtn;

    @FXML
    private Button changeServerBtn;

    @FXML
    private Label errorText;

    @FXML
    void initialize() {
        createUser();
        onLogIn();
        onServer();
    }

    private void onServer() {
        changeServerBtn.setOnAction(actionEvent -> {
            try {
                ChangeServerController.previousStage = "signup";
                ChangeServerController.previousTitle = "Sign up";
                ChangeServerController.minSize = new int[]{500, 300};
                ChangeServerController.size = new int[]{640, 400};
                ChangeScene.changeScreen(getClass(), "change_server.fxml", actionEvent,
                        "Change server", new int[]{500, 300}, new int[]{640, 400});
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });
    }

    private void createUser() {
        signupBtn.setOnAction(actionEvent -> {
            String username = usernameInput.getText();
            String password = passwordInput.getText();

            if (username.equals("")) {
                errorText.setText("Please enter the username");
            } else if (password.equals("")) {
                errorText.setText("Please enter the password");
            } else if (username.contains(" ")) {
                errorText.setText("Please enter the correct username");
            } else if (password.contains(" ")) {
                errorText.setText("Please enter the correct password");
            } else {
                try {
                    GetRequestServer getRequestServer = new GetRequestServer(Config.URL + "/get_users");
                    JSONArray users = getRequestServer.sendUsersGetRequest();

                    for (int i = 0; i < users.length(); i++) {
                        String jsonUsername = users.getJSONObject(i).getString("username");

                        if (username.equals(jsonUsername)) {
                            errorText.setText("User with this username already exist");
                            return;
                        }
                    }

                    PostRequestServer postRequestServer = new PostRequestServer(Config.URL + "/add_user");
                    try {
                        postRequestServer.sendPost(new String[]{"username", "password"}, new String[]{username, password});
                        new CurrentUser(username, password);
                        ChangeScene.changeScreen(getClass(), "messenger.fxml", actionEvent,
                                "Messenger", new int[]{400, 600}, new int[]{1200, 800});
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    errorText.setText("");
                } catch (JSONException | IOException e) {
                    errorText.setText(e.getMessage());
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    private void onLogIn() {
        loginBtn.setOnAction(actionEvent -> {
            try {
                ChangeScene.changeScreen(getClass(), "login.fxml", actionEvent,
                        "Log in", new int[]{500, 300}, new int[]{640, 400});
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });
    }
}
