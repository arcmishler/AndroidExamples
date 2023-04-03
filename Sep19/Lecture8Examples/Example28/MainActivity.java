package com.varunshankar.example28;

import android.graphics.Bitmap;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

//IMG url: https://i.imgur.com/Nwk25LA.jpg
public class MainActivity extends AppCompatActivity
                            implements View.OnClickListener{
    private ImageView mIvFetchedImage;
    private EditText mEtURL;
    private Button mBtSubmit;
    private String mURLString;
    private ImageRequest mImageRequest;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtSubmit = (Button) findViewById(R.id.button_submit);
        mBtSubmit.setOnClickListener(this);


        mEtURL = (EditText) findViewById(R.id.et_query);
        mIvFetchedImage = (ImageView) findViewById(R.id.iv_fetched_image);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_submit:{
                //Get the URL
                mURLString = mEtURL.getText().toString();

                //Initialize the default Volley request queue
                mRequestQueue = Volley.newRequestQueue(getApplicationContext());

                //Generate a request
                mImageRequest = new ImageRequest(mURLString, responseListener, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,errorListener);
                //mImageRequest = new ImageRequest(mURLString, this, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,errorListener);

                //Add that request to the request queue
                mRequestQueue.add(mImageRequest);
            }
            break;
        }
    }

    Response.Listener<Bitmap> responseListener = new Response.Listener<Bitmap>() {
        public void onResponse (Bitmap response){
            mIvFetchedImage.setImageBitmap(response);
        }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            Toast.makeText(getApplicationContext(),"Volley error!",Toast.LENGTH_SHORT);
        }
    };
}
