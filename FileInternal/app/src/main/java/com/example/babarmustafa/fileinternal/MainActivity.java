package com.example.babarmustafa.fileinternal;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    ProgressDialog progres;
    DownloadManager downloadManager;
    private static int Gallery_Request = 1;
    private static int CAMERA_REQUEST = 1;
    Button upload_btn;
    ImageButton LoadImage;
    Intent intent_of_gallery, intent_of_camera;
    ImageView dowloaded_view_of_image;
    Button for_Dowload_image;
    Uri downloadUrl;
    private Uri mImageUri = null;
    private StorageReference mStoarge;
    private DatabaseReference databaseReference;
    private Bitmap bitmap;
    String extention;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progres = new ProgressDialog(this);

        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Images Url");
        dowloaded_view_of_image = (ImageView) findViewById(R.id.d_imageview);
        LoadImage = (ImageButton) findViewById(R.id.button1);
        for_Dowload_image = (Button) findViewById(R.id.btn_dowload);

        mStoarge = FirebaseStorage.getInstance().getReference();


        upload_btn = (Button) findViewById(R.id.upload);


        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progres.setMessage("uploading...");
                progres.show();

                StorageReference filepath = mStoarge.child("Images").child(mImageUri.getLastPathSegment());

                filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        downloadUrl = taskSnapshot.getDownloadUrl();


//
                      //  DatabaseReference database_reference = databaseReference.push();
//
//                        database_reference.child("images").setValue(downloadUrl.toString());
                        Toast.makeText(MainActivity.this, "Upload image succesfully", Toast.LENGTH_SHORT).show();
                        progres.dismiss();
                    }
                });

            }
        });


        LoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose Action");
                builder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intent_of_gallery = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent_of_gallery, Gallery_Request);
                    }
                });
//builder.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
//    @Override
//    public void onClick(DialogInterface dialog, int which) {
//        intent_of_camera = new Intent(
//                Intent.ACTION_IMAGE_CAPTURE);
//        File f = new File(android.os.Environment
//                .getExternalStorageDirectory(), "temp.jpg");
//        intent_of_camera.putExtra(MediaStore.EXTRA_OUTPUT,
//                Uri.fromFile(f));
//
//        startActivityForResult(intent_of_camera,
//                CAMERA_REQUEST);
//
//    }
//});
                builder.show();

            }
        });

        for_Dowload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //progres.setTitle("Welcome to download!!!!!!!!!!!!!!!!!!");
                progres.setMessage("Dowloading............");
                progres.show();
                progres.setCancelable(false);
//                dowloadImage();
                downloadFileNow("images", String.valueOf(downloadUrl), "images");

                progres.dismiss();


            }
        });


    }


    public void dowloadImage() {
        Picasso.with(this).load(downloadUrl).into(dowloaded_view_of_image);

        //  Picasso.with(this).load(downloadUrl).into(picassoImageTarget(getApplicationContext(), "B", "my_image.jpeg"));

    }

    public void downloadFileNow(String fileType, String downLoadUrl, String fileName) {
        Uri uri = Uri.parse(downLoadUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
//                set the notification
        request.setDescription("Downloading " + fileName).setTitle("Demo App");

//                set the path to where to save the file
//                      save in app package directory
//        request.setDestinationInExternalFilesDir(MainActivity.this,
//                        Environment.DIRECTORY_DOWNLOADS, "logo.jpg");

        // request.setDestinationUri(Uri.parse("/d/images"));


//                     // save in the public downloads folder
//        File file = new File(Environment.getExternalStorageDirectory()+"/demoFolder/videos");
//        if(!file.exists()){
//            file.mkdirs();
//        }


        extention = String.valueOf(downLoadUrl);
        extention = extention.substring(extention.lastIndexOf(".") ).substring(0,4);
        Toast.makeText(this, " "+extention , Toast.LENGTH_SHORT).show();

            //demofolder is directory
            //imaGGG is folder
            request.setDestinationInExternalPublicDir("/demoFolder/GGG", fileName + extention);

//                make file visible by and manageable by system's download app
        request.setVisibleInDownloadsUi(true);

//                select which network, etc
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                | DownloadManager.Request.NETWORK_MOBILE);
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);

//                queue the download
        downloadManager.enqueue(request);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_Request && resultCode == RESULT_OK) {
            Uri ImageUri = data.getData();
            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mImageUri = result.getUri();


                LoadImage.setImageURI(mImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }


        }

    }
}