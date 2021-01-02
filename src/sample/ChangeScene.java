package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ChangeScene {
    public static void changeScreen(Class thisClass, String path, ActionEvent event, String title,
                                    int[] minSize, int[] size) throws IOException {
        Parent root = FXMLLoader.load(thisClass.getResource(path));
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.setTitle(title);
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinWidth(minSize[0]);
        primaryStage.setMinHeight(minSize[1]);
        primaryStage.setWidth(size[0]);
        primaryStage.setHeight(size[1]);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}
