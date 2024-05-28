package nrw.florian.cookbook;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class APICallHelper {
    public static Optional<JSONObject> makeAPICall(String url) {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                return Optional.empty();
            }

            String response = new BufferedReader(new InputStreamReader(connection.getInputStream())).lines().collect(Collectors.joining("\n"));
            JSONObject json = new JSONObject(response);
            connection.disconnect();
            return Optional.of(json);
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
