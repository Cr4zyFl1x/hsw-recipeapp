package nrw.florian.cookbook.weather;

import android.graphics.Bitmap;

import nrw.florian.cookbook.enums.WeatherTime;

public class Weather {
    private String location;
    private String temp;
    private Bitmap icon;
    private String time;
    private String humidity;
    private String minTemp;
    private String maxTemp;
    private String wind;
    private String windDirection;
    private String feelsLike;

    public Weather(WeatherBuilder weatherBuilder) {
        this.location = weatherBuilder.location;
        this.temp = weatherBuilder.temp;
        this.icon = weatherBuilder.icon;
        this.time = weatherBuilder.time;
        this.humidity = weatherBuilder.humidity;
        this.minTemp = weatherBuilder.minTemp;
        this.maxTemp = weatherBuilder.maxTemp;
        this.wind = weatherBuilder.wind;
        this.windDirection = weatherBuilder.windDirection;
        this.feelsLike = weatherBuilder.feelsLike;
    }

    public String getLocation() {
        return location;
    }

    public String getTemp() {
        return temp;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public String getTime() {
        return time;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public String getWind() {
        return wind;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public String getFeelsLike() {
        return feelsLike;
    }

    public static class WeatherBuilder {
        private String location;
        private String temp;
        private Bitmap icon;
        private String time;
        private String humidity;
        private String minTemp;
        private String maxTemp;
        private String wind;
        private String windDirection;
        private String feelsLike;

        public WeatherBuilder location(String location) {
            this.location = location;
            return this;
        }

        public WeatherBuilder temp(String temp) {
            this.temp = temp;
            return this;
        }

        public WeatherBuilder icon(Bitmap icon) {
            this.icon = icon;
            return this;
        }

        public WeatherBuilder time(WeatherTime time) {
            this.time = time.getLongName();
            return this;
        }

        public WeatherBuilder humidity(String humidity) {
            this.humidity = humidity;
            return this;
        }

        public WeatherBuilder minTemp(String minTemp) {
            this.minTemp = minTemp;
            return this;
        }

        public WeatherBuilder maxTemp(String maxTemp) {
            this.maxTemp = maxTemp;
            return this;
        }

        public WeatherBuilder windDirection(double windDirection) {
            if (0 > windDirection || 360 < windDirection) {
                throw new IllegalArgumentException("The degree must be between 0 and 360!");
            } else if (337 <= windDirection || windDirection < 22.5) {
                this.windDirection = "N";
            } else if (22.5 <= windDirection && windDirection < 67.5) {
                this.windDirection = "NE";
            } else if (67.5 <= windDirection && windDirection < 112.5) {
                this.windDirection = "E";
            } else if (112.5 <= windDirection && windDirection < 157.5) {
                this.windDirection = "SE";
            } else if (157.5 <= windDirection && windDirection < 202.5) {
                this.windDirection = "S";
            } else if (202.5 <= windDirection && windDirection < 247.5) {
                this.windDirection = "SW";
            } else if (247.5 <= windDirection && windDirection < 292.5) {
                this.windDirection = "W";
            } else if (292.5 <= windDirection && windDirection < 337.5) {
                this.windDirection = "NW";
            }
            return this;
        }

        public WeatherBuilder wind(String wind) {
            this.wind = wind;
            return this;
        }

        public WeatherBuilder feelsLike(String feelsLike) {
            this.feelsLike = feelsLike;
            return this;
        }

        public Weather build() {
            return new Weather(this);
        }
    }
}
