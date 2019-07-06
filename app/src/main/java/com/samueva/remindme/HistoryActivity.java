package com.samueva.remindme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    // TODO: 7/1/19 STRINGA DI DEBUG
    private static final String TAG = "ReMe_HistoryActivity";

    // AppDatabase
    private AppDatabase db;

    // RecyclerView
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    HistoryRecyclerAdapter recyclerAdapter;

    // FloatingActionButton
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Database
        this.db = AppDatabase.buildInstance(getApplicationContext(), AppDatabase.class, "database");

        // FloatingActionButton
        this.fab = (FloatingActionButton) findViewById(R.id.fab2);
        this.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DbAsyncTask(db, recyclerAdapter, dbAction.DELETEALL_HYSTORY).execute();
            }
        });

        // RecyclerView
        this.recyclerView = (RecyclerView) findViewById(R.id.history_recycler_view);
        this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }
        });
        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerAdapter = new HistoryRecyclerAdapter(new ArrayList<Task>(), new HistoryRecyclerAdapter.HistoryCardClickListener() {
            @Override
            public void onTaskCardClick(int taskId) {
                startTaskInfoActivity(taskId);
            }

            @Override
            public void onHistoryCardDelete(int taskId) {
                new DbAsyncTask(db, recyclerAdapter, dbAction.DELETE_HISTORY, taskId).execute();
            }
        });
        this.recyclerView.setAdapter(recyclerAdapter);

        new DbAsyncTask(this.db, this.recyclerAdapter, dbAction.GETCOMPLETED_HISTORY).execute();
    }

    private void startTaskInfoActivity(int taskId) {
        Intent intent = new Intent(this, TaskInfoActivity.class);
        intent.putExtra("taskId", (int) taskId);
        startActivity(intent);
    }
}
