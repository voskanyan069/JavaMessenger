package sample;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField usernameInput;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private Button loginBtn;

    @FXML
    private Button signupBtn;

    @FXML
    private Label errorText;

    @FXML
    void initialize() {
        checkUser();
        onSignUp();
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
                GetRequestServer getRequestServer = new GetRequestServer(CONFIG.URL + "/get_users");
                try {
                    JSONArray users = getRequestServer.sendUsersGetRequest();

                    for (int i = 0; i < users.length(); i++) {
                        JSONObject jsonElem = users.getJSONObject(i);
                        String jsonUsername = jsonElem.getString("username");
                        String jsonPassword = jsonElem.getString("password");

                        if (jsonUsername.equals(username) && jsonPassword.equals(password)) {
                            errorText.setText("");
                            new CurrentUser(username, password);
                            ChangeScene.changeScreen(getClass(), "messenger.fxml", actionEvent,
                                    new int[]{400, 600}, new int[]{1200, 800});
                            return;
                        }

                        if (jsonUsername.equals(username) && !jsonPassword.equals(password)) {
                            errorText.setText("The password was incorrect");
                        } else if (!jsonUsername.equals(username) && !jsonPassword.equals(password)) {
                            errorText.setText("The username & password was incorrect");
                        }
                    }
                } catch (JSONException | IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    private void onSignUp() {
        signupBtn.setOnAction(actionEvent -> {
            try {
                ChangeScene.changeScreen(getClass(), "signup.fxml", actionEvent,
                        new int[]{500, 300}, new int[]{640, 400});
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });
    }
}
