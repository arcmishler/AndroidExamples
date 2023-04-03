package com.varunshankar.example32;

import android.app.job.JobParameters;
import android.app.job.JobService;

import androidx.core.app.NotificationCompat;
import android.widget.Toast;

public class NotificationJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        //Show a toast
        Toast.makeText(getApplicationContext(),"Job is now running",Toast.LENGTH_SHORT).show();
        jobFinished(jobParameters,false);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }
}
