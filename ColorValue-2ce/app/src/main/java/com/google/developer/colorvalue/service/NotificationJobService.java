package com.google.developer.colorvalue.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.developer.colorvalue.MainActivity;
import com.google.developer.colorvalue.R;

public class NotificationJobService extends JobService {

    public static final int NOTIFICATION_ID = 18;

    @Override
    public boolean onStartJob(JobParameters params) {
        // TODO notification
        //Set up the notification content intent to launch the app when clicked
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (this, NOTIFICATION_ID, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.time_to_practice))
                .setContentText(getString(R.string.it_is_time_to_practice))
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.drawable.ic_delete)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true);

        if (manager != null) {
            manager.notify(NOTIFICATION_ID, builder.build());
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

}