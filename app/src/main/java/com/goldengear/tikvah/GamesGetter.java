package com.goldengear.tikvah;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron Ayalew on 10/21/2018.
 */

public class GamesGetter {
    Context ctx;
    public GamesGetter(String category, Context ctx) {
        this.ctx = ctx;
    }
    public void GetGames(String category) {
        String url = "https://api.myjson.com/bins/8d7n8";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject api = response.optJSONObject("api");
                        int length = api.optInt("results");
                        JSONArray fixtures = api.optJSONArray("fixtures");
                        List<Game> games  = new ArrayList();
                        for(int i = 0; i < length; i++){
                            JSONObject fixture = fixtures.optJSONObject(0);
                            int id =fixture.optInt("fixture_id");
                            Game game = new Game(id);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.d("VolleyError", error.networkResponse.toString());
                    }
                });
    }

}
