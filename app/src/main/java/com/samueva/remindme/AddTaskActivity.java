package com.samueva.remindme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddTaskActivity extends AppCompatActivity implements DatePickerFragment.TaskDatePickerListener, TimePickerFragment.TaskTimePickerListener {

    // TODO: 5/10/19 STRINGA_DI_DEBUG
    private static final String TAG = "ReMe_AddTaskActivity";

    // AppDatabase
    private AppDatabase db;
    private final DbAsyncTask.DbAsyncTaskCallback dbAsyncTaskListener = new DbAsyncTask.DbAsyncTaskCallback() {
        @Override
        public void onInsertTaskCallback() {
            addTaskActivityFinish();
        }

        @Override
        public void onInfoTaskCallback(Task task) {

        }

        @Override
        public void onGetAllCategoryCallback(List<TaskCategory> categoryList) {
            List<String> categories = new ArrayList<String>();
            for (TaskCategory category : categoryList) {
                categories.add(category.getName());
            }
            Spinner newTaskCategory = (Spinner) findViewById(R.id.new_task_category);
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categories);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            newTaskCategory.setAdapter(spinnerAdapter);
        }

        @Override
        public void onInsertCategoryCallback() {

        }

        @Override
        public void onDeleteCategoryCallback() {

        }
    };

    // Calendar
    private static Calendar newTaskCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // AppDatabase
        this.db = AppDatabase.getInstance();

        // Date and Time
        this.newTaskCalendar = Calendar.getInstance();
        TextView newTaskDate = (TextView) findViewById(R.id.new_task_date);
        newTaskDate.setText(String.format("%1$td/%1$tm/%1$tY", this.newTaskCalendar));
        TextView newTaskTime = (TextView) findViewById(R.id.new_task_time);
        newTaskTime.setText(String.format("%1$tH : %1$tM", this.newTaskCalendar));

        // Spinner
        new DbAsyncTask(this.db, dbAction.GETALL_CATEGORY, this.dbAsyncTaskListener).execute();

        // Buttons
        Button createTask = (Button) findViewById(R.id.create_task);
        createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView newTaskTitle = (TextView) findViewById(R.id.new_task_title);
                TextView newTaskPlace = (TextView) findViewById(R.id.new_task_place);
                Spinner newTaskCategory = (Spinner) findViewById(R.id.new_task_category);
                TextView newTaskNote = (TextView) findViewById(R.id.new_task_description);
                Task task = new Task(newTaskTitle.getText().toString(), newTaskCalendar, newTaskPlace.getText().toString(), newTaskNote.getText().toString(), newTaskCategory.getSelectedItem().toString(), "Pending");
                new DbAsyncTask(db, dbAction.INSERT_TASK, task, dbAsyncTaskListener).execute();
            }
        });
        /*Button editCustomCategory = (Button) findViewById(R.id.edit_custom_category);
        editCustomCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryDialogFragment newFragment = new CategoryDialogFragment();
                newFragment.setRecyclerAdapter(categoryList);
                newFragment.show(getSupportFragmentManager(), "customCategory");
            }
        });*/
        Button setTaskDate = (Button) findViewById(R.id.set_task_date);
        setTaskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        Button setTaskTime = (Button) findViewById(R.id.set_task_time);
        setTaskTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });
    }

    private void addTaskActivityFinish() {
        Intent intent = new Intent();
        intent.putExtra("new", (boolean) true);
        setResult(RESULT_OK, intent);
        super.finish();
    }

    @Override
    public void onDateSetReady(int year, int month, int dayOfMonth) {
        this.newTaskCalendar.set(year, month, dayOfMonth);
        TextView newTaskDate = (TextView) findViewById(R.id.new_task_date);
        newTaskDate.setText(String.format("%1$td/%1$tm/%1$tY", this.newTaskCalendar));
    }

    @Override
    public void onTimeSetReady(int hourOfDay, int minute) {
        this.newTaskCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        this.newTaskCalendar.set(Calendar.MINUTE, minute);
        TextView newTaskTime = (TextView) findViewById(R.id.new_task_time);
        newTaskTime.setText(String.format("%1$tH:%1$tM", this.newTaskCalendar));
    }
}
