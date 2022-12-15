package com.example.news_feed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.news_feed.Interface.ItemClickListener;
import com.example.news_feed.MainActivity;
import com.example.news_feed.Model.RSSObject;
import com.example.news_feed.R;


class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public TextView txtTitle, txtPubDate;
    private ItemClickListener itemClickListener;

    public FeedViewHolder(View itemView) {
        super(itemView);

        txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
        txtPubDate = (TextView) itemView.findViewById(R.id.txtPubDate);

        //Set Event
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v, getAdapterPosition(), false);

    }

    @Override
    public boolean onLongClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), true);
        return true;
    }
}

public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder> {

    private final RSSObject rssObject;
    private final Context mContext;
    private final LayoutInflater inflater;

    public FeedAdapter(RSSObject rssObject, Context mContext) {
        this.rssObject = rssObject;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.row, parent, false);
        return new FeedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {

        holder.txtTitle.setText(rssObject.getItems().get(position).getTitle());
        holder.txtPubDate.setText(rssObject.getItems().get(position).getPubDate());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (!isLongClick) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rssObject.getItems().get(position).getLink()));
                    browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(browserIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return rssObject.items.size();
    }
}
