package com.samueva.remindme;

import android.os.AsyncTask;

import java.util.List;

enum dbAction {
    INIT_TASK,
    INSERT_TASK,
    DELETE_TASK,
    INFO_TASK,
    CONFIG_CATEGORY
}

public class DbAsyncTask extends AsyncTask<Void, Void, Task> {

    private AppDatabase db;
    private TaskRecyclerAdapter recyclerAdapter;
    private dbAction myDbAction;
    private int taskId;
    private Task task;
    private List<String> categoryList;

    DbAsyncTaskCallback dbAsyncTaskCallback;

    public interface DbAsyncTaskCallback {
        void onInfoCallback(Task task);
    }

    public DbAsyncTask(AppDatabase db, TaskRecyclerAdapter recyclerAdapter, dbAction myDbAction, int taskId, Task task, List<String> categoryList, DbAsyncTaskCallback dbAsyncTaskCallback) {
        this.db = db;
        this.recyclerAdapter = recyclerAdapter;
        this.myDbAction = myDbAction;
        this.taskId = taskId;
        this.task = task;
        this.categoryList = categoryList;
        this.dbAsyncTaskCallback = dbAsyncTaskCallback;
    }

    // INIT_TASK
    public DbAsyncTask(AppDatabase db, TaskRecyclerAdapter recyclerAdapter, dbAction myDbAction) {
        this.db = db;
        this.recyclerAdapter = recyclerAdapter;
        this.myDbAction = myDbAction;
        this.taskId = 0;
        this.task = null;
        this.categoryList = null;
        this.dbAsyncTaskCallback = null;
    }

    // INSERT_TASK
    public DbAsyncTask(AppDatabase db, TaskRecyclerAdapter recyclerAdapter, dbAction myDbAction, Task task) {
        this.db = db;
        this.recyclerAdapter = recyclerAdapter;
        this.myDbAction = myDbAction;
        this.taskId = 0;
        this.task = task;
        this.categoryList = null;
        this.dbAsyncTaskCallback = null;
    }

    // DELETE_TASK
    public DbAsyncTask(AppDatabase db, TaskRecyclerAdapter recyclerAdapter, dbAction myDbAction, int taskId) {
        this.db = db;
        this.recyclerAdapter = recyclerAdapter;
        this.myDbAction = myDbAction;
        this.taskId = taskId;
        this.task = null;
        this.categoryList = null;
        this.dbAsyncTaskCallback = null;
    }

    // INFO_TASK
    public DbAsyncTask(AppDatabase db, TaskRecyclerAdapter recyclerAdapter, dbAction myDbAction, int taskId, DbAsyncTaskCallback dbAsyncTaskCallback) {
        this.db = db;
        this.recyclerAdapter = recyclerAdapter;
        this.myDbAction = myDbAction;
        this.taskId = taskId;
        this.task = null;
        this.categoryList = null;
        this.dbAsyncTaskCallback = dbAsyncTaskCallback;
    }

    // CONFIG_CATEGORY
    public DbAsyncTask(AppDatabase db, dbAction myDbAction, List<String> categoryList) {
        this.db = db;
        this.recyclerAdapter = null;
        this.myDbAction = myDbAction;
        this.taskId = 0;
        this.task = null;
        this.categoryList = categoryList;
        this.dbAsyncTaskCallback = null;
    }

    @Override
    protected Task doInBackground(Void... voids) {
        switch (this.myDbAction) {
            case INIT_TASK:
                recyclerAdapter.refreshData(db.taskDao().getAll());
                return null;
            case INSERT_TASK:
                db.taskDao().insertAll(this.task);
                recyclerAdapter.refreshData((db.taskDao().getAll()));
                return null;
            case DELETE_TASK:
                if(this.task != null) {
                    db.taskDao().delete(this.task);
                } else {
                    db.taskDao().delete(db.taskDao().getById(this.taskId));
                }
                recyclerAdapter.refreshData(db.taskDao().getAll());
                return null;
            case INFO_TASK:
                if (this.task != null) {
                    return this.task;
                } else {
                    return db.taskDao().getById(this.taskId);
                }
            case CONFIG_CATEGORY:
                for (String category : this.categoryList) {
                    db.taskCategoryDao().insertAll(new TaskCategory(category));
                }
                return null;
            default:
                return null;
        }
    }

    @Override
    protected void onPostExecute(Task task) {
        switch (this.myDbAction) {
            case INIT_TASK:
                recyclerAdapter.notifyDataSetChanged();
                break;
            case INSERT_TASK:
                recyclerAdapter.notifyDataSetChanged();
                break;
            case DELETE_TASK:
                recyclerAdapter.notifyDataSetChanged();
                break;
            case INFO_TASK:
                dbAsyncTaskCallback.onInfoCallback(task);
                break;
            default:
                break;
        }

    }
}
