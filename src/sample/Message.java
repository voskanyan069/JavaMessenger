package sample;

import java.util.ArrayList;
import java.util.List;

public class Message {
    static List<Message> list = new ArrayList<>();
    static List<String> dateList = new ArrayList<>();
    static List<String> authorList = new ArrayList<>();
    static List<String> textList = new ArrayList<>();
    static int afterParam;

    public Message(String date, int after, String author, String text) {}

    public static List<Message> getList() {
        return list;
    }
    
    public static void addToList(String date, int after, String author, String text) {
        Message message = new Message(date, after, author, text);
        afterParam = after;
        list.add(message);
        dateList.add(date);
        authorList.add(author);
        textList.add(text);
    }

    public static void clearList() {
        list.clear();
        dateList.clear();
        authorList.clear();
        textList.clear();
    }
    
    public static String getDate(int index) {
        return dateList.get(index);
    }

    public static String getAuthor(int index) {
        return authorList.get(index);
    }

    public static String getText(int index) {
        return textList.get(index);
    }

    public static int getAfterParam() {
        return afterParam;
    }
}
