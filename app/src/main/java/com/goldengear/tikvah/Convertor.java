package com.goldengear.tikvah;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Convertor extends AppCompatActivity {
    String[] currencies = {"Birr (Ethiopia)", "Dollar(US)", "Euro","Pound(UK)", "Yuan(China)", "Dirham (UAE)", "Dinar (Kuwait)" };
    Bundle inExtras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convertor);
        inExtras = getIntent().getExtras();
        final EditText from = (EditText) findViewById(R.id.edtFrom);
        final EditText to =(EditText) findViewById(R.id.edtTo);
        to.setEnabled(false);
        final Spinner source = (Spinner) findViewById(R.id.spnFrom);
        final Spinner target = (Spinner) findViewById(R.id.spnTo);
        //TODO: Fix up the spinner item layouts!
        ArrayAdapter srcAdapter = new ArrayAdapter(this,R.layout.spinner_item_curr, currencies);
        source.setAdapter(srcAdapter);
        ArrayAdapter tarAdapter = new ArrayAdapter(this,R.layout.spinner_item_curr, currencies);
        target.setAdapter(tarAdapter);
        target.setSelection(1);
        from.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                return false;
            }
        });
        from.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    to.setText(calcCurr(Double.valueOf(from.getText().toString()), source.getSelectedItem().toString(), target.getSelectedItem().toString()));
                } catch (NumberFormatException ex) {
                    to.setText("0.00");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
     private String getCodeFromName(String code){
        switch (code) {
            case "Birr (Ethiopia)" :
                return "ETB";
            case "Dollar(US)" :
                return "USD";
            case "Euro" :
                return "EUR";
            case "Yuan(China)" :
                return "CNY";
            case "Dirham (UAE)" :
                return "AED";
            case "Pound(UK)" :
                return "GBP";
            case "Dinar (Kuwait)" :
                return "KWD";
        }
        return null;
     }
     private String calcCurr(double value, String from, String to) {
         Log.d("Converter","Value: " + String.valueOf(value) + " From: " + from + " To: " + to );
         if (!getCodeFromName(from).contains(getCodeFromName(to))) {
             if (getCodeFromName(from).contains("ETB") || getCodeFromName(to).contains("ETB")) {
                 Log.d("Converter", "One of them is birr");
                 if (!getCodeFromName(from).contains("ETB")) {
                     Log.d("Converter", "Converting from " + String.valueOf(value) + getCodeFromName(from) + " to ETB");
                     double rate = inExtras.getDouble(getCodeFromName(from));
                     Log.d("Converter", "Rate is " + String.valueOf(rate));
                     double res = rate * value;
                     Log.d("Converter", "Result is " + String.valueOf(res));
                     DecimalFormat df = new DecimalFormat("#.##");
                     df.setRoundingMode(RoundingMode.HALF_UP);
                     return String.valueOf(df.format(res));
                 } else {
                     Log.d("Converter", "Converting from " + String.valueOf(value) +  "ETB to" + getCodeFromName(to));
                     double rate = inExtras.getDouble(getCodeFromName(to));
                     Log.d("Converter", "Rate is " + String.valueOf(rate));
                     double res = value / rate;
                     Log.d("Converter", "Result is " + String.valueOf(res));
                     DecimalFormat df = new DecimalFormat("#.##");
                     df.setRoundingMode(RoundingMode.HALF_UP);
                     return String.valueOf(df.format(res));
                 }
             } else {
                 Log.d("Converter", "Converting from " + String.valueOf(value) + getCodeFromName(from) + " to " + getCodeFromName(to));
                 String strFrom = getCodeFromName(from);
                 String strTo = getCodeFromName(to);
                 double dFrom = inExtras.getDouble(strFrom);
                 Log.d("Converter", "From Rate is " + String.valueOf(dFrom));
                 double dTo = inExtras.getDouble(strTo);
                 Log.d("Converter", "Rate is " + String.valueOf(dTo));
                 double etbfrom = dFrom * value;
                 Log.d("Converter", "ETB Res " + String.valueOf(etbfrom));
                 double finfrom = etbfrom / dTo;
                 DecimalFormat df = new DecimalFormat("#.##");
                 df.setRoundingMode(RoundingMode.HALF_UP);
                 return String.valueOf(df.format(finfrom));
             }
         } else {
             return String.valueOf(value);
         }
     }

}
