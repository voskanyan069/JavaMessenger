package sample;

import java.util.ArrayList;
import java.util.List;

public class Message {
    private static final List<Message> messagesList = new ArrayList<>();
    private static int afterParam;
    private final String text;
    private final String author;
    private final String date;

    public Message(String date, int after, String author, String text) {
        this.text = text;
        this.author = author;
        this.date = date;
    }

    public static List<Message> getMessagesList() {
        return messagesList;
    }
    
    public static void addToList(String date, int after, String author, String text) {
        Message message = new Message(date, after, author, text);
        afterParam = after;
        messagesList.add(message);
    }

    public static void clearList() {
        messagesList.clear();
    }

    public static int getAfterParam() {
        return afterParam;
    }

    public static void setAfterParam(int afterParam) {
        Message.afterParam = afterParam;
    }

    public String getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }
}
