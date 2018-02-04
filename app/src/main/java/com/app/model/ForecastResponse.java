package com.app.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Yash on 4/2/18.
 */

public class ForecastResponse implements Parcelable
{
    /*
    * cod: "200",
    * list: []
    * city: {
                id: 1907296,
                name: "Tawarano",
                    coord:
                    {
                        lat: 35.0164,
                        lon: 139.0077
                    },
                        country: "none"
            }*/

    @SerializedName("cod")
    private String cod;

    @SerializedName("list")
    private List<DataList> dataList;

    @SerializedName("city")
    private City city;

    public City getCity() {
        return city;
    }

    public String getCod() {
        return cod;
    }

    public List<DataList> getDataList() {
        return dataList;
    }

    private ForecastResponse()
    {
        dataList = new ArrayList<>();
    }

    @Override
    public String toString()
    {
        return "ForecastResponse{" +
                "cod='" + cod + '\'' +
                "city='" + city + '\'' +
                ", dataList='" + dataList + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(cod);
        dest.writeTypedList(dataList);
        dest.writeParcelable(city,flags);
    }

    protected ForecastResponse(Parcel in)
    {
        in.readTypedList(dataList,DataList.CREATOR);
        city = in.readParcelable(City.class.getClassLoader());
        cod = in.readString();
    }

    public static final Parcelable.Creator<ForecastResponse> CREATOR = new Parcelable.Creator<ForecastResponse>() {
        @Override
        public ForecastResponse createFromParcel(Parcel in) {
            return new ForecastResponse(in);
        }

        @Override
        public ForecastResponse[] newArray(int size) {
            return new ForecastResponse[size];
        }
    };
}
