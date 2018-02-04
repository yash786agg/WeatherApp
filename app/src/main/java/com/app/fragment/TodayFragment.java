package com.app.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.adapter.TodayAdapter;
import com.app.chilindo.MainActivity;
import com.app.chilindo.R;
import com.app.extras.ConstantData;
import com.app.extras.Utility;
import com.app.model.City;
import com.app.model.DataList;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by Yash on 4/2/18.
 */

public class TodayFragment extends Fragment
{
    Activity mactivity;
    MainActivity main_activity;
    private ArrayList<DataList> dataList;
    private City city;
    @BindView(R.id.cityTxt) TextView cityTxt;
    @BindView(R.id.dateTxt) TextView dateTxt;
    @BindView(R.id.weatherImg) ImageView weatherImg;
    @BindView(R.id.tempTxtv) TextView tempTxtv;
    @BindView(R.id.rangeTempTxt) TextView rangeTempTxt;
    @BindView(R.id.windTxt) TextView windTxt;
    @BindView(R.id.humidityText) TextView humidityText;
    @BindView(R.id.hourlyRcylv) RecyclerView hourlyRcylv;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mactivity = getActivity();

        main_activity = (MainActivity) mactivity;

        // Getting the dataList from Main Activity
        if(main_activity.getForecastData() != null)
            dataList = main_activity.getForecastData();

        // Getting the city object from Main Activity
        if(main_activity.getCity() != null)
            city = main_activity.getCity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.today_frag_layout, container, false);

         /* Intialization of ButterKnife */
        ButterKnife.bind(this,v);

         /* Intialization of TodayAdapter */
        TodayAdapter todayAdapter = new TodayAdapter(main_activity,dataList);

        // LinearLayoutManager.HORIZONTAL to provide Horizontal Recyclerview.
        hourlyRcylv.setLayoutManager(new LinearLayoutManager(main_activity, LinearLayoutManager.HORIZONTAL, false));

        hourlyRcylv.setAdapter(todayAdapter);

        setAllData();

        return v;
    }

    // Set labels.
    private void setAllData()
    {
        // Set all the data that need to be display on Today's Fragment Layout
        if(city != null)
        {
            if (!TextUtils.isEmpty(city.getName()) && !TextUtils.isEmpty(city.getCountry()))
            {
                String cityName = city.getName()+getString(R.string.comma)+city.getCountry();
                cityTxt.setText(cityName);
            }
            else if(!TextUtils.isEmpty(city.getName()))
            {
                String cityName = city.getName();
                cityTxt.setText(cityName);
            }
            else if(!TextUtils.isEmpty(city.getCountry()))
            {
                String cityName = city.getCountry();
                cityTxt.setText(cityName);
            }
        }

        if(dataList.size()>= 1)
        {
            if (!TextUtils.isEmpty(dataList.get(0).getDateTxt()))
            {
                String date = Utility.convertCurrentDateFormat(dataList.get(0).getDateTxt().split(" ")[0]);
                dateTxt.setText(date);
            }

            if (!TextUtils.isEmpty(dataList.get(0).getWeather().get(0).getIcon()))
            {
                Picasso.with(main_activity)
                        .load(ConstantData.IMG_URL +dataList.get(0).getWeather().get(0).getIcon())
                        .resize(100,100)
                        .into(weatherImg);
            }

            if (!TextUtils.isEmpty(dataList.get(1).getWeather().get(0).getDescription()))
            {
                String temp = dataList.get(1).getWeather().get(0).getDescription().substring(0,1).toUpperCase()
                        +dataList.get(1).getWeather().get(0).getDescription().substring(1)
                        +" "+Utility.convertKelvinToCelsius(dataList.get(0).getMain().getTemp())+getString(R.string.celsius);

                tempTxtv.setText(temp);
            }

            if (dataList.get(0).getMain().getTemp_min() != 0.0)
            {
                String rangeTemp = getString(R.string.low)+" "+Utility.convertKelvinToCelsius(dataList.get(0).getMain().getTemp_min())+getString(R.string.celsius)+" "
                        +getString(R.string.sepeartor)+" "+getString(R.string.high)+" "
                        +Utility.convertKelvinToCelsius(dataList.get(0).getMain().getTemp_max())+getString(R.string.celsius);

                rangeTempTxt.setText(rangeTemp);
            }

            if (dataList.get(0).getWind().getSpeed() != 0.0)
            {
                String windSpeed = ""+dataList.get(0).getWind().getSpeed()+" "+getString(R.string.kmPerHour);

                windTxt.setText(windSpeed);
            }

            if (dataList.get(0).getWind().getSpeed() != 0.0)
            {
                String humidity = ""+dataList.get(0).getMain().getHumidity()+getString(R.string.percentage);

                humidityText.setText(humidity);
            }
        }
    }
}
