package com.samueva.remindme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class TaskInfoActivity extends AppCompatActivity {

    // TODO: 7/1/19 STRING DI DEBUG
    private static final String TAG = "ReMe_TaskInfoActivity";

    // Need for update
    private boolean update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);

        // Need for update
        this.update = false;

        Task task = getIntent().getExtras().getParcelable("task");

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

        taskTitle.setText(task.getTitle());
        taskPlace.setText(task.getPlace());
        taskDescription.setText(task.getDescription());
        taskPriority.setText(getString(R.string.priority) + ": " + task.getPriority() + "/10");
        taskCategory.setText(getString(R.string.category) + ": " + task.getCategory());
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.task_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.task_info_optionmenu_modify) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("update", this.update);
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
