package com.app.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/*
 * Created by Yash on 4/2/18.
 */

public class Clouds implements Parcelable
{
    /*
    * clouds:
             {
                all: 0
             }
    */

    @SerializedName("all")
    private int all;

    public int getAll() {
        return all;
    }

    @Override
    public String toString()
    {
        return "Clouds{" +
                "all='" + all + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(all);
    }

    private Clouds(Parcel in)
    {
        all = in.readInt();
    }

    public static final Parcelable.Creator<Clouds> CREATOR = new Parcelable.Creator<Clouds>() {
        @Override
        public Clouds createFromParcel(Parcel in) {
            return new Clouds(in);
        }

        @Override
        public Clouds[] newArray(int size) {
            return new Clouds[size];
        }
    };
}
