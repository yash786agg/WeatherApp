package com.app.chilindo;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.app.extras.AlertDialogCallback;
import com.app.extras.ConnectivityReceiver;
import com.app.extras.ConstantData;
import com.app.extras.Utility;
import com.app.gpslocation.NetworkNotifier;
import com.app.gpslocation.NetworkUtil;
import com.app.runtimePermission.AcessLocation;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
 * Created by Yash on 4/2/18.
 */

public class Splash extends AppCompatActivity implements NetworkNotifier,View.OnClickListener,AlertDialogCallback<String>
{
    // Application supports the latest Android Architecture Component ie:
    // Code Structure: MVVM (Model View View Controller) with Live Data and View Model are Used.

    // Labels.
    private int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private NetworkUtil networkUtilObj;//----> For Getting the Latitude and Longitude
    private boolean googleBtnClicked = false, accessGps = false;
    private ProgressDialog progress;
    private String userEmail,userName,userProfilePic;

    // UI Widgets.
    @BindView(R.id.googleSignInBtn) SignInButton googleSignInBtn;
    @BindView(R.id.loginll) LinearLayout loginll;

    // Google Labels
    GoogleSignInClient mGoogleSignInClient;
    private static int GOOGLE_SIGN_IN = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

         /* Intialization of ProgressDialog */
        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.gettingLocation));
        progress.setCancelable(false);

         /* Intialization of ButterKnife */
        ButterKnife.bind(this);
        // Build a GoogleSignInClient with the options specified by googleSignInOptions.
        mGoogleSignInClient = GoogleSignIn.getClient(this, MyApplication.googleSignInOptions);

        // To check whether device is connected or not to Wifi or Data Connected
        networkCheck();
    }
    private void userLogIn()
    {
        final GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        //Check for existing Google Sign In account, if the user is already signed in
        if(account != null)
        {
            Thread timer=new Thread()
            {
                @Override
                public void run()
                {
                    try
                    {
                        sleep(2000);
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            getGoogleData(account);
                        }
                    });
                }
            };timer.start();
        }
        else
        {
            loginll.setVisibility(View.VISIBLE);
            startAnimation();
        }
    }

    private void networkCheck()
    {
        if(ConnectivityReceiver.isNetworkAvailable(this.getApplication()))
            userLogIn();
        else
            Toast.makeText(this.getApplication(),R.string.connectWifiDataConn,Toast.LENGTH_SHORT).show();
    }

    // Asking for user to provide his location using GPS
    private void askForLatLong()
    {
        progress.show();
        // Checking of current device SDK Version
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) // If device SDK version is less than or equal to 22.
        {
            networkUtilObj = new NetworkUtil(this,this);
            networkUtilObj.connectGoogleApiClient();
        }
        else if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {// If device already allow the device to access its location
            networkUtilObj = new NetworkUtil(this,this);
            networkUtilObj.connectGoogleApiClient();
        }
        else // Requesting user to access it location.
        {
            int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 2;
            AcessLocation.getLocation(this, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        }
    }

    @Override // Update the value of location to show the correct latitude and longitude.
    public void locationUpdates(Location location)
    {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        if(networkUtilObj != null)
            networkUtilObj.disconnectGoogleApiClient();// disconnect to google Api client to listen google location

        moveToMainActivity(currentLatitude,currentLongitude);
    }

    @Override
    @OnClick({R.id.googleBtn})
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.googleBtn://Click to get the google signIn details from the user device.

                if(!googleBtnClicked && !accessGps)
                {
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
                }
                else
                {
                    accessGps = false;
                    askForLatLong();// If user already signedIn then directly ask for location.
                }
                break;
        }
    }

    private void startAnimation()// Animation to show the Google signIn button from bottom to center of the screen.
    {
        Animation slideupAnimation = AnimationUtils.loadAnimation(this,R.anim.login_slide_up_animation);
        loginll.setAnimation(slideupAnimation);
    }

    private void moveToMainActivity(double currentLatitude,double currentLongitude)// Move to next screen with all the required data.
    {
        if(progress != null)
            progress.cancel();

        googleBtnClicked = false;
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putDouble(getString(R.string.latitude), currentLatitude);
        bundle.putDouble(getString(R.string.longitude), currentLongitude);
        bundle.putString(getString(R.string.userName), userName);
        bundle.putString(getString(R.string.userEmail), userEmail);
        bundle.putString(getString(R.string.userProfilePic), userProfilePic);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void googleSignInResult(Task<GoogleSignInAccount> completedTask)// Google signIn Details
    {
        try
        {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            googleBtnClicked = true;
            // Signed in successfully, get authenticated data.
            getGoogleData(account);
        }
        catch (ApiException e)
        {
            e.printStackTrace();
            if(e.getStatusCode() == 12501)
                Toast.makeText(this.getApplicationContext(),R.string.googleSigInCancelled,Toast.LENGTH_SHORT).show();
        }
    }

    private void getGoogleData(GoogleSignInAccount account)// getting all the data from the selected Google SignIn Account by the user
    {
        if (!TextUtils.isEmpty(account.getEmail()))
            userEmail = account.getEmail();

        if ((account.getPhotoUrl() != null))
            userProfilePic = String.valueOf(account.getPhotoUrl());

        if (!TextUtils.isEmpty(account.getDisplayName()))
            userName = account.getDisplayName();
        else if (!TextUtils.isEmpty(account.getGivenName()) && !TextUtils.isEmpty(account.getFamilyName()))
            userName = account.getGivenName() + account.getFamilyName();
        else if(!TextUtils.isEmpty(account.getGivenName()))
            userName = account.getGivenName();
        else if(!TextUtils.isEmpty(account.getFamilyName()))
            userName = account.getFamilyName();

        askForLatLong();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Check for the integer request code originally supplied to startActivityForResult().
        if (requestCode == GOOGLE_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            googleSignInResult(task);
        }

        switch (requestCode)
        {
            case ConstantData.REQUEST_LOCATION:
                switch (resultCode)
                {
                    case AppCompatActivity.RESULT_OK:
                    {
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    }
                    case AppCompatActivity.RESULT_CANCELED:
                    {
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(this, R.string.locationNotEnable, Toast.LENGTH_SHORT).show();
                        if (progress != null)
                            progress.cancel();

                        if(networkUtilObj != null)
                            networkUtilObj.disconnectGoogleApiClient();

                        Utility.showDialog(this,this.getResources().getString(R.string.sureAboutGps),this);

                        accessGps = true;

                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)// Callback received when a permissions request has been completed.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case 2:
            {
                Map<String, Integer> perms = new HashMap<>();
                perms.put(android.Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // If user allow the application to access it's location.
                    networkUtilObj = new NetworkUtil(this, this);
                    networkUtilObj.connectGoogleApiClient();
                }

                //Permission denied.
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    boolean should = ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
                    // Provide an additional rationale to the user. This would happen if the user denied the
                    // request previously, but didn't check the "Don't ask again" checkbox.
                    if (should) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
                        builder.setTitle(R.string.permissionDenied);
                        builder.setMessage(R.string.accessFineLoc);
                        builder.setPositiveButton(R.string.iamSure, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // If user is sure that he don't want to allow application to open gps.
                                unableToProceed();
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton(R.string.reTry, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ActivityCompat.requestPermissions(Splash.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                            }
                        });
                        builder.show();
                    }
                }
                break;
            }
            case 1:
            {
                Map<String, Integer> perms = new HashMap<>();
                perms.put(android.Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    networkUtilObj = new NetworkUtil(this, this);
                    networkUtilObj.connectGoogleApiClient();
                }
                else {
                    // 2nd time denied
                    unableToProceed();
                }
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void unableToProceed() { // If user denied to access it's location.
        Toast.makeText(this,R.string.unableToProceed,Toast.LENGTH_LONG).show();
        finish();
    }

    @Override //Confirmation alertDialogCallback from the user that he will providing his location or not
    public void alertDialogCallback(String ret) {
        if(ret.equalsIgnoreCase(this.getResources().getString(R.string.Yes)))
            unableToProceed();
        else
            askForLatLong();
    }
}
