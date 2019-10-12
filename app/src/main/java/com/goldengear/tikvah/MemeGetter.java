package com.goldengear.tikvah;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import com.google.android.material.snackbar.Snackbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Aaron Ayalew on 11/27/2018.
 */

public class MemeGetter extends AsyncTask<String,Void,String>{
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
    public MemeGetter(Context context, String param, ListView list, View rootV, SwipeRefreshLayout srl, Activity app){
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
        helper = new DBHelper(ctx,category);
        srl.setRefreshing(true);
    }
    @Override
    protected String doInBackground(String... strings) {
        try {
            String link = new TikConst().getURL() + "get_memes.php";
            final URL url = new URL(link);
            final StringBuilder sb = new StringBuilder();

                    try {
                        Log.d("Thread Meme", "Thread Running");
                        URLConnection conn = url.openConnection();
                        Log.d("Thread Meme", "opened connection");
                        conn.setDoOutput(true);
                        conn.setConnectTimeout(60000);

                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        Log.d("Thread Meme", "Input Stream Loaded");
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                            break;
                        }
                        Log.d("Thread Meme", "Done:" + sb.toString());
                    } catch (Exception ex) {
                        Log.d("ERROR Meme", ex.toString());
                        Snackbar.make(rootView, "Couldn't Connect", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               MemeGetter mg = new MemeGetter(ctx, category, lv, rootView, srl, app);
                                mg.execute();
                            }
                        }).show();
                    }

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
        helper.refreshMemeListView(s);
        srl.setRefreshing(false);

    }

}

