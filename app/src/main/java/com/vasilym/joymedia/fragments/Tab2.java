package com.vasilym.joymedia.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vasilym.joymedia.R;
import com.vasilym.joymedia.adapters.ShopAdapter;



public class Tab2 extends Fragment {

    private RecyclerView shopslist;
    private String tabname;
    private ShopAdapter shopAdapter;

    public Tab2() {

    }

    public void setTabname(String tabname){
        this.tabname = tabname;
    }

    public String getTabname(){
        return tabname;
    }

    public void setShopAdapter(ShopAdapter shopAdapter){
        this.shopAdapter = shopAdapter;
    }

    public void setScrollEnabled(boolean state){
        shopslist.setNestedScrollingEnabled(state);
    }

    public void scrollToStart(){
        shopslist.smoothScrollToPosition(0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab2, container, false);
        shopslist = view.findViewById(R.id.shopslist);
        shopslist.setLayoutManager(new LinearLayoutManager(getContext()));
        shopAdapter.setRecyclerView(shopslist);
        shopslist.setAdapter(shopAdapter);
        shopslist.setNestedScrollingEnabled(false);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
