package com.goldengear.tikvah;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

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
      /*  NewsGetter ng = new NewsGetter("Sport",getContext().getApplicationContext());
        ng.GetNews("Sport");

        rootView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NewsGetter ng = new NewsGetter("sport",getContext().getApplicationContext());
                ng.GetNews("sport");
            }
        });*/
        return result;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        
    }

}
