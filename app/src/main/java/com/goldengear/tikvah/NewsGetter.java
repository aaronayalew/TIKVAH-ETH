package com.goldengear.tikvah;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;

/**
 * Created by Aaron Ayalew on 10/21/2018.
 */

public class NewsGetter {
    Context ctx;
    ListView lv;
    SwipeRefreshLayout srl;
    Activity app;
    public NewsGetter(String category, Context ctx, ListView lv, SwipeRefreshLayout srl, Activity app) {
        this.ctx = ctx;
        this.lv = lv;
        this.srl = srl;
        this.app = app;
    }
    public void GetNews(final String category) {
        String url = "https://api.jsonbin.io/b/5e8bb8a5af7c476bc47e111f";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley", "Successfull: " + response);
                        JSONHelper helper = new JSONHelper(ctx,lv,srl,app);
                        helper.refreshListView(response,true,category);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.d("VolleyError", error.networkResponse.toString());
                    }
                });
        FeedGetter.getInstance(ctx.getApplicationContext()).addToRequestQueue(jsonObjectRequest);

    }

}
