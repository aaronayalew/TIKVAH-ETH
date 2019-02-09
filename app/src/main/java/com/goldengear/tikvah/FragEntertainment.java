package com.goldengear.tikvah;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

/**
 * Created by Aaron Ayalew on 10/18/2018.
 */

public class FragEntertainment extends Fragment {
    SwipeRefreshLayout swipeLayout;
    public FragEntertainment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result =  inflater.inflate(R.layout.frag_entertainment, container,false);
        final ListView lv = (ListView) result.findViewById(R.id.lstEnt);
        Log.d("Aaron", "Found ListView");
        Log.d("Aaron", "Initialized Adapter");
        lv.animate();
        Log.d("Aaron", "Adapter Set, Returning inflated View");
        swipeLayout = result.findViewById(R.id.lnlEnt);
        DBHelper helper = new DBHelper(getContext(),"memeDB");
        JSONObject object = helper.getMeme(0,20);
        JSONHelper jsonHelper = new JSONHelper(getContext(),lv,swipeLayout,getActivity());
        jsonHelper.refreshMemeListView(object);
        MemeGetter ng = new MemeGetter(getActivity().getApplicationContext(),"entertainment",lv,swipeLayout,swipeLayout,getActivity());
        ng.execute();
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MemeGetter ng = new MemeGetter(getActivity().getApplicationContext(),"entertainment",lv,swipeLayout,swipeLayout,getActivity());
                ng.execute();
            }
        });
        return result;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }
}