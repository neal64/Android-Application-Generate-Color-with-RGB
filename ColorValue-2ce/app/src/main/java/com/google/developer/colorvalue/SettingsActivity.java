package com.google.developer.colorvalue;

import android.app.AlarmManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.SwitchPreferenceCompat;

import com.google.developer.colorvalue.service.NotificationJobService;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = SettingsActivity.class.getSimpleName();
    private static final int JOB_ID = 88;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        final String notifyKey = getString(R.string.pref_key_notification);
        if (key.equals(notifyKey)) {
            // TODO implement JobScheduler for notification {@link ScheduledJobService}
            boolean on = sharedPreferences.getBoolean(notifyKey, false);
            JobScheduler jobScheduler =
                    (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);

            Calendar cal = Calendar.getInstance();
            cal.clear(Calendar.HOUR);
            cal.clear(Calendar.HOUR_OF_DAY);
            cal.clear(Calendar.MINUTE);
            cal.clear(Calendar.SECOND);
            cal.clear(Calendar.MILLISECOND);
            long intendedTime = cal.getTimeInMillis();

            if(on) {
                //Schedule the job and notify the user
                JobInfo job = new JobInfo.Builder(JOB_ID,
                        new ComponentName(getPackageName(),
                                NotificationJobService.class.getName()))
                        .setPeriodic(intendedTime)
                        .build();
                if (jobScheduler != null) {
                    jobScheduler.schedule(job);
                }
            } else {
                //do nothing
            }

        }
    }

    /**
     * loading setting resource
     */
    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.preference);

        }

    }
}