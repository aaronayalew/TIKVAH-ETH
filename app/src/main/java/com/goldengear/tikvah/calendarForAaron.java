package com.goldengear.tikvah;

/**
 * Created by Fitsum Ayalew on 11/23/2018.
 */

public class calendarForAaron{
    private final static double CONST_EC = 1723856;
    private final static double CONST_GC = 1721118.5;
    private final static String[] MONTH_LIST = {"መስከረም","ጥቅምት","ህዳር","ታህሳስ","ጥር","የካቲት","መጋቢት","ሚያዚያ","ግንቦት","ሰኔ","ሃምሌ","ነሐሴ","ጳጉሜ"};

    private calendarForAaron(){}

    public static int[] convertToEC (int day, int month, int year){

        double x;
        double R;
        double N;
        double year_ET;
        double month_ET;
        double day_ET;

        if (month < 3) {
            year = year - 1;
            month = month + 12;
        }

        x = day + Math.floor(30.6 * month - 91.4) + 365 * year + Math.floor(year/4) - Math.floor(year/100) + Math.floor(year/400) + CONST_GC + 0.5;
        R = ((x - CONST_EC) % 1461);
        N = (R % 365) + 365 * Math.floor(R/1460);
        year_ET = 4 * Math.floor((x- CONST_EC)/1461) + Math.floor(R/365) - Math.floor(R/1460);
        month_ET = Math.floor(N/30) + 1;
        day_ET = (N % 30) + 1;
        int[] returnArray = new int[3];
        returnArray[0] = (int) day_ET;
        returnArray[1] = (int) month_ET;
        returnArray[2] = (int) year_ET;
        return  returnArray;

    }

    public static String convertToECString (int day, int month, int year) {
        int[] convertedDate = convertToEC(day,month,year);
        String monthString = MONTH_LIST[convertedDate[1] - 1] + "";
        return String.format(monthString + " %2d, %4d",convertedDate[0], convertedDate[2]);

    }
//    public static int[] convertToGC (int day, int month, int year){
//      TO BE DONE
//    }

}
