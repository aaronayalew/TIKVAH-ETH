package com.goldengear.tikvah;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.common.server.converter.StringToIntConverter;

/**
 * Created by Aaron Ayalew on 12/16/2018.
 */
public class StandingsAdapter extends ArrayAdapter {
    int[] Poss;
    String[] names;
    int[] Pts;
    int[] Ps;
    int[] Ws;
    int[] Ds;
    int[] Ls;
    int[] GFs;
    int[] GAs;
    Activity app;
    StandingsAdapter(Context ctx, int[] Poss, String[] names, int[] Pts, int[] Ps, int[] Ws, int[] Ds, int[] Ls, int[] GFs, int[] GAs, Activity app) {
        super(ctx,R.layout.standings_row,R.id.txtTmName,names);
        this.app = app;
        this.Poss = Poss;
        this.names = names;
        this.Pts = Pts;
        this.Ps = Ps;
        this.Ws = Ws;
        this.Ds = Ds;
        this.Ls = Ls;
        this.GFs = GFs;
        this.GAs = GAs;



    }
    static class  ViewHolder {
        TextView pos, name, pts, p, w, l, d, gf, ga;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = app.getLayoutInflater();
        if(position == 0) {
            Log.d("Standings", "Called GetView for the first time");
            convertView = inflater.inflate(R.layout.standing_header,parent,false);
        } else {
            Log.d("Standings", "Called GetView");
            if (convertView == null || convertView.getTag() == null) {
                ViewHolder holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.standings_row, parent, false);
                holder.pos = convertView.findViewById(R.id.txtTmPos);
                holder.name = convertView.findViewById(R.id.txtTmName);
                holder.pts = convertView.findViewById(R.id.txtTmPts);
                holder.p = convertView.findViewById(R.id.txtTmPlayed);
                holder.w = convertView.findViewById(R.id.txtTmWins);
                holder.l = convertView.findViewById(R.id.txtTmLoss);
                holder.d = convertView.findViewById(R.id.txtTmDraws);
                holder.gf = convertView.findViewById(R.id.txtTmGF);
                holder.ga = convertView.findViewById(R.id.txtTmGA);
                convertView.setTag(holder);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.pos.setText(String.valueOf(Poss[position]));
            holder.name.setText(names[position].replace("&amp;", "&"));
            holder.pts.setText(String.valueOf(Pts[position]));
            holder.p.setText(String.valueOf(Ps[position]));
            holder.w.setText(String.valueOf(Ws[position]));
            holder.l.setText(String.valueOf(Ls[position]));
            holder.d.setText(String.valueOf(Ds[position]));
            holder.gf.setText(String.valueOf(GFs[position]));
            holder.ga.setText(String.valueOf(GAs[position]));
        }
        return convertView;
    }
}
