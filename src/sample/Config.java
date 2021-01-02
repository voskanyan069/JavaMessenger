package sample;

import java.util.Arrays;

public class Config {
    static String HOST = "localhost";
    static int PORT = 5000;
    static String URL;
//    static final String URL = "http://mighty-inlet-45066.herokuapp.com";

    static {
        updateUrl();
    }

    static String sortString(String str) {
        char[] arr = str.toCharArray();
        Arrays.sort(arr);
        return String.valueOf(arr);
    }

    static void updateUrl() {
        URL = "http://" + HOST + ":" + PORT;
    }
}
