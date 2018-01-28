package com.vasilym.joymedia.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vasilym.joymedia.fragments.Tab1;
import com.vasilym.joymedia.fragments.Tab2;


public class TabsAdapter extends FragmentPagerAdapter {


    private Tab1 tab1;
    private Tab2 tab2;
    private String title1, title2;

    public TabsAdapter(FragmentManager fragmentManager, Tab1 tab1, Tab2 tab2){
        super(fragmentManager);
        this.tab1 = tab1;
        this.tab2 = tab2;
    }

    @Override
    public CharSequence getPageTitle(int position){
        switch(position){
            case 0:
                return tab1.getTabname();
            case 1:
                return tab2.getTabname();
            default:
                return "";
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return tab1;
            case 1:
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
