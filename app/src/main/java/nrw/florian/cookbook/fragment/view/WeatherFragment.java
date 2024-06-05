package nrw.florian.cookbook.fragment.view;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.Optional;

import nrw.florian.cookbook.GPSTracker;
import nrw.florian.cookbook.Location;
import nrw.florian.cookbook.R;
import nrw.florian.cookbook.databinding.FragmentWeatherBinding;
import nrw.florian.cookbook.weather.WeatherAPIHelper;

public class WeatherFragment extends Fragment {
    private FragmentWeatherBinding binding;

    private Location currentLocation;
    private GPSTracker gpsTracker;

    private WeatherAPIHelper weatherAPIHelper;

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
        weatherAPIHelper = new WeatherAPIHelper(binding, this);

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
            Snackbar.make(requireContext(), view, getString(R.string.error_getting_location), LENGTH_LONG).show();
        }
    }


    private void refreshWeather(String locationInput) {
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s", locationInput, getString(R.string.weather_api_key));
        weatherAPIHelper.processJsonResult(url);
    }

    private void refreshWeather(Location location) {
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s", location.getLatitude(), location.getLongitude(), getString(R.string.weather_api_key));
        weatherAPIHelper.processJsonResult(url);
    }
}