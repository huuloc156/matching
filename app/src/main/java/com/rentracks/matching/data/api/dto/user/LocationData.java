package com.rentracks.matching.data.api.dto.user;

/**
 * Created by HuuLoc on 5/30/17.
 */

public class LocationData {
    public Double lat;
    public Double longi;
    public LocationData(Double la, Double lo){
        this.lat = la;
        this.longi = lo;
    }
}
