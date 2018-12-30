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
            final String data = URLEncoder.encode("url", "UTF-8") + "=" + URLEncoder.encode("https://apifootball.com/api/?action=get_events&from=2018-12-29&to=2019-01-01&league_id=" + strings[0] + "&APIkey=6b5092e236a9479381743ac4b6718f2934627d820776735bc82de4a32c24169d","UTF-8");
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
            srl.setRefreshing(false);
            Snackbar.make(srl,"Error while getting data",Snackbar.LENGTH_LONG);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONArray jsonArray = new JSONArray(result);
            int l = jsonArray.length();
            int[] ids = new int[l];
            String[] dates = new String[l];
            String[] times = new String[l];
            String[] statuses = new String[l];
            String[] homenames = new String[l];
            int[] homescores = new int[l];
            String[] awaynames = new String[l];
            int[] awayscores = new int[l];
            int[] isLives = new int[l];
            for(int i = 0; i < l;i++ ){
                JSONObject object  = jsonArray.getJSONObject(i);
                ids[i] = object.optInt("match_id");
                dates[i] = object.optString("match_date");
                times[i] = object.optString("match_time");
                statuses[i] = object.optString("match_status");
                homenames[i] = object.optString("match_hometeam_name");
                homescores[i] = object.optInt("match_hometeam_score");
                awaynames[i] = object.optString("match_awayteam_name");
                awayscores[i] = object.optInt("match_awayteam_score");
                isLives[i] = object.optInt("match_live");
            }
            GamesAdapter adapter = new GamesAdapter(ctx,ids,dates,times,statuses,homenames,homescores,awaynames,awayscores,isLives,app);
            lv.setAdapter(adapter);
            srl.setRefreshing(false);
        } catch (Exception ex) {

        }
    }
}
