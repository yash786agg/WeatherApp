package com.app.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/*
 * Created by Yash on 4/2/18.
 */

public class Main implements Parcelable
{
    /*
    * main:
       {
          temp: 283.76,
          temp_min: 283.76,
          temp_max: 283.761,
          pressure: 1017.24,
          humidity: 100,
       }
    */

    @SerializedName("temp")
    private float temp;

    @SerializedName("temp_min")
    private float temp_min;

    @SerializedName("temp_max")
    private float temp_max;

    @SerializedName("pressure")
    private float pressure;

    @SerializedName("humidity")
    private float humidity;

    public float getTemp() {
        return temp;
    }

    public float getTemp_min() {
        return temp_min;
    }

    public float getTemp_max() {
        return temp_max;
    }

    public float getPressure() {
        return pressure;
    }

    public float getHumidity() {
        return humidity;
    }

    @Override
    public String toString()
    {
        return "Main{" +
                "temp='" + temp + '\'' +
                "temp_min='" + temp_min + '\'' +
                "temp_max='" + temp_max + '\'' +
                "pressure='" + pressure + '\'' +
                ", humidity='" + humidity + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeFloat(temp);
        dest.writeFloat(temp_min);
        dest.writeFloat(temp_max);
        dest.writeFloat(pressure);
        dest.writeFloat(humidity);
    }

    private Main(Parcel in)
    {
        temp = in.readFloat();
        temp_min = in.readFloat();
        temp_max = in.readFloat();
        pressure = in.readFloat();
        humidity = in.readFloat();
    }

    public static final Parcelable.Creator<Main> CREATOR = new Parcelable.Creator<Main>() {
        @Override
        public Main createFromParcel(Parcel in) {
            return new Main(in);
        }

        @Override
        public Main[] newArray(int size) {
            return new Main[size];
        }
    };
}
