package sample;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final String username;
    private final static List<String> usernames = new ArrayList<>();

    public User(String username) {
        this.username = username;
    }

    public static void addUsernameToList(String username) {
        usernames.add(username);
    }

    public static List<String> getUsernames() {
        return usernames;
    }

    public String getUsername() {
        return username;
    }
}
