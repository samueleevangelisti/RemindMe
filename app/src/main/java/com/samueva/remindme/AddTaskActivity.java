package com.samueva.remindme;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

    // Need for update
    private boolean update;

    // AppDatabase
    private AppDatabase db;
    private final DbAsyncTask.DbAsyncTaskListener dbAsyncTaskListener = new DbAsyncTask.DbAsyncTaskListener() {
        @Override
        public void onTaskGetByIdCallback(Task task) {

        }

        @Override
        public void onTaskGetAllByStatusCallback(List<Task> taskList) {

        }

        @Override
        public void onTaskUpdateCallback(long taskId) {
            Intent intent = new Intent();
            intent.setAction("com.samueva.remindme.broadcast");
            intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), (int) taskId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, newTaskCalendar.getTimeInMillis(), pendingIntent);

            finish();
        }

        @Override
        public void onCategoryGetAllCallback(List<TaskCategory> categoryList) {
            List<String> categories = new ArrayList<String>();
            for (TaskCategory category : categoryList) {
                categories.add(category.getName());
            }
            Spinner newTaskCategory = (Spinner) findViewById(R.id.new_task_category);
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categories);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            newTaskCategory.setAdapter(spinnerAdapter);
            newTaskCategory.setSelection(spinnerAdapter.getCount() - 1);
        }

        @Override
        public void onCategoryUpdateCallback() {
            new DbAsyncTask(db, dbAction.CATEGORY_GETALL, dbAsyncTaskListener).execute();
        }
    };

    // SeekBar
    private int seekBarValue;

    // New Task
    private Task newTask;

    // Calendar
    private Calendar newTaskCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // Need for update
        this.update = false;

        // AppDatabase
        this.db = AppDatabase.getInstance();

        // Spinner
        new DbAsyncTask(this.db, dbAction.CATEGORY_GETALL, this.dbAsyncTaskListener).execute();

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

        // Date and Time
        this.newTaskCalendar = Calendar.getInstance();
        TextView newTaskDate = (TextView) findViewById(R.id.new_task_date);
        newTaskDate.setText(String.format("%1$td/%1$tm/%1$tY", this.newTaskCalendar));
        TextView newTaskTime = (TextView) findViewById(R.id.new_task_time);
        newTaskTime.setText(String.format("%1$tH:%1$tM", this.newTaskCalendar));

        // Buttons
        ImageButton addTaskCategory = (ImageButton) findViewById(R.id.add_task_add_category);
        addTaskCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment addCategoryDialogFragment = new AddCategoryDialogFragment();
                addCategoryDialogFragment.show(getSupportFragmentManager(), "acdfmanagerfromata");
            }
        });
        ImageButton setTaskDate = (ImageButton) findViewById(R.id.new_task_set_date);
        setTaskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePickerata");
            }
        });
        ImageButton setTaskTime = (ImageButton) findViewById(R.id.new_task_set_time);
        setTaskTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePickerata");
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
            EditText newTaskTitle = (EditText) findViewById(R.id.new_task_title);
            EditText newTaskPlace = (EditText) findViewById(R.id.new_task_place);
            Spinner newTaskCategory = (Spinner) findViewById(R.id.new_task_category);
            EditText newTaskDescription = (EditText) findViewById(R.id.new_task_description);
            this.newTask = new Task(newTaskTitle.getText().toString(), this.newTaskCalendar, newTaskPlace.getText().toString(), newTaskDescription.getText().toString(), newTaskCategory.getSelectedItem().toString(), seekBarValue, "Pending");
            this.update = true;
            new DbAsyncTask(this.db, dbAction.TASK_INSERT, this.newTask, this.dbAsyncTaskListener).execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int seekBarNormalization(int progress) {
        int SEEKBAR_MIN = 1;
        int SEEKBAR_MAX = 10;
        return (progress * (SEEKBAR_MAX - SEEKBAR_MIN) / 100) + SEEKBAR_MIN;
    }

    @Override
    public void onAddCategoryDialogPositiveClick(String category) {
        List<TaskCategory> categoryList = new ArrayList<TaskCategory>();
        categoryList.add(new TaskCategory(category, false, 0, 0));
        new DbAsyncTask(this.db, dbAction.CATEGORY_INSERTALL, categoryList , dbAsyncTaskListener).execute();
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

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("update", this.update);
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
