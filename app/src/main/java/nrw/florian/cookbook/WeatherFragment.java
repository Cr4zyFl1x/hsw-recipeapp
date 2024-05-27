package nrw.florian.cookbook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import nrw.florian.cookbook.databinding.FragmentWeatherBinding;
import nrw.florian.cookbook.weather.Weather;

public class WeatherFragment extends Fragment {
    private FragmentWeatherBinding binding;

    private final String ACCESS_COARSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    private final String ACCESS_FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private final String ACCESS_BACKGROUND_LOCATION = android.Manifest.permission.ACCESS_BACKGROUND_LOCATION;

    private String currentLocation = "Muenster"; // TODO: noch anpassen -> Aktueller Standort der automatisch ermittelt wird

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        new Thread(() -> refreshWeather(currentLocation)).start();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
//        getCurrentLocation(); TODO -> ermittelt aktuelle Position und schreibt sie in die Variable currentLocation

        binding.searchButton.setOnClickListener(
                v -> new Thread(() -> {
                    refreshWeather(binding.locationInput.getText().toString());
                }).start());

        binding.getCurrentLocationButton.setOnClickListener(
                v -> new Thread(() -> {
                    refreshWeather(currentLocation);
                }).start());
    }

    private void refreshWeather(String locationInput) {
        String url =
                "https://api.openweathermap.org/data/2.5/weather?q="
                        + locationInput
                        + "&appid="
                        + getString(R.string.api_key);

        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                requireActivity().runOnUiThread(() -> binding.weatherDetailsLinearLayout.setVisibility(View.INVISIBLE));
                return;
            }

            String response =
                    new BufferedReader(new InputStreamReader(connection.getInputStream()))
                            .lines()
                            .collect(Collectors.joining("\n"));
            JSONObject json = new JSONObject(response);
            connection.disconnect();

            Weather.WeatherBuilder currentWeatherBuilder = new Weather.WeatherBuilder().location(json.getString("name"));

            if (json.has("main")) {
                JSONObject main = json.getJSONObject("main");
                currentWeatherBuilder.temp(Integer.toString(main.getInt("temp") - 273));
                currentWeatherBuilder.feelsLike(Integer.toString(main.getInt("feels_like") - 273));
                currentWeatherBuilder.maxTemp(Integer.toString(main.getInt("temp_max") - 273));
                currentWeatherBuilder.minTemp(Integer.toString(main.getInt("temp_min") - 273));
                currentWeatherBuilder.humidity(Integer.toString(main.getInt("humidity")));
            }

            if (json.has("weather")) {
                JSONArray weatherArray = json.getJSONArray("weather");
                if (weatherArray.length() > 0) {
                    JSONObject weather = weatherArray.getJSONObject(0);
                    currentWeatherBuilder.icon(retrieveWeatherImage(weather.getString("icon")));
                }
            }

            if (json.has("wind")) {
                JSONObject wind = json.getJSONObject("wind");
                currentWeatherBuilder.wind(Integer.toString(wind.getInt("speed")));
                currentWeatherBuilder.windDirection(wind.getDouble("deg"));
            }

            Weather currentWeather = currentWeatherBuilder.build();

            requireActivity()
                    .runOnUiThread(
                            () -> {
                                binding.weatherDetailsLinearLayout.setVisibility(View.VISIBLE);
                                binding.locationText.setText(currentWeather.getLocation());
                                binding.temperature.setText(String.format("%s°C", currentWeather.getTemp()));
                                binding.weatherIcon.setImageBitmap(currentWeather.getIcon());
                                binding.feelsLike.setText(String.format("Gefühlt %s°C", currentWeather.getFeelsLike()));
                                binding.minTemp.setText(currentWeather.getMinTemp());
                                binding.maxTemp.setText(currentWeather.getMaxTemp());
                                binding.windSpeed.setText(String.format("%s m/s ", currentWeather.getWind()));
                                binding.windDirection.setText(currentWeather.getWindDirection());
                                binding.humidityText.setText(String.format("%s %%", currentWeather.getHumidity()));
                            });

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private Bitmap retrieveWeatherImage(String imageName) {
        try {
            HttpURLConnection con =
                    (HttpURLConnection)
                            new URL("https://openweathermap.org/img/w/" + imageName + ".png").openConnection();
            Bitmap bitmap = BitmapFactory.decodeStream(con.getInputStream());
            con.disconnect();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void processRequestPermissionResult(Boolean granted) {
        if (granted) {
            Toast.makeText(requireActivity(), R.string.permission_granted, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireActivity(), R.string.permission_denied, Toast.LENGTH_SHORT).show();
        }
    }

}
