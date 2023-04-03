package com.varunshankar.example29;

import android.graphics.Bitmap;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

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

                //Initialize the default Volley request queue
                mRequestQueue = Volley.newRequestQueue(getApplicationContext());

                //Set the network image view
                mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
                    public void putBitmap(String url, Bitmap bitmap)
                    {
                        mCache.put(url, bitmap);
                    }
                    public Bitmap getBitmap(String url)
                    {
                        return mCache.get(url);
                    }
                });
                mNivFetchedImage.setImageUrl(mURLString,mImageLoader);
            }
            break;
        }
    }
}
