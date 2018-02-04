package com.app.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/*
 * Created by Yash on 4/2/18
 */

public class City implements Parcelable
{
    @SerializedName("name")
    private String name;

    @SerializedName("country")
    private String country;

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString()
    {
        return "City{" +
                "name='" + name + '\'' +
                "country='" + country + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(name);
        dest.writeString(country);
    }

    private City(Parcel in)
    {
        name = in.readString();
        country = in.readString();
    }

    public static final Parcelable.Creator<City> CREATOR = new Parcelable.Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };
}
