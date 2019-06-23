package com.samueva.remindme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Calendar;

public class TaskInfoActivity extends AppCompatActivity {

    private Task task;
    private static Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);

        this.task = (Task) getIntent().getExtras().getParcelable("task");
        this.calendar = Calendar.getInstance();
        this.calendar.set(this.task.getYear(), this.task.getMonth(), this.task.getDayOfMonth(), this.task.getHourOfDay(), this.task.getMinute());

        TextView taskTitle = (TextView) findViewById(R.id.task_info_title);
        TextView taskDate = (TextView) findViewById(R.id.task_info_date);
        TextView taskTime = (TextView) findViewById(R.id.task_info_time);
        TextView taskPlace = (TextView) findViewById(R.id.task_info_place);
        TextView taskStatus = (TextView) findViewById(R.id.task_info_status);

        taskTitle.setText(this.task.getTitle());
        taskDate.setText(String.format("%1$td/%1$tm/%1$tY", this.calendar));
        taskTime.setText(String.format("%1$tH:%1$tM", this.calendar));
        taskPlace.setText(this.task.getPlace());
        taskStatus.setText(this.task.getStatus());
    }
}
