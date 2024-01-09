package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.service.LocationService;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService {

    private static final double EARTH_RADIUS = 6371; // Earth radius in kilometers

    @Override
    public boolean isLocationOutOfRange(double givenLatitude, double givenLongitude, double targetLatitude, double targetLongitude, double radius) {
        double distance = calculateHaversineDistance(givenLatitude, givenLongitude, targetLatitude, targetLongitude);
        return distance > radius;
    }

    @Override
    public double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        // Haversine formula to calculate distance between two points on the earth
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}
