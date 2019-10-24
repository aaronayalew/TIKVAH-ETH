package com.goldengear.tikvah;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

public class LiveGameAdapter extends ArrayAdapter {
    List<Game> games;
    Activity app;
    LiveGameAdapter(@NonNull Context context, List<Game> games, String[] homenames, Activity app) {
        super(context, R.layout.game, R.id.txtHomeName,homenames);
        this.games = games;
        this.app = app;
    }
    static class  ViewHolder {
        TextView time, stat, hname, aname, hscore, ascore;
        ImageView hEmblem, aEmblem;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = app.getLayoutInflater();
        if (convertView == null || convertView.getTag() == null) {
            GamesAdapter.ViewHolder holder = new GamesAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.game, parent, false);
            holder.time = convertView.findViewById(R.id.txtMatchTime);
            holder.hname = convertView.findViewById(R.id.txtHomeName);
            holder.aname = convertView.findViewById(R.id.txtAwayName);
            holder.hscore = convertView.findViewById(R.id.txtMatchHomeScore);
            holder.ascore = convertView.findViewById(R.id.txtAwayScore);
            holder.hEmblem = convertView.findViewById(R.id.ivHome);
            holder.aEmblem = convertView.findViewById(R.id.ivAway);
            convertView.setTag(holder);
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        Game game = games.get(position);
        holder.time.setText(game.getTime());
        holder.hname.setText(game.getHome_name().replace("&amp;", "&"));
        holder.aname.setText(game.getAway_name().replace("&amp;", "&"));
        holder.hscore.setText(game.getScore().charAt(0));
        holder.ascore.setText(game.getScore().charAt(4));
        final String hImage = game.getHome_name().replace("&amp;", "&") + ".png";
        final String aImage = game.getAway_name().replace("&amp;", "&") + ".png";
        TikConst tc = new TikConst();
        Picasso.with(app).setLoggingEnabled(true);
        Picasso.with(app.getApplicationContext()).load(tc.getURL() + "img/teams/" + hImage).into(holder.hEmblem);
        Picasso.with(app.getApplicationContext()).load(tc.getURL() + "img/teams/" + aImage).into(holder.aEmblem);
        //TODO: Use Picasso for all adapters.
        return convertView;
    }
}
