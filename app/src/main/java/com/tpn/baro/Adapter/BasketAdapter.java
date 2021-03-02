package com.tpn.baro.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.tpn.baro.AdapterHelper.ExtraOrder;
import com.tpn.baro.Dialogs.LastItemDialog;
import com.tpn.baro.R;
import com.tpn.baro.helperClass.DetailsFixToBasket;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ViewHolder>{
    private ArrayList<DetailsFixToBasket> detailsFixToBaskets;
    private Context context;
    private deleteItem mListener;
    public BasketAdapter(ArrayList<DetailsFixToBasket> detailsFixToBaskets,Context context,deleteItem mListener) {
        this.detailsFixToBaskets = detailsFixToBaskets;
        this.context = context;
        this.mListener = mListener;
    }


    public interface deleteItem {
        void delete(int count);
    }


    public ArrayList<DetailsFixToBasket> getDetailsFixToBaskets() {
        return detailsFixToBaskets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_basket_item,parent,false);
        ViewHolder viewHolder = new BasketAdapter.ViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final DetailsFixToBasket detailsFixToBasket = detailsFixToBaskets.get(position);
        HashMap<String, ExtraOrder> essentials = detailsFixToBasket.getEssentialOptions();
        HashMap<String,ExtraOrder> nonEssentials = detailsFixToBasket.getNonEssentialoptions();
        Iterator<String> iterator;
        String essentialString=context.getString(R.string.viewpager_circle_indicator)+" ";
        int essentialPrice=0;
        if(detailsFixToBasket.getName().equals("")){
            return;
        }
        ((TextView)holder.itemName.getChildAt(0)).setText(detailsFixToBasket.getName());
        ((TextView)holder.itemName.getChildAt(1)).setText(""+detailsFixToBasket.getDefaultPrice());

        if(essentials.size()!=0) {
            holder.essential.setVisibility(View.VISIBLE);
            iterator = essentials.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Integer optionPrice = essentials.get(key).getExtra_price();
                essentialString+=essentials.get(key).getExtra_name()+" / ";
                essentialPrice+=optionPrice;
            }
            essentialString = essentialString.substring(0,essentialString.length()-2);
            LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.activity_basket_item_child, null, false);
            ((TextView) ((ConstraintLayout) linearLayout.getChildAt(0)).getChildAt(0)).setText(essentialString);
            ((TextView) ((ConstraintLayout) linearLayout.getChildAt(0)).getChildAt(1)).setText("+ " + essentialPrice);
            holder.essential.removeAllViews();
            holder.essential.addView(linearLayout);
        }
        else{
            holder.essential.setVisibility(View.GONE);
        }

        if(nonEssentials.size()!=0) {
            holder.nonEssential.setVisibility(View.VISIBLE);
            iterator = nonEssentials.keySet().iterator();
            holder.nonEssential.removeAllViews();
            while (iterator.hasNext()) {
                String key = iterator.next();
               ExtraOrder optionData = nonEssentials.get(key);
                int price = optionData.getExtra_price();
                int count = optionData.getExtra_count();
                if (count == 0){
                    continue;
                }
                LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.activity_basket_item_child2, null, false);
                ((TextView) ((ConstraintLayout) linearLayout.getChildAt(0)).getChildAt(0)).setText(context.getString(R.string.viewpager_circle_indicator)+" "+key);
                ((TextView) ((ConstraintLayout) linearLayout.getChildAt(0)).getChildAt(2)).setText("" + count);
                ((TextView) ((ConstraintLayout) linearLayout.getChildAt(0)).getChildAt(3)).setText("+ " + price);
                holder.nonEssential.addView(linearLayout);
            }
        }else{
            holder.nonEssential.setVisibility(View.GONE);
        }

        ((TextView)holder.priceLinear.getChildAt(0)).setText(detailsFixToBasket.getPrice() * 100 / (100 - detailsFixToBasket.getDiscount_rate())+"원");
        ((TextView)holder.priceLinear.getChildAt(2)).setText(detailsFixToBasket.getCount()+"");
        if(detailsFixToBasket.getDiscount_rate() == 0 ){
            ((TextView)holder.priceLinear.getChildAt(3)).setVisibility(View.GONE);
            ((ImageView)holder.priceLinear.getChildAt(4)).setVisibility(View.GONE);
        }
        ((TextView)holder.priceLinear.getChildAt(3)).setText(detailsFixToBasket.getPrice() * 100 / (100 - detailsFixToBasket.getDiscount_rate())+"");
        ((TextView)holder.priceLinear.getChildAt(5)).setText(detailsFixToBasket.getPrice() + " 원");
//        ((TextView)holder.priceLinear.getChildAt(3)).setText(detailsFixToBasket.getPrice() *(100-detailsFixToBasket.getDiscount_rate()) /100 + " 원");

        holder.deleteThis.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (detailsFixToBaskets.size() == 1) {
                    LastItemDialog dialog = new LastItemDialog(context, new LastItemDialog.DoDelete() {
                        @Override
                        public void doDelete() {
                            detailsFixToBaskets.remove(position);
                            notifyItemRemoved(position);
                            notifyDataSetChanged();
                            mListener.delete(0);
                        }
                    },false);
                    dialog.callFunction();
                }else {
                    detailsFixToBaskets.get(position).setName("");
                    detailsFixToBaskets.remove(position);
                    holder.parent.removeView(holder.parent);

//                ((LinearLayout)holder.deleteThis.getParent()).removeAllViews();
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
//                notifyItemRangeChanged(position, detailsFixToBaskets.size());
                    mListener.delete(detailsFixToBaskets.size());
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return (detailsFixToBaskets == null? 0 : detailsFixToBaskets.size());
    }

    ///////////////////////////////////
    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull ViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout itemName;
        LinearLayout essential;
        LinearLayout nonEssential;
        ConstraintLayout priceLinear;
        ImageButton deleteThis;
        RelativeLayout parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = (itemView).findViewById(R.id.parent);
            itemName = (itemView).findViewById(R.id.main);
            essential = (itemView).findViewById(R.id.essentials);
            nonEssential = (itemView).findViewById(R.id.nonEssentials);
            priceLinear = (itemView).findViewById(R.id.priceLinear);
            deleteThis = (itemView).findViewById(R.id.deleteThis);
        }
    }
}
