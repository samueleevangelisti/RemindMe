package com.samueva.remindme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.List;

public class TaskInfoActivity extends AppCompatActivity {

    // TODO: 7/1/19 STRING DI DEBUG
    private static final String TAG = "ReMe_TaskInfoActivity";

    private AppDatabase db;
    private final DbAsyncTask.DbAsyncTaskListener dbAsyncTaskListener = new DbAsyncTask.DbAsyncTaskListener() {
        @Override
        public void onTaskGetByIdCallback(Task task) {
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
            taskDate.setText(String.format("%02d/%02d/%04d", task.getDayOfMonth(), task.getMonth() + 1, task.getYear()));
            taskTime.setText(String.format("%02d:%02d", task.getHourOfDay(), task.getMinute()));
            if (task.getStatus().equals("Completed")) {
                taskDoneDate.setText(String.format("%02d/%02d/%04d", task.getDoneDayOfMonth(), task.getDoneMonth() + 1, task.getDoneYear()));
                taskDoneTime.setText(String.format("%02d:%02d", task.getDoneHourOfDay(), task.getDoneMinute()));
            } else {
                taskDoneDate.setText("");
                taskDoneTime.setText("");
            }
            taskStatus.setText(task.getStatus());
            taskId.setText(String.valueOf(task.getId()));
        }

        @Override
        public void onTaskGetAllByStatusCallback(List<Task> taskList) {

        }

        @Override
        public void onTaskUpdateCallback() {

        }

        @Override
        public void onCategoryGetAllCallback(List<TaskCategory> categoryList) {

        }

        @Override
        public void onCategoryUpdateCallback() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);

        this.db = AppDatabase.getInstance();

        new DbAsyncTask(this.db, dbAction.TASK_GETBYID, getIntent().getIntExtra("taskId", 0), this.dbAsyncTaskListener).execute();
    }
}
