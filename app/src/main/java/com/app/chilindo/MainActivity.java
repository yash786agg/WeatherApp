package com.app.chilindo;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.app.extras.AlertDialogCallback;
import com.app.extras.CircleTransform;
import com.app.extras.Utility;
import com.app.fragment.TodayFragment;
import com.app.fragment.WeeklyFragment;
import com.app.model.City;
import com.app.model.DataList;
import com.app.model.ForecastResponse;
import com.app.viewmodel.ForecastViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,AlertDialogCallback<String>
{
    static
    {   // To provide support of vector icon below sdk version 21.
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    // UI Widgets.
    @BindView(R.id.navAppBar) Toolbar navAppBar;
    @BindView(R.id.toolbarTitle) TextView toolbarTitle;
    @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @BindView(R.id.navDrawRltv) RelativeLayout navDrawRltv;
    @BindView(R.id.drawerLstv) ListView drawerLstv;
    @BindView(R.id.mainActvFramelayout) FrameLayout mainActvFramelayout;
    @BindView(R.id.profilePic) ImageView profilePic;
    @BindView(R.id.userNameTxt) TextView userNameTxt;
    @BindView(R.id.emailTxt) TextView emailTxt;

    // Labels.
    private ArrayList<DataList> dataList;
    private City city;
    private GoogleSignInClient mGoogleSignInClient;
    private ProgressDialog progress;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Intialization of ProgressDialog */
        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.loading));
        progress.setCancelable(false);

        /* Intialization of ButterKnife */
        ButterKnife.bind(this);

        /*  Intialization of ArrayList */
        dataList = new ArrayList<>();

        /* Intialization of ToolBar */
        setSupportActionBar(navAppBar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        int width = getResources().getDisplayMetrics().widthPixels * 3/4;

        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navDrawRltv.getLayoutParams();
        params.width = width;
        navDrawRltv.setLayoutParams(params);

        /* Setting Up of Navigation Drawer */
        setUp(drawerLayout, navAppBar);

        String[] mDrawerTitles = getResources().getStringArray(R.array.navDrawItemArray);

        // Set the adapter for the list view
        drawerLstv.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item,R.id.navDrawerTxtv, mDrawerTitles));
        // Set the list's click listener

        // Below displayView is used to display the selected screen of Navigation Drawer
        drawerLstv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayView(position);
            }
        });

        // Build a GoogleSignInClient with the options specified by googleSignInOptions.
        mGoogleSignInClient = GoogleSignIn.getClient(this, MyApplication.googleSignInOptions);

        // Update values using data stored in the Bundle.
        Bundle bundle = getIntent().getExtras();
        emailTxt.setText(bundle.getString(getString(R.string.userName)));
        userNameTxt.setText(bundle.getString(getString(R.string.userEmail)));
        Picasso.with(this).load(bundle.getString(getString(R.string.userProfilePic))).resize(120,120)
                .transform(new CircleTransform()).into(profilePic);

        fetchData(bundle.getDouble(getString(R.string.latitude)),bundle.getDouble(getString(R.string.longitude)));
    }

    public void displayView(int position)// display the fragment acc. to selected position in listview inside navigation drawer.
    {
        Fragment fragment = null;
        switch (position)
        {
            case 0:
                toolbarTitle.setText(getString(R.string.todayForeCast));
                fragment = new TodayFragment();

                break;

            case 1:
                toolbarTitle.setText(getString(R.string.weeklyForeCast));
                fragment = new WeeklyFragment();

                break;
        }

        if(fragment != null)
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();// Fragment Transaction to perform on Fragment

            if(mainActvFramelayout.getChildCount() > 0 &&  mainActvFramelayout.getChildAt(0) != null)
            {
                mainActvFramelayout.removeAllViews();
            }
            fragmentTransaction.replace(R.id.mainActvFramelayout, fragment); // every time each fragment replace the existing one.
            fragmentTransaction.commit();
        }

        // Below closeDrawer is used to close the Navigation Drawer when click event is triggered.
        drawerLayout.closeDrawer(navDrawRltv);
    }

    private void setUp(final DrawerLayout drawerLayout , Toolbar toolbar)
    {
        // Intialization setUp for Navigation drawer
        final ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose)
        {
            @Override
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);//onDrawerOpened when Navigation drawer is completely Opened

                MainActivity.this.invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView)
            {
                super.onDrawerClosed(drawerView);//onDrawerClosed when Navigation drawer is completely Closed

                MainActivity.this.invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerToggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(this,R.color.white));

        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();//syncState is used to indicate whether DrawerLayout is open or closed
            }
        });
    }

    private void fetchData(double lat,double lon)
    {
        progress.show();
        // Observe ViewModel data
        final ForecastViewModel viewModel = ViewModelProviders.of(this).get(ForecastViewModel.class);
        viewModel.getForecast(lat, lon,this.getApplicationContext(),this).observe(this, new Observer<Response<ForecastResponse>>()
        {
            @Override
            public void onChanged(@Nullable Response<ForecastResponse> foreCastResponse)
            {
                if (foreCastResponse != null)
                {
                    // Getting all the data from ViewModel Observer
                    dataList = (ArrayList<DataList>) foreCastResponse.body().getDataList();
                    city = foreCastResponse.body().getCity();

                    if(progress != null)
                        progress.dismiss();

                    displayView(0);// Display the Today's Fragment once data is recieved from the server
                }
            }
        });
    }

    @Override
    @OnClick({R.id.SignOutTxt})
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.SignOutTxt:
                attemptingLogout();
                break;
        }
    }

    private void attemptingLogout()
    {
        Utility.showDialog(this,this.getResources().getString(R.string.logout_message),this);
    }

    //Holding dataList for the all the Fragment attached to it
    public ArrayList<DataList> getForecastData()
    {
        return dataList;
    }

    public City getCity()
    {
        return city;
    }

    @Override//Confirmation alertDialogCallback from the user that he want to logout or not.
    public void alertDialogCallback(String ret)
    {
        if(ret.equalsIgnoreCase(this.getResources().getString(R.string.Yes)))
        {
            if(mGoogleSignInClient != null)
                mGoogleSignInClient.signOut();

            Intent logout_intent = new Intent(MainActivity.this, Splash.class);
            logout_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logout_intent);
            finish();
        }
        else if(ret.equalsIgnoreCase(this.getResources().getString(R.string.fail)))
        {
            if(progress != null)
                progress.dismiss();
        }
    }

    @Override
    public void onBackPressed()
    {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
            finish();
        }
        else
            getSupportFragmentManager().popBackStack();
    }
}
