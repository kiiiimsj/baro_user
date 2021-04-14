package com.tpn.baro.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.tpn.baro.JsonParsingHelper.FavoriteListParsing;
import com.tpn.baro.JsonParsingHelper.FavoriteParsing;
import com.tpn.baro.JsonParsingHelper.ListStoreListParsing;
import com.tpn.baro.JsonParsingHelper.ListStoreParsing;
import com.tpn.baro.R;
import com.tpn.baro.Url.UrlMaker;

public class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.ListStoreViewHolder> {

    private Context context;
    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    private static StoreListAdapter.OnListItemSelectedInterface mListener;


    public ListStoreParsing listStoreParsing;
    public FavoriteParsing favoriteParsing;
    public StoreListAdapter(ListStoreParsing listStoreParsing, StoreListAdapter.OnListItemSelectedInterface listener, Context context){
        this.listStoreParsing = listStoreParsing;
        this.favoriteParsing = null;
        mListener = listener;
        this.context = context;
    }
    public StoreListAdapter(FavoriteParsing favoriteParsing, StoreListAdapter.OnListItemSelectedInterface listener, Context context){
        this.favoriteParsing = favoriteParsing;
        this.listStoreParsing = null;
        mListener = listener;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public StoreListAdapter.ListStoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.design_store_list_big, parent, false);
        ListStoreViewHolder listStoreViewHolder = new ListStoreViewHolder(view);
        return listStoreViewHolder;
    }

    @SuppressLint({"ResourceAsColor", "NewApi"})
    @Override
    public void onBindViewHolder(@NonNull StoreListAdapter.ListStoreViewHolder holder, final int position) {
        if(favoriteParsing != null && favoriteParsing.getFavorite().size() != 0) {
            FavoriteListParsing favoriteStore = favoriteParsing.getFavorite().get(position);
            holder.storeName.setText(favoriteStore.getStore_name());
            if (favoriteStore.getDistance() > 1000){
                holder.mDistance.setText(String.format("%,.1f", ((int)favoriteStore.getDistance() / 100) * 0.1) + "km");
            }else {
                holder.mDistance.setText((int) favoriteStore.getDistance() + "m");
            }
            holder.storeId.setText(String.valueOf(favoriteStore.getStore_id()));

            if(favoriteStore.getIs_open() != null) {
                if(favoriteStore.getIs_open().equals("N")) {
                    holder.isOpen.setText("준비중");
                    holder.isOpen.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.text_info_color)));
                }
                if(favoriteStore.getIs_open().equals("Y")) {
                    holder.isOpen.setText("영업중");
                }
            }
            if(favoriteStore.getDiscount_rate() != 0 ) {
                holder.discountRate.setText("SALE "+favoriteStore.getDiscount_rate()+"%");
            }else {
                holder.discountRate.setVisibility(View.INVISIBLE);
            }
            makeRequest(favoriteStore.getStore_image(), context, holder.storeImage);
        }
        if(listStoreParsing != null && listStoreParsing.store.size() != 0) {
            ListStoreListParsing listStoreHelperClass = listStoreParsing.store.get(position);

            holder.storeName.setText(listStoreHelperClass.getStore_name());
            if (listStoreHelperClass.getDistance() > 1000){
                holder.mDistance.setText(String.format("%,.1f", ((int)listStoreHelperClass.getDistance() / 100) * 0.1) + "km");
            }else {
                holder.mDistance.setText((int) listStoreHelperClass.getDistance() + "m");
            }
            holder.storeId.setText(String.valueOf(listStoreHelperClass.getStore_id()));
            if(listStoreHelperClass.getIs_open() != null) {
                if(listStoreHelperClass.getIs_open().equals("N")) {
                    holder.isOpen.setText("준비중");
                    holder.isOpen.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.text_info_color)));
                }
                if(listStoreHelperClass.getIs_open().equals("Y")) {
                    holder.isOpen.setText("영업중");
                }
            }
            if(listStoreHelperClass.getDiscount_rate() != 0 ) {
                holder.discountRate.setText("SALE "+listStoreHelperClass.getDiscount_rate()+"%");
            }else {
                holder.discountRate.setVisibility(View.INVISIBLE);
            }
            makeRequest(listStoreHelperClass.getStore_image(), context, holder.storeImage);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemSelected(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listStoreParsing == null ? (favoriteParsing == null ? 0 : favoriteParsing.getFavorite().size()) : listStoreParsing.store.size();
    }
    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull StoreListAdapter.ListStoreViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull StoreListAdapter.ListStoreViewHolder  holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewRecycled(@NonNull StoreListAdapter.ListStoreViewHolder  holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull StoreListAdapter.ListStoreViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public static class ListStoreViewHolder extends RecyclerView.ViewHolder {

        public ImageView storeImage;
        public TextView storeName;
        public TextView storeLocation;
        public TextView mDistance;
        public TextView storeId;
        public TextView isOpen;
        public TextView discountRate;
        public RelativeLayout store;

        public ListStoreViewHolder(@NonNull View itemView) {
            super(itemView);

            storeName = itemView.findViewById(R.id.store_name);
            storeImage = itemView.findViewById(R.id.store_image);
            mDistance = itemView.findViewById(R.id.distance);
            storeId = itemView.findViewById(R.id.store_id);
            isOpen = itemView.findViewById(R.id.is_open);
            discountRate = itemView.findViewById(R.id.discount_rate);
            store = itemView.findViewById(R.id.store);
        }
    }
    public void makeRequest(String store_image, Context context, final ImageView image) {
        //makeRequest(storeHelperClass.storeImages.get(viewType), listStoreViewHolder, context);
        UrlMaker urlMaker = new UrlMaker();
        String lastUrl = "ImageStore.do?image_name=";
        String url = urlMaker.UrlMake(lastUrl);
        StringBuilder urlBuilder = new StringBuilder()
                .append(url)
                .append(store_image);
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        ImageRequest request = new ImageRequest(urlBuilder.toString(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        image.setImageBitmap(response);
                    }
                }, image.getMaxWidth(), image.getMaxHeight(), ImageView.ScaleType.FIT_XY, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error", "error");
                    }
                });
        requestQueue.add(request);
    }
}
