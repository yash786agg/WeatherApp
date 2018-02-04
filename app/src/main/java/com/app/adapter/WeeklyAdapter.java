package com.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.app.chilindo.R;
import com.app.extras.ConstantData;
import com.app.extras.Utility;
import com.app.model.DataList;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by Yash on 4/2/18.
 */

public class WeeklyAdapter extends RecyclerView.Adapter<WeeklyAdapter.MyViewHolder>
{
    private ArrayList<DataList> dataList;
    private LayoutInflater layoutInflater;
    private Context context;

    public WeeklyAdapter(Context context, ArrayList<DataList> dataList)
    {
        // RecyclerViewAdapter Constructor to Initialize Data which we get from WeeklyFragment

        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // LayoutInflater is used to Inflate the view from weekly_adapter_layout for showing data in RecyclerView.

        View view = layoutInflater.inflate(R.layout.weekly_adapter_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final WeeklyAdapter.MyViewHolder holder, final int position)
    {
        // onBindViewHolder is used to Set all the respective data to Textview form dataList ArrayList

        int currentTime = Integer.parseInt(Utility.getCurrTimeInto24Format().split(":")[0]);

        int timeRange = Utility.timeRange(currentTime);

        String time;

        if(timeRange == 0 || timeRange == 3 || timeRange == 6 || timeRange == 9)
        {
            time = 0 + String.valueOf(timeRange)+context.getResources().getString(R.string.timeappend);
        }
        else
        {
            time = String.valueOf(timeRange)+context.getResources().getString(R.string.timeappend);
        }

        String dateFormat = dataList.get(position).getDateTxt().split(" ")[1];

        if(time.equalsIgnoreCase(dateFormat))// Set data of all days based on current time
        {
            holder.weeklyAdpll.setVisibility(View.VISIBLE);


            if (!TextUtils.isEmpty(dataList.get(position).getDateTxt()))
            {
                String date = dataList.get(position).getDateTxt().split(" ")[0];

                holder.weeklyDayTxt.setText(Utility.getDayOfWeek(date));

                holder.weeklyDateTxt.setText(Utility.convertCurrentDateFormat(date));
            }

            if (!TextUtils.isEmpty(dataList.get(position).getWeather().get(0).getIcon()))
            {
                Picasso.with(context)
                        .load(ConstantData.IMG_URL +dataList.get(position).getWeather().get(0).getIcon())
                        .resize(80,80)
                        .into(holder.weeklyTempImg);
            }

            if (dataList.get(position).getMain().getTemp() != 0.0)
            {
                String temp = Utility.convertKelvinToCelsius(dataList.get(position).getMain().getTemp())
                        +context.getResources().getString(R.string.celsius);
                holder.weeklyTempTxt.setText(temp);
            }

            if (!TextUtils.isEmpty(dataList.get(position).getWeather().get(0).getDescription()))
            {
                String description = dataList.get(position).getWeather().get(0).getDescription().substring(0,1).toUpperCase()
                        + dataList.get(position).getWeather().get(0).getDescription().substring(1);
                holder.weeklyDescrTxt.setText(description);
            }

            if (dataList.get(position).getMain().getHumidity() != 0.0)
            {
                String humidity = ""+dataList.get(position).getMain().getHumidity()
                        +context.getResources().getString(R.string.percentage);

                holder.weeklyHumdityTxt.setText(humidity);
            }
        }
        else
        {
            holder.weeklyAdpll.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount()
    {
        return dataList.size();//getItemCount is used to get the size of respective dataList ArrayList
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;//Return the view type of the item at position for the purposes of view recycling.
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        // UI Widgets.
        @BindView(R.id.weeklyDayTxt) TextView weeklyDayTxt;
        @BindView(R.id.weeklyDateTxt) TextView weeklyDateTxt;
        @BindView(R.id.weeklyTempTxt) TextView weeklyTempTxt;
        @BindView(R.id.weeklyDescrTxt) TextView weeklyDescrTxt;
        @BindView(R.id.weeklyHumdityTxt) TextView weeklyHumdityTxt;
        @BindView(R.id.weeklyAdpll) LinearLayout weeklyAdpll;
        @BindView(R.id.weeklyTempImg) ImageView weeklyTempImg;

        // MyViewHolder is used to Initializing the view.

        MyViewHolder(View itemView)
        {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setTag(itemView);
        }
    }
}