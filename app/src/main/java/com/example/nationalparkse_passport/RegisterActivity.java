package com.example.nationalparkse_passport;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ChosenImages;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.kbeanie.imagechooser.exceptions.ChooserException;

public class RegisterActivity extends AppCompatActivity implements ImageChooserListener {

    private ImageChooserManager mImageChooserManager;
    private String mProfilePicturePath;
    private ImageView mProfileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mProfileImageView = (ImageView) findViewById(R.id.profile_imageview);
        mProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = {"เลือกภาพจากอัลบั้ม", "ถ่ายภาพ"};

                new AlertDialog.Builder(RegisterActivity.this)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0: // choose image
                                        chooseImage();
                                        break;
                                    case 1: // take photo
                                        takePicture();
                                        break;
                                }
                            }
                        })
                        .show();
            }
        });
    }

    private void takePicture() {
        mImageChooserManager = new ImageChooserManager(
                this,
                ChooserType.REQUEST_CAPTURE_PICTURE,
                true
        );
        mImageChooserManager.setImageChooserListener(this);

        try {
            mImageChooserManager.choose();
        } catch (ChooserException e) {
            e.printStackTrace();
        }
    }

    private void chooseImage() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, false);

        mImageChooserManager = new ImageChooserManager(
                this,
                ChooserType.REQUEST_PICK_PICTURE,
                true
        );
        mImageChooserManager.setExtras(bundle);
        mImageChooserManager.setImageChooserListener(this);
        mImageChooserManager.clearOldFiles();

        try {
            mImageChooserManager.choose();
        } catch (ChooserException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onImageChosen(final ChosenImage image) {
        Toast.makeText(this,"ok",Toast.LENGTH_LONG).show();
        new Handler(Looper.getMainLooper()).post(
                new Runnable() {
                    @Override
                    public void run() {
                        if (image != null) {
                            //mChosenImage = image;

                            String filePathOriginal = image.getFilePathOriginal();
                            //Log.i(TAG, "-----");
                            //Log.i(TAG, "Image path: " + filePathOriginal);
                            //Log.i(TAG, "Image thumbnail path: " + image.getFileThumbnail());
                            //Log.i(TAG, "Image small thumbnail path: " + image.getFileThumbnailSmall());

                            mProfilePicturePath = filePathOriginal;

                            Glide.with(RegisterActivity.this)
                                    .load(filePathOriginal)
                                    .listener(new RequestListener<String, GlideDrawable>() {
                                        @Override
                                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                            //mPhotoLayout.setVisibility(View.VISIBLE);
                                            //mProfileImageView.setVisibility(View.VISIBLE);
                                            return false;
                                        }
                                    })
                                    .into(mProfileImageView);
                        } else {
                            //mChosenImage = null;
                            //Log.i(TAG, "Image is NULL !?!");
                        }
                    }
                }
        );
    }

    @Override
    public void onError(String s) {

    }

    @Override
    public void onImagesChosen(ChosenImages chosenImages) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) {
        super.onActivityResult(requestCode, resultCode, returnedIntent);

        Toast.makeText(this,"onResult",Toast.LENGTH_LONG).show();

        if (resultCode == RESULT_OK
                && (requestCode == ChooserType.REQUEST_PICK_PICTURE
                || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            if (mImageChooserManager == null) {
                //reinitializeImageChooser();
                return;
            }
            mImageChooserManager.submit(requestCode, returnedIntent);
        }
    }
}