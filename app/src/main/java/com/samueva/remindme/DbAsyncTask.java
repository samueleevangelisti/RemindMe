package com.samueva.remindme;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

enum dbAction {
    GETALL_TASK,
    GETPENDING_TASK,
    INSERT_TASK,
    DELETE_TASK,
    UPDATESTATUS_TASK,
    INFO_TASK,
    INIT_CATEGORY,
    GETALL_CATEGORY
}

public class DbAsyncTask extends AsyncTask<Void, Void, Task> {

    // TODO: 6/23/19 STRINGA DI DEBUG
    private static final String TAG = "ReMe_DbAsyncTask";

    private AppDatabase db;
    private TaskRecyclerAdapter recyclerAdapter;
    private dbAction myDbAction;
    private int taskId;
    private Task task;
    private String taskStatus;
    private List<TaskCategory> categoryList;

    DbAsyncTaskCallback dbAsyncTaskCallback;

    public interface DbAsyncTaskCallback {
        void onInsertTaskCallback();
        void onInfoTaskCallback(Task task);
        void onGetAllCategoryCallback(List<TaskCategory> categoryList);
    }

    public DbAsyncTask(AppDatabase db, TaskRecyclerAdapter recyclerAdapter, dbAction myDbAction, int taskId, Task task, String taskStatus, List<TaskCategory> categoryList, DbAsyncTaskCallback dbAsyncTaskCallback) {
        this.db = db;
        this.recyclerAdapter = recyclerAdapter;
        this.myDbAction = myDbAction;
        this.taskId = taskId;
        this.task = task;
        this.taskStatus = taskStatus;
        this.categoryList = categoryList;
        this.dbAsyncTaskCallback = dbAsyncTaskCallback;
    }

    // GETALL_TASK, GETPENDING_TASK
    public DbAsyncTask(AppDatabase db, TaskRecyclerAdapter recyclerAdapter, dbAction myDbAction) {
        this.db = db;
        this.recyclerAdapter = recyclerAdapter;
        this.myDbAction = myDbAction;
        this.taskId = 0;
        this.task = null;
        this.taskStatus = "";
        this.categoryList = null;
        this.dbAsyncTaskCallback = null;
    }

    // INSERT_TASK
    public DbAsyncTask(AppDatabase db, dbAction myDbAction, Task task, DbAsyncTaskCallback dbAsyncTaskCallback) {
        this.db = db;
        this.recyclerAdapter = null;
        this.myDbAction = myDbAction;
        this.taskId = 0;
        this.task = task;
        this.taskStatus = "";
        this.categoryList = null;
        this.dbAsyncTaskCallback = dbAsyncTaskCallback;
    }

    // DELETE_TASK
    public DbAsyncTask(AppDatabase db, TaskRecyclerAdapter recyclerAdapter, dbAction myDbAction, int taskId) {
        this.db = db;
        this.recyclerAdapter = recyclerAdapter;
        this.myDbAction = myDbAction;
        this.taskId = taskId;
        this.task = null;
        this.taskStatus = "";
        this.categoryList = null;
        this.dbAsyncTaskCallback = null;
    }

    // UPDATESTATUS_TASK
    public DbAsyncTask(AppDatabase db, TaskRecyclerAdapter recyclerAdapter, dbAction myDbAction, int taskId, String taskStatus) {
        this.db = db;
        this.recyclerAdapter = recyclerAdapter;
        this.myDbAction = myDbAction;
        this.taskId = taskId;
        this.task = null;
        this.taskStatus = taskStatus;
        this.categoryList = null;
        this.dbAsyncTaskCallback = null;
    }

    // INFO_TASK
    public DbAsyncTask(AppDatabase db, dbAction myDbAction, int taskId, DbAsyncTaskCallback dbAsyncTaskCallback) {
        this.db = db;
        this.recyclerAdapter = recyclerAdapter;
        this.myDbAction = myDbAction;
        this.taskId = taskId;
        this.task = null;
        this.taskStatus = "";
        this.categoryList = null;
        this.dbAsyncTaskCallback = dbAsyncTaskCallback;
    }

    // INIT_CATEGORY
    public DbAsyncTask(AppDatabase db, dbAction myDbAction, List<TaskCategory> categoryList) {
        this.db = db;
        this.recyclerAdapter = null;
        this.myDbAction = myDbAction;
        this.taskId = 0;
        this.task = null;
        this.taskStatus = "";
        this.categoryList = categoryList;
        this.dbAsyncTaskCallback = null;
    }

    // GETALL_CATEGORY
    public DbAsyncTask(AppDatabase db, dbAction myDbAction, DbAsyncTaskCallback dbAsyncTaskCallback) {
        this.db = db;
        this.recyclerAdapter = null;
        this.myDbAction = myDbAction;
        this.taskId = 0;
        this.task = null;
        this.taskStatus = "";
        this.categoryList = null;
        this.dbAsyncTaskCallback = dbAsyncTaskCallback;
    }

    @Override
    protected Task doInBackground(Void... voids) {
        switch (this.myDbAction) {
            case GETALL_TASK:
                this.recyclerAdapter.refreshData(this.db.taskDao().getAll());
                return null;
            case GETPENDING_TASK:
                this.recyclerAdapter.refreshData(this.db.taskDao().getAllByStatus("Pending"));
                return null;
            case INSERT_TASK:
                this.db.taskDao().insertAll(this.task);
                return null;
            case DELETE_TASK:
                this.db.taskDao().delete(this.db.taskDao().getById(this.taskId));
                recyclerAdapter.refreshData(this.db.taskDao().getAll());
                return null;
            case UPDATESTATUS_TASK:
                this.db.taskDao().updateTaskStatus(this.taskId, this.taskStatus);
                this.recyclerAdapter.refreshData(this.db.taskDao().getAllByStatus("Pending"));
                return null;
            case INFO_TASK:
                return this.db.taskDao().getById(this.taskId);
            case INIT_CATEGORY:
                for (TaskCategory category : this.categoryList) {
                    this.db.taskCategoryDao().insertAll(category);
                }
                return null;
            case GETALL_CATEGORY:
                this.categoryList = this.db.taskCategoryDao().getAll();
                return null;
            default:
                return null;
        }
    }

    @Override
    protected void onPostExecute(Task task) {
        switch (this.myDbAction) {
            case GETALL_TASK:
                this.recyclerAdapter.notifyDataSetChanged();
                break;
            case GETPENDING_TASK:
                this.recyclerAdapter.notifyDataSetChanged();
                break;
            case INSERT_TASK:
                this.dbAsyncTaskCallback.onInsertTaskCallback();
                break;
            case DELETE_TASK:
                this.recyclerAdapter.notifyDataSetChanged();
                break;
            case UPDATESTATUS_TASK:
                this.recyclerAdapter.notifyDataSetChanged();
                break;
            case INFO_TASK:
                this.dbAsyncTaskCallback.onInfoTaskCallback(task);
                break;
            case GETALL_CATEGORY:
                this.dbAsyncTaskCallback.onGetAllCategoryCallback(this.categoryList);
                break;
            default:
                break;
        }

    }
}
