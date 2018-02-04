package com.app.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class TodayAdapter extends RecyclerView.Adapter<TodayAdapter.MyViewHolder>
{
    private ArrayList<DataList> dataList;
    private LayoutInflater layoutInflater;
    private Context context;

    public TodayAdapter(Context context, ArrayList<DataList> dataList)
    {
        // RecyclerViewAdapter Constructor to Initialize Data which we get from TodayFragment

        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // LayoutInflater is used to Inflate the view from adapter_layout for showing data in RecyclerView.

        View view = layoutInflater.inflate(R.layout.today_adapter_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TodayAdapter.MyViewHolder holder, final int position)
    {
        // onBindViewHolder is used to Set all the respective data to Textview form dataList ArrayList

        String dateFormat = dataList.get(position).getDateTxt().split(" ")[0];

        if(Utility.getCurrDateFormat().equalsIgnoreCase(dateFormat))// To show only today's date data in Recyclerview
        {
            holder.todayAdpConLyt.setVisibility(View.VISIBLE);

            // Set labels.
            if (!TextUtils.isEmpty(dataList.get(position).getDateTxt().split(" ")[1]))
            {
                String timeFormat = dataList.get(position).getDateTxt().split(" ")[1];
                holder.tempTime.setText(Utility.convertTimeFormat(timeFormat));
            }

            if (!TextUtils.isEmpty(dataList.get(position).getWeather().get(0).getIcon()))
            {
                Picasso.with(context)
                        .load(ConstantData.IMG_URL +dataList.get(position).getWeather().get(0).getIcon())
                        .resize(100,100)
                        .into(holder.tempImg);
            }

            if (dataList.get(position).getMain().getTemp() != 0.0)
            {
                String temp = Utility.convertKelvinToCelsius(dataList.get(position).getMain().getTemp())+context.getResources().getString(R.string.degree);
                holder.tempTxt.setText(temp);
            }

            if (!TextUtils.isEmpty(dataList.get(position).getWeather().get(0).getMain()))
            {
                String main = dataList.get(position).getWeather().get(0).getMain();
                holder.mainTemp.setText(main);
            }

            if (!TextUtils.isEmpty(dataList.get(position).getWeather().get(0).getDescription()))
            {
                String description = dataList.get(position).getWeather().get(0).getDescription().substring(0,1).toUpperCase()
                        + dataList.get(position).getWeather().get(0).getDescription().substring(1);
                holder.tempDespr.setText(description);
            }
        }
        else
        {
            holder.todayAdpConLyt.setVisibility(View.GONE);
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
        @BindView(R.id.tempTime) TextView tempTime;
        @BindView(R.id.tempTxt) TextView tempTxt;
        @BindView(R.id.tempImg) ImageView tempImg;
        @BindView(R.id.mainTemp) TextView mainTemp;
        @BindView(R.id.tempDespr) TextView tempDespr;
        @BindView(R.id.todayAdpConLyt) ConstraintLayout todayAdpConLyt;

        // MyViewHolder is used to Initializing the view.

        MyViewHolder(View itemView)
        {
            super(itemView);

            /* Intialization of ButterKnife to bind with itemView*/
            ButterKnife.bind(this, itemView);

            itemView.setTag(itemView);
        }
    }
}