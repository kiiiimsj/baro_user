package com.example.wantchu.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Dialogs.ProgressingDetailDialog;
import com.example.wantchu.JsonParsingHelper.OrderHistoryParsingHelper;
import com.example.wantchu.JsonParsingHelper.OrderProgressingParsingHelper;
import com.example.wantchu.OrderProgressing;
import com.example.wantchu.R;
import com.example.wantchu.Url.UrlMaker;
import com.google.gson.Gson;

import java.util.ArrayList;

public class OrderProgressingAdapter extends RecyclerView.Adapter<OrderProgressingAdapter.ViewHolder> {
    private static String TAG = "OrderProgressingAdapter";
    private static String ACCEPT = "ACCEPT";
    private static String PREPARING = "PREPARING";

    static ArrayList<OrderProgressingParsingHelper> mData;
    static Context context;

    public OrderProgressingAdapter(ArrayList<OrderProgressingParsingHelper> mData, Context context) {
        this.mData = mData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_order_progressing_list,parent,false);
        ViewHolder viewHolder = new OrderProgressingAdapter.ViewHolder(view, viewType);

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final OrderProgressingParsingHelper reverse = mData.get(mData.size()-position-1);
        holder.order_date.setText(reverse.getOrder_date());
        holder.stores_name.setText(reverse.getStore_name());
        holder.total_prices.setText("합계 : " + reverse.getTotal_price()+" 원");
        holder.shell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressingDetailDialog progressingDetailDialog = ProgressingDetailDialog.newInstance(context);
                progressingDetailDialog.show(((AppCompatActivity)context).getSupportFragmentManager(),"dialog");
                Gson gson = new Gson();
                String json = gson.toJson(reverse);
                Bundle bundle = progressingDetailDialog.getArguments();
                bundle.putString("json",json);
            }
        });

        if(reverse.getOrder_state().equals(ACCEPT)){
            holder.order_state.setText("제 조 중");
            holder.progressBar.setProgress(67);
        }
        else if(reverse.getOrder_state().equals(PREPARING)){
            holder.order_state.setText("접 수 대 기");
            holder.progressBar.setProgress(34);
        }else{
            ;
        }
    }

    @Override
    public int getItemCount() {
        return (mData == null ? 0 : mData.size());
    }
    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull OrderProgressingAdapter.ViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull OrderProgressingAdapter.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewRecycled(@NonNull OrderProgressingAdapter.ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull OrderProgressingAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView order_state;
        TextView order_date;
        TextView stores_name;
        TextView total_prices;
        ImageView store_image;
        LinearLayout shell;
        ProgressBar progressBar;
        public ViewHolder(@NonNull View itemView, int po) {
            super(itemView);
            order_state = (itemView).findViewById(R.id.order_state);
            order_date = (itemView).findViewById(R.id.ordered_dates);
            stores_name = (itemView).findViewById(R.id.store_names);
            total_prices = (itemView).findViewById(R.id.totalPrices);
            shell = (itemView).findViewById(R.id.shellProgressingItem);
            store_image = (itemView).findViewById(R.id.store_image);
            progressBar = (itemView).findViewById(R.id.progressBar);
            makeRequest( mData.get(mData.size()-po-1).getStore_image(),context,store_image);
//            }
        }
    }
    public static void makeRequest(String imageName, Context context, final ImageView image) {
        UrlMaker urlMaker = new UrlMaker();
        String url = new UrlMaker().UrlMake("ImageStore.do?image_name=" + imageName);
        Log.d(TAG,url);
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        image.setImageBitmap(response);
                    }
                }, image.getWidth(), image.getHeight(), ImageView.ScaleType.FIT_XY, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("menuimageerror", "error");
                    }
                });
        requestQueue.add(request);
    }
}
