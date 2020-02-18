package com.goldengear.tikvah;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Aaron Ayalew on 10/21/2018.
 */

public class NewsGetter {
    Context ctx;
    public NewsGetter(String category, Context ctx) {
        this.ctx = ctx;
    }
    public void GetNews(String category) {
        RequestQueue queue = Volley.newRequestQueue(ctx);
        String URL = new TikConst().getURL() + "get_news.php";

    }

}
