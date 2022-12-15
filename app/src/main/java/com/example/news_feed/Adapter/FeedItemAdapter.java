package com.example.news_feed.Adapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.news_feed.Common.FeedDAO;
import com.example.news_feed.FeedsList;
import com.example.news_feed.MainActivity;
import com.example.news_feed.Model.FeedListItem;
import com.example.news_feed.R;
import com.google.android.material.snackbar.Snackbar;
import com.example.news_feed.Interface.ItemClickListener;

import java.util.List;
import java.util.Objects;

public class FeedItemAdapter extends RecyclerView.Adapter<FeedItemHolder> {

    private final List<FeedListItem> feeds;

    public FeedItemAdapter(List<FeedListItem> feeds) {
        this.feeds = feeds;
    }

    @Override
    public FeedItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FeedItemHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feed_item, parent, false));
    }

    @Override
    public void onBindViewHolder(FeedItemHolder holder, int position) {
        holder.urlTitle.setText(feeds.get(position).getUrl());

        FeedListItem feed = feeds.get(position);

        if (Objects.equals(feed.getUrl(), "https://goiania.go.gov.br/feed") || Objects.equals(feed.getUrl(), "https://g1.globo.com/rss/g1")) {
            holder.removeButton.setVisibility(View.INVISIBLE);
        }

        holder.removeButton.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = v;
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), R.style.AlertDialogTheme);
                builder.setTitle("Confirmação")
                        .setMessage("Tem certeza que deseja excluir este Feed?")
                        .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                FeedDAO dao = new FeedDAO(view.getContext());
                                boolean sucesso = dao.excluir(feed.getId());
                                if(sucesso) {
                                    removerFeed(feed);
                                    Snackbar.make(view, "Excluído!", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }else{
                                    Snackbar.make(view, "Erro ao excluir o feed!", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .create()
                        .show();
            }
        });

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (!isLongClick) {
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    MainActivity.getInstance().setActualFeed(feeds.get(position).getUrl());
                    view.getContext().startActivity(intent);
                }
            }
        });
    }

    public void adicionarFeed(FeedListItem feed){
        feeds.add(feed);
        notifyItemInserted(getItemCount());
    }

    public void removerFeed(FeedListItem feed){
        int position = feeds.indexOf(feed);
        feeds.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return feeds != null ? feeds.size() : 0;
    }
}

class FeedItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public TextView urlTitle;
    public ImageView removeButton;
    private ItemClickListener itemClickListener;

    public FeedItemHolder(View itemView) {
        super(itemView);
        
        urlTitle = (TextView) itemView.findViewById(R.id.titleUrl);
        removeButton = (ImageView) itemView.findViewById(R.id.removeButton);

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
