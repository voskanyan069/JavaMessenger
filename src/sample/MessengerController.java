package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;

public class MessengerController {
    @FXML
    private BorderPane mainContainer;

    @FXML
    private SplitPane subContainer;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button changeSeverBtn;

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
    private boolean sendMessagesRequest = true;
    private boolean sendUsersRequest = true;
    private Thread messagesThread;

    @FXML
    void initialize() {
        getMessages();
        getUsers();
        chatChangeListener();
        sendMessage();
        logout();
        changeServer();
        scrollToBottom();
    }

    private void getUsers() {
        GetRequestServer getRequestServer = new GetRequestServer(Config.URL + "/get_users");
        usersList.setOrientation(Orientation.VERTICAL);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                while (sendUsersRequest) {
                    try {
                        JSONArray jsonUsers = getRequestServer.sendUsersGetRequest();
                        System.out.println("Users request sent");
                        addUsersToList(jsonUsers);

                        ObservableList<String> users = FXCollections.observableArrayList();
                        users.addAll(User.getUsernames());
                        usersList.setItems(users);

                        Thread.sleep(4000);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        };

        new Thread(r).start();
    }

    private void logout() {
        logoutBtn.setOnAction(actionEvent -> {
            try {
                ChangeScene.changeScreen(getClass(), "login.fxml", actionEvent,
                        "Log in", new int[]{500, 300}, new int[]{640, 400});

//                TODO: CLEAR USERS LIST
                sendMessagesRequest = false;
                sendUsersRequest = false;
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });
    }

    private void changeServer() {
        changeSeverBtn.setOnAction(actionEvent -> {
            try {
                ChangeServerController.previousStage = "messenger";
                ChangeServerController.previousTitle = "Messenger";
                ChangeServerController.minSize = new int[]{400, 600};
                ChangeServerController.size = new int[]{1200, 800};
                ChangeScene.changeScreen(getClass(), "change_server.fxml", actionEvent,
                        "Change server", new int[]{500, 300}, new int[]{640, 400});
                sendMessagesRequest = false;
                sendUsersRequest = false;
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });
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
                usersList.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                    messagesThread.stop();
                    System.out.println("Selected: " + newValue);
                    chatName = Config.sortString(CurrentUser.getUsername() + newValue);
                    messagesList.setText("");
                    Message.setAfterParam(0);
                    getMessages();
                });
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
                while (sendMessagesRequest) {
                    try {
                        getRequestServer.sendMessagesGetRequest(chatName, Message.getAfterParam() + 1);
                        System.out.println("Messages request sent");
                        for (Message message : Message.getMessagesList()) {
                            messagesList.appendText("\n" + message.getDate() + " | " + message.getAuthor() + "\n" + Crypt.decrypt(message.getText()) + "\n");
                        }
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        };

        messagesThread = new Thread(r);
        messagesThread.start();
    }

    private void scrollToBottom() {
        double scrollPosition = messagesList.getScrollTop();

        messagesList.setScrollTop(scrollPosition);
        messagesList.setScrollLeft(0);
    }
}
