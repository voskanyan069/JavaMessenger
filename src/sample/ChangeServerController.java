package sample;

import java.io.IOException;
import java.net.Socket;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ChangeServerController {
    @FXML
    private TextField hostInput;

    @FXML
    private TextField portInput;

    @FXML
    private Button saveBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private Label errorText;

    public static String previousStage;
    public static String previousTitle;
    public static int[] minSize;
    public static int[] size;

    @FXML
    void initialize() {
        setPlaceholders();
        changeServer();
        cancel();
    }

    private void setPlaceholders() {
        hostInput.setText(Config.HOST);
        portInput.setText(String.valueOf(Config.PORT));
    }

    private void changeServer() {
        saveBtn.setOnAction(actionEvent -> {
            try {
                String host = hostInput.getText();
                int port = Integer.parseInt(portInput.getText());

                if (isServerAvailable(host, port)) {
                    Config.HOST = host;
                    Config.PORT = port;
                    Config.updateUrl();
                    System.out.println("Server available - " + host + ":" + port);
                    toMessenger(actionEvent);
                } else {
                    errorText.setText("Server not available");
                    System.out.println("Server not available - " + host + ":" + port);
                }
            } catch (NumberFormatException e) {
                errorText.setText(e.getMessage());
            }
        });
    }

    private boolean isServerAvailable(String host, int port) {
        try (Socket socket = new Socket(host, port)) {
            return true;
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        }
    }

    private void cancel() {
        cancelBtn.setOnAction(this::toMessenger);
    }

    private void toMessenger(ActionEvent actionEvent) {
        try {
            ChangeScene.changeScreen(getClass(), previousStage +  ".fxml", actionEvent,
                    previousTitle, minSize, size);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
