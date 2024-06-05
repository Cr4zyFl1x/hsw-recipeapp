package nrw.florian.cookbook.api.weather.data;

import android.graphics.Bitmap;

import nrw.florian.cookbook.api.weather.WeatherState;

/**
 * POJO for current weather data
 *
 * @author Florian J. Kleine-Vorholt
 */
public class CurrentWeatherData {

    /**
     * Name of the location
     */
    private String city;

    /**
     * Temperature in degrees celsius °C
     */
    private Double tempC;

    /**
     * ImageIcon representing the current weather
     */
    private Bitmap image;

    /**
     * Temperature in degrees celsius °C (min temp)
     */
    private Double tempCMin;

    /**
     * Temperature in degrees celsius °C (max temp)
     */
    private Double tempCMax;

    /**
     * Temperature in degrees celsius °C (feeling temp)
     */
    private Double tempCFeelsLike;

    /**
     * Air pressure in hPa
     */
    private Double airPressure;

    /**
     * Humidity in percent
     */
    private Double humidity;

    /**
     * Wind speed in m/s
     */
    private Double windSpeed;

    /**
     * Wind direction in degrees (meteorological)
     */
    private Double windDirection;

    /**
     * Cloudiness in percent
     */
    private Integer cloudiness;

    /**
     * Weather state
     */
    private WeatherState weatherState;



    /**
     * Gets the temperature in degrees celsius
     * @return temperature in degrees celsius
     */
    public Double getTempC()
    {
        return tempC;
    }


    /**
     * Sets the temperature in degrees celsius
     * @param tempC temperature in degrees celsius
     */
    public void setTempC(Double tempC)
    {
        this.tempC = tempC;
    }


    /**
     * Gets the image representing the current weather situation
     * @return weather image
     */
    public Bitmap getImage()
    {
        return image;
    }


    /**
     * Sets the weather image
     * @param image weather image as bitmap
     */
    public void setImage(Bitmap image)
    {
        this.image = image;
    }


    /**
     * Gets the weather location
     * @return weather location
     */
    public String getCity()
    {
        return city;
    }


    /**
     * Sets the weather location
     * @param city weather location
     */
    public void setCity(String city)
    {
        this.city = city;
    }


    /**
     * Gets maximum temperature in degrees celsius
     * @return maximum temperature in degrees celsius
     */
    public Double getTempCMax()
    {
        return tempCMax;
    }


    /**
     * Sets the maximum temperature in degrees celsius
     * @param tempCMax maximum temperature in degrees celsius
     */
    public void setTempCMax(Double tempCMax)
    {
        this.tempCMax = tempCMax;
    }


    /**
     * Gets the minimum temperature in degrees celsius
     * @return minimum temperature in degrees celsius
     */
    public Double getTempCMin()
    {
        return tempCMin;
    }


    /**
     * Sets the minimum temperature in degrees celsius
     * @param tempCMin minimum temperature in degrees celsius
     */
    public void setTempCMin(Double tempCMin)
    {
        this.tempCMin = tempCMin;
    }


    /**
     * Gets the temperature in degrees celsius (feeling temp)
     * @return temperature in degrees celsius (feeling temp)
     */
    public Double getTempCFeelsLike()
    {
        return tempCFeelsLike;
    }


    /**
     * Sets the temperature in degrees celsius (feeling temp)
     * @param tempCFeelsLike temperature in degrees celsius (feeling temp)
     */
    public void setTempCFeelsLike(Double tempCFeelsLike)
    {
        this.tempCFeelsLike = tempCFeelsLike;
    }


    /**
     * Gets the air pressure in hPa
     * @return air pressure in hPa
     */
    public Double getAirPressure()
    {
        return airPressure;
    }


    /**
     * Sets the air pressure in hPa
     * @param airPressure air pressure in hPa
     */
    public void setAirPressure(Double airPressure)
    {
        this.airPressure = airPressure;
    }


    /**
     * Gets the humidity in percent
     * @return humidity in percent
     */
    public Double getHumidity()
    {
        return humidity;
    }


    /**
     * Sets the humidity in percent
     * @param humidity humidity in percent
     */
    public void setHumidity(Double humidity)
    {
        this.humidity = humidity;
    }


    /**
     * Gets the wind speed in m/s
     * @return wind speed in m/s
     */
    public Double getWindSpeed()
    {
        return windSpeed;
    }


    /**
     * Sets the wind speed in m/s
     * @param windSpeed wind speed in m/s
     */
    public void setWindSpeed(Double windSpeed)
    {
        this.windSpeed = windSpeed;
    }


    /**
     * Gets the wind direction in degrees (meteorological)
     * @return wind direction in degrees (meteorological)
     */
    public Double getWindDirection()
    {
        return windDirection;
    }


    /**
     * Sets the wind direction in degrees (meteorological)
     * @param windDirection wind direction in degrees (meteorological)
     */
    public void setWindDirection(Double windDirection)
    {
        this.windDirection = windDirection;
    }


    /**
     * Gets the cloudiness in percent
     * @return cloudiness in percent
     */
    public Integer getCloudiness()
    {
        return cloudiness;
    }


    /**
     * Sets the cloudiness in percent
     * @param cloudiness cloudiness in percent
     */
    public void setCloudiness(Integer cloudiness)
    {
        this.cloudiness = cloudiness;
    }


    /**
     * Sets the weather state
     * @param weatherState weather state
     */
    public void setWeatherState(WeatherState weatherState)
    {
        this.weatherState = weatherState;
    }


    /**
     * Gets the weather state
     * @return weather state
     */
    public WeatherState getWeatherState()
    {
        return weatherState;
    }
}