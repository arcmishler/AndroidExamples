package com.varunshankar.example32

import android.app.job.JobService
import android.app.job.JobParameters
import android.widget.Toast

class NotificationJobService : JobService() {
    override fun onStartJob(jobParameters: JobParameters): Boolean {
        //Show a toast
        Toast.makeText(applicationContext, "Job is now running", Toast.LENGTH_SHORT).show()
        jobFinished(jobParameters, false)
        return false
    }

    override fun onStopJob(jobParameters: JobParameters): Boolean {
        return true
    }
}