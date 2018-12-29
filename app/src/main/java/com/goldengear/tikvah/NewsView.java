package com.goldengear.tikvah;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class NewsView extends AppCompatActivity {
    AdView adView;
    LinearLayout lnl;
    TextView tit;
    TextView con;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_view);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lnl = (LinearLayout) findViewById(R.id.lnlNV);
        MobileAds.initialize(this,"ca-app-pub-8089746021276960~9698421771");
        Intent i = getIntent();
        tit = (TextView) findViewById(R.id.nvTitle);
        con = (TextView) findViewById(R.id.nvCont);
        img = (ImageView) findViewById(R.id.nvImg);
        if(false) {
            Uri uri = getIntent().getData();
            String newsID = uri.getQueryParameter("id");

        } else {
            String Title = i.getStringExtra("title");
            String content = i.getStringExtra("content");
            String image = i.getStringExtra("image");


            Log.d("NewsView", "Image Loading..");
            if (!image.contains("no_image")) {
                File cachDir = getApplicationContext().getCacheDir();
                if (new File(cachDir.getAbsolutePath() + "/" + image).exists()) {
                    try {
                        Bitmap bm = BitmapFactory.decodeFile(cachDir.getAbsolutePath() + "/" + image);
                        img.setImageBitmap(bm);
                    } catch (java.lang.OutOfMemoryError ex) {
                        Log.d("OOM", "Out of memory error in newsview");
                    }
                    Log.d("NewsView", "No image so using default");
                } else {
                    DWImage downloader = new DWImage(img);
                    downloader.execute(new TikConst().getURL() + "/img/" + image, image);
                }
            } else {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        img.setImageResource(R.drawable.sucks);
                    }
                });
                thread.run();
            }
            tit.setText(Title);
            con.setText(content);
        }
        adView = (AdView) findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home :
                this.finish();
                return true;
                default:
                    return true;
        }
    }

    private class DWImage extends AsyncTask<String,Void,Bitmap> {
        ImageView bmimage;
        public DWImage(ImageView imgv) {
            this.bmimage = imgv;
        }
        @Override
        protected Bitmap doInBackground(String... strings) {
            String urldisplay = strings[0];
            Bitmap bmp = null;
            try {
                InputStream in = new URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception ex) {
                Log.d("Error",ex.getMessage());
            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            bmimage.setImageBitmap(bitmap);
        }
    }
    private class GetNews extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {
            final String id;
            id = strings[0];
            try {
                    String link = new TikConst().getURL() + "get_news_by_id.php";
                final String data = URLEncoder.encode("id", "UTF-8") + "=" + id;
                final URL url = new URL(link);
                final StringBuilder sb = new StringBuilder();
                        try {
                            Log.d("ThreadNV", "Thread Running");
                            URLConnection conn = url.openConnection();
                            Log.d("ThreadNV", "opened connection");
                            conn.setDoOutput(true);
                            conn.setConnectTimeout(5000);
                            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                            Log.d("ThreadNV", "Output Stream Got");
                            wr.write(data);
                            Log.d("ThreadNV", "Written");
                            wr.flush();
                            Log.d("ThreadNV", "Flushed");
                            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            Log.d("ThreadNV", "Input Stream Loaded");
                            String line = null;
                            while ((line = reader.readLine()) != null) {
                                sb.append(line);
                                break;
                            }
                            Log.d("ThreadNV", "Done:" + sb.toString());
                        } catch (Exception ex) {
                            Log.d("ThreadNV", ex.toString());

                            Snackbar.make(lnl, "Couldn't Connect", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    execute(id);
                                }
                            }).show();
                        }

                return sb.toString();
            } catch (Exception e) {
                Log.d("Error", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
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

                }
                tit.setText(titles[0]);

                if (!pictures[0].contains("no_image")) {
                    File cachDir = getApplicationContext().getCacheDir();
                    if (new File(cachDir.getAbsolutePath() + "/" + pictures[0]).exists()) {
                        Bitmap bm = BitmapFactory.decodeFile(cachDir.getAbsolutePath() + "/" + pictures[0]);
                        img.setImageBitmap(bm);
                        Log.d("NewsView", "No image so using default");
                    } else {
                        DWImage downloader = new DWImage(img);
                        downloader.execute(new TikConst().getURL() + "/img/" + pictures[0], pictures[0]);
                    }
                } else {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            img.setImageResource(R.drawable.sucks);
                        }
                    });
                    thread.run();

                }
            } catch (Exception ex) {

            }
        }
    }
}
