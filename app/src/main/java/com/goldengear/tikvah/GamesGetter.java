package com.goldengear.tikvah;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
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

            final String data = URLEncoder.encode("url", "UTF-8") + "=" + URLEncoder.encode("https://apifootball.com/api/?action=get_events&from="  + yesterday + "&to=2" + tomorrow + "&league_id=" + strings[0] + "&APIkey=6b5092e236a9479381743ac4b6718f2934627d820776735bc82de4a32c24169d","UTF-8");
            Log.d("Threadgames","Data = " + data);
            URL uri = new URL("http://tikvah.xyz/soccer_api_test.php");
            Log.d("Threadgames", "Starting Thread");
            URLConnection conn = uri.openConnection();
            Log.d("Threadgames", "Opened Connection");
            conn.setConnectTimeout(120000);
            conn.setReadTimeout(180000);
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            Log.d("Threadgames", "Output Stream Got");
            wr.write(data);
            Log.d("Threadgames", "Written");
            wr.flush();
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
            JSONArray jsonArray = new JSONArray(result);
            int l = jsonArray.length() + 3;
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
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ");
            String tod = formatter.format(today);
            int year = Integer.valueOf(tod.substring(0,4));
            int month = Integer.valueOf(tod.substring(5,7));
            int day = Integer.valueOf(tod.substring(8,10));
            int tom = 1;
            boolean lstDate = isLastDate(year,month,day);
            dates[0] = "Tomorrow";
            for(int i = 0; i < l - 3;i++ ) {
                JSONObject object = jsonArray.getJSONObject(i);
                String date = object.optString("match_date");
                int yeard = Integer.valueOf(date.substring(0, 4));
                int monthd = Integer.valueOf(date.substring(5, 7));
                int dayd = Integer.valueOf(date.substring(8, 10));
                if (((!lstDate) && yeard == year && monthd == month && dayd == day + 1) || (lstDate && yeard == year && monthd == month + 1 && dayd == 1)) {
                    ids[tom] = object.optInt("match_id");
                    dates[tom] = object.optString("match_date");
                    times[tom] = object.optString("match_time");
                    statuses[tom] = object.optString("match_status");
                    homenames[tom] = object.optString("match_hometeam_name");
                    homescores[tom] = object.optInt("match_hometeam_score");
                    awaynames[tom] = object.optString("match_awayteam_name");
                    awayscores[tom] = object.optInt("match_awayteam_score");
                    isLives[tom] = object.optInt("match_live");
                    Log.d("Threadga", "Tom = " + tom);
                    tom++;
                }
            }
            dates[tom] = "Today";
            Log.d("Threadga", "Tom = " + tom + " At today");

            tom++;
            for(int i = 0; i < l - 3;i++ ) {
                JSONObject object = jsonArray.getJSONObject(i);
                String date = object.optString("match_date");
                int yeard = Integer.valueOf(date.substring(0, 4));
                int monthd = Integer.valueOf(date.substring(5, 7));
                int dayd = Integer.valueOf(date.substring(8, 10));
                if (yeard == year && monthd == month && day == dayd) {
                    ids[tom] = object.optInt("match_id");
                    dates[tom] = object.optString("match_date");
                    times[tom] = object.optString("match_time");
                    statuses[tom] = object.optString("match_status");
                    homenames[tom] = object.optString("match_hometeam_name");
                    homescores[tom] = object.optInt("match_hometeam_score");
                    awaynames[tom] = object.optString("match_awayteam_name");
                    awayscores[tom] = object.optInt("match_awayteam_score");
                    isLives[tom] = object.optInt("match_live");
                    Log.d("Threadga", "Tom = " + tom);
                    tom++;
                }
            }
            String yesterday;
            if(day != 1) yesterday = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day - 1);
            else {
                if(month == 2 || month == 4 || month == 6 || month == 8 || month == 9 || month == 11 || month == 1) yesterday = String.valueOf(year) + "-" + String.valueOf(month - 1) + "-31";
                else if(month == 3 && year % 4 != 0) yesterday = String.valueOf(year) + "-" + String.valueOf(month - 1) + "-" + String.valueOf(28);
                else if(month == 3 && year % 4 == 0) yesterday = String.valueOf(year) + "-" + String.valueOf(month - 1) + "-" + String.valueOf(29);
                else yesterday = String.valueOf(year) + "-" + String.valueOf(month - 1) + "-" + String.valueOf(30);
            }
            dates[tom] = "Yesterday";
            Log.d("Threadga", "Tom = " + tom + "At yesterday");
            tom++;
            for(int i = 0; i < l - 3;i++ ) {
                JSONObject object = jsonArray.getJSONObject(i);
                String date = object.optString("match_date");
                if (date.contains("2019-01-01")) {
                    ids[tom] = object.optInt("match_id");
                    dates[tom] = object.optString("match_date");
                    times[tom] = object.optString("match_time");
                    statuses[tom] = object.optString("match_status");
                    homenames[tom] = object.optString("match_hometeam_name");
                    homescores[tom] = object.optInt("match_hometeam_score");
                    awaynames[tom] = object.optString("match_awayteam_name");
                    awayscores[tom] = object.optInt("match_awayteam_score");
                    isLives[tom] = object.optInt("match_live");
                    Log.d("Threadga", "Tom = " + tom);
                    tom++;
                }
            }
            int[] idsr = new int[tom];
            String[] datesr = new String[tom];
            String[] timesr = new String[tom];
            String[] statusesr = new String[tom];
            String[] homenamesr = new String[tom];
            int[] homescoresr = new int[tom];
            String[] awaynamesr = new String[tom];
            int[] awayscoresr = new int[tom];
            int[] isLivesr = new int[tom];
            for(int j = 0; j < tom;j++) {
                idsr[j] = ids[j];
                datesr[j] = dates[j];
                timesr[j] = times[j];
                statusesr[j] = statuses[j];
                homenamesr[j] = homenames[j];
                homescoresr[j] = homescores[j];
                awaynamesr[j] = awaynames[j];
                awayscoresr[j] = awayscores[j];
                isLivesr[j] = isLives[j];
            }
            GamesAdapter adapter = new GamesAdapter(ctx,idsr,datesr,timesr,statusesr,homenamesr,homescoresr,awaynamesr,awayscoresr,isLivesr,app);
            lv.setAdapter(adapter);
            srl.setRefreshing(false);
        } catch (Exception ex) {
            Log.d("Threadgames", "ERROR: " + ex.toString());
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
