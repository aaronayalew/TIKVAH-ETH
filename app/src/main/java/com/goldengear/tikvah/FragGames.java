package com.goldengear.tikvah;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragGames extends Fragment {


    public FragGames() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.frag_games, container, false);
        ListView lv = result.findViewById(R.id.lstGames);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),GameInfo.class);
                startActivity(intent);
            }
        });
        int Id = getArguments().getInt("league_id");
        SwipeRefreshLayout srl = result.findViewById(R.id.srlGames);
        GamesGetter gamesGetter = new GamesGetter(getContext(),lv,getActivity(),srl,srl);
        gamesGetter.execute(String.valueOf(Id));
        return result;
    }

}
