package com.example.wantchu.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wantchu.Dialogs.ProgressingDetailDialog;
import com.example.wantchu.JsonParsingHelper.OrderHistoryParsingHelper;
import com.example.wantchu.JsonParsingHelper.OrderProgressingParsingHelper;
import com.example.wantchu.OrderProgressing;
import com.example.wantchu.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class OrderProgressingAdapter extends RecyclerView.Adapter<OrderProgressingAdapter.ViewHolder> {
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
        holder.total_prices.setText(reverse.getTotal_price()+" 원");
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
        }
        else if(reverse.getOrder_state().equals(PREPARING)){
            holder.order_state.setText("접 수 대 기");
        }else{
            ;
        }


    }

    @Override
    public int getItemCount() {
        return (mData.size() <= 0 ? 0 : mData.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView order_state;
        TextView order_date;
        TextView stores_name;
        TextView total_prices;
        LinearLayout shell;
        public ViewHolder(@NonNull View itemView, int po) {
            super(itemView);
            order_state = (itemView).findViewById(R.id.order_state);
            order_date = (itemView).findViewById(R.id.ordered_dates);
            stores_name = (itemView).findViewById(R.id.store_names);
            total_prices = (itemView).findViewById(R.id.totalPrices);
            shell = (itemView).findViewById(R.id.shellProgressingItem);
            if(mData.size() == po + 1) {
                ViewGroup.LayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
                itemView.setLayoutParams(layoutParams);
            }
        }
    }
}
