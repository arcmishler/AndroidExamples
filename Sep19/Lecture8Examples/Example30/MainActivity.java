package com.varunshankar.example30;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

//Img url: https://i.imgur.com/Nwk25LA.jpg
public class MainActivity extends AppCompatActivity
                          implements View.OnClickListener{
    private NetworkImageView mNivFetchedImage;
    private EditText mEtURL;
    private Button mBtSubmit;
    private String mURLString;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtSubmit = (Button) findViewById(R.id.button_submit);
        mBtSubmit.setOnClickListener(this);

        mEtURL = (EditText) findViewById(R.id.et_query);
        mNivFetchedImage = (NetworkImageView) findViewById(R.id.niv_fetched_image);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_submit:{
                //Get the URL
                mURLString = mEtURL.getText().toString();

                //Get the only request queue
                mRequestQueue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

                //Get the image loader and set the network image view
                //Even though we're saying "this" here, the Singleton class will get the correct Application context
                mImageLoader = VolleySingleton.getInstance(this).getImageLoader();
                mNivFetchedImage.setImageUrl(mURLString,mImageLoader);
            }
            break;
        }
    }
}
