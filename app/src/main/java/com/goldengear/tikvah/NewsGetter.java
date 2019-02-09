package com.goldengear.tikvah;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Aaron Ayalew on 10/21/2018.
 */

public class NewsGetter extends AsyncTask<String,Void,String> {
    public Context ctx;
    public String category;
    ListView lv;
    ActionBar ab;
    View rootView;
    String ResultHolder;
    SwipeRefreshLayout srl;
    DBHelper helper;
    boolean newNews;
    Activity app;
    Boolean success = true;

    public NewsGetter(Context context, String param, ListView list, View rootV, SwipeRefreshLayout srl, Activity app){
        this.ctx = context;
        this.category = param;
        this.lv = list;
        this.rootView = rootV;
        this.srl = srl;
        this.app = app;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        srl.setRefreshing(true);
        helper = new DBHelper(ctx,"newsDB");

    }

    @Override
    protected String doInBackground(String... params) {
        final String cat;
        cat = this.category;
            try {
                String link = new TikConst().getURL() + "get_news.php";
                final String data = URLEncoder.encode("cat", "UTF-8") + "=" +
                        cat;
                final URL url = new URL(link);
                final StringBuilder sb = new StringBuilder();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d("Thread" + cat, "Thread Running");
                            URLConnection conn = url.openConnection();
                            Log.d("Thread" + cat, "opened connection");
                            conn.setDoOutput(true);
                            conn.setConnectTimeout(15000);
                            conn.setReadTimeout(50000);
                            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                            Log.d("Thread" + cat, "Output Stream Got");
                            wr.write(data);
                            Log.d("Thread" + cat, "Written");
                            wr.flush();
                            Log.d("Thread" + cat, "Flushed");
                            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            Log.d("Thread" + cat, "Input Stream Loaded");
                            String line = null;
                            while ((line = reader.readLine()) != null) {
                                sb.append(line);
                                break;
                            }
                            Log.d("Thread"  + cat, "Done:" + sb.toString());
                        } catch (Exception ex) {
                            Log.d("ERROR" + cat, ex.toString());
                            srl.setRefreshing(false);
                            success = false;
                           /* Snackbar.make(rootView, "Couldn't Connect", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    NewsGetter ng = new NewsGetter(ctx, category, lv, rootView, srl,app);
                                    ng.execute();
                                }
                            }).show();*/
                        }
                    }
                });
                thread.run();
            newNews = true;
            return sb.toString();
        } catch (Exception e) {
            Log.d("Error", e.toString());
            return e.getMessage();
        }


    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("Result", s);
        JSONHelper helper = new JSONHelper(ctx,lv,srl,app);
        if(success) helper.refreshListView(s,newNews,category);

    }

}
