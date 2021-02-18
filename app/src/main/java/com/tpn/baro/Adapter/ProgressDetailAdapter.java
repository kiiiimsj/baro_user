package com.tpn.baro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.tpn.baro.JsonParsingHelper.OrderProgressDetailParsing;
import com.tpn.baro.R;

import java.util.ArrayList;

public class ProgressDetailAdapter extends RecyclerView.Adapter<ProgressDetailAdapter.ViewHolder> {
    Context context;
    OrderProgressDetailParsing data;
    public ProgressDetailAdapter(OrderProgressDetailParsing orders, Context context) {
        this.context = context;
        this.data = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.fragment_progressing_detail_list,parent,false);
        ViewHolder viewHolder = new ProgressDetailAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ArrayList<OrderProgressDetailParsing.OrderProgressDetailParsingHelper.extras> extras = data.getOrders().get(position).getExtras();
        holder.menu_name.setText(data.getOrders().get(position).getMenu_name());
        holder.menu_defaultPrice.setText(data.getOrders().get(position).getMenu_defaultprice()+"");
        holder.count.setText(data.getOrders().get(position).getOrder_count()+" 개");
        int optionPrices = 0;
        holder.options.removeAllViews();
        for(int i = 0;i<extras.size();i++){
            String optionName = extras.get(i).getExtra_name();
            int optionPrice = extras.get(i).getExtra_price();
            int optionCount = extras.get(i).getExtra_count();
            LinearLayout extraOption = (LinearLayout) layoutInflater.inflate(R.layout.fragment_progressing_detail_list_extras,null,false);
            ((TextView) ((ConstraintLayout) extraOption.getChildAt(0)).getChildAt(0)).setText(optionName);
            if (optionCount ==1){
                ((TextView) ((ConstraintLayout) extraOption.getChildAt(0)).getChildAt(1)).setVisibility(View.INVISIBLE);
                ((TextView) ((ConstraintLayout) extraOption.getChildAt(0)).getChildAt(2)).setVisibility(View.INVISIBLE);
            }else{
                ((TextView) ((ConstraintLayout) extraOption.getChildAt(0)).getChildAt(2)).setText(""+optionCount);
            }
            ((TextView) ((ConstraintLayout) extraOption.getChildAt(0)).getChildAt(3)).setText("+ " + optionPrice*optionCount);
            optionPrices +=optionPrice*optionCount;
            holder.options.addView(extraOption);
        }
        int eachPrice = data.getOrders().get(position).getMenu_defaultprice()+optionPrices;
        holder.each_price.setText(""+eachPrice+"원");
        if(data.getDiscount_rate() == 0) {
            holder.ifDiscountRate.setVisibility(View.GONE);
            holder.arrowRight.setVisibility(View.GONE);
            holder.total_price.setText(""+eachPrice*data.getOrders().get(position).getOrder_count()+"원");
        }else {
            holder.ifDiscountRate.setVisibility(View.VISIBLE);
            holder.arrowRight.setVisibility(View.VISIBLE);
            holder.ifDiscountRate.setText(""+eachPrice*data.getOrders().get(position).getOrder_count());
            holder.total_price.setText(""+ (eachPrice*data.getOrders().get(position).getOrder_count() - (int)( eachPrice*data.getOrders().get(position).getOrder_count() * (data.getDiscount_rate() / 100.0)))+"원");
        }


    }


    @Override
    public int getItemCount() {
        return (data == null ? 0 : data.getOrders().size());
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull ProgressDetailAdapter.ViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ProgressDetailAdapter.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewRecycled(@NonNull ProgressDetailAdapter.ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ProgressDetailAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView menu_name;
        TextView menu_defaultPrice;
        TextView ifDiscountRate;
        ImageView arrowRight;
        TextView each_price;
        TextView total_price;
        TextView count;
        LinearLayout options;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            menu_name = (itemView).findViewById(R.id.menu_name);
            menu_defaultPrice = (itemView).findViewById(R.id.defaultPrice);
            ifDiscountRate = (itemView).findViewById(R.id.if_discount_rate);
            arrowRight = (itemView).findViewById(R.id.arrow_right);
            options = (itemView).findViewById(R.id.options);
            each_price = (itemView).findViewById(R.id.eachPrice);
            total_price = (itemView).findViewById(R.id.totalPrice);
            count = (itemView).findViewById(R.id.count);
        }
    }
}
