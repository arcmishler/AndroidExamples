package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

//Implement View.onClickListener to listen to button clicks. This means we have to override onClick().
public class MainActivity extends AppCompatActivity
        implements View.OnClickListener{

    //Create variables to hold the three strings
    private String mFullName, mFirstName, mLastName;

    //Create variables for the UI elements that we need to control
    private TextView mTvFirstName, mTvLastName;
    private Button mButtonSubmit, mButtonCamera;
    private EditText mEtFullName;

    //Create the variable for the ImageView that holds the profile pic
    ImageView mIvPic;

    //Define a request code for the camera
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get the text views where we will display names
        mTvFirstName = (TextView) findViewById(R.id.tv_fn_data);
        mTvLastName = (TextView) findViewById(R.id.tv_ln_data);

        //Get the buttons
        mButtonSubmit = (Button) findViewById(R.id.button_submit);
        mButtonCamera = (Button) findViewById(R.id.button_pic);

        //Say that this class itself contains the listener.
        mButtonSubmit.setOnClickListener(this);
        mButtonCamera.setOnClickListener(this);
    }

    //Handle clicks for ALL buttons here
    @Override
    public void onClick(View view) {
        switch(view.getId()) {

            case R.id.button_submit: {
                //First, get the string from the EditText
                mEtFullName = (EditText) findViewById(R.id.et_name);
                mFullName = mEtFullName.getText().toString();

                //Check if the EditText string is empty
                if (mFullName.matches("")) {
                    //Complain that there's no text
                    Toast.makeText(MainActivity.this, "Enter a name first!", Toast.LENGTH_SHORT).show();
                } else {
                    //Reward them for submitting their names
                    Toast.makeText(MainActivity.this, "Good job!", Toast.LENGTH_SHORT).show();

                    //Remove any leading spaces or tabs
                    mFullName = mFullName.replaceAll("^\\s+","");

                    //Separate the string into first and last name using simple Java stuff
                    String[] splitStrings = mFullName.split("\\s+");

                    if(splitStrings.length==1){
                        Toast.makeText(MainActivity.this, "Enter both first and last name!", Toast.LENGTH_SHORT).show();
                    }
                    else if(splitStrings.length==2) {
                        mFirstName = splitStrings[0];
                        mLastName = splitStrings[1];

                        //Set the text views
                        mTvFirstName.setText("" + mFirstName);
                        mTvLastName.setText("" + mLastName);
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Enter only first and last name!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
            case R.id.button_pic: {
                //The button press should open a camera
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(cameraIntent.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap thumbnailImage = (Bitmap) extras.get("data");
            mIvPic = (ImageView) findViewById(R.id.iv_pic);
            mIvPic.setImageBitmap(thumbnailImage);
        }
    }
}
