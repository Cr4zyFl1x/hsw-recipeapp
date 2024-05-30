package nrw.florian.cookbook;

import static nrw.florian.cookbook.APICallHelper.makeAPICall;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

import nrw.florian.cookbook.databinding.FragmentWeatherBinding;
import nrw.florian.cookbook.weather.Weather;

public class WeatherFragment extends Fragment {
    private FragmentWeatherBinding binding;

    private Location currentLocation;
    private GPSTracker gpsTracker;

    private final String apiKey = BuildConfig.API_KEY;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        gpsTracker = new GPSTracker(getContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getGPSLocationFromOptional(view);
        new Thread(() -> refreshWeather(currentLocation)).start();

        binding.searchButton.setOnClickListener(

        v -> {
            if (isFieldFilled(binding.locationInput)) {
                new Thread(() -> refreshWeather(binding.locationInput.getText().toString())).start();
            } else {
                binding.locationInput.setError(getString(R.string.location_required));
            }
        });

        binding.getCurrentLocationButton.setOnClickListener(v -> new Thread(() -> {
            getGPSLocationFromOptional(view);
            refreshWeather(currentLocation);
        }).start());
    }

    private boolean isFieldFilled(EditText field) {
        return !field.getText().toString().isEmpty();
    }

    private void getGPSLocationFromOptional(View view) {
        Optional<Location> optionalLocation = gpsTracker.getGPSLocation(view);
        if (optionalLocation.isPresent()) {
            currentLocation = optionalLocation.get();
        } else {
            Snackbar.make(requireContext(), view, getString(R.string.error_getting_location), Snackbar.LENGTH_LONG).show();
        }
    }


    private void refreshWeather(String locationInput) {
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s", locationInput, this.apiKey);
        processJsonResult(url);
    }

    private void refreshWeather(Location location) {
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s", location.getLatitude(), location.getLongitude(), this.apiKey);
        processJsonResult(url);
    }

    private void processJsonResult(String url) {
        try {
            Optional<JSONObject> result = makeAPICall(url);
            if (!result.isPresent()) {
                updateUIWhenNoResult();
                Snackbar.make(requireContext(), requireView(), getString(R.string.error_weather_api), Snackbar.LENGTH_LONG).show();
                return;
            }
            JSONObject json = result.get();
            Weather currentWeather = processJson(json);
            updateUI(currentWeather);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private Weather processJson(JSONObject json) throws JSONException {
        Weather.WeatherBuilder currentWeatherBuilder = new Weather.WeatherBuilder().location(json.getString("name"));
        processMainJson(json, currentWeatherBuilder);
        processWeatherJson(json, currentWeatherBuilder);
        processWindJson(json, currentWeatherBuilder);
        return currentWeatherBuilder.build();
    }

    private void updateUIWhenNoResult() {
        requireActivity().runOnUiThread(() -> binding.weatherDetailsLinearLayout.setVisibility(View.INVISIBLE));
        Snackbar.make(requireContext(), requireView(), getString(R.string.error_weather_api), Snackbar.LENGTH_LONG).show();
    }

    private void updateUI(Weather currentWeather) {
        requireActivity().runOnUiThread(() -> updateWeatherDetailsLayout(currentWeather));
    }

    private Bitmap retrieveWeatherImage(String imageName) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(String.format("https://openweathermap.org/img/w/%s.png", imageName )).openConnection();
            Bitmap bitmap = BitmapFactory.decodeStream(con.getInputStream());
            con.disconnect();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void processWindJson(JSONObject json, Weather.WeatherBuilder currentWeatherBuilder) throws JSONException {
        if (json.has("wind")) {
            JSONObject wind = json.getJSONObject("wind");
            currentWeatherBuilder.wind(Integer.toString(wind.getInt("speed")));
            currentWeatherBuilder.windDirection(wind.getDouble("deg"));
        }
    }

    private void processWeatherJson(JSONObject json, Weather.WeatherBuilder currentWeatherBuilder) throws JSONException {
        if (json.has("weather")) {
            JSONArray weatherArray = json.getJSONArray("weather");
            if (weatherArray.length() > 0) {
                JSONObject weather = weatherArray.getJSONObject(0);
                currentWeatherBuilder.icon(retrieveWeatherImage(weather.getString("icon")));
            }
        }
    }

    private void processMainJson(JSONObject json, Weather.WeatherBuilder currentWeatherBuilder) throws JSONException {
        if (json.has("main")) {
            JSONObject main = json.getJSONObject("main");
            currentWeatherBuilder.temp(Integer.toString(main.getInt("temp") - 273));
            currentWeatherBuilder.feelsLike(Integer.toString(main.getInt("feels_like") - 273));
            currentWeatherBuilder.maxTemp(Integer.toString(main.getInt("temp_max") - 273));
            currentWeatherBuilder.minTemp(Integer.toString(main.getInt("temp_min") - 273));
            currentWeatherBuilder.humidity(Integer.toString(main.getInt("humidity")));
        }
    }

    private void updateWeatherDetailsLayout(Weather currentWeather) {
        binding.weatherDetailsLinearLayout.setVisibility(View.VISIBLE);
        binding.locationText.setText(currentWeather.getLocation());
        binding.weatherIcon.setImageBitmap(currentWeather.getIcon());
        binding.temperature.setText(String.format("%s°C", currentWeather.getTemp()));
        binding.weatherIcon.setImageBitmap(currentWeather.getIcon());
        binding.feelsLike.setText(String.format("Gefühlt %s°C", currentWeather.getFeelsLike()));
        binding.minTemp.setText(currentWeather.getMinTemp());
        binding.maxTemp.setText(currentWeather.getMaxTemp());
        binding.windSpeed.setText(String.format("%s m/s ", currentWeather.getWind()));
        binding.windDirection.setText(currentWeather.getWindDirection());
        binding.humidityText.setText(String.format("%s %%", currentWeather.getHumidity()));
    }
}
