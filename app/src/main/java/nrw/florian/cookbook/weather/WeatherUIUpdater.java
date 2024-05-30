package nrw.florian.cookbook.weather;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import nrw.florian.cookbook.databinding.FragmentWeatherBinding;

public class WeatherUIUpdater {
    private final FragmentWeatherBinding binding;
    private final Fragment fragment;

    public WeatherUIUpdater(FragmentWeatherBinding binding, Fragment fragment) {
        this.binding = binding;
        this.fragment = fragment;
    }

    public void updateWeatherDetailsLayout(Weather currentWeather) {
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

    public void updateUIWhenNoResult() {
        fragment.requireActivity().runOnUiThread(() -> binding.weatherDetailsLinearLayout.setVisibility(View.INVISIBLE));
        Snackbar.make(fragment.requireContext(), fragment.requireView(), "Es ist ein Fehler aufgetreten.", Snackbar.LENGTH_LONG).show();
    }

    public void updateUI(Weather currentWeather) {
        fragment.requireActivity().runOnUiThread(() -> updateWeatherDetailsLayout(currentWeather));
    }



    // Include any other UI related methods here...
}
