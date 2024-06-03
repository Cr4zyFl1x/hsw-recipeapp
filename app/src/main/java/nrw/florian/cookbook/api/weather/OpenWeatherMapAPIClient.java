package nrw.florian.cookbook.api.weather;

import nrw.florian.cookbook.api.weather.data.CurrentWeatherData;

/**
 * @author Florian J. Kleine-Vorholt
 */
public interface OpenWeatherMapAPIClient {

    /**
     * OWM API endpoint
     */
    String API_ENDPOINT = "https://api.openweathermap.org/data/2.5";


    /**
     * Create a new OpenWeatherMapAPIClient instance.
     * @param apiKey OpenWeatherMap API key
     * @return OpenWeatherMapAPIClient instance
     */
    static OpenWeatherMapAPIClient createAPIClient(String apiKey)
    {
        return new OpenWeatherMapAPI(API_ENDPOINT, apiKey);
    }


    /**
     * Get the current weather data for a given city.
     * @param cityName City name
     * @return CurrentWeatherData instance
     */
    CurrentWeatherData getCurrentWeather(String cityName);


    /**
     * Get the current weather data for a given location.
     * @param latitude Position latitude
     * @param longitude Position longitude
     * @return CurrentWeatherData instance
     */
    CurrentWeatherData getCurrentWeather(double latitude, double longitude);
}