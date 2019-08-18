package com.samueva.remindme;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

enum dbAction {
    TASK_GETBYID,
    TASK_GETALLBYSTATUS,
    TASK_GETALLBYFILTERS,
    TASK_GETALLBYDATE,
    TASK_INSERT,
    TASK_DELETE_BYID,
    TASK_DELETE_HISTORY,
    TASK_UPDATE,
    TASK_UPDATE_COMPLETE,
    TASK_UPDATE_UNCOMPLETE,

    CATEGORY_GETALL,
    CATEGORY_INSERTALL,
    CATEGORY_DELETE
}

// TODO: 7/30/19 Eliminare tutte i Log di debug per vedere il corretto funzionamento del database
public class DbAsyncTask extends AsyncTask<Void, Void, Void> {

    // TODO: 6/23/19 STRINGA DI DEBUG
    private static final String TAG = "ReMe_DbAsyncTask";

    private AppDatabase db;
    private dbAction myDbAction;
    private String string;
    private String string2;
    private Calendar calendar;
    private Task task;
    private TaskCategory taskCategory;
    private int taskId;
    private int year;
    private int month;
    private int dayOfMonth;
    private List<Task> taskList;
    private List<TaskCategory> categoryList;


    private DbAsyncTaskListener dbAsyncTaskListener;

    public interface DbAsyncTaskListener {
        void onTaskGetByIdCallback(Task task);
        void onTaskGetAllByStatusCallback(List<Task> taskList);
        void onTaskUpdateCallback(int taskId);
        void onTaskGetAllByFiltersCallback(List<Task> taskList);

        void onCategoryGetAllCallback(List<TaskCategory> categoryList);
        void onCategoryUpdateCallback();
    }

    //TASK_GETBYID, TASK_DELETE, TASK_UPDATE_UNCOMPLETE
    DbAsyncTask(AppDatabase db, dbAction myDbAction, int taskId, DbAsyncTaskListener dbAsyncTaskListener) {
        this.db = db;
        this.myDbAction = myDbAction;
        this.taskId = taskId;
        this.dbAsyncTaskListener = dbAsyncTaskListener;
    }

    //TASK_GETALLBYSTATUS
    DbAsyncTask(AppDatabase db, dbAction myDbAction, String string, DbAsyncTaskListener dbAsyncTaskListener) {
        this.db = db;
        this.myDbAction = myDbAction;
        this.string = string;
        this.dbAsyncTaskListener = dbAsyncTaskListener;
    }

    //TASK_GETALLBYFILTERS
    DbAsyncTask(AppDatabase db, dbAction myDbAction, String string, String string2, DbAsyncTaskListener dbAsyncTaskListener) {
        this.db = db;
        this.myDbAction = myDbAction;
        this.string = string;
        this.string2 = string2;
        this.dbAsyncTaskListener = dbAsyncTaskListener;
    }

    //TASK_GETALLBYDATE
    DbAsyncTask(AppDatabase db, dbAction myDbAction, int year, int month, int dayOfMonth, DbAsyncTaskListener dbAsyncTaskListener) {
        this.db = db;
        this.myDbAction = myDbAction;
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.dbAsyncTaskListener = dbAsyncTaskListener;
    }

    //TASK_DELETE_HISTORY, CATEGORY_GETALL
    DbAsyncTask(AppDatabase db, dbAction myDbAction, DbAsyncTaskListener dbAsyncTaskListener) {
        this.db = db;
        this.myDbAction = myDbAction;
        this.dbAsyncTaskListener = dbAsyncTaskListener;
    }

    //TASK_INSERT
    DbAsyncTask(AppDatabase db, dbAction myDbAction, Task task, DbAsyncTaskListener dbAsyncTaskListener) {
        this.db = db;
        this.myDbAction = myDbAction;
        this.task = task;
        this.dbAsyncTaskListener = dbAsyncTaskListener;
    }

