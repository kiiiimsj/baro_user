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
import com.example.wantchu.JsonParsingHelper.ViewPagersListStoreParsing;
import com.example.wantchu.R;
import com.example.wantchu.Url.UrlMaker;

public class UltraStoreListAdapter extends RecyclerView.Adapter<UltraStoreListAdapter.UltraStoreListViewHolder> {

    static public Context context;

    public interface OnListItemLongSelectedInterface{
        void onUltraStoreLongSelected(View v, int adapterPosition);
    }

    public interface OnListItemSelectedInterface {
        void onUltraStoreSelected(View v, int position);
    }

    private static UltraStoreListAdapter.OnListItemLongSelectedInterface mLongListener;
    private static UltraStoreListAdapter.OnListItemSelectedInterface mListener;

    static ViewPagersListStoreParsing listStoreHelperClasses;

    public UltraStoreListAdapter(ViewPagersListStoreParsing listStoreHelperClasses, UltraStoreListAdapter.OnListItemSelectedInterface listener, UltraStoreListAdapter.OnListItemLongSelectedInterface longListener, Context context){
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
        ViewPagersListStoreParsing.ViewPagerStoreParsing viewPagerStoreParsing = listStoreHelperClasses.store.get(position);
        holder.storeName.setText(viewPagerStoreParsing.getStore_name());
        holder.mInfo.setText(viewPagerStoreParsing.getStore_info());
        holder.storeId.setText(String.valueOf(viewPagerStoreParsing.getStore_id()));
    }

    @Override
    public int getItemCount() {
        return listStoreHelperClasses.store == null ? 0 : listStoreHelperClasses.store.size();
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
        public TextView mInfo;
        public TextView storeId;

        public UltraStoreListViewHolder(@NonNull View itemView, int po) {
            super(itemView);

            storeName = itemView.findViewById(R.id.store_name);
            storeImage = itemView.findViewById(R.id.store_image);
            mInfo = itemView.findViewById(R.id.store_info);
            storeId = itemView.findViewById(R.id.store_id);
            ViewPagersListStoreParsing.ViewPagerStoreParsing list = listStoreHelperClasses.store.get(po);
            makeRequestUltraStore(list.getStore_image(), context, storeImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onUltraStoreSelected(view, getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mLongListener.onUltraStoreLongSelected(view, getAdapterPosition());
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
                    }, image.getWidth(), image.getHeight(), ImageView.ScaleType.FIT_XY, null,
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