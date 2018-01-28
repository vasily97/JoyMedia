package com.vasilym.joymedia.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vasilym.joymedia.R;
import com.vasilym.joymedia.interfaces.Tab1Callback;


public class Tab1 extends Fragment {


    private NestedScrollView tab1scroll;
    private String tabname;
    private Tab1Callback tab1Callback;
    private Button button;

    public Tab1() {

    }

    public void setTabname(String tabname){
        this.tabname = tabname;
    }

    public String getTabname(){
        return tabname;
    }

    public void setScrollEnabled(boolean state){
        tab1scroll.setNestedScrollingEnabled(state);
    }

    public void setButtonEnabled(boolean enabled){
        button.setEnabled(enabled);
    }

    public void scrollToStart(){
        tab1scroll.scrollTo(0,0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);
        tab1scroll = view.findViewById(R.id.tab1scroll);
        tab1scroll.setNestedScrollingEnabled(true);
        button = view.findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setEnabled(false);
                tab1Callback.onButtonClick();
            }
        });
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i<1000; i++){
            stringBuilder.append("Tab content ");
        }
        TextView tab1text = view.findViewById(R.id.tab1text);
        tab1text.setText(stringBuilder.toString());
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        tab1Callback = (Tab1Callback) context;
    }

}
