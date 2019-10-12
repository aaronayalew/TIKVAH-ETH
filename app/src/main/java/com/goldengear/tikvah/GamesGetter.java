package com.goldengear.tikvah;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
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
import java.util.Calendar;
import java.util.Date;

import io.fabric.sdk.android.services.concurrency.AsyncTask;

/**
 * Created by Aaron Ayalew on 12/30/2018.
 */

public class GamesGetter extends AsyncTask<String,Void,String> {
    String result;
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
            String tomorrow;
            if(!lstDate) tomorrow = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day + 1);
            else tomorrow = String.valueOf(year) + "-" + String.valueOf(month + 1) + "-1";
            String yesterday;
            if(day != 1) yesterday = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day - 1);
            else {
                if(month == 2 || month == 4 || month == 6 || month == 8 || month == 9 || month == 11 || month == 1) yesterday = String.valueOf(year) + "-" + String.valueOf(month - 1) + "-31";
                else if(month == 3 && year % 4 != 0) yesterday = String.valueOf(year) + "-" + String.valueOf(month - 1) + "-" + String.valueOf(28);
                else if(month == 3 && year % 4 == 0) yesterday = String.valueOf(year) + "-" + String.valueOf(month - 1) + "-" + String.valueOf(29);
                else yesterday = String.valueOf(year) + "-"  + String.valueOf(month - 1) + "-" + String.valueOf(30);
            }

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
            return null;
        } catch (Exception ex){
            Log.d("Threadgames", "Error: " + ex.getMessage());
            Snackbar.make(srl,"Error while getting data",Snackbar.LENGTH_LONG);
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
            Log.d("Threadga","JSONArray length = " + jsonArray.length());
            int[] ids = new int[l];
            String[] dates = new String[l];
            String[] times = new String[l];
            String[] statuses = new String[l];
            String[] homenames = new String[l];
            int[] homescores = new int[l];
            String[] awaynames = new String[l];
            int[] awayscores = new int[l];
            int[] isLives = new int[l];
            try {
                for(int i = 0; i < l; i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    ids[i] = object.optInt("id");
                    dates[i] = object.optString("date");
                    times[i] = object.optString("time");
                    homenames[i] = object.optString("home_name");
                    awaynames[i] = object.optString("away_name");
                    statuses[i] = object.optString("location");
//                  homescores[tom] = object.optInt("match_hometeam_score");
//                  awayscores[tom] = object.optInt("match_awayteam_score");
//                  isLives[tom] = object.optInt("match_live");

                }
            } catch (Exception ex) {
                Log.d("Threadgames", "Error here: " + ex.getMessage());
            }

            final GamesAdapter adapter = new GamesAdapter(ctx,ids,dates,times,statuses,homenames,awaynames,app);
            lv.setAdapter(adapter);
        /*    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(app.getApplicationContext(),GameInfo.class);
                    JSONObject obj = adapter.getDetails(position);
                    intent.putExtras(jsonToBundle(obj));
                    ctx.startActivity(intent);
                }
            });*/
            srl.setRefreshing(false);
        } catch (Exception ex) {
            Log.d("Threadgames", "ERROR: " + ex.getMessage());
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
    private Bundle jsonToBundle(JSONObject jsonObject){
        try {
            Bundle bundle = new Bundle();
            bundle.putString("home_formation", jsonObject.getString("match_hometeam_system"));
            bundle.putString("away_formation", jsonObject.getString("match_awayteam_system"));
            JSONArray scores = jsonObject.optJSONArray("goalscorer");
            int l = scores.length();
            String[] goal_times = new String[l];
            String[] homeoraways = new String[l];
            String[] scorers = new String[l];
            for(int i = 0; i < l;i++){
                goal_times[i] = scores.getJSONObject(i).optString("time");
                if(scores.getJSONObject(i).optString("home_scorer").length() > 1) {
                    homeoraways[i] = "home";
                    scorers[i] = scores.getJSONObject(i).optString("home_scorer");
                } else {
                    homeoraways[i] = "away";
                    scorers[i] = scores.getJSONObject(i).optString("away_scorer");
                }

            }
            bundle.putStringArray("goal_times",goal_times);
            bundle.putStringArray("homeoraways",homeoraways);
            bundle.putStringArray("scorers",scorers);
            JSONArray array = jsonObject.getJSONArray("statistics");
            int len = array.length();
            for(int i = 0; i < len; i++) {
                JSONObject obj = array.getJSONObject(i);
                Log.d("Thradgames","Iterating through the statictics json array");
                bundle.putInt("home_" + obj.optString("type"),obj.optInt("home"));
                bundle.putInt("away_" + obj.optString("type"),obj.optInt("away"));

            }
            bundle.putString("match_date",jsonObject.getString("match_date"));
            bundle.putString("match_status",jsonObject.getString("match_status"));
            bundle.putString("match_time", jsonObject.getString("match_time"));
            bundle.putString("home_name",jsonObject.getString("match_hometeam_name"));
            bundle.putString("away_name",jsonObject.getString("match_awayteam_name"));
            bundle.putString("home_score",jsonObject.getString("match_hometeam_score"));
            bundle.putString("away_score",jsonObject.getString("match_awayteam_score"));
            bundle.putString("isLive", jsonObject.getString("match_live"));
            return bundle;
        } catch (Exception ex) {
            Log.d("Threadga", "Error in json2Bundle: " + ex.toString());
        }
        return null;
    }
}
