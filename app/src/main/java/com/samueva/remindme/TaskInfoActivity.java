package com.samueva.remindme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

public class TaskInfoActivity extends AppCompatActivity {

    // TODO: 7/1/19 STRING DI DEBUG
    private static final String TAG = "ReMe_TaskInfoActivity";

    private AppDatabase db;
    private final DbAsyncTask.DbAsyncTaskCallback dbAsyncTaskListener = new DbAsyncTask.DbAsyncTaskCallback() {
        @Override
        public void onInsertTaskCallback() {

        }

        @Override
        public void onInfoTaskCallback(Task task) {
            calendar = Calendar.getInstance();
            calendar.set(task.getYear(), task.getMonth(), task.getDayOfMonth(), task.getHourOfDay(), task.getMinute());

            TextView taskTitle = (TextView) findViewById(R.id.task_info_title);
            TextView taskPlace = (TextView) findViewById(R.id.task_info_place);
            TextView taskDescription = (TextView) findViewById(R.id.task_info_description);
            TextView taskPriority = (TextView) findViewById(R.id.task_info_priority);
            TextView taskCategory = (TextView) findViewById(R.id.task_info_category);
            TextView taskDate = (TextView) findViewById(R.id.task_info_date);
            TextView taskTime = (TextView) findViewById(R.id.task_info_time);
            TextView taskDoneDate = (TextView) findViewById(R.id.task_info_done_date);
            TextView taskDoneTime = (TextView) findViewById(R.id.task_info_done_time);
            TextView taskStatus = (TextView) findViewById(R.id.task_info_status);
            TextView taskId = (TextView) findViewById(R.id.task_info_id);

            taskTitle.setText(task.getTitle());
            taskPlace.setText(task.getPlace());
            taskDescription.setText(task.getDescription());
            taskPriority.setText(task.getPriority() + "/10");
            taskCategory.setText(task.getCategory());
            taskDate.setText(String.format("%1$td/%1$tm/%1$tY", calendar));
            taskTime.setText(String.format("%1$tH:%1$tM", calendar));
            if (task.getStatus().equals("Completed")) {
                calendar.set(task.getDoneYear(), task.getDoneMonth(), task.getDoneDayOfMonth(), task.getDoneHourOfDay(), task.getDoneMinute());
                taskDoneDate.setText(String.format("%1$td/%1$tm/%1$tY", calendar));
                taskDoneTime.setText(String.format("%1$tH:%1$tM", calendar));
            } else {
                taskDoneDate.setText("");
                taskDoneTime.setText("");
            }
            taskStatus.setText(task.getStatus());
            taskId.setText(String.valueOf(task.getId()));
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

    private static Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);

        this.db = AppDatabase.getInstance();

        new DbAsyncTask(this.db, dbAction.INFO_TASK, getIntent().getIntExtra("taskId", 0), this.dbAsyncTaskListener).execute();
    }
}
