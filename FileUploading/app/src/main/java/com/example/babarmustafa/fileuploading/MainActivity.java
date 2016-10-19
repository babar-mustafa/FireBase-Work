package com.example.babarmustafa.fileuploading;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {
    private static final int SAVE_REQUEST_CODE = 1;
    private Button for_file_picker;
    Button for_download;
    TextView for_name_display;
    private Button for_upload;
    private DatabaseReference databaseReference;
    private StorageReference mStoarge;
    ProgressDialog progres;
    DownloadManager downloadManager;
    Uri downloadUrl;
    private Uri mImageUri = null;
    String extention;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Files Url");
        mStoarge = FirebaseStorage.getInstance().getReference();
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        for_name_display = (TextView) findViewById(R.id.filename_view);
        for_upload = (Button) findViewById(R.id.upload_b);
        for_file_picker  = (Button) findViewById(R.id.file_picker);
        for_download = (Button) findViewById(R.id.d_button);




        for_file_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/*");
                startActivityForResult(intent, SAVE_REQUEST_CODE);
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("file/*");
//                startActivityForResult(intent, PICKFILE_REQUEST_CODE);
            }
        });

        for_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference filepath = mStoarge.child("files").child(mImageUri.getLastPathSegment());
                filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        downloadUrl = taskSnapshot.getDownloadUrl();


//
                        DatabaseReference database_reference = databaseReference.push();

                        database_reference.child("path").setValue(downloadUrl.toString());
                        Toast.makeText(MainActivity.this, "Upload File succesfully", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        for_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                downloadFileNow("pdf", String.valueOf(downloadUrl), "po");

            }
        });

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


//        String myv = String.valueOf(downLoadUrl.lastIndexOf("."));
//        String mu = downLoadUrl.substring(3);
//        String s = String.valueOf(mImageUri);
//        s = s.substring(s.lastIndexOf("."));
//        Toast.makeText(this, " "+s , Toast.LENGTH_SHORT).show();
        // Log.d("TAG",myv + mu);


        //"https://firebasestorage.googleapis.com/v0/b/firedatabase-55af8.appspot.com/o/files%2FRoast-Chicken.pdf?alt=media&token=b9361359-5ae1-4881-9fe1-4d7416fa3e17"
        //"https://firebasestorage.googleapis.com/v0/b/firedatabase-55af8.appspot.com/o/files%2F1166             ?alt=media&token=a43bb08c-34da-4a84-8450-c18525446347"
        //"https://firebasestorage.googleapis.com/v0/b/firedatabase-55af8.appspot.com/o/files%2F22376              ?alt=media&token=43de65e4-fa90-4751-a121-6088882b7f0e"

//        String s = "http://www.freegreatpicture.com/files/146/26189-abstract-color-background.jpg";
//        s = s.substring(s.lastIndexOf(".")+1);
//        Toast.makeText(this, ""+s, Toast.LENGTH_SHORT).show();


        extention = String.valueOf(downLoadUrl);
        extention = extention.substring(extention.lastIndexOf(".")).substring(0,4);
        //.docx


       Toast.makeText(this, " "+extention , Toast.LENGTH_SHORT).show();
            request.setDestinationInExternalPublicDir("/demoFilesFolder/Files", fileName + extention  );


        
//        else if (fileType.equalsIgnoreCase("doc")) {
//
//            fileName = fileName + ".doc";
//            request.setDestinationInExternalPublicDir("/demoFilesFolder/pdf", fileName);
//        }

//
//        else {
//            Toast.makeText(this, "Failed to dowload", Toast.LENGTH_SHORT).show();
//        }

//                make file visible by and manageable by system's download app
        request.setVisibleInDownloadsUi(true);

//                select which network, etc
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                | DownloadManager.Request.NETWORK_MOBILE);
        //after download notification will show complete
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);

//                queue the download
        downloadManager.enqueue(request);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch(requestCode){
            case SAVE_REQUEST_CODE:
                if(resultCode==RESULT_OK){
                    String FilePath = data.getData().getPath();
                    mImageUri = data.getData();

                    for_name_display.setText(FilePath);
                }
                break;

        }
    }
    }

