package com.example.wantchu.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wantchu.JsonParsingHelper.HistoryDetailParsing;
import com.example.wantchu.R;

import java.util.ArrayList;
public class HistoryDetailAdapter extends RecyclerView.Adapter<HistoryDetailAdapter.ViewHolder> {
    private ArrayList<HistoryDetailParsing.HistoryDetailParsingHelper> parsingHelperArrayList;
    private Context context;
    private int totals = 0;

    public int getTotals() {
        return totals;
    }

    public HistoryDetailAdapter(ArrayList<HistoryDetailParsing.HistoryDetailParsingHelper> parsingHelperArrayList, Context context) {
        this.context = context;
        this.parsingHelperArrayList = parsingHelperArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_history_detail_list,parent,false);
        ViewHolder viewHolder = new HistoryDetailAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ArrayList<HistoryDetailParsing.HistoryDetailParsingHelper.extras> extras = parsingHelperArrayList.get(position).getExtras();
        holder.menu_name.setText(parsingHelperArrayList.get(position).getMenu_name());
        holder.default_price.setText(""+parsingHelperArrayList.get(position).getMenu_defaultprice());
        int itemCount = parsingHelperArrayList.get(position).getOrder_count();
        int optionPrices = 0;
        holder.options.removeAllViews();
        for(int i = 0;i<extras.size();i++){
            String optionName = extras.get(i).getExtra_name();
            int optionPrice = extras.get(i).getExtra_price();
            int optionCount = extras.get(i).getExtra_count();
            LinearLayout extraOption = (LinearLayout) inflater.inflate(R.layout.fragment_history_detail_list_extras,null,false);
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
        int eachPrice = parsingHelperArrayList.get(position).getMenu_defaultprice()+optionPrices;
        holder.itemCount.setText(""+itemCount);
        holder.eachPrice.setText(""+eachPrice+"원");
        holder.totalPrice.setText(""+eachPrice*itemCount+"원");
        totals+=eachPrice*itemCount;
        Log.i("To",""+ totals);
    }

    @Override
    public int getItemCount() {
        return (parsingHelperArrayList == null ? 0 : parsingHelperArrayList.size());
    }
    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull HistoryDetailAdapter.ViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull HistoryDetailAdapter.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewRecycled(@NonNull HistoryDetailAdapter.ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull HistoryDetailAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView menu_name;
        TextView default_price;
        TextView itemCount;
        TextView totalPrice;
        TextView eachPrice;
        LinearLayout options;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            menu_name = (itemView).findViewById(R.id.menu_name);
            default_price = (itemView).findViewById(R.id.defaultPrice);
            options = (itemView).findViewById(R.id.options);
            totalPrice = (itemView).findViewById(R.id.totalPrice);
            eachPrice = (itemView).findViewById(R.id.eachPrice);
            itemCount = (itemView).findViewById(R.id.count);
        }
    }
}
