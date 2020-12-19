package sample;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public GetRequestServer(String url) {
        this.url = url;
        this.tempUrl = url;
    }

    public JSONArray sendUsersGetRequest() throws IOException {
//        HttpsURLConnection httpsClient = (HttpsURLConnection) new URL(url).openConnection();
        HttpURLConnection httpsClient = (HttpURLConnection) new URL(url).openConnection();
        httpsClient.setRequestMethod("GET");
        httpsClient.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = httpsClient.getResponseCode();
        if (responseCode == 200) {
            try {
                String response = getResponseFromRequest(httpsClient);
                JSONObject jsonObject = new JSONObject(response);

                return (JSONArray) jsonObject.get("users");
            } catch (JSONException ignored) {}
        }
        return new JSONArray();
    }

    public void sendMessagesGetRequest(int afterParam) throws IOException {
        url = tempUrl + "?after=" + (afterParam);
//        HttpsURLConnection httpClient = (HttpsURLConnection) new URL(url).openConnection();
        HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();
        httpClient.setRequestMethod("GET");
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = httpClient.getResponseCode();
        if (responseCode == 200) {
            try {
                String response = getResponseFromRequest(httpClient);

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