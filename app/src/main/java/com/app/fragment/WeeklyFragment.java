package com.app.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.app.adapter.WeeklyAdapter;
import com.app.chilindo.MainActivity;
import com.app.chilindo.R;
import com.app.model.DataList;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by Yash on 4/2/18.
 */

public class WeeklyFragment extends Fragment
{
    Activity mactivity;
    MainActivity main_activity;
    private ArrayList<DataList> dataList;
    @BindView(R.id.weeklyRcylv) RecyclerView weeklyRcylv;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mactivity = getActivity();

        main_activity = (MainActivity) mactivity;

        // Getting the dataList from Main Activity
        if(main_activity.getForecastData() != null)
            dataList = main_activity.getForecastData();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.weekly_frag_layout, container, false);

         /* Intialization of ButterKnife */
        ButterKnife.bind(this,v);

         /* Intialization of WeeklyAdapter */
        WeeklyAdapter weeklyAdapter = new WeeklyAdapter(main_activity, dataList);

        weeklyRcylv.setLayoutManager(new LinearLayoutManager(main_activity));

        weeklyRcylv.setAdapter(weeklyAdapter);

        return v;
    }


}
