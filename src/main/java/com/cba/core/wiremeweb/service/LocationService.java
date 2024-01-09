package com.cba.core.wiremeweb.service;

public interface LocationService {

    boolean isLocationOutOfRange(double givenLatitude, double givenLongitude,
                                 double targetLatitude, double targetLongitude,
                                 double radius);

    double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2);
}
