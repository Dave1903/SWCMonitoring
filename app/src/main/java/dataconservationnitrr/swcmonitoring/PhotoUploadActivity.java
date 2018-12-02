package dataconservationnitrr.swcmonitoring;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoUploadActivity extends AppCompatActivity {
    static final int Request_Camera  = 2 ;
    private File imageFile;
    ImageView imageView;

    Button button;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == Request_Camera && resultCode == RESULT_OK ) {
            if(imageFile!= null) {
                if(imageFile.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());
                    imageView.setImageBitmap(bitmap);

                }


            }
            else {
                Toast.makeText(getApplicationContext(),"File Not saved ",Toast.LENGTH_LONG).show();
            }

        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_upload);
        button=(Button)findViewById(R.id.activity);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PhotoUploadActivity.this,MapsActivity.class);
                startActivity(intent);
            }
        });
        imageView = findViewById(R.id.img);
    }



    public void  openCam(View view){
        openCamera();
    }

    public void  openCamera(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE }, Request_Camera);
        }
        else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                imageFile = createImageFile();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (imageFile!=null) {
                Uri tempUri = FileProvider.getUriForFile(PhotoUploadActivity.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        imageFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);

                startActivityForResult(intent, Request_Camera);
            }else{
                Toast.makeText(getApplicationContext(),"Error opening camera",Toast.LENGTH_LONG).show();
            }
        }
    }







    private File createImageFile()  {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "GIS");
        File image = null;

        if(!storageDir.exists()){

            storageDir.mkdirs();

        }
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        }catch (Exception e){

            e.printStackTrace();

        }
        // Save a file: path for use with ACTION_VIEW intents

        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
if(requestCode==Request_Camera){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            }
        }


        }



}
