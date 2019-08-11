package com.samueva.remindme;

import android.content.Intent;
import android.support.annotation.Nullable;
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

    // Request codes
    private final int taskInfoActivity_requestCode = 301;

    // Need for update
    private boolean update;

    // AppDatabase
    private AppDatabase db;
    private final DbAsyncTask.DbAsyncTaskListener dbAsyncTaskListener = new DbAsyncTask.DbAsyncTaskListener() {
        @Override
        public void onTaskGetByIdCallback(Task task) {

        }

        @Override
        public void onTaskGetAllByStatusCallback(List<Task> taskList) {
            recyclerAdapter.refreshData(taskList);
            recyclerAdapter.notifyDataSetChanged();
        }

        @Override
        public void onTaskUpdateCallback(long taskId) {
            new DbAsyncTask(db, dbAction.TASK_GETALLBYSTATUS, "Completed", dbAsyncTaskListener).execute();
        }

        @Override
        public void onCategoryGetAllCallback(List<TaskCategory> categoryList) {

        }

        @Override
        public void onCategoryUpdateCallback() {

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

        // Need for update
        this.update = false;

        // Database
        this.db = AppDatabase.getInstance();

        // RecyclerView
        this.recyclerView = (RecyclerView) findViewById(R.id.history_recycler_view);
        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerAdapter = new HistoryRecyclerAdapter(new ArrayList<Task>(), new HistoryRecyclerAdapter.HistoryCardClickListener() {
            @Override
            public void onTaskCardClick(Task task) {
                startTaskInfoActivity(task);
            }

            @Override
            public void onHistoryCardRestore(long taskId) {
                update = true;
                new DbAsyncTask(db, dbAction.TASK_UPDATE_UNCOMPLETE, taskId, dbAsyncTaskListener).execute();
            }

            @Override
            public void onHistoryCardDelete(long taskId, String taskCategory) {
                new DbAsyncTask(db, dbAction.TASK_DELETE_BYID, taskId, dbAsyncTaskListener).execute();
            }
        });
        this.recyclerView.setAdapter(recyclerAdapter);

        new DbAsyncTask(this.db, dbAction.TASK_GETALL_HISTORY, this.dbAsyncTaskListener).execute();
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
            new DbAsyncTask(db, dbAction.TASK_DELETE_HISTORY, dbAsyncTaskListener).execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case taskInfoActivity_requestCode:
                    if (data.getBooleanExtra("update", false)) {
                        this.update = true;
                        new DbAsyncTask(this.db, dbAction.TASK_GETALL_HISTORY, this.dbAsyncTaskListener).execute();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void startTaskInfoActivity(Task task) {
        Intent intent = new Intent(this, TaskInfoActivity.class);
        intent.putExtra("task", (Task) task);
        startActivityForResult(intent, this.taskInfoActivity_requestCode);
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("update", this.update);
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
