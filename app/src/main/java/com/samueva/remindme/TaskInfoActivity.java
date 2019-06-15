package com.samueva.remindme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Calendar;

public class TaskInfoActivity extends AppCompatActivity {

    private static Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            TextView taskId = (TextView) findViewById(R.id.task_info_id);
            TextView taskTitle = (TextView) findViewById(R.id.task_info_title);
            TextView taskDate = (TextView) findViewById(R.id.task_info_date);
            TextView taskTime = (TextView) findViewById(R.id.task_info_time);
            TextView taskPlace = (TextView) findViewById(R.id.task_info_place);
            TextView taskStatus = (TextView) findViewById(R.id.task_info_status);
            this.calendar = Calendar.getInstance();
            this.calendar.set(extras.getInt("year"), extras.getInt("month"), extras.getInt("dayOfMonth"), extras.getInt("hourOfDay"), extras.getInt("minute"));
            taskId.setText(String.valueOf(extras.getInt("id")));
            taskTitle.setText(extras.getString("title"));
            taskDate.setText(String.format("%1$td/%1$tm/%1$tY", this.calendar));
            taskTime.setText(String.format("%1$tH : %1$tM", this.calendar));
            taskPlace.setText(extras.getString("place"));
            taskStatus.setText(extras.getString("status"));

        }
    }
}
