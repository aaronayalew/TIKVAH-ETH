package com.goldengear.tikvah;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Aaron Ayalew on 10/18/2018.
 */

public class FragHome extends Fragment {
    DBHelper helper;

    public FragHome(){
        this.helper = new DBHelper(getContext(),"homeNews");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        /*Button btn = (Button) getActivity().findViewById(R.id.testbtn);*/
        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), NewsView.class));
            }
        });*/
        Log.d("Aaron", "OnCreateView for FragHome is called");
        View result = inflater.inflate(R.layout.frag_home,container,false);
        final ListView lv = (ListView) result.findViewById(R.id.lstHome);
        Log.d("Aaron", "Found ListView");
        Log.d("Aaron", "Initialized Adapter");
        Log.d("Aaron", "Adapter Set, Returning inflated View");
        final LinearLayout rootView = result.findViewById(R.id.lnlHome);
        final SwipeRefreshLayout srl = result.findViewById(R.id.srlHome);
        TextView amDate = (TextView) result.findViewById(R.id.txtAmDate);
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ");
        String tod = formatter.format(today);
        int year = Integer.valueOf(tod.substring(0,4));
        int month = Integer.valueOf(tod.substring(5,7));
        int day = Integer.valueOf(tod.substring(8,10));
        Log.d("DateConv","Today(formatted) is: " + tod);
        String amh = calendarForAaron.convertToECString(day,month,year);
        amDate.setText("ዛሬ ቀኑ：" + amh + "ነው");
        NewsGetter ng = new NewsGetter(getContext(),"general",lv,srl,srl,getActivity());
        ng.execute();
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NewsGetter ng = new NewsGetter(getContext(),"general",lv,srl,srl,getActivity());
                ng.execute();
            }
        });
        return result;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("Aaron", "Created Inflated View");

    }

}