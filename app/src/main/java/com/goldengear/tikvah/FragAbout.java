package com.goldengear.tikvah;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Aaron Ayalew on 10/18/2018.
 */

public class FragAbout extends Fragment {
    public FragAbout (){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.frag_about,container,false);
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }
}
