package com.epam.coder.util;

import com.google.maps.DirectionsApi;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

public class GoogleMapsUtils {

    private static final String API_KEY = "AIzaSyDRQJwTCXH77GrHcEnpIhF-S78SqOlLdTQ";
    private static final GeoApiContext context = new GeoApiContext.Builder().apiKey(API_KEY).build();


    public static long measureDistance(LatLng departure, LatLng arrival) {
        try {
            DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(context);

            DirectionsApi.RouteRestriction routeRestriction = DirectionsApi.RouteRestriction.TOLLS;

            DistanceMatrix distanceMatrix = req.origins(departure)
                    .destinations(arrival)
                    .mode(TravelMode.DRIVING)
                    .avoid(routeRestriction)
                    .language("ru-RU")
                    .await();

            return distanceMatrix.rows[0].elements[0].distance.inMeters;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return 0;
    }
}
