package com.example.news_feed.Common;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.news_feed.Model.Feed;
import com.example.news_feed.Model.FeedListItem;

import java.util.ArrayList;
import java.util.List;

class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Crud.db";
    private static final int DATABASE_VERSION = 1;
    private final String CREATE_TABLE = "CREATE TABLE Feeds (ID INTEGER PRIMARY KEY AUTOINCREMENT, Url VARCHAR(2083) NOT NULL);";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

class DbGateway {
    private static DbGateway gw;
    private SQLiteDatabase db;

    private DbGateway(Context ctx){
        DbHelper helper = new DbHelper(ctx);
        db = helper.getWritableDatabase();
    }

    public static DbGateway getInstance(Context ctx){
        if(gw == null)
            gw = new DbGateway(ctx);
        return gw;
    }

    public SQLiteDatabase getDatabase(){
        return this.db;
    }
}

public class FeedDAO {

    private final String TABLE_FEEDS = "Feeds";
    private DbGateway gw;

    public FeedDAO(Context ctx){
        gw = DbGateway.getInstance(ctx);
    }

    public boolean salvar(String url){
        ContentValues cv = new ContentValues();
        cv.put("Url", url);
        return gw.getDatabase().insert(TABLE_FEEDS, null, cv) > 0;
    }

    public List<FeedListItem> retornarTodos(){
        List<FeedListItem> feeds = new ArrayList<>();

        feeds.add(new FeedListItem(0, "https://goiania.go.gov.br/feed"));
        feeds.add(new FeedListItem(1, "https://g1.globo.com/rss/g1"));

        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM Feeds", null);
        while(cursor.moveToNext()){
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("ID"));
            @SuppressLint("Range") String url = cursor.getString(cursor.getColumnIndex("Url"));
            feeds.add(new FeedListItem(id, url));
        }
        cursor.close();
        return feeds;
    }

    public FeedListItem retornarUltimo(){
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM Feeds ORDER BY ID DESC", null);
        if(cursor.moveToFirst()){
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("ID"));
            @SuppressLint("Range") String url = cursor.getString(cursor.getColumnIndex("Url"));
            cursor.close();
            return new FeedListItem(id, url);
        }

        return null;
    }

    public boolean excluir(int id){
        return gw.getDatabase().delete(TABLE_FEEDS, "ID=?", new String[]{ id + "" }) > 0;
    }
}