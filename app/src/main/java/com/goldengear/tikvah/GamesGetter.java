package com.goldengear.tikvah;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.fabric.sdk.android.services.concurrency.AsyncTask;

/**
 * Created by Aaron Ayalew on 12/30/2018.
 */

public class GamesGetter extends AsyncTask<String,Void,String> {
    String result, resultl;
    ListView lv;
    Activity app;
    Context ctx;
    SwipeRefreshLayout srl;
    View rootView;
    Date today;
    Boolean success;

    public GamesGetter(Context context, ListView listView, Activity app, SwipeRefreshLayout swipeRefreshLayout, View rootView) {
        super();
        this.ctx = context;
        this.lv = listView;
        this.app = app;
        this.srl = swipeRefreshLayout;
        this.rootView = rootView;
    }
    @Override
    protected void onPreExecute() {
       super.onPreExecute();
        Date date = Calendar.getInstance().getTime();
        this.today =date;
        srl.setRefreshing(true);
    }
    @Override
    protected String doInBackground(String... strings) {
        try {
            success = true;
            String tod = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            int year = Integer.valueOf(tod.substring(0,4));
            int month = Integer.valueOf(tod.substring(5,7));
            int day = Integer.valueOf(tod.substring(8,10));
            boolean lstDate = isLastDate(year,month,day);
           /* String tomorrow;
            if(!lstDate) tomorrow = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day + 1);
            else tomorrow = String.valueOf(year) + "-" + String.valueOf(month + 1) + "-1";
            String yesterday;
            if(day != 1) yesterday = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day - 1);
            else {
                if(month == 2 || month == 4 || month == 6 || month == 8 || month == 9 || month == 11 || month == 1) yesterday = String.valueOf(year) + "-" + String.valueOf(month - 1) + "-31";
                else if(month == 3 && year % 4 != 0) yesterday = String.valueOf(year) + "-" + String.valueOf(month - 1) + "-" + String.valueOf(28);
                else if(month == 3 && year % 4 == 0) yesterday = String.valueOf(year) + "-" + String.valueOf(month - 1) + "-" + String.valueOf(29);
                else yesterday = String.valueOf(year) + "-"  + String.valueOf(month - 1) + "-" + String.valueOf(30);
            }*/

            URL uri = new URL(new TikConst().getURL() + "get_prem_fix.php");
            Log.d("Threadgames", "Starting Thread");
            URLConnection conn = uri.openConnection();
            Log.d("Threadgames", "Opened Connection");
            conn.setConnectTimeout(120000);
            conn.setReadTimeout(180000);
            conn.setDoOutput(true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            Log.d("Threadgames", "Done:" + sb.toString());
            result = sb.toString();
            URL uril = new URL(new TikConst().getURL() + "/get_prem_live.php");
            Log.d("Threadlivegames", "Starting Thread");
            URLConnection connl = uril.openConnection();
            Log.d("Threadlivegames", "Opened Connection");
            connl.setConnectTimeout(120000);
            connl.setReadTimeout(180000);
            connl.setDoOutput(true);
            BufferedReader readerl = new BufferedReader(new InputStreamReader(connl.getInputStream()));
            String linel = null;
            StringBuilder sbl = new StringBuilder();
            while((linel = readerl.readLine()) != null) {
                sbl.append(linel);
            }
            resultl = sbl.toString();
            Log.d("Threadlivegames", "Result = " + resultl);
            return null;
        } catch (Exception ex){
            Log.d("Threadlivegames", "Error: " + ex.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        srl.setRefreshing(false);
        try {
            JSONObject jobject = new JSONObject(result).getJSONObject("data");
            JSONArray jsonArray = new JSONArray();
            jsonArray = jobject.getJSONArray("fixtures");
            int l = jsonArray.length();
            String[] homenames = new String[l];
            Log.d("Threadga", "JSONArray length = " + jsonArray.length());
            final List<Game> games = new ArrayList<Game>();
            try {
                for (int i = 0; i < l; i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    Game game = new Game(object.optInt("id"));
                    game.setLive(false);
                    game.setDate(object.optString("date"));
                    game.setTime(object.optString("time"));
                    game.setRound(object.optString("round"));
                    game.setHome_name(object.optString("home_name"));
                    game.setAway_name(object.optString("away_name"));
                    game.setLocation(object.optString("location"));
                    game.setLeague_id(object.optInt("league_id"));
                    game.setHome_id(object.optInt("home_id"));
                    game.setAway_id(object.optInt("away_id"));
                    game.setCompetition_id(object.optInt("competition_id"));
                    games.add(game);
                    homenames[i] = object.optString("home_name");
                }
            } catch (Exception ex) {
                Log.d("Threadgames", "Error here: " + ex.getMessage());
            }

            final GamesAdapter adapter = new GamesAdapter(ctx, games, homenames, app);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(ctx, GameInfo.class);
                    i.putExtra("Game", games.get(position));
                    ctx.startActivity(i);
                }
            });
                JSONObject jsonObjectl = new JSONObject(resultl).getJSONObject("data");
                JSONArray jsonArrayl = jsonObjectl.getJSONArray("match");
                RecyclerView lstLive = app.findViewById(R.id.lstLiveGames);

                if (jsonArrayl.length() == 0) {
                    lstLive.setVisibility(View.GONE);
                } else {
                    final List<Game> liveGames = new ArrayList<>();
                    String[] liveHomeNames = new String[jsonArrayl.length()];
                    for (int i = 0; i < jsonArrayl.length(); i++) {
                        JSONObject object = jsonArrayl.getJSONObject(i);
                        Game game = new Game(object.optInt("id"));
                        game.setLive(true);
                        game.setScore(object.optString("score"));
                        game.setLocation(object.optString("location"));
                        game.setHt_score(object.optString("ht_score"));
                        game.setEvents(object.optString("events"));
                        game.setCompetition_id(object.optInt("competition_id"));
                        game.setHome_name(object.optString("home_name"));
                        game.setAway_name(object.optString("away_name"));
                        game.setStatus(object.optString("status"));
                        game.setTime(object.optString("time"));
                        liveGames.add(game);
                        liveHomeNames[i] = object.optString("home_name");
                    }
                    LiveGameAdapter liveGameAdapter = new LiveGameAdapter(liveGames, app);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(app.getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                    lstLive.setLayoutManager(layoutManager);
                    liveGameAdapter.setOnItemClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
                            int position = viewHolder.getAdapterPosition();
                            Game selected = liveGames.get(position);
                            Intent i = new Intent(ctx,GameInfo.class);
                            i.putExtra("Game", selected);
                            ctx.startActivity(i);
                        }
                    });
                    lstLive.setAdapter(liveGameAdapter);


                    lv.setAdapter(adapter);
                    srl.setRefreshing(false);
                }
            } catch (Exception ex) {
                Log.d("Threadlivegames", "ERROR: " + ex.getMessage());
            }
        }

    private boolean isLastDate(int year,int month, int day) {
        if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            if(day == 31) return true;
            else return false;
        } else if(month == 2) {
            if(year % 4 == 0 && day == 29) return true;
            else if(day == 28 && year % 4 != 0) return true;
            else return false;
        } else {
            if(day == 30) return true;
            else return false;
        }
    }

}
