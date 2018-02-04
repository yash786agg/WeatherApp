package com.app.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;


/*
 * Created by Yash on 4/2/18.
 */


public class Weather implements Parcelable
{
    /*
    * weather:
                [
                    {
                        main: "Clear",
                        description: "clear sky",
                        icon: "01n"
                    }
                ]
     */

    @SerializedName("main")
    private String main;

    @SerializedName("description")
    private String description;

    @SerializedName("icon")
    private String icon;

    public String getMain() {
        return main;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public String toString()
    {
        return "Weather{" +
                "main='" + main + '\'' +
                "description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(main);
        dest.writeString(description);
        dest.writeString(icon);
    }

    private Weather(Parcel in)
    {
        main = in.readString();
        description = in.readString();
        icon = in.readString();
    }

    public static final Parcelable.Creator<Weather> CREATOR = new Parcelable.Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };

}
