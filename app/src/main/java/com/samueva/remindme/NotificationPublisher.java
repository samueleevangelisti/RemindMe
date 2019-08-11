package com.samueva.remindme;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationPublisher extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        long taskId = intent.getIntExtra("task_id", 0);
        Notification notification = intent.getParcelableExtra("notification");
        notificationManager.notify((int) taskId, notification);
    }
}
