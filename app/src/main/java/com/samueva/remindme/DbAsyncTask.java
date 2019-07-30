package com.samueva.remindme;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

enum dbAction {
    TASK_GETBYID,
    TASK_GETALLBYSTATUS,
    TASK_INSERTALL,
    TASK_DELETE_BYID,
    TASK_DELETE_HISTORY,
    TASK_UPDATE_COMPLETE,
    TASK_UPDATE_UNCOMPLETE,

    CATEGORY_GETALL,
    CATEGORY_INSERTALL,
    CATEGORY_DELETE_BYNAME
}

// TODO: 7/30/19 Eliminare tutte i Log di debug per vedere il corretto funzionamento del database
public class DbAsyncTask extends AsyncTask<Void, Void, Void> {

    // TODO: 6/23/19 STRINGA DI DEBUG
    private static final String TAG = "ReMe_DbAsyncTask";

    private AppDatabase db;
    private dbAction myDbAction;
    private String string;
    private Calendar calendar;
    private Task task;
    private TaskCategory taskCategory;
    private int taskId;
    private List<Task> taskList;
    private List<TaskCategory> categoryList;


    private DbAsyncTaskListener dbAsyncTaskListener;

    public interface DbAsyncTaskListener {
        void onTaskGetByIdCallback(Task task);
        void onTaskGetAllByStatusCallback(List<Task> taskList);
        void onTaskUpdateCallback();

        void onCategoryGetAllCallback(List<TaskCategory> categoryList);
        void onCategoryUpdateCallback();
    }

    //TASK_GETBYID, TASK_DELETE, TASK_UPDATE_UNCOMPLETE
    public DbAsyncTask(AppDatabase db, dbAction myDbAction, int taskId, DbAsyncTaskListener dbAsyncTaskListener) {
        this.db = db;
        this.myDbAction = myDbAction;
        this.taskId = taskId;
        this.dbAsyncTaskListener = dbAsyncTaskListener;
    }

    //TASK_GETALLBYSTATUS, CATEGORY_DELETE_BYNAME
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

    //TASK_DELETE_HISTORY, CATEGORY_GETALL
    public DbAsyncTask(AppDatabase db, dbAction myDbAction, DbAsyncTaskListener dbAsyncTaskListener) {
        this.db = db;
        this.myDbAction = myDbAction;
        this.dbAsyncTaskListener = dbAsyncTaskListener;
    }

    //TASK_UPDATE_COMPLETE
    public DbAsyncTask(AppDatabase db, dbAction myDbAction, int taskId, Calendar calendar, DbAsyncTaskListener dbAsyncTaskListener) {
        this.db = db;
        this.myDbAction = myDbAction;
        this.taskId = taskId;
        this.calendar = calendar;
        this.dbAsyncTaskListener = dbAsyncTaskListener;
    }

    //CATEGORY_INSERTALL
    public DbAsyncTask(AppDatabase db, dbAction myDbAction, List<TaskCategory> categoryList, DbAsyncTaskListener dbAsyncTaskListener) {
        this.db = db;
        this.myDbAction = myDbAction;
        this.categoryList = categoryList;
        this.dbAsyncTaskListener = dbAsyncTaskListener;
    }

