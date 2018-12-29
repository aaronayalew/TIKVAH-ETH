package com.goldengear.tikvah;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

/**
 * Created by Aaron Ayalew on 10/18/2018.
 */

public class FragBusiness extends Fragment {
    TextView tv;
    TextView txtDollar;
    TextView txtEuro;
    TextView txtPound;
    TextView txtYuan;
    TextView txtDirham;
    TextView txtDinar;
    int mLastFirstVisibleItem = 0;
    ImageButton convert;
    public FragBusiness (){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View result = inflater.inflate(R.layout.frag_business,container,false);
        final ListView lv = (ListView) result.findViewById(R.id.lstBus);
        RateGetter rg = new RateGetter();
        rg.execute("Yeah");
        tv = result.findViewById(R.id.txtCurrRates);
        txtDollar = result.findViewById(R.id.txtDolVal);
        txtEuro = result.findViewById(R.id.txtEurVal);
        txtPound = result.findViewById(R.id.txtPndVal);
        txtYuan = result.findViewById(R.id.txtRmbVal);
        txtDirham = result.findViewById(R.id.txtDirVal);
        txtDinar = result.findViewById(R.id.txtDinVal);
        convert = result.findViewById(R.id.btnConvert);
        convert.setEnabled(false);
        Log.d("Aaron", "Found ListView");
        Log.d("Aaron", "Initialized Adapter");
        lv.animate();
        Log.d("Aaron", "Adapter Set, Returning inflated View");
        final SwipeRefreshLayout rootView = result.findViewById(R.id.lnlBus);
        NewsGetter ng = new NewsGetter(getActivity().getApplicationContext(),"business",lv,rootView,rootView,getActivity());
        ng.execute();
        rootView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NewsGetter ng = new NewsGetter(getActivity().getApplicationContext(),"business",lv,rootView,rootView,getActivity());
                ng.execute();
            }
        });

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == 0) Log.i("RateGetter", "scrolling stopped");
                if(view.getId() == lv.getId()) {
                    final int currentFirstVisibleItem = lv.getFirstVisiblePosition();
                    if(currentFirstVisibleItem > mLastFirstVisibleItem) {
                        Log.d("RateGetter", "Scrolling down");
                    } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                        Log.d("RateGetter", "Scrolling Up");
                    }

                    mLastFirstVisibleItem = currentFirstVisibleItem;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        return result;
    }
    private class RateGetter extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                StringBuilder sb = new StringBuilder();
                Log.d("RateGetter", "Starting Execution");
                URL uri = new URL("http://free.currencyconverterapi.com/api/v5/convert?q=USD_ETB,EUR_ETB&compact=y");
                URLConnection conn = uri.openConnection();
                Log.d("RateGetter", "Opened Connection, Preparing to recieve data");
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                Log.d("RateGetter", "Input Stream Initialized, Parsing Data");

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                Log.d("RateGetter", "Result: " + sb.toString());
                String usdeur = sb.toString();
                return  usdeur;
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s != null) {
                try {
                    JSONObject object = new JSONObject(s);
                    JSONObject dollar = object.optJSONObject("USD_ETB");
                    double dolval = dollar.optDouble("val");
                    JSONObject euro = object.optJSONObject("EUR_ETB");
                    double eurval = euro.optDouble("val");
                    Log.d("RateGetter", "JSON parsed, Fixing up decimal values");
                    DecimalFormat df = new DecimalFormat("#.##");
                    df.setRoundingMode(RoundingMode.HALF_UP);
                    String dolvalstr = df.format(dolval);
                    String eurvalstr = df.format(eurval);
                    Log.d("RateGetter", "Done fixing up decimal values, setting the respective TextView's text");

                    RateGetter2 rg2 = new RateGetter2();
                    rg2.execute(dolvalstr,eurvalstr);
                    txtDollar.setText(dolvalstr);
                    txtEuro.setText(eurvalstr);

                } catch (JSONException ex) {

            }
            }

        }


    }
    public class RateGetter2 extends AsyncTask<String,Void,String> {
        String USD,EUR;
        @Override
        protected String doInBackground(String... strings) {
            try {
                USD = strings[0];
                EUR = strings[1];
                StringBuilder sb2 = new StringBuilder();
                URL uri2 = new URL("http://free.currencyconverterapi.com/api/v5/convert?q=GBP_ETB,CNY_ETB&compact=y");
                URLConnection conn2 = uri2.openConnection();
                Log.d("RateGetter", "Opened Connection, Preparing to recieve data");
                BufferedReader reader2 = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
                Log.d("RateGetter", "Input Stream Initialized, Parsing Data");
                String line2 = null;
                while ((line2 = reader2.readLine()) != null) {
                    sb2.append(line2);
                    break;
                }
                Log.d("RateGetter", "Result: " + sb2.toString());
                String gbpcny = sb2.toString();

                return gbpcny;
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s != null) {
                try {
                    DecimalFormat df = new DecimalFormat("#.##");
                    df.setRoundingMode(RoundingMode.HALF_UP);
                    JSONObject object2 = new JSONObject(s);
                    JSONObject pound = object2.optJSONObject("GBP_ETB");
                    double pndval = pound.optDouble("val");
                    JSONObject yuan = object2.optJSONObject("CNY_ETB");
                    double rmbval = yuan.optDouble("val");
                    Log.d("RateGetter", "JSON parsed, Fixing up decimal values");
                    String pndvalstr = df.format(pndval);
                    String rmbvalstr = df.format(rmbval);
                    RateGetter3 rg3 = new RateGetter3();
                    rg3.execute(USD,EUR,pndvalstr,rmbvalstr);
                    Log.d("RateGetter", "Done fixing up decimal values, setting the respective TextView's text");
                    txtPound.setText(pndvalstr);
                    txtYuan.setText(rmbvalstr);
                } catch (JSONException ex) {

                }
            }

        }

    }
    public class RateGetter3 extends AsyncTask<String,Void,String> {
        String USD,PND,EUR,RMB;
        @Override
        protected String doInBackground(String... strings) {
            try {
                USD = strings[0];
                EUR = strings[1];
                PND = strings[2];
                RMB = strings[3];
                StringBuilder sb2 = new StringBuilder();
                URL uri2 = new URL("http://free.currencyconverterapi.com/api/v5/convert?q=AED_ETB,KWD_ETB&compact=y");
                URLConnection conn2 = uri2.openConnection();
                Log.d("RateGetter", "Opened Connection, Preparing to recieve data");
                BufferedReader reader2 = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
                Log.d("RateGetter", "Input Stream Initialized, Parsing Data");
                String line2 = null;
                while ((line2 = reader2.readLine()) != null) {
                    sb2.append(line2);
                    break;
                }
                Log.d("RateGetter", "Result: " + sb2.toString());
                String gbpcny = sb2.toString();

                return gbpcny;
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                try {
                    DecimalFormat df = new DecimalFormat("#.##");
                    df.setRoundingMode(RoundingMode.HALF_UP);
                    JSONObject object2 = new JSONObject(s);
                    JSONObject dirham = object2.optJSONObject("AED_ETB");
                    final double dirval = dirham.optDouble("val");
                    JSONObject dinar = object2.optJSONObject("KWD_ETB");
                    final double dinval = dinar.optDouble("val");
                    Log.d("RateGetter", "JSON parsed, Fixing up decimal values");
                    String dirvalstr = df.format(dirval);
                    String dinvalstr = df.format(dinval);
                    Log.d("RateGetter", "Done fixing up decimal values, setting the respective TextView's text");
                    txtDinar.setText(dinvalstr);
                    txtDirham.setText(dirvalstr);
                    convert.setEnabled(true);
                    convert.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getActivity().getApplicationContext(), Convertor.class);
                            i.putExtra("USD", Double.valueOf(USD));
                            i.putExtra("EUR",Double.valueOf(EUR));
                            i.putExtra("GBP", Double.valueOf(PND));
                            i.putExtra("CNY",Double.valueOf(RMB));
                            i.putExtra("AED", dirval);
                            i.putExtra("KWD",dinval);
                            startActivity(i);

                        }
                    });
                } catch (JSONException ex) {

                }
            }

        }
    }


        @Override
    public void onDestroyView(){
        super.onDestroyView();
    }
}
