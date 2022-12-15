package com.example.news_feed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;

import com.example.news_feed.Adapter.FeedAdapter;
import com.example.news_feed.Common.HTTPDataHandler;
import com.example.news_feed.Model.RSSObject;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    RSSObject rssObject;
    String RSS_link = "https://goiania.go.gov.br/feed";
    SharedPreferences SavedActualFeed;
    String ActualFeed = "";
    private static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        SavedActualFeed = getSharedPreferences("actualFeed", MODE_PRIVATE);
        ActualFeed = SavedActualFeed.getString("actualFeed", "");

        if (!ActualFeed.isEmpty()) {
            RSS_link = ActualFeed;
        }

        toolbar.setTitle(RSS_link);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        loadRSS();
        instance = this;
    }

    public static MainActivity getInstance() {
        return instance;
    }

    public void setActualFeed(String actualFeed) {
        SharedPreferences.Editor editor = SavedActualFeed.edit();
        editor.putString("actualFeed", actualFeed);
        editor.apply();
    }

    private void loadRSS() {
        AsyncTask<String, String, String> loadRSSAsync = new AsyncTask<String, String, String>() {

            final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);

            @Override
            protected void onPreExecute() {
                mDialog.setMessage("Carregando feed...");
                mDialog.show();
            }

            @Override
            protected String doInBackground(String... params) {
                String result;
                HTTPDataHandler http = new HTTPDataHandler();
                result = http.GetHTTPData(params[0]);
                return result;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void onPostExecute(String s) {
                mDialog.dismiss();
                rssObject = new Gson().fromJson(s, RSSObject.class);
                FeedAdapter adapter = new FeedAdapter(rssObject, getBaseContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        };

        String RSS_to_Json_API = "https://api.rss2json.com/v1/api.json?rss_url=";
        //RSS link
        loadRSSAsync.execute(RSS_to_Json_API + RSS_link);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
            loadRSS();
        }
        if (item.getItemId() == R.id.add_item) {
            Intent intent = new Intent(MainActivity.this, FeedsList.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
}