package com.samueva.remindme;

import android.os.AsyncTask;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

enum dbAction {
    //REWORK
    TASK_GETALLBYSTATUS,
    TASK_INSERTALL,
    TASK_DELETE,
    TASK_UPDATE_DONE,

    GETALL_TASK,
    SETCOMPLETED_TASK,
    INFO_TASK,
    INIT_CATEGORY,
    GETALL_CATEGORY,
    INSERT_CATEGORY,
    DELETE_CATEGORY,
    DELETEALL_HYSTORY
}

public class DbAsyncTask extends AsyncTask<Void, Void, Void> {

    // TODO: 6/23/19 STRINGA DI DEBUG
    private static final String TAG = "ReMe_DbAsyncTask";

    //REWORK
    // Input
    private AppDatabase db;
    private dbAction myDbAction;
    private String string;
    private Task task;
    private int taskId;
    private Calendar calendar;
    // Output
    private List<Task> taskList;

    DbAsyncTaskListener dbAsyncTaskListener;

    public interface DbAsyncTaskListener {
        //REWORK
        void onTaskGetAllByStatusCallback(List<Task> taskList);
        void onTaskInsertAllCallback();
        void onTaskDeleteCallback();
        void onTaskUpdateCallback();

        void onInfoTaskCallback(Task task);
        void onGetAllCategoryCallback(List<TaskCategory> categoryList);
        void onInsertCategoryCallback();
        void onDeleteCategoryCallback();
    }

    private TaskRecyclerAdapter recyclerAdapter;
    private HistoryRecyclerAdapter historyRecyclerAdapter;
    private String taskStatus;
    private List<TaskCategory> categoryList;
    private TaskCategory category;
    private String categoryName;

    //REWORK
    //TASK_GETALLBYSTATUS
    public DbAsyncTask(AppDatabase db, dbAction myDbAction, String string, DbAsyncTaskListener dbAsyncTaskListener) {
        this.db = db;
        this.myDbAction = myDbAction;
        this.string = string;
        this.dbAsyncTaskListener = dbAsyncTaskListener;
    }

    //TASK_INSERTALL
    public DbAsyncTask(AppDatabase db, dbAction myDbAction, Task task, DbAsyncTaskListener dbAsyncTaskListener) {
        this.db = db;
        this.myDbAction = myDbAction;
        this.task = task;
        this.dbAsyncTaskListener = dbAsyncTaskListener;
    }

    //TASK_DELETE
    public DbAsyncTask(AppDatabase db, dbAction myDbAction, int taskId, DbAsyncTaskListener dbAsyncTaskListener) {
        this.db = db;
        this.myDbAction = myDbAction;
        this.taskId = taskId;
        this.dbAsyncTaskListener = dbAsyncTaskListener;
    }

    //TASK_UPDATE_DONE
    public DbAsyncTask(AppDatabase db, dbAction myDbAction, int taskId, Calendar calendar, DbAsyncTaskListener dbAsyncTaskListener) {
        this.db = db;
        this.myDbAction = myDbAction;
        this.taskId = taskId;
        this.calendar = calendar;
        this.dbAsyncTaskListener = dbAsyncTaskListener;
    }

    public DbAsyncTask(AppDatabase db, TaskRecyclerAdapter recyclerAdapter, HistoryRecyclerAdapter historyRecyclerAdapter, dbAction myDbAction, int taskId, Task task, String taskStatus, Calendar calendar, List<TaskCategory> categoryList, DbAsyncTaskListener dbAsyncTaskListener, TaskCategory category, String categoryName) {
        this.db = db;
        this.recyclerAdapter = recyclerAdapter;
        this.historyRecyclerAdapter = historyRecyclerAdapter;
        this.myDbAction = myDbAction;
        this.taskId = taskId;
        this.task = task;
        this.taskStatus = taskStatus;
        this.calendar = calendar;
        this.categoryList = categoryList;
        this.dbAsyncTaskListener = dbAsyncTaskListener;
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
        this.calendar = null;
        this.categoryList = null;
        this.dbAsyncTaskListener = null;
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
        this.calendar = null;
        this.categoryList = null;
        this.dbAsyncTaskListener = null;
        this.category = null;
        this.categoryName = null;
    }

    // SETCOMPLETED_TASK
    public DbAsyncTask(AppDatabase db, TaskRecyclerAdapter recyclerAdapter, dbAction myDbAction, int taskId, String taskStatus, Calendar calendar) {
        this.db = db;
        this.recyclerAdapter = recyclerAdapter;
        this.historyRecyclerAdapter = null;
        this.myDbAction = myDbAction;
        this.taskId = taskId;
        this.task = null;
        this.taskStatus = taskStatus;
        this.calendar = calendar;
        this.categoryList = null;
        this.dbAsyncTaskListener = null;
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
        this.calendar = null;
        this.categoryList = categoryList;
        this.dbAsyncTaskListener = null;
        this.category = null;
        this.categoryName = null;
    }

    // GETALL_CATEGORY
    public DbAsyncTask(AppDatabase db, dbAction myDbAction, DbAsyncTaskListener dbAsyncTaskListener) {
        this.db = db;
        this.recyclerAdapter = null;
        this.historyRecyclerAdapter = null;
        this.myDbAction = myDbAction;
        this.taskId = 0;
        this.task = null;
        this.taskStatus = "";
        this.calendar = null;
        this.categoryList = null;
        this.dbAsyncTaskListener = dbAsyncTaskListener;
        this.category = null;
        this.categoryName = null;
    }

