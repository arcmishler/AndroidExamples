package com.varunshankar.example29

import androidx.appcompat.app.AppCompatActivity

import android.widget.EditText
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import android.os.Bundle
import com.android.volley.toolbox.Volley
import android.graphics.Bitmap
import android.util.LruCache
import android.view.View
import android.widget.Button
import com.android.volley.toolbox.NetworkImageView

//Img url: https://i.imgur.com/Nwk25LA.jpg
class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var mNivFetchedImage: NetworkImageView? = null
    private var mEtURL: EditText? = null
    private var mBtSubmit: Button? = null
    private var mURLString: String? = null
    private var mRequestQueue: RequestQueue? = null
    private var mImageLoader: ImageLoader? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mBtSubmit = findViewById<View>(R.id.button_submit) as Button
        mBtSubmit!!.setOnClickListener(this)
        mEtURL = findViewById<View>(R.id.et_query) as EditText
        mNivFetchedImage = findViewById<View>(R.id.niv_fetched_image) as NetworkImageView
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_submit -> {

                //Get the URL
                mURLString = mEtURL!!.text.toString()

                //Initialize the default Volley request queue
                mRequestQueue = Volley.newRequestQueue(applicationContext)

                //Set the network image view
                mImageLoader = ImageLoader(mRequestQueue, object : ImageLoader.ImageCache {
                    private val mCache = LruCache<String, Bitmap>(10)
                    override fun putBitmap(url: String, bitmap: Bitmap) {
                        mCache.put(url, bitmap)
                    }

                    override fun getBitmap(url: String): Bitmap? {
                        return mCache[url]
                    }
                })
                mNivFetchedImage!!.setImageUrl(mURLString, mImageLoader)
            }
        }
    }
}