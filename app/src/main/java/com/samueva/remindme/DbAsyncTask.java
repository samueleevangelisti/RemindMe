package com.samueva.remindme;

import android.os.AsyncTask;

enum dbAction {
    INIT,
    INSERT,
    DELETE,
    INFO
}

public class DbAsyncTask extends AsyncTask<Void, Void, Task> {

    private AppDatabase db;
    private TaskRecyclerAdapter recyclerAdapter;
    private dbAction myDbAction;
    private int taskId;
    private Task task;

    public DbAsyncTask(AppDatabase db, TaskRecyclerAdapter recyclerAdapter, dbAction myDbAction, int taskId, Task task) {
        this.db = db;
        this.recyclerAdapter = recyclerAdapter;
        this.myDbAction = myDbAction;
        this.taskId = taskId;
        this.task = task;
    }

    public DbAsyncTask(AppDatabase db, TaskRecyclerAdapter recyclerAdapter, dbAction myDbAction) {
        this.db = db;
        this.recyclerAdapter = recyclerAdapter;
        this.myDbAction = myDbAction;
        this.taskId = 0;
        this.task = null;
    }

    public DbAsyncTask(AppDatabase db, TaskRecyclerAdapter recyclerAdapter, dbAction myDbAction, Task task) {
        this.db = db;
        this.recyclerAdapter = recyclerAdapter;
        this.myDbAction = myDbAction;
        this.taskId = 0;
        this.task = task;
    }

    public DbAsyncTask(AppDatabase db, TaskRecyclerAdapter recyclerAdapter, dbAction myDbAction, int taskId) {
        this.db = db;
        this.recyclerAdapter = recyclerAdapter;
        this.myDbAction = myDbAction;
        this.taskId = taskId;
        this.task = null;
    }

    @Override
    protected Task doInBackground(Void... voids) {
        switch (this.myDbAction) {
            case INIT:
                recyclerAdapter.refreshData(db.taskDao().getAll());
                return null;
            case INSERT:
                db.taskDao().insertAll(this.task);
                recyclerAdapter.refreshData((db.taskDao().getAll()));
                return null;
            case DELETE:
                if(this.task != null) {
                    db.taskDao().delete(this.task);
                } else {
                    db.taskDao().delete(db.taskDao().getById(this.taskId));
                }
                recyclerAdapter.refreshData(db.taskDao().getAll());
                return null;
            case INFO:
                if (this.task != null) {
                    return this.task;
                } else {
                    return db.taskDao().getById(this.taskId);
                }
            default:
                return null;
        }
    }

    @Override
    protected void onPostExecute(Task task) {
        switch (this.myDbAction) {
            case INIT:
                recyclerAdapter.notifyDataSetChanged();
                break;
            case INSERT:
                recyclerAdapter.notifyDataSetChanged();
                break;
            case DELETE:
                recyclerAdapter.notifyDataSetChanged();
                break;
            case INFO:
                // TODO: 6/12/19 Gestione dell'activity per chiedere informazioni
                //startTaskInfoActivity(task);
                break;
            default:
                break;
        }

    }
}
