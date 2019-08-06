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

public class UpdateTaskActivity extends AppCompatActivity implements AddCategoryDialogFragment.AddCategoryDialogListener, DatePickerFragment.TaskDatePickerListener, TimePickerFragment.TaskTimePickerListener {

    // TODO: 8/5/19 STRINGA DI DEBUG
    private static final String TAG = "ReMe_UpdateTask";

    // Need for update
    private Boolean update;

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
            Spinner newTaskCategory = (Spinner) findViewById(R.id.new_task_category);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // Need for update
        this.update = false;

        // Task
        this.task = getIntent().getExtras().getParcelable("task");

        // Spinner
        new DbAsyncTask(AppDatabase.getInstance(), dbAction.CATEGORY_GETALL, this.dbAsyncTaskListener).execute();

        // SeekBar
        SeekBar seekBar = (SeekBar) findViewById(R.id.new_task_priority);
        seekBar.setProgress(this.task.getPriority() * 10);
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
        this.taskCalendar = Calendar.getInstance();
        this.taskCalendar.set(this.task.getYear(), this.task.getMonth(), this.task.getDayOfMonth(), this.task.getHourOfDay(), this.task.getMinute());
        TextView newTaskDate = (TextView) findViewById(R.id.new_task_date);
        newTaskDate.setText(String.format("%1$td/%1$tm/%1$tY", this.taskCalendar));
        TextView newTaskTime = (TextView) findViewById(R.id.new_task_time);
        newTaskTime.setText(String.format("%1$tH:%1$tM", this.taskCalendar));

        // Buttons
        ImageButton addTaskCategory = (ImageButton) findViewById(R.id.add_task_category);
        addTaskCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment addCategoryDialogFragment = new AddCategoryDialogFragment();
                addCategoryDialogFragment.show(getSupportFragmentManager(), "acdfmanagerfromuta");
            }
        });
        ImageButton setTaskDate = (ImageButton) findViewById(R.id.set_task_date);
        setTaskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePickeruta");
            }
        });
        ImageButton setTaskTime = (ImageButton) findViewById(R.id.set_task_time);
        setTaskTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePickeruta");
            }
        });

        // TextView
        EditText taskTitle = (EditText) findViewById(R.id.new_task_title);
        taskTitle.setText(this.task.getTitle());
        EditText taskPlace = (EditText) findViewById(R.id.new_task_place);
        taskPlace.setText(this.task.getPlace());
        EditText taskDescription = (EditText) findViewById(R.id.new_task_description);
        taskDescription.setText(this.task.getDescription());
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
            EditText taskTitle = (EditText) findViewById(R.id.new_task_title);
            EditText taskPlace = (EditText) findViewById(R.id.new_task_place);
            Spinner taskCategory = (Spinner) findViewById(R.id.new_task_category);
            EditText taskDescription = (EditText) findViewById(R.id.new_task_description);
            this.task.setTitle(taskTitle.getText().toString());
            this.task.setCalendar(this.taskCalendar);
            this.task.setPlace(taskPlace.getText().toString());
            this.task.setCategory(taskCategory.getSelectedItem().toString());
            this.task.setDescription(taskDescription.getText().toString());
            this.update = true;
            new DbAsyncTask(AppDatabase.getInstance(), dbAction.TASK_UPDATE, this.task, categoryOld, this.dbAsyncTaskListener).execute();
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
        this.taskCalendar.set(year, month, dayOfMonth);
        TextView newTaskDate = (TextView) findViewById(R.id.new_task_date);
        newTaskDate.setText(String.format("%1$td/%1$tm/%1$tY", this.taskCalendar));
    }

    @Override
    public void onTimeSetReady(int hourOfDay, int minute) {
        this.taskCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        this.taskCalendar.set(Calendar.MINUTE, minute);
        TextView newTaskTime = (TextView) findViewById(R.id.new_task_time);
        newTaskTime.setText(String.format("%1$tH:%1$tM", this.taskCalendar));
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
