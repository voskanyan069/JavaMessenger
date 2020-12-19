package sample;

public class SendMessage {
    public static void sendMessage(String author, String text, String chatName) {
        PostRequestServer postRequestServer = new PostRequestServer(CONFIG.URL + "/send_message");

        try {
            postRequestServer.sendPost(new String[]{"author", "text", "chat"}, new String[]{author, text, chatName});
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
