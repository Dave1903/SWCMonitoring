package dataconservationnitrr.swcmonitoring;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import org.json.JSONArray;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;

public class PhotoUploadActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    EditText editText;
    Cloud_storage cloud_storage;
    Storage storage;
    ProgressDialog dialog;
    File imageFile;
    Bitmap bitmap;
    LocationTracker tracker;
    String cityname;
    Double latitude,longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_upload);
         imageFile = (File)getIntent().getExtras().get("filePath");
        textView = (TextView) findViewById(R.id.activity);


        dialog= new ProgressDialog(this);

        cloud_storage = new Cloud_storage(this);
        storage =cloud_storage.getStorage();

         bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

        imageView = (ImageView) findViewById(R.id.img);
        imageView.setImageBitmap(bitmap);
        editText=(EditText)findViewById(R.id.description);
    }

    public void imgupload(View view) {
uploadImg();

    }

    public void uploadImg(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {

            TrackerSettings settings =
                    new TrackerSettings()
                            .setUseGPS(true)
                            .setUseNetwork(true)
                            .setUsePassive(true)
                            .setTimeBetweenUpdates(1*1000);

            tracker = new LocationTracker(this, settings) {
                @Override
                public void onLocationFound(Location location)
                {

                    if (location != null) {
                        latitude =location.getLatitude();
                        longitude =location.getLongitude();

                        /*lat_all =21.239321;
                         long_all = 81.659291;*/


                        Geocoder geocoder = new Geocoder(PhotoUploadActivity.this, Locale.getDefault());
                        List<Address> addresses = null;
                        try {

                            addresses = geocoder.getFromLocation(latitude,longitude, 1);
                            if(addresses.size()>0) {
                                 cityname = addresses.get(0).getLocality();




                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }


                }

                @Override
                public void onTimeout() {


                }
            };

             tracker.startListening();


            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50/*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();
            ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);

            PostTask ps = new PostTask(bs);
            ps.execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                uploadImg();
            }
        }

    }
    private class PostTask extends AsyncTask<String, Integer, String> {
        ByteArrayInputStream inputStream;

        public PostTask(ByteArrayInputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            BlobInfo blobInfo =
                    storage.create(
                            BlobInfo
                                    .newBuilder("clay_uploads","image_"+System.currentTimeMillis())
                                    .setContentType("image/png")
                                    .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))))
                                    .build(),
                            inputStream);

            return blobInfo.getMediaLink();


        }



        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            uploadPost(All_urls.values.dataUpload(latitude,longitude,result,editText.getText().toString(),cityname));



        }
    }

    private void uploadPost(String url) {
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String p) {
                        // display response
                        Log.d("Response", p.toString());

                        dialog.dismiss();
                        try {

                            if (p.length() > 0) {

                                Toasty.success(getApplicationContext(),"Details Uploaded",Toast.LENGTH_LONG).show();




                            }


                        } catch (Exception e) {
                            // JSON error
                            e.printStackTrace();
                            dialog.dismiss();



                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", "");
                        dialog.dismiss();


                    }
                }
        );


        AppController.getInstance().addToRequestQueue(getRequest,"");
    }


}
