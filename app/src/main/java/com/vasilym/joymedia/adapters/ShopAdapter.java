package com.vasilym.joymedia.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vasilym.joymedia.R;
import com.vasilym.joymedia.interfaces.ShopAdapterCallback;
import com.vasilym.joymedia.models.Shop;

import java.util.ArrayList;
import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private ShopAdapterCallback shopAdapterCallback;
    private List<Shop> shops;
    private Context context;
    private boolean isloading=false;

    public ShopAdapter(Context context){
        this.context = context;
        shops = new ArrayList<>();
    }

    public void setRecyclerView(RecyclerView recyclerView){
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = shops.size()-1;
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isloading && totalItemCount <= lastVisibleItem && shops.size()>0) {
                    shops.add(null);
                    notifyDataSetChanged();
                    shopAdapterCallback.onLoadMore(shops.size());
                    isloading = true;
                }
            }
        });
    }

    public void setShopAdapterCallback(ShopAdapterCallback shopAdapterCallback){
        this.shopAdapterCallback = shopAdapterCallback;
    }

    public void firstLoading(){
        isloading = false;
        shops.add(null);
        notifyDataSetChanged();
        isloading = true;
    }

    public void finishLoading(){
        shops.remove(shops.size()-1);
        isloading = false;
        notifyDataSetChanged();
    }

    public void clearShops(){
        this.shops.clear();
        notifyDataSetChanged();
    }

    public void addShops(List<Shop> shops){
        this.shops.addAll(shops);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return shops.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.shops_item, parent, false);
            view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            return new ShopViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.loading_item, parent, false);
            view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ShopViewHolder) {
            ShopViewHolder shopViewHolder = (ShopViewHolder) holder;
            Shop shop = shops.get(position);
            String distance="";
            shopViewHolder.name.setText(shop.getName());
            shopViewHolder.address.setText(shop.getAddress());
            shopViewHolder.opening_hours.setText(shop.getOpeningHours());
            if(shop.getDistance()>1000){
                distance = String.valueOf(Math.rint(10.0 * (shop.getDistance()/1000)) / 10.0) + " км";
            }
            else{
                distance = String.valueOf((int) shop.getDistance()) + " м";
            }
            shopViewHolder.distance.setText(distance);
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.shopsprogress.setIndeterminate(true);
        }
    }



    @Override
    public int getItemCount() {
        return shops.size();
    }

    public class ShopViewHolder extends RecyclerView.ViewHolder {

        TextView name, address, opening_hours, distance;
        View shopitemroot;


        public ShopViewHolder(View itemView) {
            super(itemView);
            shopitemroot = itemView.findViewById(R.id.shopitemroot);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            opening_hours = itemView.findViewById(R.id.opening_hours);
            distance = itemView.findViewById(R.id.distance);
            shopitemroot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,shops.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
                }
            });

        }


    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar shopsprogress;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            shopsprogress = itemView.findViewById(R.id.shopsprogress);
        }
    }

}
