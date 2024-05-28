package nrw.florian.cookbook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import nrw.florian.cookbook.databinding.FragmentWeatherBinding;
import nrw.florian.cookbook.weather.Weather;

public class WeatherFragment extends Fragment {
    private FragmentWeatherBinding binding;

    private nrw.florian.cookbook.Location currentLocation;
    private GPSTracker gpsTracker;

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

        binding.searchButton.setOnClickListener(v -> new Thread(() -> {
            refreshWeather(binding.locationInput.getText().toString());
        }).start());

        binding.getCurrentLocationButton.setOnClickListener(v -> {
            new Thread(() -> {
                getGPSLocationFromOptional(view);
    //            getGPSLocation(view);
                refreshWeather(currentLocation);
            }).start();
        });
    }

    private void getGPSLocationFromOptional(View view) {
        Optional<nrw.florian.cookbook.Location> optionalLocation = gpsTracker.getGPSLocation(view);
        if (optionalLocation.isPresent()) {
            currentLocation = optionalLocation.get();
        } else {
            Snackbar.make(requireContext(), view, getString(R.string.error_getting_location), Snackbar.LENGTH_LONG).show();
        }
    }


    private void refreshWeather(String locationInput) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + locationInput + "&appid=" + getString(R.string.api_key);

        try {
            Optional<JSONObject> result = APICallHelper.makeAPICall(url);
            if (!result.isPresent()) {
                requireActivity().runOnUiThread(() -> binding.weatherDetailsLinearLayout.setVisibility(View.INVISIBLE));
                Snackbar.make(requireContext(), requireView(), getString(R.string.error_weather_api), Snackbar.LENGTH_LONG).show();
                return;
            }
            JSONObject json = result.get();

            Weather.WeatherBuilder currentWeatherBuilder = new Weather.WeatherBuilder().location(json.getString("name"));
            JSONHelper jsonHelper = new JSONHelper(json);

            if (jsonHelper.hasJsonObject("main")) {
                JSONObject main = json.getJSONObject("main");
                currentWeatherBuilder.temp(Integer.toString(main.getInt("temp") - 273));
                currentWeatherBuilder.feelsLike(Integer.toString(main.getInt("feels_like") - 273));
                currentWeatherBuilder.maxTemp(Integer.toString(main.getInt("temp_max") - 273));
                currentWeatherBuilder.minTemp(Integer.toString(main.getInt("temp_min") - 273));
                currentWeatherBuilder.humidity(Integer.toString(main.getInt("humidity")));
            }

            if (jsonHelper.hasJsonObject("weather")) {
                JSONArray weatherArray = json.getJSONArray("weather");
                if (weatherArray.length() > 0) {
                    JSONObject weather = weatherArray.getJSONObject(0);
                    currentWeatherBuilder.icon(retrieveWeatherImage(weather.getString("icon")));
                }
            }

            if (jsonHelper.hasJsonObject("wind")) {
                JSONObject wind = json.getJSONObject("wind");
                currentWeatherBuilder.wind(Integer.toString(wind.getInt("speed")));
                currentWeatherBuilder.windDirection(wind.getDouble("deg"));
            }

            Weather currentWeather = currentWeatherBuilder.build();

            requireActivity().runOnUiThread(() -> {
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
            });

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private Bitmap retrieveWeatherImage(String imageName) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL("https://openweathermap.org/img/w/" + imageName + ".png").openConnection();
            Bitmap bitmap = BitmapFactory.decodeStream(con.getInputStream());
            con.disconnect();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*private void getGPSLocation(View view) {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(getContext(), view, getString(R.string.no_permission_cannot_access_location),
                            Snackbar.LENGTH_LONG)
                    .setAction(R.string.permission_edit, click -> {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", requireContext().getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    })
                    .show();
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    nrw.florian.cookbook.Location.LocationBuilder currentLocationBuilder = new nrw.florian.cookbook.Location.LocationBuilder();
                    currentLocationBuilder.latitude(location.getLatitude());
                    currentLocationBuilder.longitude(location.getLongitude());
                    currentLocation = currentLocationBuilder.build();
                    new Thread(() -> refreshWeather(currentLocation)).start();
                }
            }
        });
    }*/
    private void refreshWeather(nrw.florian.cookbook.Location location) {
//        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + locationInput + "&appid=" + getString(R.string.api_key);
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s", location.getLatitude(), location.getLongitude(), "7e77eb340f3f5e8e118a9724031a4ca4"); // TODO: api_key nicht mit hochladen

        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                requireActivity().runOnUiThread(() -> binding.weatherDetailsLinearLayout.setVisibility(View.INVISIBLE));
                return;
            }

            String response = new BufferedReader(new InputStreamReader(connection.getInputStream())).lines().collect(Collectors.joining("\n"));
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

            requireActivity().runOnUiThread(() -> {
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
            });

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
