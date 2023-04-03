package com.varunshankar.example30

import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.NetworkImageView
import android.widget.EditText
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import android.os.Bundle
import android.view.View
import android.widget.Button

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


                //Get the only request queue
                mRequestQueue = VolleySingleton.getInstance(this.applicationContext).requestQueue

                //Get the image loader and set the network image view
                //Even though we're saying "this" here, the Singleton class will get the correct Application context
                mImageLoader = VolleySingleton.getInstance(this).imageLoader
                mNivFetchedImage!!.setImageUrl(mURLString, mImageLoader)
            }
        }
    }
}