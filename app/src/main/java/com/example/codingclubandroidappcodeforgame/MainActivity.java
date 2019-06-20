package com.example.codingclubandroidappcodeforgame;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private TabLayout tab;
    private TabItem profileLabel;
    private TabItem allUSer;
    public static Activity ma;
    private TabItem notification;
    private pagerViewerAdapter mPagerViewAdapter;
    private ViewPager viewPager;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getUid() == null){
            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
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
        setContentView(R.layout.activity_main);

        ma=this;
        startActivity(new Intent(MainActivity.this,allMatchActivity.class));
        finish();
        tab=(TabLayout)findViewById(R.id.tabLayout);
        profileLabel=(TabItem)findViewById(R.id.profileLabel);
        allUSer=(TabItem)findViewById(R.id.allUserLabel);
        notification=(TabItem)findViewById(R.id.notificationLabel);
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(2);
        mPagerViewAdapter=new pagerViewerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPagerViewAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


}
