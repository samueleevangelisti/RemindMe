package com.samueva.remindme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddTaskActivity extends AppCompatActivity implements AddCategoryDialogFragment.AddCategoryDialogListener, DatePickerFragment.TaskDatePickerListener, TimePickerFragment.TaskTimePickerListener {

    // TODO: 5/10/19 STRINGA_DI_DEBUG
    private static final String TAG = "ReMe_AddTaskActivity";

    // SeekBar Normalization
    private final int SEEKBAR_MIN = 1;
    private final int SEEKBAR_MAX = 10;

    // AppDatabase
    private AppDatabase db;
    private final DbAsyncTask.DbAsyncTaskListener dbAsyncTaskListener = new DbAsyncTask.DbAsyncTaskListener() {
        @Override
        public void onTaskGetAllByStatusCallback(List<Task> taskList) {

        }

        @Override
        public void onTaskUpdateCallback() {
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
            new DbAsyncTask(db, dbAction.GETALL_CATEGORY, dbAsyncTaskListener).execute();
        }

        @Override
        public void onDeleteCategoryCallback() {

        }
    };

    // SeekBar
    private int seekBarValue;

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

        // SeekBar
        SeekBar seekBar = (SeekBar) findViewById(R.id.new_task_priority);
        final TextView seekBarValueText = (TextView) findViewById(R.id.new_task_priority_value);
        this.seekBarValue = seekBarNormalization(seekBar.getProgress());
        seekBarValueText.setText(seekBarValue + "/10");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBarValue = seekBarNormalization(i);
                seekBarValueText.setText(seekBarValue + "/10");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Buttons
        ImageButton addTaskCategory = (ImageButton) findViewById(R.id.add_task_category);
        addTaskCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment addCategoryDialogFragment = new AddCategoryDialogFragment();
                addCategoryDialogFragment.show(getSupportFragmentManager(), "acdfmanagerfromata");
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_task_optionmenu_create) {
            TextView newTaskTitle = (TextView) findViewById(R.id.new_task_title);
            TextView newTaskPlace = (TextView) findViewById(R.id.new_task_place);
            Spinner newTaskCategory = (Spinner) findViewById(R.id.new_task_category);
            TextView newTaskNote = (TextView) findViewById(R.id.new_task_description);
            Task task = new Task(newTaskTitle.getText().toString(), newTaskCalendar, newTaskPlace.getText().toString(), newTaskNote.getText().toString(), newTaskCategory.getSelectedItem().toString(), seekBarValue, "Pending");
            new DbAsyncTask(db, dbAction.TASK_INSERTALL, task, dbAsyncTaskListener).execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int seekBarNormalization(int progress) {
        return (progress * (this.SEEKBAR_MAX - this.SEEKBAR_MIN) / 100) + this.SEEKBAR_MIN;
    }

    private void addTaskActivityFinish() {
        Intent intent = new Intent();
        intent.putExtra("new", (boolean) true);
        setResult(RESULT_OK, intent);
        super.finish();
    }

    @Override
    public void onDialogPositiveClick(String category) {
        TaskCategory newCategory = new TaskCategory(category, false);
        new DbAsyncTask(this.db, dbAction.INSERT_CATEGORY, this.dbAsyncTaskListener, newCategory).execute();
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
