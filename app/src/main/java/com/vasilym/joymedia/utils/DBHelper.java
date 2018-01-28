package com.vasilym.joymedia.utils;


import android.content.Context;
import android.location.Location;

import com.google.gson.Gson;
import com.vasilym.joymedia.R;
import com.vasilym.joymedia.models.Shop;
import com.vasilym.joymedia.models.ShopList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;


import io.realm.Realm;
import io.realm.RealmResults;

public class DBHelper {

    private Context context;
    private int resultscount;

    public DBHelper(Context context){
        this.context = context;
    }

    public int getResultscount(){
        return resultscount;
    }


    public void deleteShops(){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Shop> result = realm.where(Shop.class).findAll();
        realm.beginTransaction();
        result.deleteAllFromRealm();
        realm.commitTransaction();
    }

    public void loadShops(){
        Realm realm = Realm.getDefaultInstance();
        InputStream inputStream = context.getResources().openRawResource(R.raw.shops);
        Reader reader = new BufferedReader(new InputStreamReader(inputStream));
        Gson gson = new Gson();
        List<Shop> shops = gson.fromJson(reader, ShopList.class).getShop();
        resultscount = shops.size();
        realm.beginTransaction();
        for(Shop shop : shops){
            realm.copyToRealm(shop);
        }
        realm.commitTransaction();
    }

    public void sortShops(Location current){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Shop> result = realm.where(Shop.class).findAll();
        realm.beginTransaction();
        for(Shop shop : result){
            Location location = new Location("");
            location.setLatitude(Double.parseDouble(shop.getLatitude()));
            location.setLongitude(Double.parseDouble(shop.getLongitude()));
            float distance = location.distanceTo(current);
            shop.setDistance(distance);
        }
        realm.commitTransaction();
    }

    public List<Shop> getShopsPage(int start){
        int end = start + 30;
        if(end > resultscount){
            end = resultscount;
        }
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Shop> result = realm.where(Shop.class).sort("distance").findAll();
        List<Shop> response = new ArrayList<>();
        for(int i = start; i<end ; i++){
            response.add(realm.copyFromRealm(result.get(i)));
        }
        return response;
    }

    public void testWait(long ms) throws Exception{
        Thread.sleep(ms);
    }




}
