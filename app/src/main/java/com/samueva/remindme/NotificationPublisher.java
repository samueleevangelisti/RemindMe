package com.samueva.remindme;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationPublisher extends BroadcastReceiver {

    // TODO: 8/12/19 Stringa di debug
    private static final String TAG = "ReMe_NotificationPublisher";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "E' arrivato qualcosa");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int taskId = intent.getIntExtra("task_id", 0);
        Notification notification = intent.getParcelableExtra("notification");
        notificationManager.notify(taskId, notification);
    }
}
