package com.goldengear.tikvah;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.crypto.spec.DHGenParameterSpec;

/**
 * Created by Aaron Ayalew on 10/31/2018.
 */

public class JSONHelper {
    Context ctx;
    ListView lv;
    SwipeRefreshLayout srl;
    DBHelper helper;
    Activity app;
    public  JSONHelper(Context context, ListView lv, SwipeRefreshLayout srl, Activity app){
        this.ctx = context;
        this.lv = lv;
        this.srl = srl;
        this.app = app;
    }
    public void refreshListView(String s, boolean newNews,String cat) {
        helper= new DBHelper(ctx,cat);
        try {
            JSONObject object = new JSONObject(s);
            String arrStr = object.optString("android");
            Log.d("JSON", "arrstr = " + arrStr);
            JSONArray arr = object.getJSONArray("android");
            Log.d("JSON", "Opted out JSONArray");
            int length = arr.length();
            String[] IDs = new String[length];
            String[] titles = new String[length];
            String[] descriptions = new String[length];
            String[] contents = new String[length];
            String[] pictures = new String[length];
            String[] times = new String[length];
            String[] isExpandeds = new String[length];
            Log.d("JSON", "Gotten Length" + String.valueOf(length));
            for(int i = 0; i < length; i++) {
                JSONObject item = arr.optJSONObject(i);
                Log.d("JSON", "loop: " + String.valueOf(i));
                IDs[i] = item.getString("ID");
                titles[i] = item.getString("title");
                contents[i] = item.getString("content");
                descriptions[i] = item.getString("desc");
                contents[i] = item.getString("content");
                pictures[i] = item.getString("picture");
                times[i] = item.getString("time");
                isExpandeds[i] = "false";
                if (newNews) {
                    helper.insertNews(IDs[i],titles[i],descriptions[i],contents[i],pictures[i],times[i],cat);
                }

            }
            NewsAdapter adapter = new NewsAdapter(ctx,IDs,titles,descriptions,pictures,contents,times,isExpandeds,app);
            lv.setAdapter(adapter);
            srl.setRefreshing(false);
            switch (cat) {
                case "general" :
                    app.getActionBar().setTitle("Home");
                    break;
                case "business" :
                    app.getActionBar().setTitle("Business");
                    break;
                case "spott" :
                    app.getActionBar().setTitle("Sport");
                    break;
                case "entertainment" :
                    app.getActionBar().setTitle("Entertainment");
                    break;
                default :
                    app.getActionBar().setTitle("Tikvah-ETH");
                    break;
            }
        } catch (Exception ex) {
            Log.d("JSON",ex.toString());
        }

    }
}
