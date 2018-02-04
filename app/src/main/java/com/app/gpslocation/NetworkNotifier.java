package com.app.gpslocation;

import android.location.Location;

/*
 * Created by Yash on 4/2/18
 */

public interface NetworkNotifier
{
    void locationUpdates(Location location);// Location Listener
}
