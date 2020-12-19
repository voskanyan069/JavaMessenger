package sample;

public class SendMessage {
    public static void sendMessage(String author, String text) {
        PostRequestServer postRequestServer = new PostRequestServer("http://mighty-inlet-45066.herokuapp.com/send_message");

        try {
            postRequestServer.sendPost(new String[]{"author", "text"}, new String[]{author, text});
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
