package sample;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

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
    private ListView<?> usersList;

    @FXML
    private BorderPane inputContainer;

    @FXML
    private TextField messageInput;

    @FXML
    private Button sendMessageBtn;

    @FXML
    void initialize() {
        getMessages();
        sendMessage();
        scrollToBottom();
    }

    private void sendMessage() {
        sendMessageBtn.setOnAction(actionEvent -> {
            String messageText = messageInput.getText();
                if (!messageText.equals("")) {
                    SendMessage.sendMessage(CurrentUser.getUsername(), messageText);
                    System.out.println("Message sent to server");

                    messageInput.setText("");
                    scrollToBottom();
                }
        });
    }

    private void getMessages() {
        GetRequestServer getRequestServer = new GetRequestServer("http://mighty-inlet-45066.herokuapp.com/get_messages");

        Runnable r = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        getRequestServer.sendMessagesGetRequest(Message.getAfterParam() + 1);
                        System.out.println("Request sent");
                        for (int i = 0; i < Message.getList().size(); i++) {
                            messagesList.appendText("\n" + Message.getDate(i) + " | " + Message.getAuthor(i) + "\n" + Message.getText(i) + "\n");
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