    //TASK_UPDATE
    DbAsyncTask(AppDatabase db, dbAction myDbAction, Task task, String string, String string2, DbAsyncTaskListener dbAsyncTaskListener) {
        this.db = db;
        this.myDbAction = myDbAction;
        this.task = task;
        this.string = string;
        this.string2 = string2;
        this.dbAsyncTaskListener = dbAsyncTaskListener;
    }

    //TASK_UPDATE_COMPLETE
    DbAsyncTask(AppDatabase db, dbAction myDbAction, int taskId, Calendar calendar, DbAsyncTaskListener dbAsyncTaskListener) {
        this.db = db;
        this.myDbAction = myDbAction;
        this.taskId = taskId;
        this.calendar = calendar;
        this.dbAsyncTaskListener = dbAsyncTaskListener;
    }

    //CATEGORY_INSERTALL
    DbAsyncTask(AppDatabase db, dbAction myDbAction, List<TaskCategory> categoryList, DbAsyncTaskListener dbAsyncTaskListener) {
        this.db = db;
        this.myDbAction = myDbAction;
        this.categoryList = categoryList;
        this.dbAsyncTaskListener = dbAsyncTaskListener;
    }

    //CATEGORY_DELETE
    DbAsyncTask(AppDatabase db, dbAction myDbAction, TaskCategory taskCategory, DbAsyncTaskListener dbAsyncTaskListener) {
        this.db = db;
        this.myDbAction = myDbAction;
        this.taskCategory = taskCategory;
        this.dbAsyncTaskListener = dbAsyncTaskListener;
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
            case TASK_GETALLBYFILTERS:
                if (this.string.equals("")) {
                    this.string = "%";
                } else {
                    this.string = "%" + this.string + "%";
                }
                if (this.string2.equals("")) {
                    this.string2 = "%";
                } else {
                    this.string2 = "%" + this.string2 + "%";
                }
                Log.d(TAG, "TASK_GETALLBYFILTERS\ntitle : " + this.string + "\nplace : " + this.string2);
                this.taskList = this.db.taskDao().getAllByFilters(this.string, this.string2);
                break;
            case TASK_GETALLBYDATE:
                this.taskList = this.db.taskDao().getAllByDate(this.year, this.month, this.dayOfMonth);
                break;
            case TASK_INSERT:
                this.taskCategory = this.db.taskCategoryDao().getByName(this.task.getCategory());
                this.taskCategory.setTasks(this.taskCategory.getTasks() + 1);
                Log.d(TAG, "category: " + this.task.getCategory());
                Log.d(TAG, "tasks: " + this.taskCategory.getTasks());
                Log.d(TAG, "historyTasks: " + this.taskCategory.getHistoryTasks());
                this.taskId = (int) this.db.taskDao().insert(this.task);
                this.db.taskCategoryDao().update(this.taskCategory);
                break;
            case TASK_DELETE_BYID:
                this.task = this.db.taskDao().getById(this.taskId);
                this.taskCategory = this.db.taskCategoryDao().getByName(this.task.getCategory());
                this.taskCategory.setTasks(this.taskCategory.getTasks() - 1);
                if (this.task.getStatus().equals("Completed")) {
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
                this.db.taskDao().deleteByStatus("Completed");
                break;
            case TASK_UPDATE:
                this.db.taskDao().update(this.task);
                this.taskCategory = this.db.taskCategoryDao().getByName(this.string);
                // Aggiustamento della category riguardante il cambiamento di status del task
                if (!(this.task.getStatus().equals(this.string2))) {
                    if (this.string2.equals("Completed")) {
                        this.taskCategory.setHistoryTasks(this.taskCategory.getHistoryTasks() - 1);
                    }
                    if (this.task.getStatus().equals("Completed")) {
                        this.taskCategory.setHistoryTasks(this.taskCategory.getHistoryTasks() + 1);
                    }
                    Log.d(TAG, "category: " + this.taskCategory.getName());
                    Log.d(TAG, "tasks: " + this.taskCategory.getTasks());
                    Log.d(TAG, "historyTasks: " + this.taskCategory.getHistoryTasks());
                    this.db.taskCategoryDao().update(this.taskCategory);
                }
                // Aggiustamento delle category riguardante il cambiamento di category
                if (!(this.task.getCategory().equals(this.string))) {
                    this.taskCategory.setTasks(this.taskCategory.getTasks() - 1);
                    if (this.task.getStatus().equals("Completed")) {
                        this.taskCategory.setHistoryTasks(this.taskCategory.getHistoryTasks() - 1);
                    }
                    Log.d(TAG, "category: " + this.taskCategory.getName());
                    Log.d(TAG, "tasks: " + this.taskCategory.getTasks());
                    Log.d(TAG, "historyTasks: " + this.taskCategory.getHistoryTasks());
                    this.db.taskCategoryDao().update(this.taskCategory);
                    this.taskCategory = this.db.taskCategoryDao().getByName(this.task.getCategory());
                    this.taskCategory.setTasks(this.taskCategory.getTasks() + 1);
                    if (this.task.getStatus().equals("Completed")) {
                        this.taskCategory.setHistoryTasks(this.taskCategory.getHistoryTasks() + 1);
                    }
                    Log.d(TAG, "category: " + this.taskCategory.getName());
                    Log.d(TAG, "tasks: " + this.taskCategory.getTasks());
                    Log.d(TAG, "historyTasks: " + this.taskCategory.getHistoryTasks());
                    this.db.taskCategoryDao().update(this.taskCategory);
                }
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
                this.categoryList.sort(new Comparator<TaskCategory>() {
                    @Override
                    public int compare(TaskCategory taskCategory, TaskCategory t1) {
                        if (taskCategory.getIsDefault()) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                });
                break;
            case CATEGORY_INSERTALL:
                for (TaskCategory category : this.categoryList) {
                    this.db.taskCategoryDao().insertAll(category);
                }
                break;
            case CATEGORY_DELETE:
                if (this.taskCategory.getTasks() > 0) {
                    this.db.taskDao().deleteByCategory(this.taskCategory.getName());
                }
                this.db.taskCategoryDao().delete(this.taskCategory);
                break;

            default:
                break;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        switch (this.myDbAction) {
            case TASK_GETBYID:
                this.dbAsyncTaskListener.onTaskGetByIdCallback(this.task);
                break;
            case TASK_GETALLBYSTATUS:
                this.dbAsyncTaskListener.onTaskGetAllByStatusCallback(this.taskList);
                break;
            case TASK_GETALLBYFILTERS:
                this.dbAsyncTaskListener.onTaskGetAllByFiltersCallback(this.taskList);
                break;
            case TASK_GETALLBYDATE:
                this.dbAsyncTaskListener.onTaskGetAllByFiltersCallback(this.taskList);
                break;
            case TASK_INSERT:
                this.dbAsyncTaskListener.onTaskUpdateCallback(this.taskId);
                break;
            case TASK_DELETE_BYID:
                this.dbAsyncTaskListener.onTaskUpdateCallback(-1);
                break;
            case TASK_DELETE_HISTORY:
                this.dbAsyncTaskListener.onTaskUpdateCallback(-1);
                break;
            case TASK_UPDATE:
                this.dbAsyncTaskListener.onTaskUpdateCallback(-1);
                break;
            case TASK_UPDATE_COMPLETE:
                this.dbAsyncTaskListener.onTaskUpdateCallback(-1);
                break;
            case TASK_UPDATE_UNCOMPLETE:
                this.dbAsyncTaskListener.onTaskUpdateCallback(-1);
                break;

            case CATEGORY_GETALL:
                this.dbAsyncTaskListener.onCategoryGetAllCallback(this.categoryList);
                break;
            case CATEGORY_INSERTALL:
                this.dbAsyncTaskListener.onCategoryUpdateCallback();
                break;
            case CATEGORY_DELETE:
                this.dbAsyncTaskListener.onCategoryUpdateCallback();
                break;

            default:
                break;
        }

    }
}
