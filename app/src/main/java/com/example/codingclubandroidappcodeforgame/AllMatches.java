package com.example.codingclubandroidappcodeforgame;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllMatches extends Fragment {

    private RecyclerView recyclerView;
    private allMatchAdapter adapter;
    private List<allMatchModel> itemList;
    private String url="http://cricapi.com/api/matches?apikey=YfQA0ujGWhYgdazPHa3KQeqbl8j1";


    public AllMatches() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_all_matches, container, false);
        recyclerView=v.findViewById(R.id.allMatchRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        itemList=new ArrayList<>();

        //adapter=new allMatchAdapter(itemList,container.getContext());
        //recyclerView.setAdapter(adapter);
        loadUrlData();


        return v;
    }
    private void loadUrlData(){

        final ProgressDialog pd=new ProgressDialog(getContext());
        pd.setMessage("Loading All matches data...");
        pd.show();

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();

                try{
                    JSONArray jsonArray=new JSONObject(response).getJSONArray("matches");
                    for(int i=0;i<jsonArray.length();i++){
                        try{
                            String uniqueId=jsonArray.getJSONObject(i).getString("unique_id");
                            String team1=jsonArray.getJSONObject(i).getString("team-1");
                            String team2=jsonArray.getJSONObject(i).getString("team-2");
                            String matchType=jsonArray.getJSONObject(i).getString("type");
                            String matchStatus=jsonArray.getJSONObject(i).getString("matchStarted");
                            if(matchType.equals("true")){
                                matchType="Match Started";
                            }
                            else{
                                matchType="Match Not started";
                            }
                            String date=jsonArray.getJSONObject(i).getString("dateTimeGMT");
                            SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                            format1.setTimeZone(TimeZone.getTimeZone(date));
                            Date date2=format1.parse(date);
                            SimpleDateFormat format2=new SimpleDateFormat("dd/MM/yyyy HH:mm");
                            format2.setTimeZone(TimeZone.getTimeZone("GMT"));
                            String datetime=format2.format(date2);
                            itemList.add(new allMatchModel(uniqueId,team1,team2,matchType,matchStatus,datetime));
                        }
                        catch(Exception e){
                            Toast.makeText(getContext(), "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                }catch(Exception e){
                    Toast.makeText(getContext(), "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                adapter=new allMatchAdapter(itemList,getContext());
                recyclerView.setAdapter(adapter);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

}
