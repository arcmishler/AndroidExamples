package com.varunshankar.example32

import androidx.appcompat.app.AppCompatActivity
import android.app.job.JobScheduler
import android.os.Bundle
import android.widget.RadioGroup
import android.app.job.JobInfo
import android.content.ComponentName
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private var mScheduler: JobScheduler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
    }

    fun scheduleJob(view: View?) {
        val networkOptions = findViewById<View>(R.id.networkOptions) as RadioGroup
        val selectedNetworkID = networkOptions.checkedRadioButtonId
        var selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE
        when (selectedNetworkID) {
            R.id.noNetwork -> selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE
            R.id.anyNetwork -> selectedNetworkOption = JobInfo.NETWORK_TYPE_ANY
            R.id.wifiNetwork -> selectedNetworkOption = JobInfo.NETWORK_TYPE_UNMETERED
        }
        val serviceName = ComponentName(packageName, NotificationJobService::class.java.name)
        val builder = JobInfo.Builder(JOB_ID, serviceName)
        builder.setRequiredNetworkType(selectedNetworkOption)
        builder.setRequiresCharging(false)
        val myJobInfo = builder.build()
        mScheduler!!.schedule(myJobInfo)
        Toast.makeText(this, "Job was scheduled!", Toast.LENGTH_SHORT).show()
    }

    fun cancelJobs(view: View?) {
        if (mScheduler != null) {
            mScheduler!!.cancelAll()
            mScheduler = null
            Toast.makeText(this, "Jobs Canceled", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val JOB_ID = 0
    }
}