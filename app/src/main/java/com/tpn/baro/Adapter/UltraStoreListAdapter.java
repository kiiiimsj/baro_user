package com.tpn.baro.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.tpn.baro.JsonParsingHelper.ViewPagersListStoreParsing;
import com.tpn.baro.R;
import com.tpn.baro.Url.UrlMaker;

public class UltraStoreListAdapter extends RecyclerView.Adapter<UltraStoreListAdapter.UltraStoreListViewHolder> {
    private static String TAG = "UltraStoreListAdapter";
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.ultra_store_design, parent, false);
        UltraStoreListViewHolder ultraStoreListViewHolder = new UltraStoreListViewHolder(view, viewType);
        return ultraStoreListViewHolder;
    }


    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onBindViewHolder(@NonNull UltraStoreListViewHolder holder, int position) {
        ViewPagersListStoreParsing.ViewPagerStoreParsing viewPagerStoreParsing = listStoreHelperClasses.store.get(position);
        holder.storeName.setText(viewPagerStoreParsing.getStore_name());
        if (viewPagerStoreParsing.getDistance() > 1000){
            holder.storeDistance.setText(String.format("%,.1f", ((int)viewPagerStoreParsing.getDistance() / 100) * 0.1) + "km");
        }else {
            holder.storeDistance.setText((int) viewPagerStoreParsing.getDistance() + "m");
        }
        holder.storeId.setText(String.valueOf(viewPagerStoreParsing.getStore_id()));
        if(viewPagerStoreParsing.getIs_open().equals("Y")) {
            holder.isOpen.setText("영업중");
            holder.isOpen.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.main)));

        }else {
            holder.isOpen.setText("영업종료");
        }
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
        public TextView storeDistance;
        public TextView storeId;
        public TextView isOpen;

        public UltraStoreListViewHolder(@NonNull View itemView, int po) {
            super(itemView);

            storeName = itemView.findViewById(R.id.store_name);
            storeImage = itemView.findViewById(R.id.store_image);
            storeDistance = itemView.findViewById(R.id.store_infomation);
            storeId = itemView.findViewById(R.id.store_id);
            isOpen = itemView.findViewById(R.id.is_open);
            ViewPagersListStoreParsing.ViewPagerStoreParsing list = listStoreHelperClasses.store.get(po);
            if(!list.getStore_name().equals("")) {
                makeRequestUltraStore(list.getStore_image(), context, storeImage);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onUltraStoreSelected(view, getAdapterPosition());
                }
            });
//
//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    mLongListener.onUltraStoreLongSelected(view, getAdapterPosition());
//                }
//            });
        }

        public void makeRequestUltraStore(String store_image, final Context context, final ImageView image){
            UrlMaker urlMaker = new UrlMaker();
            String lastUrl = "UltraNewImageStore.do?image_name=";
            String url = urlMaker.UrlMake(lastUrl);
            final StringBuilder urlBuilder = new StringBuilder()
                    .append(url)
                    .append(store_image);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    RequestQueue requestQueue = Volley.newRequestQueue(context);

                    ImageRequest request = new ImageRequest(urlBuilder.toString(),
                            new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    image.setImageBitmap(response);
                                }
                            }, image.getWidth(), image.getHeight(), ImageView.ScaleType.FIT_XY, null,
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i(TAG, "error");
                                }
                            });
                    requestQueue.add(request);
                }
            }).start();
        }
    }
}
