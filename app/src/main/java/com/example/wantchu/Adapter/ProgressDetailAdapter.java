package com.example.wantchu.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wantchu.JsonParsingHelper.OrderProgressDetailParsing;
import com.example.wantchu.R;

import java.util.ArrayList;

public class ProgressDetailAdapter extends RecyclerView.Adapter<ProgressDetailAdapter.ViewHolder> {
    Context context;
    ArrayList<OrderProgressDetailParsing.OrderProgressDetailParsingHelper> data;
    public ProgressDetailAdapter(ArrayList<OrderProgressDetailParsing.OrderProgressDetailParsingHelper> orders, Context context) {
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
        ArrayList<OrderProgressDetailParsing.OrderProgressDetailParsingHelper.extras> extras = data.get(position).getExtras();
        holder.menu_name.setText(data.get(position).getMenu_name());
        holder.menu_defaultPrice.setText(data.get(position).getMenu_defaultprice()+"");
        holder.count.setText(data.get(position).getOrder_count()+"");
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
        int eachPrice = data.get(position).getMenu_defaultprice()+optionPrices;
        holder.each_price.setText(""+eachPrice+"원");
        holder.total_price.setText(""+eachPrice*data.get(position).getOrder_count()+"원");
    }


    @Override
    public int getItemCount() {
        return (data == null ? 0 : data.size());
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
        TextView each_price;
        TextView total_price;
        TextView count;
        LinearLayout options;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            menu_name = (itemView).findViewById(R.id.menu_name);
            menu_defaultPrice = (itemView).findViewById(R.id.defaultPrice);
            options = (itemView).findViewById(R.id.options);
            each_price = (itemView).findViewById(R.id.eachPrice);
            total_price = (itemView).findViewById(R.id.totalPrice);
            count = (itemView).findViewById(R.id.count);
        }
    }
}
