package com.app.apirequest;

import com.app.model.ForecastResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/*
 * Created by Yash on 4/2/18.
 */

public interface ApiInterface
{
    @GET("forecast")// @GET is the type of request
    Call<ForecastResponse> getForcast(@Query("appid") String appId, @Query("lat") double lat, @Query("lon") double lon);
}
