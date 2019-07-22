package com.samueva.remindme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    // TODO: 7/1/19 STRINGA DI DEBUG
    private static final String TAG = "ReMe_HistoryActivity";

    // AppDatabase
    private AppDatabase db;
    private final DbAsyncTask.DbAsyncTaskListener dbAsyncTaskListener = new DbAsyncTask.DbAsyncTaskListener() {
        @Override
        public void onTaskGetAllByStatusCallback(List<Task> taskList) {
            recyclerAdapter.refreshData(taskList);
            recyclerAdapter.notifyDataSetChanged();
        }

        @Override
        public void onTaskUpdateCallback() {
            new DbAsyncTask(db, dbAction.TASK_GETALLBYSTATUS, "Completed", dbAsyncTaskListener).execute();
        }

        @Override
        public void onInfoTaskCallback(Task task) {

        }

        @Override
        public void onGetAllCategoryCallback(List<TaskCategory> categoryList) {

        }

        @Override
        public void onInsertCategoryCallback() {

        }

        @Override
        public void onDeleteCategoryCallback() {

        }
    };

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
            public void onHistoryCardRestore(int taskId) {
                // TODO: 7/23/19 La main activity non viene notificata del cambiamento quindi è necessario gestire tutto a fragment con un dirty bit
                new DbAsyncTask(db, dbAction.TASK_UPDATE_UNCOMPLETE, taskId, dbAsyncTaskListener).execute();
            }

            @Override
            public void onHistoryCardDelete(int taskId) {
                new DbAsyncTask(db, dbAction.TASK_DELETE, taskId, dbAsyncTaskListener).execute();
            }
        });
        this.recyclerView.setAdapter(recyclerAdapter);

        new DbAsyncTask(this.db, dbAction.TASK_GETALLBYSTATUS, "Completed", this.dbAsyncTaskListener).execute();
    }

    private void startTaskInfoActivity(int taskId) {
        Intent intent = new Intent(this, TaskInfoActivity.class);
        intent.putExtra("taskId", (int) taskId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.history_optionmenu_deleteforever) {
            new DbAsyncTask(db, recyclerAdapter, dbAction.DELETEALL_HYSTORY).execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
