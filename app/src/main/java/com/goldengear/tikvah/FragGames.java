package com.goldengear.tikvah;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        final View result =  inflater.inflate(R.layout.frag_games, container, false);
        final ListView lv = result.findViewById(R.id.lstGames);

        final int Id = getArguments().getInt("league_id");
        final SwipeRefreshLayout srl = result.findViewById(R.id.srlGames);
        GamesGetter gamesGetter = new GamesGetter(getContext(),lv,getActivity(),srl,srl);
        gamesGetter.execute(String.valueOf(Id));
        //TODO: add the onClick listener and start working on GameStat
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GamesGetter gamesGetter = new GamesGetter(getContext(),lv,getActivity(),srl,srl);
                gamesGetter.execute(String.valueOf(Id));
            }
        });
        return result;
    }

}
