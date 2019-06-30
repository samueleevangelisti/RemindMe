package com.samueva.remindme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    // TODO: 7/1/19 STRINGA DI DEBUG
    private static final String TAG = "ReMe_HistoryActivity";

    // AppDatabase
    private AppDatabase db;

    // RecyclerView
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    HistoryRecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Database
        this.db = AppDatabase.buildInstance(getApplicationContext(), AppDatabase.class, "database");

        // RecyclerView
        this.recyclerView = (RecyclerView) findViewById(R.id.history_recycler_view);
        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerAdapter = new HistoryRecyclerAdapter(new ArrayList<Task>(), new HistoryRecyclerAdapter.HistoryCardClickListener() {
            @Override
            public void onTaskCardClick(int taskId) {
                startTaskInfoActivity(taskId);
            }

            @Override
            public void onHistoryCardDelete(int taskId) {
                new DbAsyncTask(db, recyclerAdapter, dbAction.DELETE_TASK, taskId).execute();
            }
        });
        this.recyclerView.setAdapter(recyclerAdapter);

        new DbAsyncTask(this.db, this.recyclerAdapter, dbAction.GETDONE_TASK).execute();
    }

    private void startTaskInfoActivity(int taskId) {
        Intent intent = new Intent(this, TaskInfoActivity.class);
        intent.putExtra("taskId", (int) taskId);
        startActivity(intent);
    }
}
