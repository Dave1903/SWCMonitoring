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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoUploadActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_upload);
        File imageFile = (File)getIntent().getExtras().get("filePath");
        textView = (TextView) findViewById(R.id.activity);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PhotoUploadActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        File file= new File(imageFile.getAbsolutePath());
        Bitmap bitmap = BitmapFactory.decodeFile(file.toString());

        imageView = (ImageView) findViewById(R.id.img);
        imageView.setImageBitmap(bitmap);
        editText=(EditText)findViewById(R.id.description);
    }


}
