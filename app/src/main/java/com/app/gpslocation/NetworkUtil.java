package com.app.gpslocation;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import com.app.extras.ConstantData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

/*
 * Created by Yash on 4/2/18.
 */

public class NetworkUtil implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
{
    //  Use of the Location API to retrieve the last known location for a device.

    private AppCompatActivity activity;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;//Stores parameters for requests to the FusedLocationProviderApi.
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private NetworkNotifier informer;

    //Represents a geographical location.
    private Location mCurrentLocation;
    private LocationCallback mLocationCallback;

    //Provides the entry point to the Fused Location Provider API.
    private FusedLocationProviderClient mFusedLocationClient;

    public NetworkUtil(AppCompatActivity activity, NetworkNotifier informer)
    {
        this.activity = activity;
        this.informer = informer;

        buildGoogleApiClient();
    }

    private void buildGoogleApiClient()
    {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);

        googleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);

        /*
         * Stores the types of location services the client is interested in using. Used for checking
         * settings to determine if the device has optimal location settings.
         */

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                .checkLocationSettings(googleApiClient, builder.build());

        /*
         * Return the current state of the Gps needed to access the Location
         */
        result.setResultCallback(new ResultCallback<LocationSettingsResult>()
        {
            @Override
            public void onResult(@NonNull LocationSettingsResult result)
            {
                final Status status = result.getStatus();

                switch (status.getStatusCode())
                {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try
                        {
                            status.startResolutionForResult(activity, ConstantData.REQUEST_LOCATION);

                        } catch (IntentSender.SendIntentException e)
                        {
                            e.printStackTrace();
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @Override // Begin by checking if the device has the necessary location settings.
    public void onConnected(Bundle bundle)
    {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {

            return;
        }

        //Callback for Location events.

        mLocationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult)
            {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();

                handleNewLocation(mCurrentLocation);
            }
        };

        if(mCurrentLocation == null)
            mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
        else
            handleNewLocation(mCurrentLocation);
    }

    @Override
    public void onConnectionSuspended(int i)
    { }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        if (connectionResult.hasResolution())
        {
            try
            {
                connectionResult.startResolutionForResult(activity, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            }
            catch (IntentSender.SendIntentException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location)
    {
        // Kick off the process of building the LocationCallback, LocationRequest, and
        // LocationSettingsRequest objects.

        if(location.toString() != null)
            handleNewLocation(location);
    }

    private void handleNewLocation(Location location)
    {
        informer.locationUpdates(location);
    }

    //Requests location updates from the FusedLocationApi. Note: we don't call this unless location runtime permission has been granted.
    public void connectGoogleApiClient()
    {
        googleApiClient.connect();
    }

    //Handles the Stop Updates button, and requests removal of location updates.
    public void disconnectGoogleApiClient()
    {
        if (googleApiClient.isConnected())
        {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            googleApiClient.disconnect();
        }
    }
}
