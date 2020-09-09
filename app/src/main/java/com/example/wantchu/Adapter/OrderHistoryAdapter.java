package com.example.wantchu.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wantchu.JsonParsingHelper.OrderHistoryParsingHelper;
import com.example.wantchu.R;
import com.example.wantchu.Dialogs.HistoryDetailDialog;

import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    static ArrayList<OrderHistoryParsingHelper> order;
    Context context;
    private OnItemClickListener mListener = null;
    public OrderHistoryAdapter(ArrayList<OrderHistoryParsingHelper> order,Context context) {
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
        final OrderHistoryParsingHelper reverse = order.get(order.size()-position-1);
        holder.store_name.setText(reverse.getStore_name());
        holder.ordered_date.setText(reverse.getOrder_date());
        holder.totalPrice.setText(""+reverse.getTotal_price());
        holder.receipt_id.setText(""+reverse.getReceipt_id());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context,"sdddd",Toast.LENGTH_LONG).show();
//                Bundle bundle = new Bundle();
                HistoryDetailDialog fragment = HistoryDetailDialog.newInstance(context);
                Bundle bundle = fragment.getBundle();
                bundle.putString("receipt_id",reverse.getReceipt_id());
                bundle.putString("storeName",reverse.getStore_name());
                bundle.putString("orderedDate",reverse.getOrder_date());
                bundle.putInt("totalPrice",reverse.getTotal_price());
//                HistoryDetail fragment = new HistoryDetail(context);
//                fragment.setArguments(bundle);
//                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
//                FragmentTransaction transaction = manager.beginTransaction();
//                transaction.replace(R.id.frameLayout,fragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
                fragment.show(((AppCompatActivity)context).getSupportFragmentManager(),"dialog");
            }
        });

    }

    @Override
    public int getItemCount() {
        return (order==null ? 0:order.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView store_name;
        TextView ordered_date;
        TextView totalPrice;
        TextView receipt_id;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView, int po) {
            super(itemView);
            store_name = (itemView).findViewById(R.id.store_names);
            ordered_date = (itemView).findViewById(R.id.ordered_dates);
            totalPrice = (itemView).findViewById(R.id.totalPrices);
            linearLayout =(itemView).findViewById(R.id.shellHistoryItem);
            receipt_id = (itemView).findViewById(R.id.orderGroup_id);
            if(order.size() == po + 1) {
                ViewGroup.LayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
                itemView.setLayoutParams(layoutParams);
            }
        }

    }

}
