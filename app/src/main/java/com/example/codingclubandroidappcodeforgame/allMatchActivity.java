package com.example.codingclubandroidappcodeforgame;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class allMatchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private allMatchAdapter adapter;
    private List<allMatchModel> itemList;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private String url="http://cricapi.com/api/matches?apikey=YfQA0ujGWhYgdazPHa3KQeqbl8j1";

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getUid() == null){
            Intent intent=new Intent(allMatchActivity.this,LoginActivity.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            }
            else{
                startActivity(intent);
            }
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_match);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("All Matches");
        recyclerView=findViewById(R.id.allMatchRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList=new ArrayList<>();

        //adapter=new allMatchAdapter(itemList,container.getContext());
        //recyclerView.setAdapter(adapter);
        loadUrlData();

    }
    private void loadUrlData(){

        final ProgressDialog pd=new ProgressDialog(this);
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
                            if(matchStatus.equals("true")){
                                matchStatus="Match Started";
                            }
                            else{
                                matchStatus="Match Not started";
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
                            Toast.makeText(allMatchActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                }catch(Exception e){
                    Toast.makeText(allMatchActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                adapter=new allMatchAdapter(itemList,allMatchActivity.this);
                recyclerView.setAdapter(adapter);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(allMatchActivity.this, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(allMatchActivity.this);
        requestQueue.add(stringRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.logout){
            AlertDialog.Builder builder
                    = new AlertDialog
                    .Builder(allMatchActivity.this);
            builder.setMessage("Are you sure want to Logout from current account?");

            builder.setTitle("Alert !");

            builder.setCancelable(false);
            builder
                    .setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which)
                                {
                                    mAuth.signOut();
                                    startActivity(new Intent(allMatchActivity.this,LoginActivity.class));
                                    finish();
                                    //finish();
                                }
                            });
            builder
                    .setNegativeButton(
                            "No",
                            new DialogInterface
                                    .OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which)
                                {
                                    dialog.cancel();
                                }
                            });

            // Create the Alert dialog
            AlertDialog alertDialog = builder.create();

            // Show the Alert Dialog box
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }
}
