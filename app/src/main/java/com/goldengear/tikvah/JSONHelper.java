package com.goldengear.tikvah;

import android.app.Activity;
import android.content.Context;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
            Log.d("JSONHelper", "Parsing json...");
            JSONArray data = object.optJSONArray("data");
            int len = data.length();
            String[] titles = new String[len];
            List<Article> articles = new ArrayList(len);
            Log.d("JSONHelper", "Array Obtained");
            for(int i = 0; i < len; i++) {
                Log.d("JSONHelper", "Parsing object - " + i);
                JSONObject obj = data.getJSONObject(i);
                int id = obj.optInt("id");
                Article article = new Article(id);
                article.setTitle(obj.optString("title"));
                titles[i] = obj.optString("title");
                article.setDatetime(obj.optString("date"));
                article.setCategory(obj.optString("category"));
                article.setContent(obj.optString("content"));
                JSONArray pics = obj.optJSONArray("pictures");
                int piclen = pics.length();
                String[] pictures = new String[piclen];
                for(int j = 0; j < piclen; j++){
                    pictures[j] = pics.optString(j);
                }
                article.setPictures(pictures);
                JSONArray tgs = obj.optJSONArray("tags");
                int tagslen = tgs.length();
                String[] tags = new String[tagslen];
                for(int k = 0; k < tagslen; k++) {
                    tags[k] = tgs.optString(k);
                }
                article.setTags(tags);
                JSONObject prov = obj.optJSONObject("provider");
                article.setProv_id(prov.optInt("id"));
                article.setProv_name(prov.optString("name"));
                article.setProv_pic(prov.optString("picture"));
                article.setExpanded(false);
                articles.add(article);
            }
            Log.d("JSONHelper", "Creating Adapter...");

            NewsAdapter adapter = new NewsAdapter(ctx,articles,app,titles);
            lv.setAdapter(adapter);
            Log.d("JSONHelper", "Adapter created and set");
        } catch (Exception ex) {
            Log.d("JSON",ex.toString() + "in JSONHelper.java");
            srl.setRefreshing(false);
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
            srl.setRefreshing(false);
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
            srl.setRefreshing(false);
        }
    }
}
