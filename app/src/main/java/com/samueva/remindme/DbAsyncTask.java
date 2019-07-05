package com.samueva.remindme;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

enum dbAction {
    GETALL_TASK,
    GETPENDING_TASK,
    INSERT_TASK,
    DELETE_TASK,
    SETDONE_TASK,
    INFO_TASK,
    INIT_CATEGORY,
    GETALL_CATEGORY,
    INSERT_CATEGORY,
    DELETE_CATEGORY,
    GETDONE_HISTORY,
    DELETE_HISTORY,
    DELETEALL_HYSTORY
}

public class DbAsyncTask extends AsyncTask<Void, Void, Task> {

    // TODO: 6/23/19 STRINGA DI DEBUG
    private static final String TAG = "ReMe_DbAsyncTask";

    private AppDatabase db;
    private TaskRecyclerAdapter recyclerAdapter;
    private HistoryRecyclerAdapter historyRecyclerAdapter;
    private dbAction myDbAction;
    private int taskId;
    private Task task;
    private String taskStatus;
    private Calendar taskCalendar;
    private List<TaskCategory> categoryList;
    private TaskCategory category;
    private String categoryName;

    DbAsyncTaskCallback dbAsyncTaskCallback;

    public interface DbAsyncTaskCallback {
        void onInsertTaskCallback();
        void onInfoTaskCallback(Task task);
        void onGetAllCategoryCallback(List<TaskCategory> categoryList);
        void onInsertCategoryCallback();
        void onDeleteCategoryCallback();
    }

    public DbAsyncTask(AppDatabase db, TaskRecyclerAdapter recyclerAdapter, HistoryRecyclerAdapter historyRecyclerAdapter, dbAction myDbAction, int taskId, Task task, String taskStatus, Calendar taskCalendar, List<TaskCategory> categoryList, DbAsyncTaskCallback dbAsyncTaskCallback, TaskCategory category, String categoryName) {
        this.db = db;
        this.recyclerAdapter = recyclerAdapter;
        this.historyRecyclerAdapter = historyRecyclerAdapter;
        this.myDbAction = myDbAction;
        this.taskId = taskId;
        this.task = task;
        this.taskStatus = taskStatus;
        this.taskCalendar = taskCalendar;
        this.categoryList = categoryList;
        this.dbAsyncTaskCallback = dbAsyncTaskCallback;
        this.category = category;
        this.categoryName = categoryName;
    }

    // GETALL_TASK, GETPENDING_TASK
    public DbAsyncTask(AppDatabase db, TaskRecyclerAdapter recyclerAdapter, dbAction myDbAction) {
        this.db = db;
        this.recyclerAdapter = recyclerAdapter;
        this.historyRecyclerAdapter = null;
        this.myDbAction = myDbAction;
        this.taskId = 0;
        this.task = null;
        this.taskStatus = "";
        this.taskCalendar = null;
        this.categoryList = null;
        this.dbAsyncTaskCallback = null;
        this.category = null;
        this.categoryName = null;
    }

    // INSERT_TASK
    public DbAsyncTask(AppDatabase db, dbAction myDbAction, Task task, DbAsyncTaskCallback dbAsyncTaskCallback) {
        this.db = db;
        this.recyclerAdapter = null;
        this.historyRecyclerAdapter = null;
        this.myDbAction = myDbAction;
        this.taskId = 0;
        this.task = task;
        this.taskStatus = "";
        this.taskCalendar = null;
        this.categoryList = null;
        this.dbAsyncTaskCallback = dbAsyncTaskCallback;
        this.category = null;
        this.categoryName = null;
    }

    // DELETE_TASK
    public DbAsyncTask(AppDatabase db, TaskRecyclerAdapter recyclerAdapter, dbAction myDbAction, int taskId) {
        this.db = db;
        this.recyclerAdapter = recyclerAdapter;
        this.historyRecyclerAdapter = null;
        this.myDbAction = myDbAction;
        this.taskId = taskId;
        this.task = null;
        this.taskStatus = "";
        this.taskCalendar = null;
        this.categoryList = null;
        this.dbAsyncTaskCallback = null;
        this.category = null;
        this.categoryName = null;
    }

    // SETDONE_TASK
    public DbAsyncTask(AppDatabase db, TaskRecyclerAdapter recyclerAdapter, dbAction myDbAction, int taskId, String taskStatus, Calendar taskCalendar) {
        this.db = db;
        this.recyclerAdapter = recyclerAdapter;
        this.historyRecyclerAdapter = null;
        this.myDbAction = myDbAction;
        this.taskId = taskId;
        this.task = null;
        this.taskStatus = taskStatus;
        this.taskCalendar = taskCalendar;
        this.categoryList = null;
        this.dbAsyncTaskCallback = null;
        this.category = null;
        this.categoryName = null;
    }

    // INFO_TASK
    public DbAsyncTask(AppDatabase db, dbAction myDbAction, int taskId, DbAsyncTaskCallback dbAsyncTaskCallback) {
        this.db = db;
        this.recyclerAdapter = null;
        this.historyRecyclerAdapter = null;
        this.myDbAction = myDbAction;
        this.taskId = taskId;
        this.task = null;
        this.taskStatus = "";
        this.taskCalendar = null;
        this.categoryList = null;
        this.dbAsyncTaskCallback = dbAsyncTaskCallback;
        this.category = null;
        this.categoryName = null;
    }

