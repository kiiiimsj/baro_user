package com.tpn.baro.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.tpn.baro.JsonParsingHelper.OrderHistoryParsingHelper;
import com.tpn.baro.R;
import com.tpn.baro.Dialogs.HistoryDetailDialog;
import com.tpn.baro.StoreInfoReNewer;
import com.tpn.baro.Url.UrlMaker;

import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    static ArrayList<OrderHistoryParsingHelper> order;
    Context context;
    private OnItemClickListener mListener = null;
    public OrderHistoryAdapter(ArrayList<OrderHistoryParsingHelper> order, Context context) {
        this.order = order;
        this.context = context;
    }
    public interface OnItemClickListener
    {
        void onItemClick(View v, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }
    @NonNull
    @Override
    public OrderHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_order_history_list,parent,false);
        ViewHolder viewHolder = new OrderHistoryAdapter.ViewHolder(view, viewType);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.ViewHolder holder, final int position) {
//        final OrderHistoryParsingHelper reverse = order.get(order.size() - position - 1);
        final OrderHistoryParsingHelper reverse = order.get(position);
        holder.store_name.setText(reverse.getStore_name());
        holder.ordered_date.setText(reverse.getOrder_date());
        holder.totalPrice.setText("합계 : "+reverse.getTotal_price() + " 원");
        if (reverse.getOrder_state() == "DONE") {
            holder.order_state.setText("수 령 완 료");
        } else if (reverse.getOrder_state() == "CANCEL"){
            holder.order_state.setText("주 문 취 소");
        }

        holder.goDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Bundle bundle = new Bundle();
                HistoryDetailDialog fragment = HistoryDetailDialog.newInstance(context);
                Bundle bundle = fragment.getBundle();
                bundle.putString("receipt_id",reverse.getReceipt_id());
                bundle.putString("storeName",reverse.getStore_name());
                bundle.putString("orderedDate",reverse.getOrder_date());
                bundle.putInt("totalPrice",reverse.getTotal_price());
                fragment.show(((AppCompatActivity)context).getSupportFragmentManager(),"dialog");
            }
        });
        holder.goStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StoreInfoReNewer.class);
                intent.putExtra("store_id",reverse.getStore_id());
                intent.putExtra("store_name",reverse.getStore_name());
                context.startActivity(intent);
                CustomIntent.customType(context,"left-to-right");
            }
        });

    }

    @Override
    public int getItemCount() {
        return (order==null ? 0:order.size());
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull OrderHistoryAdapter.ViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull OrderHistoryAdapter.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewRecycled(@NonNull OrderHistoryAdapter.ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull OrderHistoryAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView store_name;
        TextView ordered_date;
        TextView totalPrice;
        TextView receipt_id;
        TextView order_state;
        RelativeLayout relativeLayout;
        ImageView store_image;
        Button goStore;
        Button goDetail;
        public ViewHolder(@NonNull View itemView, int po) {
            super(itemView);
            store_name = (itemView).findViewById(R.id.store_names);
            ordered_date = (itemView).findViewById(R.id.ordered_dates);
            totalPrice = (itemView).findViewById(R.id.totalPrices);
            relativeLayout =(itemView).findViewById(R.id.shellHistoryItem);
            receipt_id = (itemView).findViewById(R.id.orderGroup_id);
            order_state = (itemView).findViewById(R.id.order_state);
            store_image = (itemView).findViewById(R.id.store_image);
            goStore = (itemView).findViewById(R.id.go_to_store);
            goDetail = (itemView).findViewById(R.id.history_detail);
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            UrlMaker urlMaker = new UrlMaker();
            String lastUrl = "ImageStore.do?image_name=";
            String url = urlMaker.UrlMake(lastUrl);
            StringBuilder urlBuilder = new StringBuilder()
                    .append(url)
                    .append(order.get(po).getStore_image());

            ImageRequest request = new ImageRequest(urlBuilder.toString(),
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            store_image.setImageBitmap(response);
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