    //CATEGORY_UPDATE_TASKSADD, CATEGORY_UPDATE_TASKSDELETE
    public DbAsyncTask(AppDatabase db, dbAction myDbAction, String string) {
        this.db = db;
        this.myDbAction = myDbAction;
        this.string = string;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        switch (this.myDbAction) {
            case TASK_GETBYID:
                this.task = this.db.taskDao().getById(this.taskId);
                break;
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
                this.taskCategory = this.db.taskCategoryDao().getByName(this.task.getCategory());
                this.taskCategory.setTasks(this.taskCategory.getTasks() + 1);
                Log.d(TAG, "category: " + this.task.getCategory());
                Log.d(TAG, "tasks: " + this.taskCategory.getTasks());
                Log.d(TAG, "historyTasks: " + this.taskCategory.getHistoryTasks());
                this.db.taskDao().insertAll(this.task);
                this.db.taskCategoryDao().update(this.taskCategory);
                break;
            case TASK_DELETE_BYID:
                this.task = this.db.taskDao().getById(this.taskId);
                this.taskCategory = this.db.taskCategoryDao().getByName(this.task.getCategory());
                this.taskCategory.setTasks(this.taskCategory.getTasks() - 1);
                if (this.task.getStatus().compareTo("Completed") == 0 || this.task.getStatus().compareTo("Failed") == 0) {
                    this.taskCategory.setHistoryTasks(this.taskCategory.getHistoryTasks() - 1);
                }
                Log.d(TAG, "category: " + this.task.getCategory());
                Log.d(TAG, "tasks: " + this.taskCategory.getTasks());
                Log.d(TAG, "historyTasks: " + this.taskCategory.getHistoryTasks());
                this.db.taskDao().delete(this.task);
                this.db.taskCategoryDao().update(this.taskCategory);
                break;
            case TASK_DELETE_HISTORY:
                this.categoryList = this.db.taskCategoryDao().getAllInHistory();
                for (TaskCategory category : this.categoryList) {
                    category.setTasks(category.getTasks() - category.getHistoryTasks());
                    category.setHistoryTasks(0);
                    Log.d(TAG, "category: " + category.getName());
                    Log.d(TAG, "tasks: " + category.getTasks());
                    Log.d(TAG, "historyTasks: " + category.getHistoryTasks());
                    this.db.taskCategoryDao().update(category);
                }
                this.db.taskDao().deleteHistory();
                break;
            case TASK_UPDATE_COMPLETE:
                this.task = this.db.taskDao().getById(this.taskId);
                this.taskCategory = this.db.taskCategoryDao().getByName(this.task.getCategory());
                this.task.setStatus("Completed");
                this.task.setDoneCalendar(calendar);
                this.taskCategory.setHistoryTasks(this.taskCategory.getHistoryTasks() + 1);
                Log.d(TAG, "category: " + this.task.getCategory());
                Log.d(TAG, "tasks: " + this.taskCategory.getTasks());
                Log.d(TAG, "historyTasks: " + this.taskCategory.getHistoryTasks());
                this.db.taskDao().update(this.task);
                this.db.taskCategoryDao().update(this.taskCategory);
                break;
            case TASK_UPDATE_UNCOMPLETE:
                this.task = this.db.taskDao().getById(this.taskId);
                this.taskCategory = this.db.taskCategoryDao().getByName(this.task.getCategory());
                this.task.setStatus("Pending");
                this.taskCategory.setHistoryTasks(this.taskCategory.getHistoryTasks() - 1);
                Log.d(TAG, "category: " + this.task.getCategory());
                Log.d(TAG, "tasks: " + this.taskCategory.getTasks());
                Log.d(TAG, "historyTasks: " + this.taskCategory.getHistoryTasks());
                this.db.taskDao().update(this.task);
                this.db.taskCategoryDao().update(this.taskCategory);
                break;

            case CATEGORY_GETALL:
                this.categoryList = this.db.taskCategoryDao().getAll();
                break;
            case CATEGORY_INSERTALL:
                for (TaskCategory category : this.categoryList) {
                    this.db.taskCategoryDao().insertAll(category);
                }
                break;
            case CATEGORY_DELETE_BYNAME:
                this.db.taskCategoryDao().deleteByName(this.string);
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
            case TASK_GETBYID:
                this.dbAsyncTaskListener.onTaskGetByIdCallback(this.task);
                break;
            case TASK_GETALLBYSTATUS:
                this.dbAsyncTaskListener.onTaskGetAllByStatusCallback(this.taskList);
                break;
            case TASK_INSERTALL:
                this.dbAsyncTaskListener.onTaskUpdateCallback();
                break;
            case TASK_DELETE_BYID:
                this.dbAsyncTaskListener.onTaskUpdateCallback();
                break;
            case TASK_DELETE_HISTORY:
                this.dbAsyncTaskListener.onTaskUpdateCallback();
                break;
            case TASK_UPDATE_COMPLETE:
                this.dbAsyncTaskListener.onTaskUpdateCallback();
                break;
            case TASK_UPDATE_UNCOMPLETE:
                this.dbAsyncTaskListener.onTaskUpdateCallback();
                break;

            case CATEGORY_GETALL:
                this.dbAsyncTaskListener.onCategoryGetAllCallback(this.categoryList);
                break;
            case CATEGORY_INSERTALL:
                this.dbAsyncTaskListener.onCategoryUpdateCallback();
                break;
            case CATEGORY_DELETE_BYNAME:
                this.dbAsyncTaskListener.onCategoryUpdateCallback();
                break;

            default:
                break;
        }

    }
}
