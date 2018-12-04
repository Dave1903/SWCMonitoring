package dataconservationnitrr.swcmonitoring;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ApprovedFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ListAdapter adapter;
     ArrayList<MapModel> mapModels = new ArrayList<>();



    public ApprovedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_list, container, false);

         recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) view.findViewById(R.id.progd);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        progressBar.setVisibility(View.VISIBLE);
        getUserData(All_urls.values.approvedData);
        return view;
    }

    private void getData(String url) {

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject p) {
                        // display response
                        Log.d("Response", p.toString());


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


                                }

                                adapter.notifyDataSetChanged();




                            }


                        } catch (Exception e) {
                            // JSON error
                            e.printStackTrace();



                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", "");


                    }
                }
        );


        AppController.getInstance().addToRequestQueue(getRequest);
    }

    private void getUserData(String url) {

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject p) {
                        // display response
                        Log.d("Response", p.toString());

                        progressBar.setVisibility(View.GONE);
                        try {

                            JSONArray arr= p.getJSONArray("posts");


                            if (arr.length() > 0) {

                                for (int i=0;i<arr.length();i++){
                                    MapModel mapModel = new MapModel();

                                    JSONObject post = arr.getJSONObject(i).getJSONObject("post");
                                    mapModel.setRiver(post.getString("description"));
                                    mapModel.setLat(post.getString("latitude"));
                                    mapModel.setLang(post.getString("longitude"));
                                    mapModel.setVillage(post.getString("locality"));
                                    mapModel.setImgUrl(post.getString("imgUrl"));
                                    mapModel.setId(post.getString("id"));

                                    mapModels.add(mapModel);


                                }

                                 adapter = new ListAdapter(getActivity(),mapModels,1);
                                recyclerView.setAdapter(adapter);

                                getData(All_urls.values.mapData);

                            }


                        } catch (Exception e) {
                            // JSON error
                            e.printStackTrace();
                            progressBar.setVisibility(View.VISIBLE);


                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", "");
                        progressBar.setVisibility(View.VISIBLE);

                    }
                }
        );


        AppController.getInstance().addToRequestQueue(getRequest);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }


}
