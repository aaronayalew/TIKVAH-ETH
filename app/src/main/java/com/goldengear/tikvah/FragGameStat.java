package com.goldengear.tikvah;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Struct;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragGameStat extends Fragment {


    public FragGameStat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.frag_game_stat, container, false);
        Bundle extras = getArguments();
        Game g = (Game) extras.getSerializable("Game");
        TextView stat,score,hname,aname,hgoals,agoals,apos,hpos,ashots,hshots,hshotsot,ashotsot,hcorners,acorners,hoffsides,aoffsides,hfouls,afouls,hyellow,ayellow,hgoalkicks,agoalkicks,htreats,atreats;
        stat = (TextView) result.findViewById(R.id.txtMatchStat);
        stat.setText(g.getStatus());
        score = (TextView) result.findViewById(R.id.txtMatchScore);
        score.setText(g.getScore());
        hname = (TextView) result.findViewById(R.id.txtmatchHName);
        String home = g.getHome_name();
        String away = g.getAway_name();
        hname.setText(home.replace("&amp;", "&"));
        aname = (TextView) result.findViewById(R.id.txtMatchAName);
        aname.setText(away.replace("&amp;", "&"));
        TextView mScore = result.findViewById(R.id.txtMatchScore);
        StringBuilder sbh = new StringBuilder();
        StringBuilder sba = new StringBuilder();
        String[] goal_times = extras.getStringArray("goal_times");
        String[] homeoraways = extras.getStringArray("homeoraways");
        String[] scorers = extras.getStringArray("scorers");
        hpos = (TextView) result.findViewById(R.id.txtHomePos);
        apos = (TextView) result.findViewById(R.id.txtAwayPos);
        hshots = (TextView) result.findViewById(R.id.txtHomeShots);
        try {


        } catch (Exception ex) {

        }

        int shotsOT;
        try {
             shotsOT = extras.getInt("home_shots on target");
        } catch(NumberFormatException ex) {
                shotsOT = 0;
        }
        int shotsOFFT;
        try {
            shotsOFFT = extras.getInt("home_shots off target");
        } catch(NumberFormatException ex) {
            shotsOFFT = 0;
        }
        hshots.setText(String.valueOf(shotsOT + shotsOFFT));
        ashots = (TextView) result.findViewById(R.id.txtAwayShots);
        int ashotsOT;
        try {
            ashotsOT = extras.getInt("away_shots on target");
        } catch (NumberFormatException ex) {
            ashotsOT = 0;
        }
        int ashotsOFFT;
        try {
            ashotsOFFT = extras.getInt("away_shots off target");
        } catch (NumberFormatException ex) {
            ashotsOFFT = 0;
        }
        /*ashots.setText(String.valueOf(ashotsOT + ashotsOFFT));*/
        hshotsot = (TextView) result.findViewById(R.id.txtHomeShotsOT);
        ashotsot = (TextView) result.findViewById(R.id.txtAwayShotsOT);
        hcorners = (TextView) result.findViewById(R.id.txtHomeCorners);
        acorners = (TextView) result.findViewById(R.id.txtAwayCorners);
        hoffsides = (TextView) result.findViewById(R.id.txtHomeOffS);
        aoffsides = (TextView) result.findViewById(R.id.txtAwayOffs);
        hfouls = (TextView) result.findViewById(R.id.txtHomeFouls);
        afouls = (TextView) result.findViewById(R.id.txtAwayFouls);
        hyellow = (TextView) result.findViewById(R.id.txtHomeYCs);
        ayellow = (TextView) result.findViewById(R.id.txtAwayYCs);
        hgoalkicks = (TextView) result.findViewById(R.id.txtHomeGKs);
        agoalkicks = (TextView) result.findViewById(R.id.txtAwayGKs);
        htreats = (TextView) result.findViewById(R.id.txtHomeTreats);
        atreats = (TextView) result.findViewById(R.id.txtAwayTreats);
        try {
            hshotsot.setText(String.valueOf(shotsOT));
            ashotsot.setText(String.valueOf(ashotsOT));
            hcorners.setText(String.valueOf(extras.getInt("home_corners")));
            acorners.setText(String.valueOf(extras.getInt("away_corners")));
            hoffsides.setText(String.valueOf(extras.getInt("home_offsides")));
            aoffsides.setText(String.valueOf(extras.getInt("away_offsides")));
            hfouls.setText(String.valueOf(extras.getInt("home_fouls")));
            afouls.setText(String.valueOf(extras.getInt("away_fouls")));
            hyellow.setText(String.valueOf(extras.getInt("home_yellow cards")));
            ayellow.setText(String.valueOf(extras.getInt("away_yellow cards")));
            hgoalkicks.setText(String.valueOf(extras.getInt("home_goal kicks")));
            agoalkicks.setText(String.valueOf(extras.getInt("away_goal kicks")));
            htreats.setText(String.valueOf(extras.getInt("home_treatments")));
            atreats.setText(String.valueOf(extras.getInt("away_treatments")));
        } catch (Exception ex) {
            hshotsot.setText("");
            ashotsot.setText("");
            hcorners.setText("");
            acorners.setText("");
            hoffsides.setText("");
            aoffsides.setText("");
            hfouls.setText("");
            afouls.setText("");
            hyellow.setText("");
            ayellow.setText("");
            hgoalkicks.setText("");
            agoalkicks.setText("");
            htreats.setText("");
            atreats.setText("");
        }
        ImageView imgHome = result.findViewById(R.id.imgHome);
        ImageView imgAway = result.findViewById(R.id.imgAway);
        DWImage downloader = new DWImage(imgHome,getContext());
        downloader.execute(new TikConst().getURL() + "img/teams/", home + ".png");
        DWImage downloader2 = new DWImage(imgAway,getContext());
        downloader2.execute(new TikConst().getURL() + "img/teams/",away + ".png" );
        return result;
    }
    private class DWImage extends AsyncTask<String,Void,Bitmap> {
        ImageView bmimage;
        Context ctx;
        public DWImage(ImageView imgv, Context ctx) {
            this.bmimage = imgv;
            this.ctx = ctx;
        }
        @Override
        protected Bitmap doInBackground(String... strings) {

            Bitmap bmp = null;
            try {
                String urldisplay = strings[0];
                String imgName =  URLEncoder.encode(strings[1],"UTF-8");
                InputStream in = new URL(urldisplay + imgName.replace("+", "%20")).openStream();
                bmp = BitmapFactory.decodeStream(in);
                Log.d("Threadgames", "BMP = " + bmp.toString());
                File file = new File(ctx.getCacheDir().getAbsolutePath() + "/" + imgName );
                FileOutputStream stream = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.JPEG,100,stream);
                stream.flush();
                stream.close();
            } catch (Exception ex) {
                Log.d("ThreadGamesError",ex.toString());
                try {
                    String urldisplay = strings[0];
                    String imgName =  URLEncoder.encode(strings[1].replace(".png"," FC.png"),"UTF-8");
                    InputStream in = new URL(urldisplay + imgName.replace("+", "%20")).openStream();
                    Log.d("Threadgames", "URL = " + urldisplay + imgName.replace("+", "%20"));
                    bmp = BitmapFactory.decodeStream(in);
                    File file = new File(ctx.getCacheDir().getAbsolutePath() + "/" + imgName);
                    if(file.exists()) {
                        Log.d("Threadgames", file.getName() + " exists!");
                    }
                    FileOutputStream stream = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.JPEG,100,stream);
                    stream.flush();
                    stream.close();
                } catch (UnsupportedEncodingException ex1) {
                    Log.d("ThreadGamesError2",ex1.toString());
                 } catch (FileNotFoundException ex2) {
                    Log.d("ThreadGamesError2",ex2.toString());
                } catch (MalformedURLException ex3) {
                    Log.d("ThreadGamesError2",ex3.toString());
                } catch (IOException ex4) {
                    Log.d("ThreadGamesError2",ex4.toString());
                } catch (Exception ex5) {
                    Log.d("ThreadGamesError2",ex5.toString());
                }

            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            bmimage.setImageBitmap(bitmap);
        }
    }
    private class EventGetter extends AsyncTask<String,Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            try{
                String url = strings[0];
                URLConnection conn = new URL(url).openConnection();
                conn.setConnectTimeout(500000);
                conn.setReadTimeout(1800000);
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while((line = reader.readLine()) != null){
                    sb.append(line);
                }
                result = sb.toString();
            } catch (Exception ex) {

            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject object = new JSONObject(s);

            } catch (Exception ex) {

            }
        }
    }

}
