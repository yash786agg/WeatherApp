package com.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.widget.Toast;
import com.app.apirequest.ApiInterface;
import com.app.apirequest.ApiRequestSingleton;
import com.app.chilindo.R;
import com.app.extras.AlertDialogCallback;
import com.app.extras.ConstantData;
import com.app.model.ForecastResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Created by Yash on 4/2/18.
 */

public class ForecastViewModel extends ViewModel
{
    private MutableLiveData<Response<ForecastResponse>> forecastArrayList;

    public LiveData<Response<ForecastResponse>> getForecast(double lat, double lon, Context context,AlertDialogCallback<String> callback)
    {
        if (forecastArrayList == null)
        {
            //  Intialization of MutableLiveData.

            forecastArrayList = new MutableLiveData<>();

            forecastApi(lat, lon,context,callback);
        }

        return forecastArrayList;
    }

    private void forecastApi(double lat, double lon, final Context context,final AlertDialogCallback<String> callback)
    {
        // Asyncronous operation to fetch Forecast.

        ApiInterface apiService = ApiRequestSingleton.getClient().create(ApiInterface.class);

        //get call object for the request
        Call<ForecastResponse> call = apiService.getForcast(ConstantData.weatherApiKey,lat, lon);

        // execute network request
        call.enqueue(new Callback<ForecastResponse>()
        {
            @Override
            public void onResponse(Call<ForecastResponse>call, Response<ForecastResponse> response)
            {
                if(response.body() != null)
                {
                    if(response.body().getCod().equalsIgnoreCase("200"))
                    {
                        forecastArrayList.setValue(response);
                    }
                }
            }

            @Override
            public void onFailure(Call<ForecastResponse>call, Throwable t)
            {
                Toast.makeText(context, R.string.unableToConnectServer,Toast.LENGTH_LONG).show();
                callback.alertDialogCallback(context.getResources().getString(R.string.fail));
            }
        });
    }
}
