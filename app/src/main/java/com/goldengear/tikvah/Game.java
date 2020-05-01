package com.goldengear.tikvah;

import android.util.Log;

import java.io.Serializable;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Game implements Serializable {
    //TODO: Finish the model
    public int id;
    private int league_id;
    private int home_id;
    private int away_id;
    private String date;
    private String round;
    private Team home;
    private Team away;
    private String location;
    private String referee;
    private String score;
    private String ht_score;
    private String events;
    private String status;
    private String match_time;
    private boolean live;
    public class Team{
        public Team() {

        }
    }
    public String getMatch_time() {
        return match_time;
    }

    public void setMatch_time(String match_time) {
        this.match_time = match_time;
    }

    public Game(int id) {
        this.id = id;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getHt_score() {
        return ht_score;
    }

    public void setHt_score(String ht_score) {
        this.ht_score = ht_score;
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public int getLeague_id() {
        return league_id;
    }

    public void setLeague_id(int league_id) {
        this.league_id = league_id;
    }



    public int getHome_id() {
        return home_id;
    }

    public void setHome_id(int home_id) {
        this.home_id = home_id;
    }

    public int getAway_id() {
        return away_id;
    }

    public void setAway_id(int away_id) {
        this.away_id = away_id;
    }

    public String getDate() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date parsed = sdf.parse(date);
            SimpleDateFormat sdfinal = new SimpleDateFormat("MM-dd");
            sdfinal.setTimeZone(TimeZone.getTimeZone("Africa/Addis Ababa"));
            return sdfinal.format(parsed);
        } catch(Exception ex) {
            Log.d("ThreadGames", "DATE EXCEPTION: " + ex.toString());
            return null;
        }
    }

    public void setDate(String date) {
        this.date = date;
    }



    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }



}
