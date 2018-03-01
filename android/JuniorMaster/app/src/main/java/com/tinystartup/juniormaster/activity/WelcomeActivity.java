package com.tinystartup.juniormaster.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tinystartup.juniormaster.R;
import com.tinystartup.juniormaster.activity.welcome.RegisterFragment;
import com.tinystartup.juniormaster.activity.welcome.WelcomeFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class WelcomeActivity extends AppCompatActivity {

    //keep track of camera capture intent
    final static private int CAMERA_CAPTURE = 1;
    final static private int IMAGE_MAX_LENGTH = 800;
    final static private Uri sTempPhotoUri = getOutputMediaFileUri();

    private RegisterFragment mRegisterFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        FragmentManager fragMgr = getSupportFragmentManager();
        WelcomeFragment fragment = new WelcomeFragment();
        fragMgr.beginTransaction().add(R.id.frameLayout, fragment).commit();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (requestCode == CAMERA_CAPTURE) {

                final File file = new File(getCacheDir(), "IMG_StudentCard.jpg");
                storeResizedImage(sTempPhotoUri, IMAGE_MAX_LENGTH, file);
                mRegisterFragment.setStudentCardImageView(Uri.fromFile(file));
            }
        }
    }

    public void startCameraActivityForResult(RegisterFragment fragment) {

        mRegisterFragment = fragment;

        try {
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, sTempPhotoUri);
            startActivityForResult(captureIntent, CAMERA_CAPTURE);
        }
        catch(ActivityNotFoundException ex) {
            //display an error message
            Toast.makeText(this, "Your device doesn't support capturing images!", Toast.LENGTH_SHORT).show();
        }
    }

    // create a file Uri for saving an image
    private static Uri getOutputMediaFileUri() {
        File tempFile = getOutputMediaFile();
        return (tempFile != null) ? Uri.fromFile(tempFile) : null;
    }

    // create a file for saving an image
    private static File getOutputMediaFile(){
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File mediaStorageDir = new File(storageDir, "JuniorMaster");

        // create the storage directory if it does not exist
        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){

                Log.d("JuniorMaster", "failed to create directory");
                return null;
            }
        }

        return new File(mediaStorageDir.getPath() + File.separator + "IMG_TEMP.jpg");
    }

    // resize the captured image and store it into cache folder
    private boolean storeResizedImage(Uri uri, final int maxImageLength, final File outputFile) {

        try {
            // this bit determines only the width/height of the bitmap without loading the contents
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            FileInputStream fileStream = new FileInputStream(new File(uri.getPath()));
            BitmapFactory.decodeStream(fileStream, null, options);
            fileStream.close();

            // find the correct scale value. It should be a power of 2
            int resizeScale = 1;
            if (options.outHeight > maxImageLength || options.outWidth > maxImageLength) {
                double maxLength = (double) Math.max(options.outHeight, options.outWidth);
                resizeScale = (int) Math.pow(2, (int) Math.round(Math.log(maxImageLength / maxLength) / Math.log(0.5)));
            }

            // load pre-scaled bitmap
            options = new BitmapFactory.Options();
            options.inSampleSize = resizeScale;
            fileStream = new FileInputStream(new File(uri.getPath()));
            Bitmap scaledBitmap = BitmapFactory.decodeStream(fileStream, null, options);
            fileStream.close();

            // store to cache folder
            FileOutputStream fileOutStream = new FileOutputStream(outputFile);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutStream);
            fileOutStream.flush();
            fileOutStream.close();

            scaledBitmap.recycle();
            return true;

        } catch (IOException ex) {
            Log.d("JuniorMaster", "failed to store resized image.");
        }

        return false;
    }
}
