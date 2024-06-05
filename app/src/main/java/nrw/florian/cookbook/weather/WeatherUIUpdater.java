package nrw.florian.cookbook.weather;

import android.view.View;

import nrw.florian.cookbook.api.weather.data.CurrentWeatherData;
import nrw.florian.cookbook.databinding.FragmentWeatherBinding;

public class WeatherUIUpdater {
    private final FragmentWeatherBinding binding;

    public WeatherUIUpdater(FragmentWeatherBinding binding) {
        this.binding = binding;
    }

    public void updateWeatherDetailsLayout(CurrentWeatherData currentWeather) {
        if (currentWeather == null) {
            binding.weatherDetailsLinearLayout.setVisibility(View.INVISIBLE);
            binding.getCurrentLocationButton.setVisibility(View.INVISIBLE);
            return;
        }
        binding.weatherLinearLayout.setVisibility(View.VISIBLE);
        binding.weatherDetailsLinearLayout.setVisibility(View.VISIBLE);
        binding.locationText.setText(currentWeather.getCity());
        binding.weatherIcon.setImageBitmap(currentWeather.getImage());
        binding.temperature.setText(String.format("%s°C", formatTemp(currentWeather.getTempC())));
        binding.weatherIcon.setImageBitmap(currentWeather.getImage());
        binding.feelsLike.setText(String.format("Gefühlt %s°C", formatTemp(currentWeather.getTempCFeelsLike())));
        binding.minTemp.setText(formatTemp(currentWeather.getTempCMin()));
        binding.maxTemp.setText(formatTemp(currentWeather.getTempCMax()));
        binding.windSpeed.setText(String.format("%s m/s ", currentWeather.getWindSpeed()));
        binding.windDirection.setText(processWindDegree(currentWeather.getWindDirection()));
        binding.humidityText.setText(String.format("%s %%", currentWeather.getHumidity()));
    }

    private String processWindDegree(double windDirection) {
        if (0 > windDirection || 360 < windDirection) {
            throw new IllegalArgumentException("The degree must be between 0 and 360!");
        } else if (337 <= windDirection || windDirection < 22.5) {
            return "N";
        } else if (22.5 <= windDirection && windDirection < 67.5) {
            return "NE";
        } else if (67.5 <= windDirection && windDirection < 112.5) {
            return "E";
        } else if (112.5 <= windDirection && windDirection < 157.5) {
            return "SE";
        } else if (157.5 <= windDirection && windDirection < 202.5) {
            return "S";
        } else if (202.5 <= windDirection && windDirection < 247.5) {
            return "SW";
        } else if (247.5 <= windDirection && windDirection < 292.5) {
            return "W";
        } else if (292.5 <= windDirection && windDirection < 337.5) {
            return "NW";
        }
        return "";
    }

    private String formatTemp(Double temp) {
        return String.format("%.0f", temp);
    }
}
