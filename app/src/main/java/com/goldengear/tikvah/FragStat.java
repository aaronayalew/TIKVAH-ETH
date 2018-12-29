package com.goldengear.tikvah;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragStat extends Fragment {

    String[] league_names = {"English Premier League", "Liga BBVA", "Serie A", "Bundesliga", "Ligue 1", "Champions League", "Europa League", "FA Cup", "Copa Del Rey" };

    public FragStat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.frag_stat, container, false);
        final LeagueAdapter adapter = new LeagueAdapter(getContext(),getActivity(), league_names);
        ListView leagues = (ListView) result.findViewById(R.id.lstLeagues);
        leagues.setAdapter(adapter);
        leagues.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),LeagueStats.class);
                intent.putExtra("league_id", adapter.getLeagueID(position));
                startActivity(intent);
            }
        });
        return result;
    }

}
