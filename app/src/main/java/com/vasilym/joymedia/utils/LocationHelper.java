package com.vasilym.joymedia.utils;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

public class LocationHelper implements LocationListener{

    private Context context;
    private Location baselocation;
    private LocationManager locationManager;

    public LocationHelper(Context context){
        this.context = context;
        baselocation = new Location("");
        baselocation.setLatitude(55.753752);
        baselocation.setLongitude(37.622517);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,10,this);
        }

    }

    public Location getLocation() throws Exception{
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        else{
            return baselocation;
        }

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
