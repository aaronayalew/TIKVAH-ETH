package com.goldengear.tikvah;

import android.util.Log;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Game {
    //TODO: Finish the model
    public int id;
    private int league_id;
    private int competition_id;
    private int home_id;
    private int away_id;
    private String date;
    private String time;
    private String round;
    private String home_name;
    private String away_name;
    private String location;
    private String score;
    private String ht_score;
    private String events;
    private String status;
    private String match_time;
    private boolean live;

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

    public int getCompetition_id() {
        return competition_id;
    }

    public void setCompetition_id(int competition_id) {
        this.competition_id = competition_id;
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
            Date parsed = sdf.parse(time);
            SimpleDateFormat sdfinal = new SimpleDateFormat("MM-dd");
            sdfinal.setTimeZone(TimeZone.getTimeZone("Africa/Addis Ababa"));
            return sdfinal.format(parsed);
        } catch(Exception ex) {
            return null;
        }
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        if (!this.isLive()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date parsed = sdf.parse(time);
                SimpleDateFormat sdfinal = new SimpleDateFormat("HH:mm");
                sdfinal.setTimeZone(TimeZone.getTimeZone("Africa/Addis_Ababa"));
                return sdfinal.format(parsed);
            } catch (Exception ex) {
                Log.d("GameTime", "error: " + ex.toString());
                return null;

            }
        } else {
            Log.d("GameTime", "Time = " + time);
            return time;
        }
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getHome_name() {
        return home_name;
    }

    public void setHome_name(String home_name) {
        this.home_name = home_name;
    }

    public String getAway_name() {
        return away_name;
    }

    public void setAway_name(String away_name) {
        this.away_name = away_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }



}