    // INIT_CATEGORY
    public DbAsyncTask(AppDatabase db, dbAction myDbAction, List<TaskCategory> categoryList) {
        this.db = db;
        this.recyclerAdapter = null;
        this.historyRecyclerAdapter = null;
        this.myDbAction = myDbAction;
        this.taskId = 0;
        this.task = null;
        this.taskStatus = "";
        this.taskCalendar = null;
        this.categoryList = categoryList;
        this.dbAsyncTaskCallback = null;
        this.category = null;
        this.categoryName = null;
    }

    // GETALL_CATEGORY
    public DbAsyncTask(AppDatabase db, dbAction myDbAction, DbAsyncTaskCallback dbAsyncTaskCallback) {
        this.db = db;
        this.recyclerAdapter = null;
        this.historyRecyclerAdapter = null;
        this.myDbAction = myDbAction;
        this.taskId = 0;
        this.task = null;
        this.taskStatus = "";
        this.taskCalendar = null;
        this.categoryList = null;
        this.dbAsyncTaskCallback = dbAsyncTaskCallback;
        this.category = null;
        this.categoryName = null;
    }

    // INSERT_CATEGORY
    public DbAsyncTask(AppDatabase db, dbAction myDbAction, DbAsyncTaskCallback dbAsyncTaskCallback, TaskCategory category) {
        this.db = db;
        this.recyclerAdapter = null;
        this.historyRecyclerAdapter = null;
        this.myDbAction = myDbAction;
        this.taskId = 0;
        this.task = null;
        this.taskStatus = "";
        this.taskCalendar = null;
        this.categoryList = null;
        this.dbAsyncTaskCallback = dbAsyncTaskCallback;
        this.category = category;
        this.categoryName = null;
    }

    // DELETE_CATEGORY
    public DbAsyncTask(AppDatabase db, dbAction myDbAction, DbAsyncTaskCallback dbAsyncTaskCallback, String categoryName) {
        this.db = db;
        this.recyclerAdapter = null;
        this.historyRecyclerAdapter = null;
        this.myDbAction = myDbAction;
        this.taskId = 0;
        this.task = null;
        this.taskStatus = "";
        this.taskCalendar = null;
        this.categoryList = null;
        this.dbAsyncTaskCallback = dbAsyncTaskCallback;
        this.category = null;
        this.categoryName = categoryName;
    }

    // GETDONE_HISTORY, DELETEALL_HISTORY
    public DbAsyncTask(AppDatabase db, HistoryRecyclerAdapter historyRecyclerAdapter, dbAction myDbAction) {
        this.db = db;
        this.recyclerAdapter = null;
        this.historyRecyclerAdapter = historyRecyclerAdapter;
        this.myDbAction = myDbAction;
        this.taskId = 0;
        this.task = null;
        this.taskStatus = "";
        this.taskCalendar = null;
        this.categoryList = null;
        this.dbAsyncTaskCallback = null;
        this.category = null;
        this.categoryName = null;
    }

    // DELETE_HISTORY
    public DbAsyncTask(AppDatabase db, HistoryRecyclerAdapter recyclerAdapter, dbAction myDbAction, int taskId) {
        this.db = db;
        this.recyclerAdapter = null;
        this.historyRecyclerAdapter = recyclerAdapter;
        this.myDbAction = myDbAction;
        this.taskId = taskId;
        this.task = null;
        this.taskStatus = "";
        this.taskCalendar = null;
        this.categoryList = null;
        this.dbAsyncTaskCallback = null;
        this.category = null;
        this.categoryName = null;
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
                this.recyclerAdapter.refreshData(this.db.taskDao().getAllByStatus("Pending"));
                return null;
            case SETDONE_TASK:
                this.db.taskDao().updateTaskStatus(this.taskId, this.taskStatus);
                this.db.taskDao().updateTaskDoneCalendar(this.taskId, this.taskCalendar.get(Calendar.YEAR), this.taskCalendar.get(Calendar.MONTH), this.taskCalendar.get(Calendar.DAY_OF_MONTH), this.taskCalendar.get(Calendar.HOUR_OF_DAY), this.taskCalendar.get(Calendar.MINUTE));
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
            case INSERT_CATEGORY:
                this.db.taskCategoryDao().insertAll(this.category);
                return null;
            case DELETE_CATEGORY:
                this.db.taskCategoryDao().deleteByName(categoryName);
                return null;
            case GETDONE_HISTORY:
                this.historyRecyclerAdapter.refreshData(this.db.taskDao().getAllByStatus("Done"));
                return null;
            case DELETE_HISTORY:
                this.db.taskDao().delete(this.db.taskDao().getById(this.taskId));
                this.historyRecyclerAdapter.refreshData(this.db.taskDao().getAllByStatus("Done"));
                return null;
            case DELETEALL_HYSTORY:
                this.db.taskDao().deleteAllByStatus("Done");
                this.historyRecyclerAdapter.refreshData(this.db.taskDao().getAllByStatus("Done"));
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
            case SETDONE_TASK:
                this.recyclerAdapter.notifyDataSetChanged();
                break;
            case INFO_TASK:
                this.dbAsyncTaskCallback.onInfoTaskCallback(task);
                break;
            case GETALL_CATEGORY:
                this.dbAsyncTaskCallback.onGetAllCategoryCallback(this.categoryList);
                break;
            case INSERT_CATEGORY:
                this.dbAsyncTaskCallback.onInsertCategoryCallback();
                break;
            case DELETE_CATEGORY:
                this.dbAsyncTaskCallback.onDeleteCategoryCallback();
                break;
            case GETDONE_HISTORY:
                this.historyRecyclerAdapter.notifyDataSetChanged();
                break;
            case DELETE_HISTORY:
                this.historyRecyclerAdapter.notifyDataSetChanged();
                break;
            case DELETEALL_HYSTORY:
                this.historyRecyclerAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }

    }
}
