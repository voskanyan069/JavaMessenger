package sample;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.json.JSONArray;
import org.json.JSONObject;

public class MessengerController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private BorderPane mainContainer;

    @FXML
    private SplitPane subContainer;

    @FXML
    private TextArea messagesList;

    @FXML
    private ListView<String> usersList;

    @FXML
    private BorderPane inputContainer;

    @FXML
    private TextField messageInput;

    @FXML
    private Button sendMessageBtn;

    private String chatName = "Admin";

    @FXML
    void initialize() {
//        System.out.println(Crypt.crypt("Hello World"));
//        System.out.println(Crypt.decrypt("0110111 0011010 0010011 0010011 0010000 011111 0101000 0010000 0001101 0010011 0011011"));

        getMessages();
        getUsers();
        chatChangeListener();
        sendMessage();
        scrollToBottom();
    }

    private void getUsers() {
        GetRequestServer getRequestServer = new GetRequestServer(Config.URL + "/get_users");

        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray jsonUsers = getRequestServer.sendUsersGetRequest();
                    addUsersToList(jsonUsers);

                    ObservableList<String> users = FXCollections.observableArrayList();
                    users.addAll(User.getUsernames());
                    usersList.setOrientation(Orientation.VERTICAL);
                    usersList.setItems(users);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        };

        new Thread(r).start();
    }

    private void addUsersToList(JSONArray jsonUsers) {
        for (int i = 0; i < jsonUsers.length(); i++) {
            JSONObject user = jsonUsers.getJSONObject(i);
            String username = user.getString("username");
            if (!username.equals(CurrentUser.getUsername())) {
                User.addUsernameToList(username);
            }
        }
    }

    private void chatChangeListener() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                usersList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> observableValue,
                                        final String oldValue, final String newValue) {
                        System.out.println("Selected: " + newValue);
                        chatName = Config.sortString(CurrentUser.getUsername() + newValue);
                        GetRequestServer.disconnectInChatChange();
                        messagesList.setText("");
                        Message.setAfterParam(0);
                        getMessages();
                    }});
            }
        };

        new Thread(r).start();
    }

    private void sendMessage() {
        sendMessageBtn.setOnAction(actionEvent -> {
            String messageText = messageInput.getText();
                if (!messageText.equals("")) {
                    SendMessage.sendMessage(CurrentUser.getUsername(), Crypt.crypt(messageText), chatName);
                    System.out.println("Message sent to server");

                    messageInput.setText("");
                    scrollToBottom();
                }
        });
    }

    private void getMessages() {
        GetRequestServer getRequestServer = new GetRequestServer(Config.URL + "/get_messages");

        Runnable r = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        getRequestServer.sendMessagesGetRequest(chatName, Message.getAfterParam() + 1);
                        System.out.println("Request sent");
                        for (Message message : Message.getMessagesList()) {
                            messagesList.appendText("\n" + message.getDate() + " | " + message.getAuthor() + "\n" + Crypt.decrypt(message.getText()) + "\n");
                        }
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        };

        new Thread(r).start();
    }

    private void scrollToBottom() {
        double scrollPosition = messagesList.getScrollTop();

        messagesList.setScrollTop(scrollPosition);
        messagesList.setScrollLeft(0);
    }
}
