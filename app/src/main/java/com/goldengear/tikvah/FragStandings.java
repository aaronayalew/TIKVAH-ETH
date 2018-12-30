package com.goldengear.tikvah;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragStandings extends Fragment {

    public FragStandings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.frag_standings,container,false);
        ListView standings = (ListView) result.findViewById(R.id.lstStandings);
        String[] test = new String[1];
        int[] test2 = new int[1];
        int Id = getArguments().getInt("league_id");
        SwipeRefreshLayout srl = (SwipeRefreshLayout) result.findViewById(R.id.srlStand);
        srl.setRefreshing(true);
        View view = result.findViewById(R.id.lnlStandings);
        StandingsGetter sg = new StandingsGetter(getContext(),standings,getActivity(),srl,view);
        sg.execute(String.valueOf(Id));
        return result;
    }

}
