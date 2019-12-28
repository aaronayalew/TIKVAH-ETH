package com.goldengear.tikvah;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import com.google.android.material.snackbar.Snackbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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

/**
 * Created by Aaron Ayalew on 12/16/2018.
 */

public class StandingsGetter extends AsyncTask<String,Void,String> {
    String result;
    ListView lv;
    Activity app;
    Context ctx;
    SwipeRefreshLayout srl;
    View rootView;
    public StandingsGetter(Context context, ListView listView, Activity app, SwipeRefreshLayout swipeRefreshLayout, View rootView) {
        super();
        this.ctx = context;
        this.lv = listView;
        this.app = app;
        this.srl = swipeRefreshLayout;
        this.rootView = rootView;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            final String data = URLEncoder.encode("url", "UTF-8") + "=" + URLEncoder.encode("https://apifootball.com/api/?action=get_standings&league_id=" + strings[0] + "&APIkey=6b5092e236a9479381743ac4b6718f2934627d820776735bc82de4a32c24169d","UTF-8");
            Log.d("Threadst","Data = " + data);
            URL uri = new URL("http://tikvah.xyz/soccer_api_test.php");
            Log.d("Threadst", "Starting Thread");
            URLConnection conn = uri.openConnection();
            Log.d("Threadst", "Opened Connection");
            conn.setConnectTimeout(120000);
            conn.setReadTimeout(180000);
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            Log.d("Threadst", "Output Stream Got");
            wr.write(data);
            Log.d("Threadst", "Written");
            wr.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            Log.d("Threadst", "Done:" + sb.toString());
            result = sb.toString();
            return null;
        } catch (Exception ex){
            Log.d("Threadst", "Error: " + ex.getMessage());
        }
        return null;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONArray jsonArray = new JSONArray(result);
            int length = jsonArray.length();
            int[] positions = new int[length + 1];
            String[] names = new String[length + 1];
            int[] Pts = new int[length + 1];
            int[] Ps = new int[length + 1];
            int[] Ws = new int[length + 1];
            int[] Ds = new int[length + 1];
            int[] Ls = new int[length + 1];
            int[] GFs = new int[length + 1];
            int[] GAs = new int[length + 1];
            names[0] = "Place_holder";
            positions[0] = 0;
            Pts[0] = 0;
            Ps[0] = 0;
            Ws[0] = 0;
            Ds[0] = 0;
            Ls[0] = 0;
            GFs[0] = 0;
            GAs[0] = 0;
            int j = 0;
            for(int i = 0; i < length; i++){
                JSONObject object = jsonArray.getJSONObject(i);
                int place = object.getInt("overall_league_position");
                positions[place] = object.getInt("overall_league_position");
                names[place] = object.optString("team_name");
                Pts[place] = object.optInt("overall_league_PTS");
                Ps[place] = object.optInt("overall_league_payed");
                Ws[place] = object.optInt("overall_league_W");
                Ds[place] = object.optInt("overall_league_D");
                Ls[place] = object.optInt("overall_league_L");
                GFs[place] = object.optInt("overall_league_GF");
                GAs[place] = object.optInt("overall_league_GA");
                Log.d("Standings", "loop: " + String.valueOf(i));
            }
            srl.setRefreshing(false);
            StandingsAdapter adapter = new StandingsAdapter(ctx,positions,names,Pts,Ps,Ws,Ds,Ls,GFs,GAs,app);
            lv.setAdapter(adapter);

        } catch (Exception ex) {
            Log.d("Standings", "Error: " + ex.getMessage());
        }
        srl.setRefreshing(false);

    }
}
