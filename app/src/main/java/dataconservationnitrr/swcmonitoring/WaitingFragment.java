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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class WaitingFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    ProgressBar progressBar;



    public WaitingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) view.findViewById(R.id.progd);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        progressBar.setVisibility(View.VISIBLE);
        getData(All_urls.values.pendingData);
        return view;
    }

    private void getData(String url) {
        final ArrayList<MapModel> mapModels = new ArrayList<>();
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

                                ListAdapter adapter = new ListAdapter(getActivity(),mapModels,2);
                                recyclerView.setAdapter(adapter);

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
