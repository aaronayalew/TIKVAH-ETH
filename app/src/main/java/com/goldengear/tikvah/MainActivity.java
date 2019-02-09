package com.goldengear.tikvah;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final String TAG_HOME = "home fragment";
    private static final String TAG_SPORT = "sport fragment";
    private static final String TAG_BUSINESS = "business fragment";
    private static final String TAG_ENTERTAINMENT = "entertainment fragment";
    private static final String TAG_ABOUT = "about fragment";
    private FirebaseAnalytics mFirebaseAnalytics;
    private FragmentManager fragmentManager;
    private String currentFragment = null;
    private boolean sportLo = false, businLo = false, entLo = false;
    AdView mAdView;
    FragHome fh;
    FragSport fs;
    FragBusiness fb;
    FragEntertainment fe;
    FragStat fst;
    private TextView mTextMessage;
    String[] permission = {Manifest.permission.INTERNET};
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

         /*   ViewGroup vg = (ViewGroup) findViewById(R.id.fHome);
            vg.removeAllViews();
*/
            FragmentTransaction ft = fragmentManager.beginTransaction();
            boolean fragChanged = false;
            Fragment curFrag = fragmentManager.getPrimaryNavigationFragment();
            if(curFrag!= null) {
                ft.detach(curFrag);
            }
            switch (item.getItemId()) {
                case R.id.nav_home:
                    Log.d("FRAGMAN", "Home Selected");
                    Fragment fragment = fragmentManager.findFragmentByTag("HOME_FRAG");
                    if(fragment == null) {
                        Log.d("FRAGMAN", "fragment is null");
                        fragment = new FragHome();
                        ft.add(R.id.fHome,fragment,"HOME_FRAG");
                        ft.attach(fragment).commitNowAllowingStateLoss();
                    } else {
                        Log.d("FRAGMAN", "fragment is not null");
                        ft.replace(R.id.fHome,fragment).commitNowAllowingStateLoss();
                    }
                    ft.setPrimaryNavigationFragment(fragment);
                    getSupportActionBar().setTitle("Home");
                    return true;
                case R.id.nav_sport:

                    Log.d("FRAGMAN", "Sport Selected");
                    Fragment fragment2 = fragmentManager.findFragmentByTag("SPORT_FRAG");
                    if(fragment2 == null) {
                        Log.d("FRAGMAN", "fragment is null");
                        fragment2 = new FragSport();
                        ft.add(R.id.fHome,fragment2,"SPORT_FRAG");
                        ft.attach(fragment2).commitNowAllowingStateLoss();

                    } else {
                        Log.d("FRAGMAN", "fragment is not null");

                        ft.replace(R.id.fHome,fragment2).commit();
                    }
                    ft.setPrimaryNavigationFragment(fragment2);
                    getSupportActionBar().setTitle("Sport");
                    return true;
                case R.id.nav_leagues :
                    Log.d("FRAGMAN", "Sport Selected");
                    Fragment fragment5 = fragmentManager.findFragmentByTag("LEAGUES_FRAG");
                    if(fragment5 == null) {
                        Log.d("FRAGMAN", "fragment is null");
                        fragment5 = new FragStat();
                        ft.add(R.id.fHome,fragment5,"LEAGUES_FRAG");
                        ft.attach(fragment5).commitNowAllowingStateLoss();

                    } else {
                        Log.d("FRAGMAN", "fragment is not null");

                        ft.replace(R.id.fHome,fragment5).commit();
                    }
                    ft.setPrimaryNavigationFragment(fragment5);
                    getSupportActionBar().setTitle("Soccer Updates");
                    return true;
                case R.id.nav_business:

                    Log.d("FRAGMAN", "Business Selected");
                    Fragment fragment3 = fragmentManager.findFragmentByTag("BUSINESS_FRAG");
                    if(fragment3 == null) {
                        Log.d("FRAGMAN", "fragment is null");
                        fragment3 = new FragBusiness();
                        ft.add(R.id.fHome,fragment3,"BUSINESS_FRAG");
                        ft.attach(fragment3).commitNowAllowingStateLoss();

                    } else {
                        Log.d("FRAGMAN", "fragment is not null");
                        ft.replace(R.id.fHome,fragment3).commitNowAllowingStateLoss();
                    }
                    ft.setPrimaryNavigationFragment(fragment3);
                    getSupportActionBar().setTitle("Business");
                    return true;
                case R.id.nav_entertainment:
                    Log.d("FRAGMAN", "Entertainment Selected");
                    Fragment fragment4 = fragmentManager.findFragmentByTag("ENT_FRAG");
                    if(fragment4 == null) {
                        Log.d("FRAGMAN", "fragment is null");
                        fragment4 = new FragEntertainment() ;
                        ft.add(R.id.fHome,fragment4,"ENT_FRAG");
                        ft.attach(fragment4).commitNowAllowingStateLoss();

                    } else {
                        Log.d("FRAGMAN", "fragment is not null");
                        ft.replace(R.id.fHome,fragment4).commitNowAllowingStateLoss();
                    }
                    ft.setPrimaryNavigationFragment(fragment4);
                    getSupportActionBar().setTitle("Entertainment");
                    return true;

            }

            ft.setReorderingAllowed(true);
            ft.commit();
            return false;
        }
    };

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                reqPermissions();
            }
        }
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("newDevice")) {
            Log.d("Reg","Not a new device");
        } else {
            Intent srart = new Intent(MainActivity.this,StartScreen.class);
            startActivity(srart);
            this.finish();
        }
        MobileAds.initialize(this,"ca-app-pub-8089746021276960~9698421771");
        fh = new FragHome();
        fs = new FragSport();
        fb = new FragBusiness();
        fe = new FragEntertainment();
        fst = new FragStat();
        ActionBar ab = getSupportActionBar();
        ab.setLogo(R.drawable.ic_tikvah);
        fragmentManager = getSupportFragmentManager();
        /*fragmentManager.beginTransaction().add(R.id.fHome,fh,TAG_HOME).commit();
        fragmentManager.beginTransaction().add(R.id.fHome,fs,TAG_SPORT).commit();
        fragmentManager.beginTransaction().add(R.id.fHome,fe,TAG_ENTERTAINMENT).commit();
        fragmentManager.beginTransaction().add(R.id.fHome,fb,TAG_BUSINESS).commit();
        */File dir = getCacheDir();
        File output = new File(dir, ".nomedia");
        try {
            if (output.createNewFile()) {
                Log.d("Cache", "File found not creating .nomedia");
            }
        } catch (java.io.IOException ex) {
            Log.d("Cache", "EXCEPTION: " + ex.getMessage());
        }


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAdView.loadAd(adRequest);
        if(currentFragment == null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            navigation.setSelectedItemId(R.id.nav_home);
            FragHome fragment = new FragHome();
            ft.add(R.id.fHome,fragment,"HOME_FRAG");
            getSupportActionBar().setTitle("Home");
            currentFragment = "home";
        }
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FCM", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        Log.d("FCM", "Token is: " + token);
                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        if (grantResults.length > 0) {

        }
    }
    @TargetApi(23)
    public void reqPermissions(){
        requestPermissions(permission,100);
    }

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }


}


