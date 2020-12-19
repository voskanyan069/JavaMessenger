package sample;

import java.util.Arrays;

public class CONFIG {
    static final String URL = "http://localhost:5000";
//    static final String URL = "http://mighty-inlet-45066.herokuapp.com";

    static String sortString(String str) {
        char[] arr = str.toCharArray();
        Arrays.sort(arr);
        return String.valueOf(arr);
    }
}
