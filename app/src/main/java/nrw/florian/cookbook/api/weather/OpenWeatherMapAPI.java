package nrw.florian.cookbook.api.weather;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

import nrw.florian.cookbook.api.weather.data.CurrentWeatherData;


/**
 * OpenWeatherMap-API-Client
 *
 * @author Florian J. Kleine-Vorholt
 */
public class OpenWeatherMapAPI implements OpenWeatherMapAPIClient {

    /**
     * API-Key
     */
    private final String apiKey;

    /**
     * API-Endpoint-URL
     */
    private final String url;



    /**
     * Creates a new OpenWeatherMap-API-Client
     * @param endpoint API-Endpoint-URL
     * @param apiKey API-Key
     */
    OpenWeatherMapAPI(final String endpoint, final String apiKey)
    {
        this.apiKey = apiKey;
        this.url = endpoint;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public CurrentWeatherData getCurrentWeather(String cityName)
    {
        final String url = "/weather?q=" + cityName;
        return buildObjectFromJSON(doRequest(url));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public CurrentWeatherData getCurrentWeather(double latitude, double longitude)
    {
        final String url = "/weather?lat=" + latitude + "&lon=" + longitude;
        return buildObjectFromJSON(doRequest(url));
    }


    /**
     * Makes the API-Request to the OpenWeatherMap-API
     * @param url API-Endpoint-URL (without API-Key) (with api component)
     * @return JSON-Object
     */
    private JSONObject doRequest(final String url)
    {
        final String reqUrl = this.url + url + "&appid=" + this.apiKey;

        try {
            HttpURLConnection jsonConn = (HttpURLConnection) new URL(reqUrl).openConnection();
            final String response = new BufferedReader(new InputStreamReader(jsonConn.getInputStream())).lines().collect(Collectors.joining("\n"));
            JSONObject json = new JSONObject(response);
            jsonConn.disconnect();
            return json;
        } catch (IOException | JSONException e) {
            throw new RuntimeException("Error handling API-Request!", e);
        }
    }


    /**
     * Builds a new CurrentWeatherData-Object from a JSON-Object
     * @param jsonObject JSON-Object
     * @return CurrentWeatherData-Object
     */
    private CurrentWeatherData buildObjectFromJSON(final JSONObject jsonObject)
    {
        if (jsonObject == null)
            throw new IllegalArgumentException("JSON object cannot be null");

        try {
            // Create object
            CurrentWeatherData weather = new CurrentWeatherData();

            // Native values
            weather.setCity(jsonObject.getString("name"));

            // Weather values
            if (jsonObject.has("weather")) {
                JSONObject weatherJSON = jsonObject.getJSONArray("weather").getJSONObject(0);
                weather.setImage(getWeatherIcon(weatherJSON.getString("icon")));
                weather.setWeatherState(WeatherState.valueOf(weatherJSON.getString("main").toUpperCase()));
            }

            // Main values
            if (jsonObject.has("main")){
                JSONObject main = jsonObject.getJSONObject("main");
                weather.setTempC(main.getDouble("temp") - 273.15);
                weather.setTempCMin(main.getDouble("temp_min") - 273.15);
                weather.setTempCMax(main.getDouble("temp_max") - 273.15);
                weather.setHumidity(main.getDouble("humidity"));
                weather.setTempCFeelsLike(main.getDouble("feels_like") - 273.15);
                weather.setAirPressure(main.getDouble("pressure"));
            }

            // Wind values
            if (jsonObject.has("wind")){
                JSONObject wind = jsonObject.getJSONObject("wind");
                weather.setWindSpeed(wind.getDouble("speed"));
                weather.setWindDirection(wind.getDouble("deg"));
            }

            // Clouds values
            if (jsonObject.has("clouds")){
                JSONObject clouds = jsonObject.getJSONObject("clouds");
                weather.setCloudiness(clouds.getInt("all"));
            }

            return weather;
        } catch (JSONException e) {
            throw new RuntimeException("Error parsing JSON object", e);
        }
    }


    /**
     * Gets the weather icon from the OpenWeatherMap-API
     * @param weatherIconId ID of the icon
     * @return Bitmap of the icon
     */
    private Bitmap getWeatherIcon(String weatherIconId)
    {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL("https://openweathermap.org/img/wn/" + weatherIconId + ".png").openConnection();
            Bitmap bitmap = BitmapFactory.decodeStream(con.getInputStream());

            con.disconnect();
            return bitmap;
        } catch (IOException e) {
            throw new RuntimeException("Error getting Icon!", e);
        }
    }
}