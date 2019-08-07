package com.samueva.remindme;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

enum calendarType {
    TASKCALENDAR,
    TASKDONECALENDAR
}

public class UpdateTaskActivity extends AppCompatActivity implements AddCategoryDialogFragment.AddCategoryDialogListener, DatePickerFragment.TaskDatePickerListener, TimePickerFragment.TaskTimePickerListener {

    // TODO: 8/5/19 STRINGA DI DEBUG
    private static final String TAG = "ReMe_UpdateTask";

    // Need for update
    private Boolean update;

    // Calendar modified
    private calendarType myCalendarType;

    // Task
    private Task task;

    // AppDatabase
    private final DbAsyncTask.DbAsyncTaskListener dbAsyncTaskListener = new DbAsyncTask.DbAsyncTaskListener() {
        @Override
        public void onTaskGetByIdCallback(Task task) {

        }

        @Override
        public void onTaskGetAllByStatusCallback(List<Task> taskList) {

        }

        @Override
        public void onTaskUpdateCallback() {
            finish();
        }

        @Override
        public void onCategoryGetAllCallback(List<TaskCategory> categoryList) {
            List<String> categories = new ArrayList<String>();
            for (TaskCategory category : categoryList) {
                categories.add(category.getName());
            }
            Spinner newTaskCategory = (Spinner) findViewById(R.id.update_task_category);
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categories);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            newTaskCategory.setAdapter(spinnerAdapter);
            newTaskCategory.setSelection(spinnerAdapter.getPosition(task.getCategory()));
        }

