package nrw.florian.cookbook.weather;

import static nrw.florian.cookbook.APICallHelper.makeAPICall;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

import nrw.florian.cookbook.R;
import nrw.florian.cookbook.databinding.FragmentWeatherBinding;

public class WeatherAPIHelper {
    private final String apiKey;
    private final WeatherUIUpdater weatherUIUpdater;
    FragmentWeatherBinding binding;
    Fragment fragment;
    public WeatherAPIHelper(FragmentWeatherBinding binding, Fragment fragment) {
        this.binding = binding;
        this.fragment = fragment;
        this.weatherUIUpdater = new WeatherUIUpdater(binding, fragment);
        this.apiKey = fragment.requireContext().getString(R.string.weather_api_key);
    }

   /* public Weather getWeatherByLocationName(String locationName) {
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s", locationName, this.apiKey);
        return processJsonResult(url);
    }

    public Weather getWeatherByCoordinates(double latitude, double longitude) {
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s", latitude, longitude, this.apiKey);
        return processJsonResult(url);
    }*/

    public Weather processJsonResult(String url) {
        try {
            Optional<JSONObject> result = makeAPICall(url);
            if (result.isPresent()) {
                JSONObject json = result.get();
                Weather currentWeather = processJson(json);
                weatherUIUpdater.updateUI(currentWeather);
                return currentWeather;
            } else {
                weatherUIUpdater.updateUIWhenNoResult();
                Snackbar.make(fragment.requireContext(), fragment.requireView(), "Es ist ein Fehler bei der Datenbankabfrage aufgekommen.", Snackbar.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Weather processJson(JSONObject json) throws JSONException {
        Weather.WeatherBuilder currentWeatherBuilder = new Weather.WeatherBuilder().location(json.getString("name"));
        processMainJson(json, currentWeatherBuilder);
        processWeatherJson(json, currentWeatherBuilder);
        processWindJson(json, currentWeatherBuilder);
        return currentWeatherBuilder.build();
    }

    public void processWindJson(JSONObject json, Weather.WeatherBuilder currentWeatherBuilder) throws JSONException {
        if (json.has("wind")) {
            JSONObject wind = json.getJSONObject("wind");
            currentWeatherBuilder.wind(Integer.toString(wind.getInt("speed")));
            currentWeatherBuilder.windDirection(wind.getDouble("deg"));
        }
    }

    public void processWeatherJson(JSONObject json, Weather.WeatherBuilder currentWeatherBuilder) throws JSONException {
        if (json.has("weather")) {
            JSONArray weatherArray = json.getJSONArray("weather");
            if (weatherArray.length() > 0) {
                JSONObject weather = weatherArray.getJSONObject(0);
                currentWeatherBuilder.icon(retrieveWeatherImage(weather.getString("icon")));
            }
        }
    }

    public void processMainJson(JSONObject json, Weather.WeatherBuilder currentWeatherBuilder) throws JSONException {
        if (json.has("main")) {
            JSONObject main = json.getJSONObject("main");
            currentWeatherBuilder.temp(Integer.toString(main.getInt("temp") - 273));
            currentWeatherBuilder.feelsLike(Integer.toString(main.getInt("feels_like") - 273));
            currentWeatherBuilder.maxTemp(Integer.toString(main.getInt("temp_max") - 273));
            currentWeatherBuilder.minTemp(Integer.toString(main.getInt("temp_min") - 273));
            currentWeatherBuilder.humidity(Integer.toString(main.getInt("humidity")));
        }
    }

    public Bitmap retrieveWeatherImage(String imageName) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(String.format("https://openweathermap.org/img/w/%s.png", imageName)).openConnection();
            Bitmap bitmap = BitmapFactory.decodeStream(con.getInputStream());
            con.disconnect();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
