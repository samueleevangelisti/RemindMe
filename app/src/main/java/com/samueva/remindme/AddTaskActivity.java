package com.samueva.remindme;

import android.app.AlarmManager;
import android.app.Notification;
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

enum calendarType {
    TASKCALENDAR,
    TASKNOTIFICATIONCALENDAR,
    TASKDONECALENDAR
}

public class AddTaskActivity extends AppCompatActivity implements AddCategoryDialogFragment.AddCategoryDialogListener, DatePickerFragment.TaskDatePickerListener, TimePickerFragment.TaskTimePickerListener {

    // TODO: 5/10/19 STRINGA_DI_DEBUG
    private static final String TAG = "ReMe_AddTaskActivity";

    // Need for update
    private boolean update;

    // Calendar modified
    private calendarType myCalendarType;

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
        public void onTaskUpdateCallback(int taskId) {
            Log.d(TAG, "newTask id : " + taskId);

            if (newTask.getPriority() >= 6) {
                Log.d(TAG, "Setting Notification");
                Log.d(TAG, newTaskNotificationCalendar.get(Calendar.YEAR) + " " + (newTaskNotificationCalendar.get(Calendar.MONTH) + 1) + " " + newTaskNotificationCalendar.get(Calendar.DAY_OF_MONTH) + " " + newTaskNotificationCalendar.get(Calendar.HOUR_OF_DAY) + " " + newTaskNotificationCalendar.get(Calendar.MINUTE));
                Notification notification = new Notification.Builder(getApplicationContext())
                        .setContentTitle(newTask.getTitle())
                        .setContentText(newTask.getHourOfDay() + ":" + newTask.getMinute() + " - " + newTask.getPlace())
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .build();

                Intent intent = new Intent();
                intent.setAction("com.samueva.remindme.broadcast");
                intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                intent.putExtra("task_id", taskId);
                intent.putExtra("notification", notification);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), taskId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, newTaskNotificationCalendar.getTimeInMillis(), pendingIntent);
            }

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

    // NotificationDate and NotificationTime
    private ImageButton newTaskSetNotificationDate;
    private ImageButton newTaskSetNotificationTime;
    private TextView newTaskNotificationDate;
    private TextView newTaskNotificationTime;

    // New Task
    private Task newTask;

    // Calendar
    private Calendar newTaskCalendar;
    private Calendar newTaskNotificationCalendar;

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

        // Date and Time
        this.newTaskCalendar = Calendar.getInstance();
        this.newTaskNotificationCalendar = Calendar.getInstance();
        TextView newTaskDate = (TextView) findViewById(R.id.new_task_date);
        newTaskDate.setText(String.format("%1$td/%1$tm/%1$tY", this.newTaskCalendar));
        TextView newTaskTime = (TextView) findViewById(R.id.new_task_time);
        newTaskTime.setText(String.format("%1$tH:%1$tM", this.newTaskCalendar));

        // NotificationDate and NotificationTime
        this.newTaskNotificationDate = (TextView) findViewById(R.id.new_task_notification_date);
        this.newTaskNotificationTime = (TextView) findViewById(R.id.new_task_notification_time);
        this.newTaskSetNotificationDate = (ImageButton) findViewById(R.id.new_task_set_notification_date);
        this.newTaskSetNotificationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCalendarType = calendarType.TASKNOTIFICATIONCALENDAR;
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "notificationDatePickerata");
            }
        });
        this.newTaskSetNotificationTime = (ImageButton) findViewById(R.id.new_task_set_notification_time);
        this.newTaskSetNotificationTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myCalendarType = calendarType.TASKNOTIFICATIONCALENDAR;
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "notificationTimePickerata");
            }
        });

        // SeekBar
        SeekBar seekBar = (SeekBar) findViewById(R.id.new_task_priority);
        final TextView seekBarValueText = (TextView) findViewById(R.id.new_task_priority_value);
        final TextView notificationStatus = (TextView) findViewById(R.id.new_task_notification_status);
        this.seekBarValue = seekBarNormalization(seekBar.getProgress());
        seekBarValueText.setText(seekBarValue + "/10");
        if (seekBarValue >= 6) {
            notificationStatus.setText("Notification Enabled");
            this.newTaskNotificationDate.setText(String.format("%1$td/%1$tm/%1$tY", this.newTaskNotificationCalendar));
            this.newTaskSetNotificationDate.setEnabled(true);
            this.newTaskSetNotificationDate.setAlpha((float) 1);
            this.newTaskNotificationTime.setText(String.format("%1$tH:%1$tM", this.newTaskNotificationCalendar));
            this.newTaskSetNotificationTime.setEnabled(true);
            this.newTaskSetNotificationTime.setAlpha((float) 1);
        } else {
            notificationStatus.setText("Notification Disabled");
            this.newTaskNotificationDate.setText("");
            this.newTaskSetNotificationDate.setEnabled(false);
            this.newTaskSetNotificationDate.setAlpha((float) 0.5);
            this.newTaskNotificationTime.setText("");
            this.newTaskSetNotificationTime.setEnabled(false);
            this.newTaskSetNotificationTime.setAlpha((float) 0.5);
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seekBarValue = seekBarNormalization(i);
                seekBarValueText.setText(seekBarValue + "/10");
                if (seekBarValue >= 6) {
                    notificationStatus.setText("Notification Enabled");
                    newTaskNotificationDate.setText(String.format("%1$td/%1$tm/%1$tY", newTaskNotificationCalendar));
                    newTaskSetNotificationDate.setEnabled(true);
                    newTaskSetNotificationDate.setAlpha((float) 1);
                    newTaskNotificationTime.setText(String.format("%1$tH:%1$tM", newTaskNotificationCalendar));
                    newTaskSetNotificationTime.setEnabled(true);
                    newTaskSetNotificationTime.setAlpha((float) 1);
                } else {
                    notificationStatus.setText("Notification Disabled");
                    notificationStatus.setText("Notification Disabled");
                    newTaskNotificationDate.setText("");
                    newTaskSetNotificationDate.setEnabled(false);
                    newTaskSetNotificationDate.setAlpha((float) 0.5);
                    newTaskNotificationTime.setText("");
                    newTaskSetNotificationTime.setEnabled(false);
                    newTaskSetNotificationTime.setAlpha((float) 0.5);
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
                myCalendarType = calendarType.TASKCALENDAR;
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePickerata");
            }
        });
        ImageButton setTaskTime = (ImageButton) findViewById(R.id.new_task_set_time);
        setTaskTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCalendarType = calendarType.TASKCALENDAR;
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
        switch (this.myCalendarType) {
            case TASKCALENDAR:
                this.newTaskCalendar.set(year, month, dayOfMonth);
                TextView taskDate = (TextView) findViewById(R.id.new_task_date);
                taskDate.setText(String.format("%1$td/%1$tm/%1$tY", this.newTaskCalendar));
                if (this.seekBarValue >= 6) {
                    this.newTaskNotificationCalendar.set(year, month, dayOfMonth);
                    this.newTaskNotificationDate.setText(String.format("%1$td/%1$tm/%1$tY", this.newTaskNotificationCalendar));
                }
                break;
            case TASKNOTIFICATIONCALENDAR:
                this.newTaskNotificationCalendar.set(year, month, dayOfMonth);
                this.newTaskNotificationDate.setText(String.format("%1$td/%1$tm/%1$tY", this.newTaskNotificationCalendar));
                break;
            default:
                break;
        }

    }

    @Override
    public void onTimeSetReady(int hourOfDay, int minute) {
        switch (this.myCalendarType) {
            case TASKCALENDAR:
                this.newTaskCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                this.newTaskCalendar.set(Calendar.MINUTE, minute);
                TextView taskTime = (TextView) findViewById(R.id.new_task_time);
                taskTime.setText(String.format("%1$tH:%1$tM", this.newTaskCalendar));
                if (this.seekBarValue >= 6) {
                    this.newTaskNotificationCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    this.newTaskNotificationCalendar.set(Calendar.MINUTE, minute);
                    this.newTaskNotificationTime.setText(String.format("%1$tH:%1$tM", this.newTaskNotificationCalendar));
                }
                break;
            case TASKNOTIFICATIONCALENDAR:
                this.newTaskNotificationCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                this.newTaskNotificationCalendar.set(Calendar.MINUTE, minute);
                this.newTaskNotificationTime.setText(String.format("%1$tH:%1$tM", this.newTaskNotificationCalendar));
                break;
            default:
                break;
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("update", this.update);
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
