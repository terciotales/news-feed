package com.example.news_feed;

import android.app.Application;

public class Globals extends Application {
    private String actualFeed;

    public String getActualFeed() {
        return actualFeed;
    }

    public void setActualFeed(String actualFeed) {
        this.actualFeed = actualFeed;
    }
}
