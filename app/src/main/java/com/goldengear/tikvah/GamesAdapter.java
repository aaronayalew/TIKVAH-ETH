package com.goldengear.tikvah;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Aaron Ayalew on 12/30/2018.
 */

public class GamesAdapter extends ArrayAdapter {
    int[] ids;
    String[] dates;
    String[] times;
    String[] statuses;
    String[] homenames;
    int[] homescores;
    String[] awaynames;
    int[] awayscores;
    int[] isLives;
    Activity app;
    GamesAdapter(@NonNull Context context, int[] ids, String[] dates, String[] times, String[] statuses, String[] homenames,  String[] awaynames, Activity app) {
        super(context, R.layout.game, R.id.txtHomeName,homenames);
        this.ids = ids;
        this.dates = dates;
        this.times = times;
        this.statuses = statuses;
        this.homenames = homenames;
        this.awaynames = awaynames;
//      this.isLives = isLives;
//      this.details = details;
        this.app = app;
    }
    static class  ViewHolder {
        TextView time, stat, hname, aname, hscore, ascore;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = app.getLayoutInflater();
        if (convertView == null || convertView.getTag() == null) {
            ViewHolder holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.game, parent, false);
            holder.time = convertView.findViewById(R.id.txtMatchTime);
            holder.hname = convertView.findViewById(R.id.txtHomeName);
            holder.aname = convertView.findViewById(R.id.txtAwayName);
            holder.hscore = convertView.findViewById(R.id.txtMatchHomeScore);
            holder.ascore = convertView.findViewById(R.id.txtAwayScore);
            convertView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.time.setText(times[position]);
        holder.hname.setText(homenames[position].replace("&amp;", "&"));
        holder.aname.setText(awaynames[position].replace("&amp;", "&"));
        holder.hscore.setText("-");
        holder.ascore.setText("-");


        return convertView;
    }

   /* public boolean isGame(int pos) {
        if(dates[pos].contains("Today") || dates[pos].contains("Yesterday") || dates[pos].contains("Tomorrow")) {
            return false;
        } else {
            return true;
        }
    }*/
   /* public JSONObject getDetails(int pos) {
        try {
            return details.getJSONObject(pos);
        } catch (Exception ex){
            Log.d("Threadgames", "An error occured while trying to obtaining Details from json array. Error: " + ex.getMessage() );
            return null;
        }
    }*/
}

