package com.goldengear.tikvah;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Aaron Ayalew on 10/22/2018.
 */

public class RegisterDevice extends AsyncTask<String,Void,Void> {
    String devID;
    Context context;
    public RegisterDevice(String deviceID, Context ctx){
        this.devID = deviceID;
        this.context = ctx;
    }

    @Override
    protected Void doInBackground(String... strings) {
        try {
            String link = new TikConst().getURL() + "new_user.php";
            final String data = URLEncoder.encode("userId","UTF-8") + "=" + this.devID;
            final URL url = new URL(link);
            final StringBuilder sb = new StringBuilder();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d("Reg", "Thread Running");
                        URLConnection conn = url.openConnection();
                        Log.d("Reg", "opened connection");
                        conn.setDoOutput(true);
                        conn.setConnectTimeout(5000);
                        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                        Log.d("Reg", "Output Stream Got");
                        wr.write(data);
                        Log.d("Reg", "Written");
                        wr.flush();
                        Log.d("Reg", "Flushed");
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        Log.d("Reg", "Input Stream Loaded");
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                            break;
                        }
                        Log.d("Reg", "Done:" + sb.toString());
                        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userId",devID);
                        editor.commit();
                    } catch (Exception ex) {
                        Log.d("RegError", ex.toString());
                    }
                }
            });
            thread.run();

        } catch (Exception e) {
            Log.d("RegError", e.toString());
        }

        return null;
    }
}
