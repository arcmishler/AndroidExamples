package com.varunshankar.example28

import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import com.android.volley.toolbox.ImageRequest
import com.android.volley.RequestQueue
import android.os.Bundle
import com.android.volley.toolbox.Volley
import android.graphics.Bitmap
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Response

//IMG url: https://i.imgur.com/Nwk25LA.jpg (A Mars rover)
class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var mIvFetchedImage: ImageView? = null
    private var mEtURL: EditText? = null
    private var mBtSubmit: Button? = null
    private var mURLString: String? = null
    private var mImageRequest: ImageRequest? = null
    private var mRequestQueue: RequestQueue? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mBtSubmit = findViewById<View>(R.id.button_submit) as Button
        mBtSubmit!!.setOnClickListener(this)
        mEtURL = findViewById<View>(R.id.et_query) as EditText
        mIvFetchedImage = findViewById<View>(R.id.iv_fetched_image) as ImageView
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_submit -> {
                //Get the URL
                mURLString = mEtURL!!.text.toString()

                //Initialize the default Volley request queue
                mRequestQueue = Volley.newRequestQueue(applicationContext)

                //Generate a request
                mImageRequest = ImageRequest(
                    mURLString,
                    responseListener,
                    0,
                    0,
                    ImageView.ScaleType.CENTER_CROP,
                    Bitmap.Config.RGB_565,
                    errorListener
                )
                //Add that request to the request queue
                mRequestQueue!!.add(mImageRequest)
            }
        }
    }

    private var responseListener: Response.Listener<Bitmap> =
        Response.Listener { response -> mIvFetchedImage!!.setImageBitmap(response) }
    
    private var errorListener = Response.ErrorListener { error ->
        error.printStackTrace()
        Toast.makeText(applicationContext, "Volley error!", Toast.LENGTH_SHORT)
    }
}