package com.app.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/*
 * Created by Yash on 4/2/18.
 */

public class Wind implements Parcelable
{
    /*
    * wind:
       {
          speed: 7.27,
          deg: 15.0048
       }
    */

    @SerializedName("speed")
    private float speed;

    @SerializedName("deg")
    private double deg;

    public float getSpeed() {
        return speed;
    }

    public double getDeg() {
        return deg;
    }

    @Override
    public String toString()
    {
        return "Wind{" +
                "speed='" + speed + '\'' +
                ", deg='" + deg + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeFloat(speed);
        dest.writeDouble(deg);
    }

    private Wind(Parcel in)
    {
        speed = in.readFloat();
        deg = in.readDouble();
    }

    public static final Parcelable.Creator<Wind> CREATOR = new Parcelable.Creator<Wind>() {
        @Override
        public Wind createFromParcel(Parcel in) {
            return new Wind(in);
        }

        @Override
        public Wind[] newArray(int size) {
            return new Wind[size];
        }
    };
}
