package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class LoginController {
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
        checkUser();
        onServer();
        onSignUp();
    }

    private void onServer() {
        changeServerBtn.setOnAction(actionEvent -> {
            try {
                ChangeServerController.previousStage = "login";
                ChangeServerController.previousTitle = "Log in";
                ChangeServerController.minSize = new int[]{500, 300};
                ChangeServerController.size = new int[]{640, 400};
                ChangeScene.changeScreen(getClass(), "change_server.fxml", actionEvent,
                        "Change server", new int[]{500, 300}, new int[]{640, 400});
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });
    }

    private void checkUser() {
        loginBtn.setOnAction(actionEvent -> {
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
                        JSONObject jsonElem = users.getJSONObject(i);
                        String jsonUsername = jsonElem.getString("username");
                        String jsonPassword = jsonElem.getString("password");

                        if (jsonUsername.equals(username) && jsonPassword.equals(password)) {
                            errorText.setText("");
                            Config.currentUsername = username;
                            ChangeScene.changeScreen(getClass(), "messenger.fxml", actionEvent,
                                    "Messenger", new int[]{400, 600}, new int[]{1200, 800});
                            return;
                        }

                        if (jsonUsername.equals(username) && !jsonPassword.equals(password)) {
                            errorText.setText("The password was incorrect");
                        } else if (!jsonUsername.equals(username) && !jsonPassword.equals(password)) {
                            errorText.setText("The username & password was incorrect");
                        }
                    }
                } catch (JSONException | IOException e) {
                    errorText.setText(e.getMessage());
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    private void onSignUp() {
        signupBtn.setOnAction(actionEvent -> {
            try {
                ChangeScene.changeScreen(getClass(), "signup.fxml", actionEvent,
                        "Sign up", new int[]{500, 300}, new int[]{640, 400});
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });
    }
}
