package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignupController {
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
        createUser();
        onLogIn();
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
                GetRequestServer getRequestServer = new GetRequestServer(CONFIG.URL + "/get_users");
                try {
                    JSONArray users = getRequestServer.sendUsersGetRequest();

                    for (int i = 0; i < users.length(); i++) {
                        String jsonUsername = users.getJSONObject(i).getString("username");

                        if (username.equals(jsonUsername)) {
                            errorText.setText("User with this username already exist");
                            return;
                        }
                    }

                    PostRequestServer postRequestServer = new PostRequestServer(CONFIG.URL + "/add_user");
                    try {
                        postRequestServer.sendPost(new String[]{"username", "password"}, new String[]{username, password});
                        new CurrentUser(username, password);
                        ChangeScene.changeScreen(getClass(), "messenger.fxml", actionEvent,
                                new int[]{400, 600}, new int[]{1200, 800});
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    errorText.setText("");
                } catch (JSONException | IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    private void onLogIn() {
        loginBtn.setOnAction(actionEvent -> {
            try {
                ChangeScene.changeScreen(getClass(), "login.fxml", actionEvent,
                        new int[]{500, 300}, new int[]{640, 400});
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });
    }
}
