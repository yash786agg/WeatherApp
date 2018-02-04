package com.app.chilindo;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import io.fabric.sdk.android.Fabric;

/*
 * Created by Yash on 4/2/18.
 */

public class MyApplication extends Application
{
    public static GoogleSignInOptions googleSignInOptions;

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Intialization of Crashlytics
        Fabric.with(this, new Crashlytics());

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    }
}
