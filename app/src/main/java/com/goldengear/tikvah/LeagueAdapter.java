package com.goldengear.tikvah;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Aaron Ayalew on 12/16/2018.
 */

public class LeagueAdapter extends ArrayAdapter {
    String[] league_names = {"English Premier League", "Liga BBVA", "Serie A", "Bundesliga", "Ligue 1", "Champions League", "Europa League", "FA Cup", "Copa Del Rey" };
    String[] league_countries = {"England", "Spain", "Italy", "German", "France", "Europe", "Europe", "England", "Spain"};
    int[] league_ids = {62,109,79,117,127,1842,1843,69,115};
    int[] pics = {R.drawable.pllogo,R.drawable.lalogo,R.drawable.serielogo,R.drawable.bundeslogo,R.drawable.liglogo,R.drawable.champlogo,R.drawable.eurologo,R.drawable.falogo,R.drawable.copadelogo};
    Context ctx;
    Activity app;
    LeagueAdapter(Context context, Activity appl, String[] leagues) {
        super(context,R.layout.league,R.id.txtLgName,leagues);
        this.ctx = context;
        this.app = appl;
        Log.d("League", "Adapter Initialized");
    }
    public int getLeagueID(int position) {
        return league_ids[position];
    }
    static class ViewHolder {
        ImageView logo;
        TextView leag_name;
        TextView leag_country;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("Leagues", "Get View was Called");
        LayoutInflater inflater = app.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.league, parent, false);
            final ViewHolder holder = new ViewHolder();
            holder.logo = (ImageView) convertView.findViewById(R.id.lglogo);
            holder.leag_name = (TextView) convertView.findViewById(R.id.txtLgName);
            holder.leag_country = (TextView) convertView.findViewById(R.id.txtLgCountry);
            convertView.setTag(holder);
            Log.d("Logger", "convertView is null");
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.leag_name.setText(league_names[position]);
        holder.leag_country.setText(league_countries[position]);
        holder.logo.setImageResource(pics[position]);
        return convertView;
    }
}
