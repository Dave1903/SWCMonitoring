package dataconservationnitrr.swcmonitoring;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {


    static final int Request_Camera  = 2 ;
    private File imageFile;
    private GoogleMap mMap;
    FloatingActionButton floatingActionButton;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        dialog=new ProgressDialog(this);

        floatingActionButton=(FloatingActionButton)findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        dialog.setTitle("Getting Data...");
       dialog.show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == Request_Camera && resultCode == RESULT_OK ) {
            if(imageFile!= null) {
                if(imageFile.exists()) {

                    Intent intent = new Intent(MapsActivity.this,PhotoUploadActivity.class);
                    intent.putExtra("filePath",imageFile);
                    startActivity(intent);

                }


            }
            else {
                Toast.makeText(getApplicationContext(),"File Not saved ",Toast.LENGTH_LONG).show();
            }

        }

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
                Uri tempUri = FileProvider.getUriForFile(MapsActivity.this,
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



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==Request_Camera){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
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


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(this);


        // Add a marker in Sydney and move the camera

       getData(All_urls.values.mapData);




    }




    private void getData(String url) {
        final ArrayList<MapModel> mapModels = new ArrayList<>();
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject p) {
                        // display response
                        Log.d("Response", p.toString());

                        dialog.dismiss();
                        try {

                            JSONArray arr= p.getJSONArray("posts");


                            if (arr.length() > 0) {

                                for (int i=0;i<arr.length();i++){
                                    MapModel mapModel = new MapModel();

                                   JSONObject post = arr.getJSONObject(i).getJSONObject("post");
                                   mapModel.setArea(post.getString("Area_in_ha"));
                                    mapModel.setRiver(post.getString("River_Name"));
                                    mapModel.setLat(post.getString("X2"));
                                    mapModel.setLang(post.getString("Y2"));
                                    mapModel.setVillage(post.getString("Village"));

                                    mapModels.add(mapModel);

                                    LatLng place = new LatLng(Double.parseDouble(mapModel.getLat()), Double.parseDouble(mapModel.getLang()));
                                    mMap.addMarker(new MarkerOptions().position(place).title("Latitude:"+mapModel.getLat()+"\nLongitude:"+mapModel.getLang()+"\nArea in Ha:"+mapModel.getArea()+"\nVillage:"+mapModel.getVillage()+"\nRiver:"+mapModel.getRiver()));


                                }

                                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(mapModels.get(0).getLat()),Double.parseDouble(mapModels.get(0).getLang()))));







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


        AppController.getInstance().addToRequestQueue(getRequest);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toasty.info(getApplicationContext(),marker.getTitle(),Toast.LENGTH_LONG).show();
        return false;
    }
}
