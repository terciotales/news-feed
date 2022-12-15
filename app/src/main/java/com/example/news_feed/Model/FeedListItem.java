package com.example.news_feed.Model;

public class FeedListItem  {
    private final int id;
    private final String url;

    public FeedListItem(int id, String url){
        this.id = id;
        this.url = url;
    }

    public int getId(){ return this.id; }
    public String getUrl(){ return this.url; }

    @Override
    public boolean equals(Object o){
        return this.id == ((FeedListItem)o).id;
    }

    @Override
    public int hashCode(){
        return this.id;
    }
}