        @Override
        public void onCategoryUpdateCallback() {
            new DbAsyncTask(AppDatabase.getInstance(), dbAction.CATEGORY_GETALL, dbAsyncTaskListener).execute();
        }
    };

    // SeekBar
    private int seekBarValue;

    // Calendar
    private Calendar taskCalendar;
    private Calendar taskDoneCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);

        // Need for update
        this.update = false;

        // Task
        this.task = getIntent().getExtras().getParcelable("task");

        // Spinner
        new DbAsyncTask(AppDatabase.getInstance(), dbAction.CATEGORY_GETALL, this.dbAsyncTaskListener).execute();

        // SeekBar
        SeekBar seekBar = (SeekBar) findViewById(R.id.update_task_priority);
        seekBar.setProgress(this.task.getPriority() * 10);
        final TextView seekBarValueText = (TextView) findViewById(R.id.update_task_priority_value);
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
        this.taskCalendar = Calendar.getInstance();
        this.taskCalendar.set(this.task.getYear(), this.task.getMonth(), this.task.getDayOfMonth(), this.task.getHourOfDay(), this.task.getMinute());
        TextView updateTaskDate = (TextView) findViewById(R.id.update_task_date);
        updateTaskDate.setText(String.format("%1$td/%1$tm/%1$tY", this.taskCalendar));
        TextView updateTaskTime = (TextView) findViewById(R.id.update_task_time);
        updateTaskTime.setText(String.format("%1$tH:%1$tM", this.taskCalendar));

        // Buttons
        ImageButton addTaskCategory = (ImageButton) findViewById(R.id.update_task_add_category);
        addTaskCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment addCategoryDialogFragment = new AddCategoryDialogFragment();
                addCategoryDialogFragment.show(getSupportFragmentManager(), "acdfmanagerfromuta");
            }
        });
        ImageButton setTaskDate = (ImageButton) findViewById(R.id.update_task_set_date);
        setTaskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCalendarType = calendarType.TASKCALENDAR;
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePickeruta");
            }
        });
        ImageButton setTaskTime = (ImageButton) findViewById(R.id.update_task_set_time);
        setTaskTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCalendarType = calendarType.TASKCALENDAR;
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePickeruta");
            }
        });
        ImageButton setTaskDoneDate = (ImageButton) findViewById(R.id.update_task_set_done_date);
        setTaskDoneDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCalendarType = calendarType.TASKDONECALENDAR;
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "doneDatePickeruta");
            }
        });
        ImageButton setTaskDoneTime = (ImageButton) findViewById(R.id.update_task_set_done_time);
        setTaskDoneTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCalendarType = calendarType.TASKDONECALENDAR;
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "DoneTimePickeruta");
            }
        });

        // TextView
        EditText taskTitle = (EditText) findViewById(R.id.update_task_title);
        taskTitle.setText(this.task.getTitle());
        EditText taskPlace = (EditText) findViewById(R.id.update_task_place);
        taskPlace.setText(this.task.getPlace());
        EditText taskDescription = (EditText) findViewById(R.id.update_task_description);
        taskDescription.setText(this.task.getDescription());

        // DoneDate and DoneTime
        TextView updateTaskDoneDate = (TextView) findViewById(R.id.update_task_done_date);
        TextView updateTaskDoneTime = (TextView) findViewById(R.id.update_task_done_time);
        if (!(this.task.getStatus().equals("Completed")) && !(this.task.getStatus().equals("Failed"))) {
            updateTaskDoneDate.setText("");
            setTaskDoneDate.setEnabled(false);
            setTaskDoneDate.setAlpha((float) 0.5);
            updateTaskDoneTime.setText("");
            setTaskDoneTime.setEnabled(false);
            setTaskDoneTime.setAlpha((float) 0.5);
        } else {
            this.taskDoneCalendar = Calendar.getInstance();
            this.taskDoneCalendar.set(this.task.getDoneYear(), this.task.getDoneMonth(), this.task.getDoneDayOfMonth(), this.task.getDoneHourOfDay(), this.task.getDoneMinute());
            updateTaskDoneDate.setText(String.format("%1$td/%1$tm/%1$tY", this.taskDoneCalendar));
            updateTaskDoneTime.setText(String.format("%1$tH:%1$tM", this.taskDoneCalendar));
        }
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
            String categoryOld = this.task.getCategory();
            EditText taskTitle = (EditText) findViewById(R.id.update_task_title);
            EditText taskPlace = (EditText) findViewById(R.id.update_task_place);
            Spinner taskCategory = (Spinner) findViewById(R.id.update_task_category);
            EditText taskDescription = (EditText) findViewById(R.id.update_task_description);
            this.task.setTitle(taskTitle.getText().toString());
            this.task.setPlace(taskPlace.getText().toString());
            this.task.setDescription(taskDescription.getText().toString());
            this.task.setCategory(taskCategory.getSelectedItem().toString());
            this.task.setPriority(this.seekBarValue);
            // TODO: 8/6/19 status
            if (this.taskCalendar.compareTo(this.taskDoneCalendar) >= 0) {
                // TODO: 8/7/19 dialog di errore perchè le due date non sono cronologicamente corrette
                Log.d(TAG, "Il task viene terminato prima di iniziare, impossibile");
            } else {
                this.task.setCalendar(this.taskCalendar);
                this.task.setDoneCalendar(this.taskDoneCalendar);
                this.update = true;
                new DbAsyncTask(AppDatabase.getInstance(), dbAction.TASK_UPDATE, this.task, categoryOld, this.dbAsyncTaskListener).execute();
            }
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
        new DbAsyncTask(AppDatabase.getInstance(), dbAction.CATEGORY_INSERTALL, categoryList , dbAsyncTaskListener).execute();
    }

    @Override
    public void onDateSetReady(int year, int month, int dayOfMonth) {
        TextView taskDate;
        switch (this.myCalendarType) {
            case TASKCALENDAR:
                this.taskCalendar.set(year, month, dayOfMonth);
                taskDate = (TextView) findViewById(R.id.update_task_date);
                taskDate.setText(String.format("%1$td/%1$tm/%1$tY", this.taskCalendar));
                break;
            case TASKDONECALENDAR:
                this.taskDoneCalendar.set(year, month, dayOfMonth);
                taskDate = (TextView) findViewById(R.id.update_task_done_date);
                taskDate.setText(String.format("%1$td/%1$tm/%1$tY", this.taskDoneCalendar));
                break;
            default:
                break;
        }
    }

    @Override
    public void onTimeSetReady(int hourOfDay, int minute) {
        TextView taskTime;
        switch (this.myCalendarType) {
            case TASKCALENDAR:
                this.taskCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                this.taskCalendar.set(Calendar.MINUTE, minute);
                taskTime = (TextView) findViewById(R.id.update_task_time);
                taskTime.setText(String.format("%1$tH:%1$tM", this.taskCalendar));
                break;
            case TASKDONECALENDAR:
                this.taskDoneCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                this.taskDoneCalendar.set(Calendar.MINUTE, minute);
                taskTime = (TextView) findViewById(R.id.update_task_done_time);
                taskTime.setText(String.format("%1$tH:%1$tM", this.taskDoneCalendar));
                break;
            default:
                break;
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("update", this.update);
        if (this.update) {
            intent.putExtra("task", this.task);
        }
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
