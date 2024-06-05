package nrw.florian.cookbook.fragment.view;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import nrw.florian.cookbook.R;
import nrw.florian.cookbook.api.weather.OpenWeatherMapAPIClient;
import nrw.florian.cookbook.api.weather.data.CurrentWeatherData;
import nrw.florian.cookbook.databinding.FragmentWeatherBinding;
import nrw.florian.cookbook.weather.WeatherUIUpdater;

public class WeatherFragment extends Fragment {
    private FragmentWeatherBinding binding;

    /**
     * Location manager
     */
    private LocationManager locationManager;

    /**
     * Location listener
     */
    private LocationListener locationListener;
    private android.location.Location currentLocation;

    /**
     * Last weather data
     */
    private CurrentWeatherData lastWeatherData;

    /**
     * Request permission launcher
     */
    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), e -> processResult());

    private WeatherUIUpdater weatherUIUpdater;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        weatherUIUpdater = new WeatherUIUpdater(binding);

        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        initLocationListeners();

        refreshWeather(currentLocation);

        binding.searchButton.setOnClickListener(

                v -> {
                    if (isFieldFilled(binding.locationInput)) {
                        new Thread(() -> refreshWeather(binding.locationInput.getText().toString())).start();
                    } else {
                        binding.locationInput.setError(getString(R.string.location_required));
                    }
                });

        binding.getCurrentLocationButton.setOnClickListener(v -> new Thread(() -> {
            refreshWeather(currentLocation);
        }).start());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume() {
        super.onResume();
        if (lastWeatherData != null)
            weatherUIUpdater.updateWeatherDetailsLayout(lastWeatherData);
        if (currentLocation != null)
            refreshWeather(currentLocation);
    }

    /**
     * Initializes the location listeners
     */
    private void initLocationListeners() {
        if (this.requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                this.requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, getWeatherLocationListener());
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }


    /**
     * Processes the result of the location permission request
     */
    private void processResult() {

        requireActivity().runOnUiThread(() -> {
            // Permission granted
            if (this.requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    this.requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), R.string.location_permitted_message, Toast.LENGTH_SHORT).show();
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, getWeatherLocationListener());
                return;
            }

            // Permission denied
            Toast.makeText(requireActivity(), getString(R.string.location_denied_message), Toast.LENGTH_SHORT).show();
            weatherUIUpdater.updateWeatherDetailsLayout(lastWeatherData);
        });
    }

    private synchronized LocationListener getWeatherLocationListener() {
        if (locationListener == null) {
            locationListener = loc -> {
                this.currentLocation = loc;
                refreshWeather(loc);
            };
        }
        return locationListener;
    }


    private boolean isFieldFilled(EditText field) {
        return !field.getText().toString().isEmpty();
    }

    private void refreshWeather(final Location loc) {
        final OpenWeatherMapAPIClient client = OpenWeatherMapAPIClient.createAPIClient(getString(R.string.weather_api_key));
        new Thread(() -> {
            try {
                lastWeatherData = client.getCurrentWeather(loc.getLatitude(), loc.getLongitude());
                requireActivity().runOnUiThread(() -> weatherUIUpdater.updateWeatherDetailsLayout(lastWeatherData));
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), getString(R.string.error), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void refreshWeather(String city) {
        final OpenWeatherMapAPIClient client = OpenWeatherMapAPIClient.createAPIClient(getString(R.string.weather_api_key));
        new Thread(() -> {
            try {
                lastWeatherData = client.getCurrentWeather(city);
                requireActivity().runOnUiThread(() -> weatherUIUpdater.updateWeatherDetailsLayout(lastWeatherData));
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), getString(R.string.error), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}