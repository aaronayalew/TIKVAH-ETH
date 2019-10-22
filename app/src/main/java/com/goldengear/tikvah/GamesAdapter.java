package com.goldengear.tikvah;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Aaron Ayalew on 12/30/2018.
 */

public class GamesAdapter extends ArrayAdapter {
    List<Game> games;
    Activity app;
    GamesAdapter(@NonNull Context context, List<Game> games, String[] homenames, Activity app) {
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
            ViewHolder holder = new ViewHolder();
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
        holder.hscore.setText("V");
        holder.ascore.setText("S");
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

