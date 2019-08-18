package com.samueva.remindme;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
        public void onTaskUpdateCallback(int taskId) {
            Log.d(TAG, "newTask id : " + task.getId());

            PendingIntent pendingIntent = NotificationBuilder.build(getApplicationContext(), task.getId(), task);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            if (task.getPriority() >= 6 && task.getStatus().equals("Pending")) {
                    Log.d(TAG, "Setting Notification");
                    Log.d(TAG, taskNotificationCalendar.get(Calendar.YEAR) + " " + (taskNotificationCalendar.get(Calendar.MONTH) + 1) + " " + taskNotificationCalendar.get(Calendar.DAY_OF_MONTH) + " " + taskNotificationCalendar.get(Calendar.HOUR_OF_DAY) + " " + taskNotificationCalendar.get(Calendar.MINUTE) + " " + taskNotificationCalendar.get(Calendar.SECOND));
                    alarmManager.set(AlarmManager.RTC_WAKEUP, taskNotificationCalendar.getTimeInMillis(), pendingIntent);
            } else {
                Log.d(TAG, "Deleting Notification");
                alarmManager.cancel(pendingIntent);
            }

            finish();
        }

        @Override
        public void onTaskGetAllByFiltersCallback(List<Task> taskList) {

        }

        @Override
        public void onCategoryGetAllCallback(List<TaskCategory> categoryList) {
            List<String> categories = new ArrayList<String>();
            for (TaskCategory category : categoryList) {
                categories.add(category.getName());
            }
            Spinner updateTaskCategory = (Spinner) findViewById(R.id.update_task_category);
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categories);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            updateTaskCategory.setAdapter(spinnerAdapter);
            updateTaskCategory.setSelection(spinnerAdapter.getPosition(task.getCategory()));
        }

        @Override
        public void onCategoryUpdateCallback() {
            new DbAsyncTask(AppDatabase.getInstance(), dbAction.CATEGORY_GETALL, dbAsyncTaskListener).execute();
        }
    };

    // SeekBar
    private int seekBarValue;

    // DoneDate and DoneTime
    private ImageButton setTaskDoneDate;
    private ImageButton setTaskDoneTime;
    private TextView updateTaskDoneDate;
    private TextView updateTaskDoneTime;

    // NotificationDate and NotificationTime
    private ImageButton setTaskNotificationDate;
    private ImageButton setTaskNotificationTime;
    private TextView updateTaskNotificationDate;
    private TextView updateTaskNotificationTime;
    private TextView notificationStatus;

    // Calendar
    private Calendar taskCalendar;
    private Calendar taskDoneCalendar;
    private Calendar taskNotificationCalendar;

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

        // Date and Time
        this.taskCalendar = Calendar.getInstance();
        this.taskCalendar.set(Calendar.SECOND, 0);
        this.taskDoneCalendar = Calendar.getInstance();
        this.taskDoneCalendar.set(Calendar.SECOND, 0);
        this.taskNotificationCalendar = Calendar.getInstance();
        this.taskNotificationCalendar.set(Calendar.SECOND, 0);
        this.taskCalendar.set(this.task.getYear(), this.task.getMonth(), this.task.getDayOfMonth(), this.task.getHourOfDay(), this.task.getMinute());
        TextView updateTaskDate = (TextView) findViewById(R.id.update_task_date);
        updateTaskDate.setText(String.format("%1$td/%1$tm/%1$tY", this.taskCalendar));
        TextView updateTaskTime = (TextView) findViewById(R.id.update_task_time);
        updateTaskTime.setText(String.format("%1$tH:%1$tM", this.taskCalendar));

        // NotificationDate and NotificationTime
        this.updateTaskNotificationDate = (TextView) findViewById(R.id.update_task_notification_date);
        this.updateTaskNotificationTime = (TextView) findViewById(R.id.update_task_notification_time);
        this.setTaskNotificationDate = (ImageButton) findViewById(R.id.update_task_set_notification_date);
        this.setTaskNotificationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCalendarType = calendarType.TASKNOTIFICATIONCALENDAR;
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "notificationDatePickeruta");
            }
        });
        this.setTaskNotificationTime = (ImageButton) findViewById(R.id.update_task_set_notification_time);
        this.setTaskNotificationTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCalendarType = calendarType.TASKNOTIFICATIONCALENDAR;
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "notificationTimePickeruta");
            }
        });

        // SeekBar
        SeekBar seekBar = (SeekBar) findViewById(R.id.update_task_priority);
        seekBar.setProgress(this.task.getPriority() * 10);
        final TextView seekBarValueText = (TextView) findViewById(R.id.update_task_priority_value);
        this.notificationStatus = (TextView) findViewById(R.id.update_task_notification_status);
        this.seekBarValue = seekBarNormalization(seekBar.getProgress());
        seekBarValueText.setText(seekBarValue + "/10");
        if (this.task.getPriority() >= 6) {
            this.taskNotificationCalendar.set(this.task.getNotificationYear(), this.task.getNotificationMonth(), this.task.getNotificationDayOfMonth(), this.task.getNotificationHourOfDay(), this.task.getNotificationMinute());
            this.notificationStatus.setText("Notification Enabled");
            this.updateTaskNotificationDate.setText(String.format("%1$td/%1$tm/%1$tY", this.taskNotificationCalendar));
            this.setTaskNotificationDate.setEnabled(true);
            this.setTaskNotificationDate.setAlpha((float) 1);
            this.updateTaskNotificationTime.setText(String.format("%1$tH:%1$tM", this.taskNotificationCalendar));
            this.setTaskNotificationTime.setEnabled(true);
            this.setTaskNotificationTime.setAlpha((float) 1);
        } else {
            this.notificationStatus.setText("Notification Disabled");
            this.updateTaskNotificationDate.setText("");
            this.setTaskNotificationDate.setEnabled(false);
            this.setTaskNotificationDate.setAlpha((float) 0.5);
            this.updateTaskNotificationTime.setText("");
            this.setTaskNotificationTime.setEnabled(false);
            this.setTaskNotificationTime.setAlpha((float) 0.5);
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBarValue = seekBarNormalization(i);
                seekBarValueText.setText(seekBarValue + "/10");
                if (seekBarValue >= 6) {
                    notificationStatus.setText("Notification Enabled");
                    updateTaskNotificationDate.setText(String.format("%1$td/%1$tm/%1$tY", taskNotificationCalendar));
                    setTaskNotificationDate.setEnabled(true);
                    setTaskNotificationDate.setAlpha((float) 1);
                    updateTaskNotificationTime.setText(String.format("%1$tH:%1$tM", taskNotificationCalendar));
                    setTaskNotificationTime.setEnabled(true);
                    setTaskNotificationTime.setAlpha((float) 1);
                } else {
                    notificationStatus.setText("Notification Disabled");
                    updateTaskNotificationDate.setText("");
                    setTaskNotificationDate.setEnabled(false);
                    setTaskNotificationDate.setAlpha((float) 0.5);
                    updateTaskNotificationTime.setText("");
                    setTaskNotificationTime.setEnabled(false);
                    setTaskNotificationTime.setAlpha((float) 0.5);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

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
        this.setTaskDoneDate = (ImageButton) findViewById(R.id.update_task_set_done_date);
        this.setTaskDoneDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCalendarType = calendarType.TASKDONECALENDAR;
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "doneDatePickeruta");
            }
        });
        this.setTaskDoneTime = (ImageButton) findViewById(R.id.update_task_set_done_time);
        this.setTaskDoneTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCalendarType = calendarType.TASKDONECALENDAR;
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "doneTimePickeruta");
            }
        });

        // TextView
        EditText taskTitle = (EditText) findViewById(R.id.update_task_title);
        taskTitle.setText(this.task.getTitle());
        EditText taskPlace = (EditText) findViewById(R.id.update_task_place);
        taskPlace.setText(this.task.getPlace());
        EditText taskDescription = (EditText) findViewById(R.id.update_task_description);
        taskDescription.setText(this.task.getDescription());

        // Radio Buttons
        RadioButton radioPending = (RadioButton) findViewById(R.id.update_task_radio_pending);
        radioPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRadioClicked(view);
            }
        });
        RadioButton radioCompleted = (RadioButton) findViewById(R.id.update_task_radio_completed);
        radioCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRadioClicked(view);
            }
        });
        switch (this.task.getStatus()) {
            case "Pending":
                radioPending.setChecked(true);
                break;
            case "Completed":
                radioCompleted.setChecked(true);
                break;
            default:
                break;
        }

        // DoneDate and DoneTime
        this.updateTaskDoneDate = (TextView) findViewById(R.id.update_task_done_date);
        this.updateTaskDoneTime = (TextView) findViewById(R.id.update_task_done_time);
        if (!(this.task.getStatus().equals("Completed"))) {
            this.updateTaskDoneDate.setText("");
            this.setTaskDoneDate.setEnabled(false);
            this.setTaskDoneDate.setAlpha((float) 0.5);
            this.updateTaskDoneTime.setText("");
            this.setTaskDoneTime.setEnabled(false);
            this.setTaskDoneTime.setAlpha((float) 0.5);
        } else {
            this.taskDoneCalendar.set(this.task.getDoneYear(), this.task.getDoneMonth(), this.task.getDoneDayOfMonth(), this.task.getDoneHourOfDay(), this.task.getDoneMinute());
            this.updateTaskDoneDate.setText(String.format("%1$td/%1$tm/%1$tY", this.taskDoneCalendar));
            this.updateTaskDoneTime.setText(String.format("%1$tH:%1$tM", this.taskDoneCalendar));
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
            String statusOld = this.task.getStatus();
            EditText taskTitle = (EditText) findViewById(R.id.update_task_title);
            EditText taskPlace = (EditText) findViewById(R.id.update_task_place);
            Spinner taskCategory = (Spinner) findViewById(R.id.update_task_category);
            EditText taskDescription = (EditText) findViewById(R.id.update_task_description);
            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.update_task_radio_group);
            this.task.setTitle(taskTitle.getText().toString());
            this.task.setPlace(taskPlace.getText().toString());
            this.task.setDescription(taskDescription.getText().toString());
            this.task.setCategory(taskCategory.getSelectedItem().toString());
            this.task.setPriority(this.seekBarValue);
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.update_task_radio_pending:
                    this.task.setStatus("Pending");
                    break;
                case R.id.update_task_radio_completed:
                    this.task.setStatus("Completed");
                    break;
                default:
                    break;
            }
            if ((this.task.getStatus().equals("Completed")) && this.taskCalendar.compareTo(this.taskDoneCalendar) >= 0) {
                DialogFragment wrongDateDialogFragment = new WrongDateDialogFragment();
                wrongDateDialogFragment.show(getSupportFragmentManager(), "wddfmanagerfromuta");
            } else {
                this.task.setCalendar(this.taskCalendar);
                this.task.setDoneCalendar(this.taskDoneCalendar);
                if (this.task.getPriority() >= 6 && this.task.getStatus().equals("Pending")) {
                    this.task.setNotificationCalendar(this.taskNotificationCalendar);
                }
                this.update = true;
                new DbAsyncTask(AppDatabase.getInstance(), dbAction.TASK_UPDATE, this.task, categoryOld, statusOld, this.dbAsyncTaskListener).execute();
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

    private void onRadioClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.update_task_radio_pending:
                if (checked) {
                    this.updateTaskDoneDate.setText("");
                    this.setTaskDoneDate.setEnabled(false);
                    this.setTaskDoneDate.setAlpha((float) 0.5);
                    this.updateTaskDoneTime.setText("");
                    this.setTaskDoneTime.setEnabled(false);
                    this.setTaskDoneTime.setAlpha((float) 0.5);
                    this.notificationStatus.setText("Notification Enabled");
                    this.updateTaskNotificationDate.setText(String.format("%1$td/%1$tm/%1$tY", this.taskNotificationCalendar));
                    this.setTaskNotificationDate.setEnabled(true);
                    this.setTaskNotificationDate.setAlpha((float) 1);
                    this.updateTaskNotificationTime.setText(String.format("%1$tH:%1$tM", this.taskNotificationCalendar));
                    this.setTaskNotificationTime.setEnabled(true);
                    this.setTaskNotificationTime.setAlpha((float) 1);
                }
                break;
            case R.id.update_task_radio_completed:
                if (checked) {
                    this.updateTaskDoneDate.setText(String.format("%1$td/%1$tm/%1$tY", this.taskDoneCalendar));
                    this.setTaskDoneDate.setEnabled(true);
                    this.setTaskDoneDate.setAlpha((float) 1);
                    this.updateTaskDoneTime.setText(String.format("%1$tH:%1$tM", this.taskDoneCalendar));
                    this.setTaskDoneTime.setEnabled(true);
                    this.setTaskDoneTime.setAlpha((float) 1);
                    this.notificationStatus.setText("Notification Disabled");
                    this.updateTaskNotificationDate.setText("");
                    this.setTaskNotificationDate.setEnabled(false);
                    this.setTaskNotificationDate.setAlpha((float) 0.5);
                    this.updateTaskNotificationTime.setText("");
                    this.setTaskNotificationTime.setEnabled(false);
                    this.setTaskNotificationTime.setAlpha((float) 0.5);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onAddCategoryDialogPositiveClick(String category) {
        List<TaskCategory> categoryList = new ArrayList<TaskCategory>();
        categoryList.add(new TaskCategory(category, false, 0, 0));
        new DbAsyncTask(AppDatabase.getInstance(), dbAction.CATEGORY_INSERTALL, categoryList , dbAsyncTaskListener).execute();
    }

    @Override
    public void onDateSetReady(int year, int month, int dayOfMonth) {
        switch (this.myCalendarType) {
            case TASKCALENDAR:
                this.taskCalendar.set(year, month, dayOfMonth);
                TextView taskDate = (TextView) findViewById(R.id.update_task_date);
                taskDate.setText(String.format("%1$td/%1$tm/%1$tY", this.taskCalendar));
                this.taskNotificationCalendar.set(year, month, dayOfMonth);
                if (this.seekBarValue >= 6) {
                    this.updateTaskNotificationDate.setText(String.format("%1$td/%1$tm/%1$tY", this.taskNotificationCalendar));
                }
                break;
            case TASKDONECALENDAR:
                this.taskDoneCalendar.set(year, month, dayOfMonth);
                this.updateTaskDoneDate.setText(String.format("%1$td/%1$tm/%1$tY", this.taskDoneCalendar));
                break;
            case TASKNOTIFICATIONCALENDAR:
                this.taskNotificationCalendar.set(year, month, dayOfMonth);
                this.updateTaskNotificationDate.setText(String.format("%1$td/%1$tm/%1$tY", this.taskNotificationCalendar));
                break;
            default:
                break;
        }
    }

    @Override
    public void onTimeSetReady(int hourOfDay, int minute) {
        switch (this.myCalendarType) {
            case TASKCALENDAR:
                this.taskCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                this.taskCalendar.set(Calendar.MINUTE, minute);
                TextView taskTime = (TextView) findViewById(R.id.update_task_time);
                taskTime.setText(String.format("%1$tH:%1$tM", this.taskCalendar));
                this.taskNotificationCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                this.taskNotificationCalendar.set(Calendar.MINUTE, minute);
                if (this.seekBarValue >= 6) {
                    this.updateTaskNotificationTime.setText(String.format("%1$tH:%1$tM", this.taskNotificationCalendar));
                }
                break;
            case TASKDONECALENDAR:
                this.taskDoneCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                this.taskDoneCalendar.set(Calendar.MINUTE, minute);
                this.updateTaskDoneTime.setText(String.format("%1$tH:%1$tM", this.taskDoneCalendar));
                break;
            case TASKNOTIFICATIONCALENDAR:
                this.taskNotificationCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                this.taskNotificationCalendar.set(Calendar.MINUTE, minute);
                this.updateTaskNotificationTime.setText(String.format("%1$tH:%1$tM", this.taskNotificationCalendar));
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
