package com.goldengear.tikvah;

import android.app.ActionBar;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class LiveGameAdapter extends RecyclerView.Adapter<LiveGameAdapter.ViewHolder> {
    List<Game> games;
    Activity app;
    private View.OnClickListener onItemClickListener;
    LiveGameAdapter(List<Game> games, Activity app) {
        super();
        this.games = games;
        this.app = app;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Game game = games.get(position);
        holder.time.setText(game.getTime());
        holder.hname.setText(game.getHome_name().replace("&amp;", "&"));
        holder.aname.setText(game.getAway_name().replace("&amp;", "&"));
        holder.hscore.setText(game.getScore().substring(0,1));
        holder.ascore.setText(game.getScore().substring(4));
        DisplayMetrics displayMetrics = new DisplayMetrics();
        app.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int finWid = displayMetrics.widthPixels - 80;
        holder.frmParent.setLayoutParams(new FrameLayout.LayoutParams(finWid, ViewGroup.LayoutParams.WRAP_CONTENT));
        final String hImage = game.getHome_name().replace("&amp;", "&") + ".png";
        final String aImage = game.getAway_name().replace("&amp;", "&") + ".png";
        TikConst tc = new TikConst();
        Picasso.with(app).setLoggingEnabled(true);
        Picasso.with(app.getApplicationContext()).load(tc.getURL() + "img/teams/" + hImage).into(holder.hEmblem);
        Picasso.with(app.getApplicationContext()).load(tc.getURL() + "img/teams/" + aImage).into(holder.aEmblem);
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView time, stat, hname, aname, hscore, ascore;
        ImageView hEmblem, aEmblem;
        FrameLayout frmParent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.txtMatchTime);
            hname = itemView.findViewById(R.id.txtHomeName);
            aname = itemView.findViewById(R.id.txtAwayName);
            hscore = itemView.findViewById(R.id.txtMatchHomeScore);
            ascore = itemView.findViewById(R.id.txtAwayScore);
            hEmblem = itemView.findViewById(R.id.ivHome);
            aEmblem = itemView.findViewById(R.id.ivAway);
            frmParent = itemView.findViewById(R.id.itemParent);
            itemView.setTag(this);
            itemView.setOnClickListener(onItemClickListener);
        }
    }

    /*@Override
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
        holder.hscore.setText(game.getScore().substring(0,1));
        holder.ascore.setText(game.getScore().substring(3,4));
        final String hImage = game.getHome_name().replace("&amp;", "&") + ".png";
        final String aImage = game.getAway_name().replace("&amp;", "&") + ".png";
        TikConst tc = new TikConst();
        Picasso.with(app).setLoggingEnabled(true);
        Picasso.with(app.getApplicationContext()).load(tc.getURL() + "img/teams/" + hImage).into(holder.hEmblem);
        Picasso.with(app.getApplicationContext()).load(tc.getURL() + "img/teams/" + aImage).into(holder.aEmblem);
        //TODO: Use Picasso for all adapters.
        return convertView;
    }*/
}
