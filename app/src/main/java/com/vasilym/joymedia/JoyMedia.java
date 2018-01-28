package com.vasilym.joymedia;

import android.app.Application;

import io.realm.Realm;


public class JoyMedia extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());
    }

}
