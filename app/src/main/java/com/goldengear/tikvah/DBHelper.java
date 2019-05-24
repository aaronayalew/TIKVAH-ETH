package com.goldengear.tikvah;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron Ayalew on 10/30/2018.
 */

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name) {
        super(context,name,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if(db.getPath().contains("/data/data/com.goldengear.tikvah/databases/newsDB")) {
            db.execSQL("CREATE TABLE IF NOT EXISTS `articles` (`ID` int(11) NOT NULL,`title` tinytext NOT NULL,`desc` mediumtext NOT NULL,`picture` tinytext NOT NULL,`time` tinytext,`category` text NOT NULL)");
        } else {
            db.execSQL("CREATE TABLE IF NOT EXISTS `memes` (`ID` int(11) NOT NULL, `image` tinytext NOT NULL,`date` tinytext NOT NULL,`provider_id` text NOT NULL,`provider_name` tinytext NOT NULL,`provider_image` tinytext NOT NULL,`text` text NOT NULL)");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public boolean insertNews  (String[] id,String[] title, String[] desc, String[] pic,String[] time, String[] category)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS `articles` (`ID` int(11) NOT NULL,`title` tinytext NOT NULL,`desc` mediumtext NOT NULL,`picture` tinytext NOT NULL,`time` tinytext,`category` text NOT NULL)");
        db.execSQL("DELETE FROM `articles` WHERE `articles`.`category` LIKE '" + category + "'");
        int l = id.length;
        for(int i = 0; i < l; i++) {

            ContentValues contentValues = new ContentValues();
            contentValues.put("ID", id[i]);
            contentValues.put("title", title[i]);
            contentValues.put("desc", desc[i]);
            contentValues.put("picture", pic[i]);
            contentValues.put("time", time[i]);
            contentValues.put("category", category[i]);
            db.insertOrThrow("articles", null, contentValues);
        }
        Log.d("DBHelper", "Inserted data to DB: " + db.getPath());

        return true;
    }
    public boolean insertMeme  (String[] id,String[] image, String[] date, String[] provider_id,String[] provider_image, String[] provider_name, String[] text)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS memes");
        db.execSQL("CREATE TABLE IF NOT EXISTS `memes` (`ID` int(11) NOT NULL, `image` tinytext NOT NULL,`date` tinytext NOT NULL,`provider_id` text NOT NULL,`provider_name` tinytext NOT NULL,`provider_image` tinytext NOT NULL,`text` text NOT NULL)");
        int l = id.length;
        for(int i = 0; i < l; i++) {

            ContentValues contentValues = new ContentValues();
            contentValues.put("ID", id[i]);
            contentValues.put("image", image[i]);
            contentValues.put("date", date[i]);
            contentValues.put("provider_id", provider_id[i]);
            contentValues.put("provider_name", provider_name[i]);
            contentValues.put("provider_image", provider_image[i]);
            contentValues.put("text",text[i]);
            db.insertOrThrow("memes", null, contentValues);
        }
        Log.d("DBHelper", "Inserted data to DB: " + db.getPath());

        return true;
    }
    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from articles where id="+id+"", null );
        return res;
    }
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows;
        try {
            numRows = (int) DatabaseUtils.queryNumEntries(db,
                    "articles");
        } catch (android.database.sqlite.SQLiteException ex) {
            numRows = 0;
        }
        return numRows;
    }
    public JSONObject getMeme(int startId, int stopId)
    {
        JSONObject list = new JSONObject();
        JSONArray news = new JSONArray();
        try {

            //hp = new HashMap();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res;
            if (stopId < numberOfRows()) {
                res = db.rawQuery("SELECT * FROM 'memes' ORDER BY ID DESC LIMIT " + stopId, null);
            } else {
                stopId = numberOfRows() - 1;
                res = db.rawQuery("SELECT * FROM 'memes' ORDER BY ID DESC", null);
            }
            res.moveToFirst();
            int i = 0;
            Log.d("DBHelper", "Cursor Moved to First");
            Log.d("DBHelper", "Result: " + res.getString(1));
            while (res.isAfterLast() == false) {
                Log.d("DBHelper","Got into while loop");
                JSONObject sinNews = new JSONObject();
                sinNews.put("ID", res.getString(res.getColumnIndex("ID")));
                sinNews.put("image", res.getString(res.getColumnIndex("image")));
                sinNews.put("date", res.getString(res.getColumnIndex("date")));
                sinNews.put("provider_id", res.getString(res.getColumnIndex("provider_id")));
                sinNews.put("provider_image", res.getString(res.getColumnIndex("provider_image")));
                sinNews.put("provider_name", res.getString(res.getColumnIndex("provider_name")));
                sinNews.put("text",res.getString(res.getColumnIndex("text")));
                Log.d("DBHelper", "No error here");
                news.put(i,sinNews);
                i++;
                res.moveToNext();
            }
            list.put("android",news);
        } catch (Exception exe) {
            Log.e("DBHelper", "Error: " + exe.getMessage());
        }
        return list;
    }
    public JSONObject getNews(String cat, int startId, int stopId)
    {
        JSONObject list = new JSONObject();
        JSONArray news = new JSONArray();
        try {

            //hp = new HashMap();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res;
            if (stopId < numberOfRows()) {
                res = db.rawQuery("SELECT * FROM articles WHERE category LIKE '" + cat + "' ORDER BY ID DESC LIMIT " + stopId, null);
            } else {
                stopId = numberOfRows() - 1;
                res = db.rawQuery("SELECT * FROM articles WHERE category LIKE '" + cat + "' ORDER BY ID DESC", null);
            }
            res.moveToFirst();
            int i = 0;
            Log.d("DBHelper", "Cursor Moved to First");
            Log.d("DBHelper", "Result: " + res.getString(1));
            while (res.isAfterLast() == false) {
                Log.d("DBHelper","Got into while loop");
                JSONObject sinNews = new JSONObject();
                sinNews.put("ID", res.getString(res.getColumnIndex("ID")));
                sinNews.put("title", res.getString(res.getColumnIndex("title")));
                sinNews.put("desc", res.getString(res.getColumnIndex("desc")));
/*
                sinNews.put("content", res.getString(res.getColumnIndex("content")));
*/
                sinNews.put("picture", res.getString(res.getColumnIndex("picture")));
                sinNews.put("time", res.getString(res.getColumnIndex("time")));
                sinNews.put("category", res.getString(res.getColumnIndex("category")));
                Log.d("DBHelper", "No error here");
                news.put(i,sinNews);
                i++;
                res.moveToNext();
            }
            list.put("android",news);
        } catch (Exception exe) {
            Log.e("DBHelper", "Error: " + exe.getMessage());
        }
        return list;
    }
}
