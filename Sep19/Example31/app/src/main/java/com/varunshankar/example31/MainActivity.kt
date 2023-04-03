package com.varunshankar.example31

import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.app.DownloadManager
import android.os.Bundle
import android.content.IntentFilter
import android.os.Environment
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

//Img url: https://i.imgur.com/Nwk25LA.jpg
//Remember to get storage permission at runtime
class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var mIvFetchedImage: ImageView? = null
    private var mEtURL: EditText? = null
    private var mBtSubmit: Button? = null
    private var mURLString: String? = null
    private var mDownloadId: Long = 0
    private var mDownloadManager: DownloadManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mBtSubmit = findViewById<View>(R.id.button_submit) as Button
        mBtSubmit!!.setOnClickListener(this)
        mEtURL = findViewById<View>(R.id.et_query) as EditText
        mIvFetchedImage = findViewById<View>(R.id.iv_fetched_image) as ImageView
        mDownloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_submit -> {

                //Get the string for URL
                mURLString = mEtURL!!.text.toString()

                //DownloadManager wants a Uri object (not URI or URL)
                val downloadUri = Uri.parse(mURLString)

                //Create the DownloadManager request
                val downloadRequest = DownloadManager.Request(downloadUri)

                //Decide whether we can download over wifi or data or both
                downloadRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)

                //Set a nice title and description
                downloadRequest.setTitle("Image Download")
                downloadRequest.setDescription("Download image from $mURLString")

                //Say where to save stuff
                downloadRequest.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "DownloadedImage.jpg"
                )

                //Put this request in the queue
                mDownloadId = mDownloadManager!!.enqueue(downloadRequest)
            }
        }
    }

    //Create our BroadcastReceiver to receive the system broadcast
    private var onComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadId == mDownloadId) {
                Toast.makeText(context, "File downloaded!", Toast.LENGTH_LONG).show()
            }
        }
    }
}