package sample;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final static List<String> usernames = new ArrayList<>();

    public static void addUsernameToList(String username) {
        usernames.add(username);
    }

    public static List<String> getUsernames() {
        return usernames;
    }

    public static void clearList() {
        usernames.clear();
    }
}
