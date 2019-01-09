package com.goldengear.tikvah;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
    GamesAdapter(@NonNull Context context, int[] ids, String[] dates, String[] times, String[] statuses, String[] homenames, int[] homescores, String[] awaynames, int[] awayscores, int[] isLives,Activity app) {
        super(context, R.layout.game, R.id.txtHomeName,homenames);
        this.ids = ids;
        this.dates = dates;
        this.times = times;
        this.statuses = statuses;
        this.homenames = homenames;
        this.homescores = homescores;
        this.awaynames = awaynames;
        this.awayscores = awayscores;
        this.isLives = isLives;
        this.app = app;
    }
    static class  ViewHolder {
        TextView time, stat, hname, aname, hscore, ascore;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = app.getLayoutInflater();
        if(dates[position].contains("Today") || dates[position].contains("Yesterday") || dates[position].contains("Tomorrow")) {
            convertView = inflater.inflate(R.layout.games_header,parent,false);
            TextView daate = convertView.findViewById(R.id.txtMatchDate);
            daate.setText(dates[position]);
        } else {
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
            holder.hname.setText(homenames[position]);
            holder.aname.setText(awaynames[position]);
            holder.hscore.setText(String.valueOf(homescores[position]));
            holder.ascore.setText(String.valueOf(awayscores[position]));
        }

        return convertView;
    }
}
