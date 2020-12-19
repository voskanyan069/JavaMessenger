package sample;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetRequestServer {
    private String url;
    private final String tempUrl;
    private int after = 0;
    private static HttpURLConnection httpMessagesClient;

    public GetRequestServer(String url) {
        this.url = url;
        this.tempUrl = url;
    }

    public JSONArray sendUsersGetRequest() throws IOException {
        HttpURLConnection httpUsersClient = (HttpURLConnection) new URL(url).openConnection();
        httpUsersClient.setRequestMethod("GET");
        httpUsersClient.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = httpUsersClient.getResponseCode();
        if (responseCode == 200) {
            try {
                String response = getResponseFromRequest(httpUsersClient);
                JSONObject jsonObject = new JSONObject(response);

                return (JSONArray) jsonObject.get("users");
            } catch (JSONException ignored) {}
        }
        return new JSONArray();
    }

    public void sendMessagesGetRequest(String chatName, int afterParam) throws IOException {
        url = tempUrl + "?after=" + afterParam + "&chat=" + chatName;
        httpMessagesClient = (HttpURLConnection) new URL(url).openConnection();
        httpMessagesClient.setRequestMethod("GET");
        httpMessagesClient.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = httpMessagesClient.getResponseCode();
        if (responseCode == 200) {
            try {
                String response = getResponseFromRequest(httpMessagesClient);

                Message.clearList();
                JSONObject jsonObject = new JSONObject(response);
                JSONArray messages = (JSONArray) jsonObject.get("messages");

                for (int i = 0; i < messages.length(); i++) {
                    JSONObject jsonElem = messages.getJSONObject(i);
                    String author = jsonElem.getString("author");
                    String message = jsonElem.getString("text");
                    int after = jsonElem.getInt("time");

                    Date date = new Date((long) after * 1000);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String fDate = simpleDateFormat.format(date);

                    Message.addToList(fDate, after, author, message);
                }

                after = messages.getJSONObject(messages.length() - 1).getInt("time");
            } catch (JSONException ignored) {}
        }
    }

    public static void disconnectInChatChange() {
        httpMessagesClient.disconnect();
    }

    private String getResponseFromRequest(HttpURLConnection httpClient) throws IOException {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            return response.toString();
        }
    }
}