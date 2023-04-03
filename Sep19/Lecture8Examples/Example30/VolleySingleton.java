package com.varunshankar.example30;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mContext; //This will hold the Application context (not Activity)

    //Note the private constructor
    private VolleySingleton(Context context){
        mContext = context;
        mRequestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache(){
            private final LruCache<String, Bitmap>
                    cache = new LruCache<String,Bitmap>(40);

                    @Override
                    public Bitmap getBitmap(String url) {

                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {

                        cache.put(url,bitmap);
                    }
                });
    }

    //factory method to get the singleton instance
    //synchronized makes it thread-safe
    public static synchronized  VolleySingleton getInstance(Context context){
        if(mInstance ==null){
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        if(mRequestQueue == null){
            //Guard against someone sending in the wrong context!
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request, String tag){
        request.setTag(tag);
        getRequestQueue().add(request);
    }

    public void cancelAllRequests(String tag){
        getRequestQueue().cancelAll(tag);
    }

    public ImageLoader getImageLoader(){
        return mImageLoader;
    }
}
