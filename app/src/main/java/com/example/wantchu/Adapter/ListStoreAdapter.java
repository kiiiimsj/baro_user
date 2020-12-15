package com.example.wantchu.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.icu.text.Normalizer2;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.AdapterHelper.ListStoreHelperClass;
import com.example.wantchu.R;
import com.example.wantchu.Url.UrlMaker;

import java.util.ArrayList;

public class ListStoreAdapter extends RecyclerView.Adapter<ListStoreAdapter.ListStoreViewHolder> {

    static public Context context;

    public interface OnListItemLongSelectedInterface{
        void onItemLongSelected(View v, int adapterPosition);
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    private static ListStoreAdapter.OnListItemSelectedInterface mListener;
    private static ListStoreAdapter.OnListItemLongSelectedInterface mLongListener;


    static ArrayList<ListStoreHelperClass> listStoreLocations;

    public ListStoreAdapter(ArrayList<ListStoreHelperClass> listStoreLocations, ListStoreAdapter.OnListItemSelectedInterface listener, ListStoreAdapter.OnListItemLongSelectedInterface longListener, Context context){
        this.listStoreLocations = listStoreLocations; //DataList
        this.mListener = listener;
        this.mLongListener = longListener;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ListStoreAdapter.ListStoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.store_design, parent, false);
        ListStoreViewHolder listStoreViewHolder = new ListStoreViewHolder(view, viewType);
        return listStoreViewHolder;
    }

    @SuppressLint({"ResourceAsColor", "NewApi"})
    @Override
    public void onBindViewHolder(@NonNull ListStoreAdapter.ListStoreViewHolder holder, int position) {
        ListStoreHelperClass listStoreHelperClass = listStoreLocations.get(position);
        holder.storeName.setText(listStoreHelperClass.storeName);
        if (listStoreHelperClass.storeDistance > 1000){
            holder.mDistance.setText(String.format("%,.1f", ((int)listStoreHelperClass.storeDistance / 100) * 0.1) + "km");
        }else {
            holder.mDistance.setText((int) listStoreHelperClass.storeDistance + "m");
        }
        holder.storeId.setText(String.valueOf(listStoreHelperClass.storeId));
        if(listStoreHelperClass.storeIsOpen != null) {
            if(listStoreHelperClass.storeIsOpen.equals("N")) {
                holder.isOpen.setText("준비중");
                holder.isOpen.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.text_info_color)));
            }
            if(listStoreHelperClass.storeIsOpen.equals("Y")) {
                holder.isOpen.setText("영업중");
            }
        }
    }

    @Override
    public int getItemCount() {
        return listStoreLocations == null ? 0 : listStoreLocations.size();
    }
    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull ListStoreAdapter.ListStoreViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ListStoreAdapter.ListStoreViewHolder  holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewRecycled(@NonNull ListStoreAdapter.ListStoreViewHolder  holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ListStoreAdapter.ListStoreViewHolder holder) {
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
        public LinearLayout store;

        public ListStoreViewHolder(@NonNull View itemView, int po) {
            super(itemView);

            storeName = itemView.findViewById(R.id.store_name);
            storeImage = itemView.findViewById(R.id.store_image);
            mDistance = itemView.findViewById(R.id.distance);
            storeId = itemView.findViewById(R.id.store_id);
            isOpen = itemView.findViewById(R.id.is_open);
            store = itemView.findViewById(R.id.store);
            ListStoreHelperClass list =listStoreLocations.get(po);
            makeRequest(list.storeImage, context, storeImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemSelected(view, getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongListener.onItemLongSelected(v, getAdapterPosition());
                    return false;
                }
            });
        }

        public void makeRequest(String store_image, Context context, final ImageView image) {
            //makeRequest(storeHelperClass.storeImages.get(viewType), listStoreViewHolder, context);
            UrlMaker urlMaker = new UrlMaker();
            String lastUrl = "ImageStore.do?image_name=";
            String url = urlMaker.UrlMake(lastUrl);
            StringBuilder urlBuilder = new StringBuilder()
                    .append(url)
                    .append(store_image);
            Log.i("store", urlBuilder.toString());
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            ImageRequest request = new ImageRequest(urlBuilder.toString(),
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            image.setImageBitmap(response);
                            Log.i("response", response.toString());
                        }
                    }, 100, 100, ImageView.ScaleType.FIT_CENTER, null,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("error", "error");
                        }
                    });
            requestQueue.add(request);
        }
    }
}
