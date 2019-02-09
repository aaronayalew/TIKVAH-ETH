package com.goldengear.tikvah;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Aaron Ayalew on 10/18/2018.
 */

public class FragSport extends Fragment {
    TextView games;
    public FragSport (){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View result = inflater.inflate(R.layout.frag_sport,container, false);
        final ListView lv = (ListView) result.findViewById(R.id.lstSport);
        Log.d("Aaron", "Found ListView");
        Log.d("Aaron", "Initialized Adapter");
        lv.animate();
        Log.d("Aaron", "Adapter Set, Returning inflated View");
        final SwipeRefreshLayout rootView = result.findViewById(R.id.lnlSport);
        DBHelper helper = new DBHelper(getContext(),"newsDB");
        JSONObject object = helper.getNews("sport",0,20);
        new JSONHelper(getContext(),lv,rootView,getActivity()).refreshListView(object,false,"sport");
        NewsGetter ng = new NewsGetter(getActivity().getApplicationContext(),"sport",lv,rootView,rootView,getActivity());
        ng.execute();

        rootView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NewsGetter ng = new NewsGetter(getActivity().getApplicationContext(),"sport",lv,rootView,rootView,getActivity());
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
