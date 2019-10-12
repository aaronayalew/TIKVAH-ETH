package com.goldengear.tikvah;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class StartScreen extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.

        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        final SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("newDevice")) {
            Log.d("Reg","Not a new device");
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("fromMe", "yes");
            startActivity(i);
        } else {
            Log.d("Reg", "This is a new device");
            final Button start = (Button) findViewById(R.id.startBtn);
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*String[] permission = {Manifest.permission.READ_PHONE_STATE};
                    if(Build.VERSION.SDK_INT >= 23) {
                        reqPermission(permission, 1);
                    } else {
                        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        String n = tm.getDeviceId();
                        Log.d("Start", "Device ID: " + n);
                        RegisterDevice registerDevice = new RegisterDevice(n, getApplicationContext());
                        registerDevice.execute();
                        Intent i = new Intent(StartScreen.this, MainActivity.class);
                        startActivity(i);
                        closeApp();
                    }*/
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("newDevice", "Yes");
                    editor.commit();
                    startActivity(new Intent(StartScreen.this,MainActivity.class));
                }
            });

        }
        FirebaseAnalytics fa = FirebaseAnalytics.getInstance(this);
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("hh:MM mm/DD");
        Bundle log = new Bundle();
        log.putString("Date", formatter.format(date));
        log.putInt("Build_Ver", Build.VERSION.SDK_INT);
        fa.logEvent("NewUser",log );
        fa.setAnalyticsCollectionEnabled(true);
        // Set up the user interaction to manually show or hide the system UI.

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
    }

    private void closeApp() {
        this.finish();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar

        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
    @TargetApi(23)
    protected void reqPermission(String[] permm, int resCo) {
        requestPermissions(permm,resCo);
    }
    /*@Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        if (grantResults.length > 0) {
            try {
                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String n = tm.getDeviceId();
                Log.d("Start", "Device ID: " + n);
                RegisterDevice registerDevice = new RegisterDevice(n, getApplicationContext());
                registerDevice.execute();
                closeApp();
            } catch (Exception ex) {

            }
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }*/
}
