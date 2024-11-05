package com.cywalk.spring_boot.Locations;

public class LocationUtils {

    private static final double EARTH_RADIUS_METERS = 6371000.0; // used for getting the distance between objects

    /**
     * Computes the distance between two points on a sphere (the earth) using the Haverisne equation {@linkplain <a href="https://en.wikipedia.org/wiki/Haversine_formula">...</a>}
     *
     * @param location1 the first location point
     * @param location2 the second location point
     * @return the distance in meters between the points
     */
    public static double calculateDistance(Location location1, Location location2) {
        // Convert latitude and longitude from degrees to radians for caclulation
        double lat1 = Math.toRadians(location1.getLatitude());
        double lon1 = Math.toRadians(location1.getLongitude());
        double lat2 = Math.toRadians(location2.getLatitude());
        double lon2 = Math.toRadians(location2.getLongitude());

        // Differences in latitude and longitude
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        // Haversine formula
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat1) * Math.cos(lat2) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Distance in meters (without considering elevation)
        double distance = EARTH_RADIUS_METERS * c;

        double elevationDiff = location2.getElevation() - location1.getElevation();
        distance = Math.sqrt(Math.pow(distance, 2) + Math.pow(elevationDiff, 2));

        return distance; // Distance in kilometers
    }

}
