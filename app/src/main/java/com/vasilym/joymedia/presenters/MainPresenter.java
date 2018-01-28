package com.vasilym.joymedia.presenters;

import android.content.Context;

import com.vasilym.joymedia.interfaces.MainPresenterCallback;
import com.vasilym.joymedia.models.Shop;
import com.vasilym.joymedia.utils.DBHelper;
import com.vasilym.joymedia.utils.LocationHelper;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter {

    private MainPresenterCallback mainPresenterCallback;
    private CompositeDisposable disposables;
    private DBHelper dbHelper;
    private LocationHelper locationHelper;

    public MainPresenter(Context context, MainPresenterCallback mainPresenterCallback){
        this.mainPresenterCallback = mainPresenterCallback;
        this.disposables = new CompositeDisposable();
        dbHelper = new DBHelper(context);
        locationHelper = new LocationHelper(context);
    }

    public void loadShops(){
        disposables.add(Completable.fromAction(() -> dbHelper.deleteShops())
                .andThen(Completable.fromAction(() -> dbHelper.loadShops()))
                .andThen(Completable.fromAction(() -> dbHelper.sortShops(locationHelper.getLocation())))
                .andThen(Single.fromCallable(() -> dbHelper.getShopsPage(0)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Shop>>() {
                    @Override
                    public void onSuccess(List<Shop> shops) {
                        mainPresenterCallback.onLoadShops(shops, dbHelper.getResultscount());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mainPresenterCallback.onError(e.getMessage());
                    }
                }));
    }

    public void addShops(int start){
        disposables.add(Completable.fromAction(() -> dbHelper.testWait(1000))
                .andThen(Single.fromCallable(() -> dbHelper.getShopsPage(start)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Shop>>() {
                    @Override
                    public void onSuccess(List<Shop> shops) {
                        mainPresenterCallback.onAddShops(shops);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mainPresenterCallback.onError(e.getMessage());
                    }
                }));

    }

    public void onDestroy(){
        disposables.clear();
    }

}
