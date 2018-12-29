package com.goldengear.tikvah;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Aaron Ayalew on 10/18/2018.
 */

public class FragSport extends Fragment {
    TextView games;
    public FragSport (){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View result = inflater.inflate(R.layout.frag_sport,container, false);
        final ListView lv = (ListView) result.findViewById(R.id.lstSport);
        Log.d("Aaron", "Found ListView");
        Log.d("Aaron", "Initialized Adapter");
        lv.animate();
        Log.d("Aaron", "Adapter Set, Returning inflated View");
        final SwipeRefreshLayout rootView = result.findViewById(R.id.lnlSport);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ScoreGetter sg = new ScoreGetter(getActivity().getApplicationContext());
                sg.execute("Sport");
            }
        }).run();
        NewsGetter ng = new NewsGetter(getActivity().getApplicationContext(),"sport",lv,rootView,rootView,getActivity());
        ng.execute();

        rootView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NewsGetter ng = new NewsGetter(getActivity().getApplicationContext(),"sport",lv,rootView,rootView,getActivity());
                ng.execute();
            }
        });
        return result;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        
    }
    private class ScoreGetter extends AsyncTask<String,Void,Void> {
        String result;
        Context ctx;
        public ScoreGetter(Context context){
            this.ctx = context;
        }
        @Override
        protected Void doInBackground(String... strings) {
            try {
                final String data = URLEncoder.encode("url", "UTF-8") + "=" + URLEncoder.encode("https://apifootball.com/api/?action=get_events&from=2018-12-10&to=2018-12-16&league_id=62&APIkey=6b5092e236a9479381743ac4b6718f2934627d820776735bc82de4a32c24169d","UTF-8");
                Log.d("Thread","Data = " + data);
                URL uri = new URL("http://tikvah.xyz/soccer_api_test.php");
                Log.d("Threadsp", "Strarting Thread");
                URLConnection conn = uri.openConnection();
                Log.d("Threadsp", "Opened Connection");
                conn.setConnectTimeout(120000);
                conn.setReadTimeout(180000);
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                Log.d("ThreadSp", "Output Stream Got");
                wr.write(data);
                Log.d("Thread", "Written");
                wr.flush();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = null;
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                Log.d("ThreadSp", "Done:" + sb.toString());
                result = sb.toString();
                return null;
            } catch (Exception ex){
                Log.d("Threadsp", "Error: " + ex.getMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                JSONArray jsonArray = new JSONArray(result);
                int length = jsonArray.length();
                String[] leagues = new String[length];
                int j = 0;
                for(int i = 0; i < length; i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    if(object.optString("match_live").contains("1")){
                        leagues[j] = "";
                        if(object.optString("match_hometeam_name").length() < 31) {
                            for(i = 31 - object.optString("match_hometeam_name").length();i < 32;i++ ) {
                                leagues[j] += " ";
                            }
                            leagues[j] = object.optString("match_hometeam_name") + "  ";

                        } else {

                        }
                        leagues[j] += object.optString("match_hometeam_score") + " - " + object.optString("match_awayteam_score") + "  ";
                        leagues[j] += object.optString("match_awayteam_name");
                        if(object.optString("match_awayteam_name").length() < 31) {
                            for(i = 31 - object.optString("match_awayteam_name").length();i < 32;i++ ) {
                                leagues[j] += " ";
                            }
                            leagues[j] += object.optString("match_status");
                        } else {

                        }
                        j++;
                    }
                }
                int len = leagues.length;
                StringBuilder toPut = new StringBuilder();
                int tot = 0;
                for(int k = 0; k < len;k++){
                    if(leagues[k] != null) {
                        toPut.append("\n" + leagues[k]);
                        tot++;
                    }
                }
                Log.d("Score", "Tot = " + tot);
                int tmp = tot;
                for(int i = 0; i < length; i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    if(!object.optString("match_live").contains("1")) {
                        leagues[tot] = "";
                        if(object.optString("match_hometeam_name").length() < 31) {
                            for(int o = 31 - object.optString("match_hometeam_name").length();o < 32;o++ ) {
                                leagues[tot] += " ";
                            }
                            leagues[tot] = object.optString("match_hometeam_name");

                        } else {

                        }
                        leagues[tot] += "  " + object.optString("match_hometeam_score") + " - " + object.optString("match_awayteam_score") + "  ";
                        leagues[tot] += object.optString("match_awayteam_name");
                        if(object.optString("match_awayteam_name").length() < 32) {
                            for(int o = 31 - object.optString("match_awayteam_name").length();o < 29;o++ ) {
                                leagues[tot] += " ";
                            }
                            leagues[tot] += object.optString("match_status");
                        } else {

                        }
                        tot++;
                    }

                }
                Log.d("Score", "Tot = " + tot);
                for(int i = tmp; i < len;i++) {
                    if(leagues[i] != null) {
                        toPut.append("\n" + leagues[i]);
                    }
                }
/*
                games.setText(toPut.toString());
*/

            } catch (Exception ex) {
                Log.d("Threadsp", "Error2: " + ex.getMessage());

            }
        }
    }
}
