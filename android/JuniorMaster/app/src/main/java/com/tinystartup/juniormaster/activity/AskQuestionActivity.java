package com.tinystartup.juniormaster.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tinystartup.juniormaster.R;
import com.tinystartup.juniormaster.adapter.asking.CatalogAdapter;
import com.tinystartup.juniormaster.AppController;
import com.tinystartup.juniormaster.model.network.RequestListener;
import com.tinystartup.juniormaster.activity.common.TaggingLayout;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AskQuestionActivity extends AppCompatActivity implements RequestListener.OnPostQuestionResultListener {
    private static Logger logger = Logger.getLogger(AskQuestionActivity.class);

    private LinearLayout mContainer;
    private ArrayList<ImageView> mImageViewList;
    private EditText mContent;
    private Dialog mTaggingDialog;

    //keep track of camera capture intent
    final private int CAMERA_CAPTURE = 1;
    //keep track of cropping intent
    final private int PICTURE_CROP = 2;
    private Uri mPhotoUri;

    private ExpandableListView mExpandableListView;
    private CatalogAdapter mCatalogAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int maxWidth = getWindowManager().getDefaultDisplay().getWidth();
        final int maxHeight = getWindowManager().getDefaultDisplay().getHeight();

        setContentView(R.layout.activity_ask);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContent = (EditText) findViewById(R.id.content);
        mPhotoUri = getOutputMediaFileUri();
        mContainer = (LinearLayout) findViewById(R.id.containerLayout);
        mImageViewList = new ArrayList<ImageView>();

        List expandableListTitle = getTitleData();
        HashMap<String, List<String>> expandableListDetail = getDetailData();
        mExpandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        mCatalogAdapter = new CatalogAdapter(
                this,
                expandableListTitle,
                expandableListDetail,
                (LinearLayout) findViewById(R.id.catalogLayout),
                (TextView) findViewById(R.id.catalogContent),
                mExpandableListView);
        mExpandableListView.setAdapter(mCatalogAdapter);
        mExpandableListView.expandGroup(0);

        ((TextView)findViewById(R.id.catalogContent)).setText(expandableListDetail.get(getTitleData().get(0)).get(0));

        mTaggingDialog = new Dialog(this);
        mTaggingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mTaggingDialog.setCanceledOnTouchOutside(false);
        mTaggingDialog.setContentView(R.layout.dialog_tagging);
        TaggingLayout layout = (TaggingLayout) mTaggingDialog.findViewById(R.id.tags);
        layout.inititalize(getLayoutInflater(), (int) (maxWidth * 0.85));
        for (String tagName: Arrays.asList("小考", "平時考", "模擬考", "段考", "大會考", "課本習題", "回家作業", "補習班題目", "講義題目", "競賽題目", "自創題目")) {
            layout.addCandidate(tagName);
        }
        mTaggingDialog.getWindow().setLayout(maxWidth, (int) (maxHeight * 0.6));
        ((Button) mTaggingDialog.findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_asking, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            (new AlertDialog.Builder(this))
                    .setTitle("離開提問視窗")
                    .setMessage("你確定要放棄提問嗎？")
                    .setPositiveButton("放棄", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish(); // finish activity
                        }
                    })
                    .setNegativeButton("繼續編輯", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();

            return true;
        } else if (id == R.id.action_camera) {
            startCameraActivityForResult();
        } else if (id == R.id.action_post) {
            startPostingQuestion();
            //mTaggingDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (requestCode == CAMERA_CAPTURE) {
                File file = new File(mPhotoUri.getPath());
                logger.info("onActivityResult(): CAMERA_CAPTURE - " + file.getPath() + "(size: " + file.length() + ")");
                startCropActivityForResult();
            }
            else if (requestCode == PICTURE_CROP) {
                File file = new File(mPhotoUri.getPath());
                logger.info("onActivityResult(): PICTURE_CROP - " + file.getPath() + "(size: " + file.length() + ")");
                Uri temp = storeCroppedImage(mPhotoUri);
                addNewCapturedImage(temp);
            }
        }
        else
        {
            logger.error("onActivityResult() - requestCode = " + String.valueOf(requestCode) +
                    " resultCode = " + String.valueOf(resultCode));
        }
    }

    private Uri storeCroppedImage(Uri uri) {
        final int IMAGE_MAX_SIZE = 800;

        try {
            // This bit determines only the width/height of the bitmap without loading the contents
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            FileInputStream fileStream = new FileInputStream(new File(uri.getPath()));
            BitmapFactory.decodeStream(fileStream, null, options);
            fileStream.close();

            // Find the correct scale value. It should be a power of 2
            int resizeScale = 1;
            if (options.outHeight > IMAGE_MAX_SIZE || options.outWidth > IMAGE_MAX_SIZE) {
                double maxLength = (double) Math.max(options.outHeight, options.outWidth);
                resizeScale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / maxLength) / Math.log(0.5)));
            }

            // Load pre-scaled bitmap
            options = new BitmapFactory.Options();
            options.inSampleSize = resizeScale;
            fileStream = new FileInputStream(new File(uri.getPath()));
            Bitmap scaledBitmap = BitmapFactory.decodeStream(fileStream, null, options);
            fileStream.close();

            // Store to cache folder
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File outputFile = new File(getCacheDir(), "IMG_" + timeStamp + ".jpg");
            FileOutputStream fileOutStream = new FileOutputStream(outputFile);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutStream);
            fileOutStream.flush();
            fileOutStream.close();

            scaledBitmap.recycle();
            logger.debug("storeCroppedImage() - store to " + outputFile.getPath() + "(size: " + outputFile.length() + ")");
            return Uri.fromFile(outputFile);

        } catch (IOException ex) {
            logger.error("storeCroppedImage()", ex);
        }

        return null;
    }

    private void addNewCapturedImage(Uri uri) {
        LayoutInflater inflater = getLayoutInflater();
        final View photoImageView = inflater.inflate(R.layout.photo_item, null, false);
        final ImageButton imageButton = (ImageButton) photoImageView.findViewById(R.id.imageButton);
        final ImageView imageView = (ImageView)photoImageView.findViewById(R.id.imageView);

        imageView.setImageURI(uri);
        imageView.setTag(uri);
        mImageViewList.add(imageView);
        mContainer.addView(photoImageView);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageViewList.remove(imageView);
                mContainer.removeView(photoImageView);
            }
        });
    }

    private void startCameraActivityForResult() {
        try {
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
            startActivityForResult(captureIntent, CAMERA_CAPTURE);
        }
        catch(ActivityNotFoundException ex) {
            //display an error message
            logger.error("startCameraActivityForResult() - Your device doesn't support capturing images!", ex);
            Toast.makeText(this, "Your device doesn't support capturing images!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void startCropActivityForResult() {
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");

            //indicate image type and Uri
            cropIntent.setDataAndType(mPhotoUri, "image/*");
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
            cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

            //set crop properties
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("scale", true);
            cropIntent.putExtra("noFaceDetection", true);

            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PICTURE_CROP);
        }
        catch(ActivityNotFoundException ex) {
            //display an error message
            logger.error("startCropActivityForResult() - Your device doesn't support the crop action!", ex);
            Toast.makeText(this, "Your device doesn't support the crop action!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void startPostingQuestion() {

        List<Uri> uriList = new ArrayList<Uri>();
        for (ImageView view : mImageViewList) {
            uriList.add(Uri.parse(view.getTag().toString()));
        }

        AppController.getInstance().getRequestCenter().makePostQuestion(mContent.getText().toString(), uriList, this);
    }

    // Create a file Uri for saving an image or video
    private Uri getOutputMediaFileUri() {
        File tempFile = getOutputMediaFile();
        return (tempFile != null) ? Uri.fromFile(tempFile) : null;
    }

    // Create a File for saving an image
    private File getOutputMediaFile(){
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File mediaStorageDir = new File(storageDir, "JuniorMaster");

        // create the storage directory if it does not exist
        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){

                logger.error("getOutputMediaFile() - failed to create directory:" + mediaStorageDir.getPath());
                return null;
            }
        }

        return new File(mediaStorageDir.getPath() + File.separator + "IMG_TEMP.jpg");
    }

    public static List getTitleData() {
        List expandableListTitle = new ArrayList();

        expandableListTitle.add("段考範圍");
        expandableListTitle.add("國中ㄧ年級");
        expandableListTitle.add("國中二年級");
        expandableListTitle.add("國中三年級");

        return expandableListTitle;
    }

    public static HashMap<String, List<String>> getDetailData() {
        final List expandableListTitle = getTitleData();
        HashMap expandableListDetail = new HashMap();

        List exam = new ArrayList();
        exam.add("分數的運算");
        exam.add("一元一次方程式");

        List chapter1 = new ArrayList();
        chapter1.add("整數與數線");
        chapter1.add("分數的運算");
        chapter1.add("一元一次方程式");
        chapter1.add("二元一次方程式");
        chapter1.add("直角坐標與二元一次方程式");
        chapter1.add("比例");
        chapter1.add("線性函數");
        chapter1.add("一元一次不等式");

        List chapter2 = new ArrayList();
        chapter2.add("乘法公式與多項式");
        chapter2.add("二次方根與畢氏定理");
        chapter2.add("因式分解");
        chapter2.add("一元二次方程式");
        chapter2.add("數列與級數");
        chapter2.add("幾何圖形");
        chapter2.add("三角形的基本性質");
        chapter2.add("平行與四邊形");

        List chapter3 = new ArrayList();
        chapter3.add("相似形");
        chapter3.add("圓形");
        chapter3.add("二次函數");
        chapter3.add("立體圖形");
        chapter3.add("統計與機率");

        expandableListDetail.put(expandableListTitle.get(0), exam);
        expandableListDetail.put(expandableListTitle.get(1), chapter1);
        expandableListDetail.put(expandableListTitle.get(2), chapter2);
        expandableListDetail.put(expandableListTitle.get(3), chapter3);

        return expandableListDetail;
    }

    public static HashMap<String, Integer> getTagHashMap() {
        HashMap<String, Integer> tagHashMap = new HashMap<String, Integer>();

        tagHashMap.put("整數與數線", 1);
        tagHashMap.put("分數的運算", 2);
        tagHashMap.put("一元一次方程式", 3);
        tagHashMap.put("二元一次方程式", 4);
        tagHashMap.put("直角坐標與二元一次方程式", 5);
        tagHashMap.put("比例", 6);
        tagHashMap.put("線性函數", 7);
        tagHashMap.put("一元一次不等式", 8);

        tagHashMap.put("乘法公式與多項式", 9);
        tagHashMap.put("二次方根與畢氏定理", 10);
        tagHashMap.put("因式分解", 11);
        tagHashMap.put("一元二次方程式", 12);
        tagHashMap.put("數列與級數", 13);
        tagHashMap.put("幾何圖形", 14);
        tagHashMap.put("三角形的基本性質", 15);
        tagHashMap.put("平行與四邊形", 16);

        tagHashMap.put("相似形", 17);
        tagHashMap.put("圓形", 18);
        tagHashMap.put("二次函數", 19);
        tagHashMap.put("立體圖形", 20);
        tagHashMap.put("統計與機率", 21);

        return tagHashMap;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("離開提問視窗");
        builder.setMessage("你確定要放棄提問嗎？");
        builder.setPositiveButton("放棄", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AskQuestionActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("繼續編輯", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.show();
    }

    @Override
    public void OnPostQuestionSuccess() {
        mTaggingDialog.show();
    }
}
