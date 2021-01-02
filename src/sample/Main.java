package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("Log in");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(300);
        primaryStage.setWidth(640);
        primaryStage.setHeight(400);
        primaryStage.centerOnScreen();
        primaryStage.show();

        primaryStage.setOnCloseRequest(windowEvent -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
