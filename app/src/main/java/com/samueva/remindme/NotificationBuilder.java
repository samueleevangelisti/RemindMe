package com.samueva.remindme;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationBuilder {

    public static PendingIntent build(Context mContext, int taskId, Task task) {
        Intent mainActivityIntent = new Intent(mContext, MainActivity.class);
        PendingIntent notificationActionIntent = PendingIntent.getActivity(mContext, taskId, mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(mContext)
                .setContentTitle(task.getTitle())
                .setContentText(task.getHourOfDay() + ":" + task.getMinute() + " - " + task.getPlace())
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentIntent(notificationActionIntent)
                .setAutoCancel(true)
                .build();

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.samueva.remindme.broadcast.notificationpublisher");
        broadcastIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        broadcastIntent.putExtra("task_id", taskId);
        broadcastIntent.putExtra("notification", notification);

        return PendingIntent.getBroadcast(mContext, taskId, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
