package com.example.tc2r.ctabustracker;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;


/**
 * Created by TC2R on 10/28/17.
 */

public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;

    boolean isGPSEnabled = false;

    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    Location mLocation;

    double latitude, longitude;

    private static final long MIN_DISTANCE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BETWEEN_UPDATES = 1000 * 60 * 1;
    protected LocationManager mLocationManager;

    public GPSTracker(Context context){
        this.mContext = context;
        getLocation();
    }

    public Location getLocation(){

        try{
            mLocationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(isGPSEnabled && !isNetworkEnabled){

            }else{
                this.canGetLocation = true;
                if(isNetworkEnabled){
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                            (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                        return null;
                    }
                    //mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BETWEEN_UPDATES, MIN_DISTANCE_FOR_UPDATES);
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BETWEEN_UPDATES, MIN_DISTANCE_FOR_UPDATES, this);
                    if(mLocationManager != null){
                        mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if(mLocation != null){
                            latitude = mLocation.getLatitude();
                            longitude = mLocation.getLongitude();
                        }
                    }
                }
                if(isGPSEnabled){
                    if(mLocation == null){
                        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BETWEEN_UPDATES, MIN_DISTANCE_FOR_UPDATES, this);

                        if(mLocationManager != null){
                            mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if(mLocation != null){
                                latitude = mLocation.getLatitude();
                                longitude = mLocation.getLongitude();
                            }
                        }
                    }
                }

            }
        }catch (Exception e){

            e.printStackTrace();
        }
        return mLocation;
    }

    public void stopUsingGPS(){

        if(mLocationManager != null){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                return;
            }
            mLocationManager.removeUpdates(GPSTracker.this);
        }
    }

    public double getLatitude(){

        if(mLocation != null){
            latitude = mLocation.getLatitude();
        }
        return latitude;
    }

    public double getLongitude(){

        if(mLocation != null){
            longitude = mLocation.getLongitude();
        }
        return longitude;
    }

    public boolean canGetLocation(){
        return this.canGetLocation;
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        //Setting Dialog Title
        alertDialog.setTitle("GPS Settings");

        //Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        //On pressing Settings Button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // On pressing Cancel Button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        //Showing Alert Message
        alertDialog.show();

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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