    // INSERT_CATEGORY
    public DbAsyncTask(AppDatabase db, dbAction myDbAction, DbAsyncTaskListener dbAsyncTaskListener, TaskCategory category) {
        this.db = db;
        this.recyclerAdapter = null;
        this.historyRecyclerAdapter = null;
        this.myDbAction = myDbAction;
        this.taskId = 0;
        this.task = null;
        this.taskStatus = "";
        this.calendar = null;
        this.categoryList = null;
        this.dbAsyncTaskListener = dbAsyncTaskListener;
        this.category = category;
        this.categoryName = null;
    }

    // DELETE_CATEGORY
    public DbAsyncTask(AppDatabase db, dbAction myDbAction, DbAsyncTaskListener dbAsyncTaskListener, String categoryName) {
        this.db = db;
        this.recyclerAdapter = null;
        this.historyRecyclerAdapter = null;
        this.myDbAction = myDbAction;
        this.taskId = 0;
        this.task = null;
        this.taskStatus = "";
        this.calendar = null;
        this.categoryList = null;
        this.dbAsyncTaskListener = dbAsyncTaskListener;
        this.category = null;
        this.categoryName = categoryName;
    }

    // GETCOMPLETED_HISTORY, DELETEALL_HISTORY
    public DbAsyncTask(AppDatabase db, HistoryRecyclerAdapter historyRecyclerAdapter, dbAction myDbAction) {
        this.db = db;
        this.recyclerAdapter = null;
        this.historyRecyclerAdapter = historyRecyclerAdapter;
        this.myDbAction = myDbAction;
        this.taskId = 0;
        this.task = null;
        this.taskStatus = "";
        this.calendar = null;
        this.categoryList = null;
        this.dbAsyncTaskListener = null;
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
        this.calendar = null;
        this.categoryList = null;
        this.dbAsyncTaskListener = null;
        this.category = null;
        this.categoryName = null;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        switch (this.myDbAction) {
            //REWORK
            case TASK_GETALLBYSTATUS:
                this.taskList = this.db.taskDao().getAllByStatus(this.string);
                this.taskList.sort(new Comparator<Task>() {
                    @Override
                    public int compare(Task task, Task t1) {
                        String s1 = String.format("%04d%02d%02d%02d%02d", task.getYear(), task.getMonth(), task.getDayOfMonth(), task.getHourOfDay(), task.getMinute());
                        String s2 = String.format("%04d%02d%02d%02d%02d", t1.getYear(), t1.getMonth(), t1.getDayOfMonth(), t1.getHourOfDay(), t1.getMinute());
                        return s1.compareTo(s2);
                    }
                });
                break;
            case TASK_INSERTALL:
                this.db.taskDao().insertAll(this.task);
                break;
            case TASK_DELETE:
                this.db.taskDao().delete(this.db.taskDao().getById(this.taskId));
                break;
            case TASK_UPDATE_DONE:
                this.task = this.db.taskDao().getById(this.taskId);
                task.setStatus("Completed");
                task.setDoneCalendar(calendar);
                this.db.taskDao().update(this.task);
                break;

            case GETALL_TASK:
                this.recyclerAdapter.refreshData(this.db.taskDao().getAll());
                break;
            case SETCOMPLETED_TASK:
                this.db.taskDao().updateTaskStatus(this.taskId, this.taskStatus);
                this.db.taskDao().updateTaskDoneCalendar(this.taskId, this.calendar.get(Calendar.YEAR), this.calendar.get(Calendar.MONTH), this.calendar.get(Calendar.DAY_OF_MONTH), this.calendar.get(Calendar.HOUR_OF_DAY), this.calendar.get(Calendar.MINUTE));
                this.recyclerAdapter.refreshData(this.db.taskDao().getAllByStatus("Pending"));
                break;
            case INFO_TASK:
                this.task = this.db.taskDao().getById(this.taskId);
                break;
            case INIT_CATEGORY:
                for (TaskCategory category : this.categoryList) {
                    this.db.taskCategoryDao().insertAll(category);
                }
                break;
            case GETALL_CATEGORY:
                this.categoryList = this.db.taskCategoryDao().getAll();
                break;
            case INSERT_CATEGORY:
                this.db.taskCategoryDao().insertAll(this.category);
                break;
            case DELETE_CATEGORY:
                this.db.taskCategoryDao().deleteByName(categoryName);
                break;
            case DELETEALL_HYSTORY:
                this.db.taskDao().deleteAllByStatus("Completed");
                this.historyRecyclerAdapter.refreshData(this.db.taskDao().getAllByStatus("Completed"));
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        switch (this.myDbAction) {
            //REWORK
            case TASK_GETALLBYSTATUS:
                dbAsyncTaskListener.onTaskGetAllByStatusCallback(this.taskList);
                break;
            case TASK_INSERTALL:
                this.dbAsyncTaskListener.onTaskInsertAllCallback();
                break;
            case TASK_DELETE:
                this.dbAsyncTaskListener.onTaskDeleteCallback();
                break;
            case TASK_UPDATE_DONE:
                this.dbAsyncTaskListener.onTaskUpdateCallback();
                break;

            case GETALL_TASK:
                this.recyclerAdapter.notifyDataSetChanged();
                break;
            case SETCOMPLETED_TASK:
                this.recyclerAdapter.notifyDataSetChanged();
                break;
            case INFO_TASK:
                this.dbAsyncTaskListener.onInfoTaskCallback(this.task);
                break;
            case GETALL_CATEGORY:
                this.dbAsyncTaskListener.onGetAllCategoryCallback(this.categoryList);
                break;
            case INSERT_CATEGORY:
                this.dbAsyncTaskListener.onInsertCategoryCallback();
                break;
            case DELETE_CATEGORY:
                this.dbAsyncTaskListener.onDeleteCategoryCallback();
                break;
            case DELETEALL_HYSTORY:
                this.historyRecyclerAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }

    }
}
