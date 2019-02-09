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
    public void refreshListView(JSONObject object, boolean newNews,String cat) {

        try {
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
                descriptions[i] = item.getString("desc");
                pictures[i] = item.getString("picture");
                times[i] = item.getString("time");
                isExpandeds[i] = "false";

            }
            NewsAdapter adapter = new NewsAdapter(ctx,IDs,titles,descriptions,pictures,contents,times,isExpandeds,app);
            lv.setAdapter(adapter);
            srl.setRefreshing(false);
            switch (cat) {
                case "General" :
                    app.getActionBar().setTitle("Home");
                    break;
                case "Business" :
                    app.getActionBar().setTitle("Business");
                    break;
                case "Sport" :
                    app.getActionBar().setTitle("Sport");
                    break;
                case "Entertainment" :
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
    public void refreshListView(String s, boolean newNews,String cat) {
        helper= new DBHelper(ctx,"newsDB");
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
            String[] categories = new String[length];
            Log.d("JSON", "Gotten Length" + String.valueOf(length));
            for(int i = 0; i < length; i++) {
                JSONObject item = arr.optJSONObject(i);
                Log.d("JSON", "loop: " + String.valueOf(i));
                IDs[i] = item.getString("ID");
                titles[i] = item.getString("title");
                contents[i] = item.getString("content");
                descriptions[i] = item.getString("desc");
                pictures[i] = item.getString("picture");
                times[i] = item.getString("time");
                isExpandeds[i] = "false";
                categories[i] = cat;


            }
            helper.insertNews(IDs,titles,descriptions,pictures,times,categories);
            NewsAdapter adapter = new NewsAdapter(ctx,IDs,titles,descriptions,pictures,contents,times,isExpandeds,app);
            lv.setAdapter(adapter);
            srl.setRefreshing(false);
            switch (cat) {
                case "General" :
                    app.getActionBar().setTitle("Home");
                    break;
                case "Business" :
                    app.getActionBar().setTitle("Business");
                    break;
                case "Sport" :
                    app.getActionBar().setTitle("Sport");
                    break;
                case "Entertainment" :
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
    public void refreshMemeListView(String s){
        helper= new DBHelper(ctx,"memeDB");
        try {
            JSONObject object = new JSONObject(s);
            String arrStr = object.optString("android");
            Log.d("JSON", "arrstr = " + arrStr);
            JSONArray arr = object.getJSONArray("android");
            Log.d("JSON", "Opted out JSONArray");
            int length = arr.length();
            String[] IDs = new String[length];
            String[] images = new String[length];
            String[] dates = new String[length];
            String[] provider_ids = new String[length];
            String[] logos = new String[length];
            String[] provider_names = new String[length];
            String[] texts = new String[length];
            Log.d("JSON", "Gotten Length" + String.valueOf(length));
            for(int i = 0; i < length; i++) {
                JSONObject item = arr.optJSONObject(i);
                Log.d("JSON", "loop: " + String.valueOf(i));
                IDs[i] = item.getString("ID");
                images[i] = item.getString("image");
                dates[i] = item.getString("date");
                provider_ids[i] = item.getString("provider_id");
                provider_names[i] = item.getString("provider_name");
                logos[i] = item.getString("provider_image");
                texts[i] = item.getString("text");

            }
            MemeAdapter adapter = new MemeAdapter(ctx,IDs,images,dates,provider_ids,provider_names,logos,texts,app);
            lv.setAdapter(adapter);
            srl.setRefreshing(false);
        } catch (Exception ex) {
            Log.d("JSON",ex.toString());
        }
    }
    public void refreshMemeListView(JSONObject object){
        helper= new DBHelper(ctx,"memeDB");
        try {
            String arrStr = object.optString("android");
            Log.d("JSON", "arrstr = " + arrStr);
            JSONArray arr = object.getJSONArray("android");
            Log.d("JSON", "Opted out JSONArray");
            int length = arr.length();
            String[] IDs = new String[length];
            String[] images = new String[length];
            String[] dates = new String[length];
            String[] provider_ids = new String[length];
            String[] logos = new String[length];
            String[] provider_names = new String[length];
            String[] texts = new String[length];
            Log.d("JSON", "Gotten Length" + String.valueOf(length));
            for(int i = 0; i < length; i++) {
                JSONObject item = arr.optJSONObject(i);
                Log.d("JSON", "loop: " + String.valueOf(i));
                IDs[i] = item.getString("ID");
                images[i] = item.getString("image");
                dates[i] = item.getString("date");
                provider_ids[i] = item.getString("provider_id");
                provider_names[i] = item.getString("provider_name");
                logos[i] = item.getString("provider_image");
                texts[i] = item.getString("text");

            }
            MemeAdapter adapter = new MemeAdapter(ctx,IDs,images,dates,provider_ids,provider_names,logos,texts,app);
            lv.setAdapter(adapter);
            srl.setRefreshing(false);
        } catch (Exception ex) {
            Log.d("JSON",ex.toString());
        }
    }
}
