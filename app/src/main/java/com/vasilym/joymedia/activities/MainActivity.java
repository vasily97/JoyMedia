package com.vasilym.joymedia.activities;

import android.Manifest;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vasilym.joymedia.R;
import com.vasilym.joymedia.adapters.ShopAdapter;
import com.vasilym.joymedia.adapters.TabsAdapter;
import com.vasilym.joymedia.fragments.Tab1;
import com.vasilym.joymedia.fragments.Tab2;
import com.vasilym.joymedia.interfaces.MainPresenterCallback;
import com.vasilym.joymedia.interfaces.ShopAdapterCallback;
import com.vasilym.joymedia.interfaces.Tab1Callback;
import com.vasilym.joymedia.models.Shop;
import com.vasilym.joymedia.presenters.MainPresenter;

import java.util.List;


public class MainActivity extends AppCompatActivity implements MainPresenterCallback, Tab1Callback, ShopAdapterCallback, SwipeRefreshLayout.OnRefreshListener{

    private MainPresenter mainPresenter;
    private TextView maincontent;
    private LinearLayout bottom_sheet;
    private SwipeRefreshLayout swiperefresh;
    private BottomSheetBehavior bottomSheetBehavior;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabsAdapter tabsAdapter;
    private Tab1 tab1;
    private Tab2 tab2;
    private ShopAdapter shopAdapter;


    View shadow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        maincontent = findViewById(R.id.maincontent);
        shadow = findViewById(R.id.shadow);
        bottom_sheet = findViewById(R.id.bottom_sheet);
        DisplayMetrics displaymetrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        bottomSheetBehavior.setPeekHeight(height/2);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        swiperefresh.setEnabled(false);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        shadow.setVisibility(View.GONE);
                        swiperefresh.setEnabled(true);
                        tab1.scrollToStart();
                        tab2.scrollToStart();
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                shadow.setVisibility(View.VISIBLE);
                shadow.setAlpha(slideOffset);
            }
        });
        swiperefresh = findViewById(R.id.swiperefresh);
        swiperefresh.setOnRefreshListener(this);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        tab1 = new Tab1();
        tab2 = new Tab2();
        tab1.setTabname(getString(R.string.tab1));
        tab2.setTabname(getString(R.string.tab2));
        shopAdapter = new ShopAdapter(this);
        shopAdapter.setShopAdapterCallback(this);
        tab2.setShopAdapter(shopAdapter);
        tabsAdapter = new TabsAdapter(getSupportFragmentManager(), tab1, tab2);
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case 0:
                        tab1.setScrollEnabled(true);
                        tab2.setScrollEnabled(false);
                        break;
                    case 1:
                        tab1.setScrollEnabled(false);
                        tab2.setScrollEnabled(true);
                        break;
                }
                bottom_sheet.requestLayout();
                bottom_sheet.invalidate();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
        mainPresenter = new MainPresenter(this,this);
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);
    }

    @Override
    public void onRefresh() {
        Toast.makeText(getApplicationContext(),getString(R.string.refreshing), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swiperefresh.setRefreshing(false);
            }
        }, 2000);
    }

    @Override
    public void onButtonClick(){
        Toast.makeText(getApplicationContext(),getString(R.string.loading), Toast.LENGTH_SHORT).show();
        maincontent.setText(getString(R.string.loadingcatalog));
        shopAdapter.clearShops();
        shopAdapter.firstLoading();
        mainPresenter.loadShops();
    }

    @Override
    public void onLoadShops(List<Shop> shops, int resultscount){
        maincontent.setText(getString(R.string.loaded) + " " + String.valueOf(resultscount) + " " + getString(R.string.objects));
        shopAdapter.finishLoading();
        shopAdapter.addShops(shops);
        tab1.setButtonEnabled(true);
    }

    @Override
    public void onAddShops(List<Shop> shops){
        shopAdapter.finishLoading();
        shopAdapter.addShops(shops);
        tab1.setButtonEnabled(true);
    }

    @Override
    public void onError(String message){
        Toast.makeText(getApplicationContext(),getString(R.string.error) + " " + message, Toast.LENGTH_SHORT).show();
        maincontent.setText("Main content");
        shopAdapter.finishLoading();
        tab1.setButtonEnabled(true);
    }


    @Override
    public void onLoadMore(int start){
        tab1.setButtonEnabled(false);
        mainPresenter.addShops(start);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.onDestroy();
    }
}
