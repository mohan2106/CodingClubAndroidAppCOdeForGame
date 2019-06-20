package com.example.codingclubandroidappcodeforgame;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class pagerViewerAdapter extends FragmentPagerAdapter {

    public pagerViewerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                AllMatches mProfile=new AllMatches();
                return mProfile;
            case 1:
                LiveScoreFragment ne=new LiveScoreFragment();
                return ne;
            case 2:
                ProfileFragment nf=new ProfileFragment();
                return nf;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
