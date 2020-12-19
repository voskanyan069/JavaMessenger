package sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class PostRequestServer {
    private final String url;

    public PostRequestServer(String url) {
        this.url = url;
    }

    public void sendPost(String[] jsonNames, String[] jsonValues) throws Exception {
        HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();

        // Add request header
        httpClient.setRequestMethod("POST");
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");
        httpClient.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        httpClient.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        httpClient.setRequestProperty("Accept", "application/json; charset=UTF-8");
        httpClient.setDoOutput(true);

        // JSON String format
        StringBuilder jsonStringBuilder = new StringBuilder();
        jsonStringBuilder.append("{\"");

        for (int i = 0; i < jsonNames.length; i++) {
            jsonStringBuilder
                    .append(jsonNames[i])
                    .append("\":\"")
                    .append(jsonValues[i])
                    .append("\",\"");
        }

        jsonStringBuilder.delete(jsonStringBuilder.length() - 2, jsonStringBuilder.length());
        jsonStringBuilder.append("}");

        String jsonStr = jsonStringBuilder.toString();
        System.out.println(jsonStr);

        // Send post request
        try(OutputStream os = httpClient.getOutputStream()) {
            byte[] input = jsonStr.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpClient.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String responseLine;

            while ((responseLine = in.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
    }
}
