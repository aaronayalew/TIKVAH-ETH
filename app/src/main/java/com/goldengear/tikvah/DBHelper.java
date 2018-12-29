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
        db.execSQL("CREATE TABLE IF NOT EXISTS `articles` (`ID` int(11) NOT NULL,`title` tinytext NOT NULL,`desc` text NOT NULL,`content` mediumtext NOT NULL,`picture` tinytext NOT NULL,`time` tinytext,`category` text NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public boolean insertNews  (String id,String title, String desc, String
            Content, String pic,String time, String category)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID",id);
        contentValues.put("title", title);
        contentValues.put("desc", desc);

        contentValues.put("content", Content);
        contentValues.put("picture", pic);
        contentValues.put("time", time);
        contentValues.put("category",category);
        db.insert("articles", null, contentValues);
        return true;
    }
    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from articles where id="+id+"", null );
        return res;
    }
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db,
                "articles");
        return numRows;
    }
    public JSONObject getNews(int startId, int stopId)
    {
        JSONObject list = new JSONObject();
        JSONArray news = new JSONArray();
        try {

            //hp = new HashMap();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res;
            if (stopId < numberOfRows()) {
                res = db.rawQuery("SELECT * FROM articles ORDER BY ID DESC LIMIT " + startId + " " + stopId, null);
            } else {
                stopId = numberOfRows() - 1;
                res = db.rawQuery("SELECT * FROM articles ORDER BY ID DESC", null);
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
                sinNews.put("content", res.getString(res.getColumnIndex("content")));
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
