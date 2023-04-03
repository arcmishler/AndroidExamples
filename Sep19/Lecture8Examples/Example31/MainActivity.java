package com.varunshankar.example31;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

//Img url: https://i.imgur.com/Nwk25LA.jpg
//Remember to get storage permission at runtime
public class MainActivity extends AppCompatActivity
        implements View.OnClickListener{
    private ImageView mIvFetchedImage;
    private EditText mEtURL;
    private Button mBtSubmit;
    private String mURLString;
    private long mDownloadId;
    private DownloadManager mDownloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtSubmit = (Button) findViewById(R.id.button_submit);
        mBtSubmit.setOnClickListener(this);

        mEtURL = (EditText) findViewById(R.id.et_query);
        mIvFetchedImage = (ImageView) findViewById(R.id.iv_fetched_image);

        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        registerReceiver(onComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_submit:{
                //Get the string for URL
                mURLString = mEtURL.getText().toString();

                //DownloadManager wants a Uri object (not URI or URL)
                Uri downloadUri = Uri.parse(mURLString);

                //Create the DownloadManager request
                DownloadManager.Request downloadRequest = new DownloadManager.Request(downloadUri);

                //Decide whether we can download over wifi or data or both
                downloadRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

                //Set a nice title and description
                downloadRequest.setTitle("Image Download");
                downloadRequest.setDescription("Download image from " + mURLString);

                //Set if it's visible in the downloads window or not
                downloadRequest.setVisibleInDownloadsUi(true);

                //Say where to save stuff
                downloadRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"DownloadedImage.jpg");

                //Put this request in the queue
                mDownloadId =  mDownloadManager.enqueue(downloadRequest);
            }
            break;
        }
    }

    //Create our BroadcastReceiver to receive the system broadcast
    BroadcastReceiver onComplete = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1);
            if(downloadId==mDownloadId){
                Toast.makeText(context,"File downloaded!",Toast.LENGTH_LONG).show();
            }
        }
    };
}