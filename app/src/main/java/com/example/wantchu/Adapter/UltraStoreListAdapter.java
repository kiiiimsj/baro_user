package com.example.wantchu.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

public class UltraStoreListAdapter extends RecyclerView.Adapter<UltraStoreListAdapter.UltraStoreListViewHolder> {

    static public Context context;

    public interface OnListItemLongSelectedInterface{
        void onItemLongSelected(View v, int adapterPosition);
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    private static UltraStoreListAdapter.OnListItemLongSelectedInterface mLongListener;
    private static UltraStoreListAdapter.OnListItemSelectedInterface mListener;

    static ArrayList<ListStoreHelperClass> listStoreHelperClasses;

    public UltraStoreListAdapter(ArrayList<ListStoreHelperClass> listStoreHelperClasses, UltraStoreListAdapter.OnListItemSelectedInterface listener, UltraStoreListAdapter.OnListItemLongSelectedInterface longListener, Context context){
        this.listStoreHelperClasses = listStoreHelperClasses;
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
    public UltraStoreListAdapter.UltraStoreListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.ultra_store_design, parent, false);
        UltraStoreListViewHolder ultraStoreListViewHolder = new UltraStoreListViewHolder(view, viewType);
        return ultraStoreListViewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull UltraStoreListAdapter.UltraStoreListViewHolder holder, int position) {
        ListStoreHelperClass listStoreHelperClass = listStoreHelperClasses.get(position);

        holder.storeImage.setBackgroundDrawable(Drawable.createFromPath(listStoreHelperClass.storeImage));
        holder.storeName.setText(listStoreHelperClass.storeName);
        holder.storeLocation.setText(listStoreHelperClass.storeLocation);
        holder.mDistance.setText(String.valueOf((int)listStoreHelperClass.storeDistance + "m"));
        holder.storeId.setText(String.valueOf(listStoreHelperClass.storeId));
    }

    @Override
    public int getItemCount() {
        return listStoreHelperClasses == null ? 0 : listStoreHelperClasses.size();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull UltraStoreListAdapter.UltraStoreListViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull UltraStoreListAdapter.UltraStoreListViewHolder  holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewRecycled(@NonNull UltraStoreListAdapter.UltraStoreListViewHolder  holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull UltraStoreListAdapter.UltraStoreListViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class UltraStoreListViewHolder extends RecyclerView.ViewHolder {

        public ImageView storeImage;
        public TextView storeName;
        public TextView storeLocation;
        public TextView mDistance;
        public TextView storeId;

        public UltraStoreListViewHolder(@NonNull View itemView, int po) {
            super(itemView);

            storeName = itemView.findViewById(R.id.store_name);
            storeImage = itemView.findViewById(R.id.store_image);
            storeLocation = itemView.findViewById(R.id.store_location);
            mDistance = itemView.findViewById(R.id.store_distance);
            storeId = itemView.findViewById(R.id.store_id);
            ListStoreHelperClass list = listStoreHelperClasses.get(po);
            makeRequestUltraStore(list.storeImage, context, storeImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemSelected(view, getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mLongListener.onItemLongSelected(view, getAdapterPosition());
                    return false;
                }
            });
        }

        public void makeRequestUltraStore(String store_image, Context context, final ImageView image){
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
