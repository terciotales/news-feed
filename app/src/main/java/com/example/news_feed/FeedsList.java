package com.example.news_feed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.news_feed.Adapter.FeedItemAdapter;
import com.example.news_feed.Common.FeedDAO;
import com.example.news_feed.Model.FeedListItem;
import com.google.android.material.snackbar.Snackbar;

public class FeedsList extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button addButton;
    EditText url;
    PopupWindow popupWindow;
    FeedItemAdapter adapter;
    public Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Feeds");

        setSupportActionBar(toolbar);

        configurarRecycler();
    }

    private void configurarRecycler() {
        // Configurando o gerenciador de layout para ser uma lista.
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewFeeds);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        // Adiciona o adapter que irá anexar os objetos à lista.
        FeedDAO dao = new FeedDAO(this);
        adapter = new FeedItemAdapter(dao.retornarTodos());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.menu_refresh).setVisible(false);
        menu.findItem(R.id.back_to_main).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.back_to_main) {
            intent = new Intent(FeedsList.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        if (item.getItemId() == R.id.add_item) {
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup, null);

            // create the popup window
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            popupWindow = new PopupWindow(popupView, width, height, true);

            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window tolken
            popupWindow.showAtLocation(this.findViewById(android.R.id.content), Gravity.CENTER, 0, -500);

            addButton = popupView.findViewById(R.id.add_feed_button);
            url = popupView.findViewById(R.id.url);

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FeedDAO dao = new FeedDAO(getBaseContext());
                    if (url.getText().toString().isEmpty()) {
                        Toast.makeText(FeedsList.this, "A url não pode ser vazia!", Toast.LENGTH_SHORT).show();
                    } else {
                        boolean sucesso = dao.salvar(url.getText().toString());
                        if (sucesso) {
                            FeedListItem cliente = dao.retornarUltimo();
                            adapter.adicionarFeed(cliente);
                            popupWindow.dismiss();
                            Toast.makeText(FeedsList.this, "Feed inserido!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FeedsList.this, "Erro ao salvar feed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            // dismiss the popup window when touched
            popupView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popupWindow.dismiss();
                    return true;
                }
            });
        }
        return true;
    }
}