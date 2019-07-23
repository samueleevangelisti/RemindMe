package com.samueva.remindme;

import android.os.AsyncTask;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

enum dbAction {
    TASK_GETBYID,
    TASK_GETALLBYSTATUS,
    TASK_INSERTALL,
    TASK_DELETE_BYTASKID,
    TASK_DELETE_HISTORY,
    TASK_UPDATE_COMPLETE,
    TASK_UPDATE_UNCOMPLETE,

    CATEGORY_GETALL,
    CATEGORY_INSERTALL,
    CATEGORY_DELETE_BYNAME,
    CATEGORY_UPDATE_NTASKADD,
}

public class DbAsyncTask extends AsyncTask<Void, Void, Void> {

    // TODO: 6/23/19 STRINGA DI DEBUG
    private static final String TAG = "ReMe_DbAsyncTask";

    private AppDatabase db;
    private dbAction myDbAction;
    private String string;
    private Task task;
    private int taskId;
    private Calendar calendar;
    private List<TaskCategory> categoryList;
    private List<Task> taskList;

    DbAsyncTaskListener dbAsyncTaskListener;

    public interface DbAsyncTaskListener {
        //REWORK
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

    //CATEGORY_UPDATE_NTASKADD
    public DbAsyncTask(AppDatabase db, dbAction myDbAction, String string) {
        this.db = db;
        this.myDbAction = myDbAction;
        this.string = string;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        switch (this.myDbAction) {
            //REWORK
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
                this.db.taskDao().insertAll(this.task);
                break;
            case TASK_DELETE_BYTASKID:
                this.db.taskDao().deleteByTaskId(this.taskId);
                break;
            case TASK_DELETE_HISTORY:
                this.db.taskDao().deleteHistory();
                break;
            case TASK_UPDATE_COMPLETE:
                this.task = this.db.taskDao().getById(this.taskId);
                task.setStatus("Completed");
                task.setDoneCalendar(calendar);
                this.db.taskDao().update(this.task);
                break;
            case TASK_UPDATE_UNCOMPLETE:
                this.task = this.db.taskDao().getById(this.taskId);
                task.setStatus("Pending");
                this.db.taskDao().update(this.task);
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
            case CATEGORY_UPDATE_NTASKADD:
                this.db.taskCategoryDao().updateNTaskAdd(this.string);
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
            case TASK_DELETE_BYTASKID:
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
