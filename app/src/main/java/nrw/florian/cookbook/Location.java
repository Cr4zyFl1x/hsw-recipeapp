package nrw.florian.cookbook;

public class Location {
    private double longitude;
    private double latitude;
    private String city;

    public Location(LocationBuilder locationBuilder) {
        this.longitude = locationBuilder.longitude;
        this.latitude = locationBuilder.latitude;
        this.city = locationBuilder.city;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getCity() {
        return city;
    }

    public static class LocationBuilder {
        private double longitude;
        private double latitude;
        private String city;

        public LocationBuilder longitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public LocationBuilder latitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public LocationBuilder city(String city) {
            this.city = city;
            return this;
        }

        public Location build() {
            return new Location(this);
        }
    }
}
