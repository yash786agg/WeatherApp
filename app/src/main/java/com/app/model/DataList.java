package com.app.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Yash on 4/2/18.
 */

public class DataList implements Parcelable
{
    /*
    * list: [
              {
                dt: 1485799200,
                main:
                  {
                    temp: 283.76,
                    temp_min: 283.76,
                    temp_max: 283.761,
                    pressure: 1017.24,
                    humidity: 100,
                  },
                weather:
                [
                    {
                        main: "Clear",
                        description: "clear sky",
                        icon: "01n"
                    }
                ],
                clouds:
                {
                    all: 0
                },
                wind:
                {
                    speed: 7.27,
                    deg: 15.0048
                },
                sys:
                {
                    pod: "n"
                },
                dt_txt: "2017-01-30 18:00:00"
               }
            ]
        */

    @SerializedName("main")
    private Main main;

    @SerializedName("weather")
    private List<Weather> weather;

    @SerializedName("clouds")
    private Clouds clouds;

    @SerializedName("wind")
    private Wind wind;

    @SerializedName("dt_txt")
    private String dateTxt;

    public String getDateTxt() {
        return dateTxt;
    }

    public Main getMain() {
        return main;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public Wind getWind() {
        return wind;
    }

    private DataList()
    {
        weather = new ArrayList<>();
    }

    @Override
    public String toString()
    {
        return "DataList{" +
                "main='" + main + '\'' +
                "weather='" + weather + '\'' +
                "clouds='" + clouds + '\'' +
                "dateTxt='" + dateTxt + '\'' +
                ", wind='" + wind + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeParcelable(main,flags);
        dest.writeTypedList(weather);
        dest.writeParcelable(clouds,flags);
        dest.writeParcelable(wind,flags);
        dest.writeString(dateTxt);
    }

    private DataList(Parcel in)
    {
        main = in.readParcelable(Main.class.getClassLoader());
        in.readTypedList(weather,Weather.CREATOR);
        clouds = in.readParcelable(Clouds.class.getClassLoader());
        wind = in.readParcelable(Wind.class.getClassLoader());
        dateTxt = in.readString();
    }

    public static final Parcelable.Creator<DataList> CREATOR = new Parcelable.Creator<DataList>() {
        @Override
        public DataList createFromParcel(Parcel in) {
            return new DataList(in);
        }

        @Override
        public DataList[] newArray(int size) {
            return new DataList[size];
        }
    };

}
