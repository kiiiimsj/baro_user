package com.example.wantchu.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wantchu.AdapterHelper.ExtraOrder;
import com.example.wantchu.R;
import com.example.wantchu.helperClass.DetailsFixToBasket;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ViewHolder> {
    private ArrayList<DetailsFixToBasket> detailsFixToBaskets;
    private Context context;
    public BasketAdapter(ArrayList<DetailsFixToBasket> detailsFixToBaskets,Context context) {
        this.detailsFixToBaskets = detailsFixToBaskets;
        this.context = context;
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
        String essentialString="";
        int essentialPrice=0;
        if(detailsFixToBasket.getName().equals("")){
            return;
        }
        ((TextView)holder.itemName.getChildAt(0)).setText(detailsFixToBasket.getName());
        ((TextView)holder.itemName.getChildAt(1)).setText(""+detailsFixToBasket.getDefaultPrice());
        if(essentials.size()!=0) {
            iterator = essentials.keySet().iterator();
            while (iterator.hasNext()) {
//                String key = iterator.next();
//                Integer optionPrice = essentials.get(key);
//                LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.activity_basket_item_child, null, false);
//                ((TextView) ((ConstraintLayout) linearLayout.getChildAt(0)).getChildAt(0)).setText(key);
//                ((TextView) ((ConstraintLayout) linearLayout.getChildAt(0)).getChildAt(1)).setText("" + optionPrice);
//                holder.essential.addView(linearLayout);
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
            holder.essential.setVisibility(View.INVISIBLE);
        }
        if(nonEssentials.size()!=0) {
            iterator = nonEssentials.keySet().iterator();
            holder.nonEssential.removeAllViews();
            while (iterator.hasNext()) {
                String key = iterator.next();
               ExtraOrder optionData = nonEssentials.get(key);
                int price = optionData.getExtra_price();
                int count = optionData.getExtra_count();
                LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.activity_basket_item_child2, null, false);
                ((TextView) ((ConstraintLayout) linearLayout.getChildAt(0)).getChildAt(0)).setText(key);
                ((TextView) ((ConstraintLayout) linearLayout.getChildAt(0)).getChildAt(2)).setText("" + count);
                ((TextView) ((ConstraintLayout) linearLayout.getChildAt(0)).getChildAt(3)).setText("+ " + price);
                holder.nonEssential.addView(linearLayout);
            }
        }else{
            holder.nonEssential.setVisibility(View.INVISIBLE);
        }
        ((TextView)holder.priceLinear.getChildAt(3)).setText("total : "+detailsFixToBasket.getPrice()+"원");
        ((TextView)holder.priceLinear.getChildAt(2)).setText(""+detailsFixToBasket.getCount()+" 개");
        ((TextView)holder.priceLinear.getChildAt(0)).setText(""+detailsFixToBasket.getPrice()/detailsFixToBasket.getCount()+" 원");
        holder.deleteThis.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                detailsFixToBaskets.get(position).setName("");
                detailsFixToBaskets.remove(position);
                Log.i("FIXTOSIZE",detailsFixToBaskets.size()+"");
                holder.parent.removeView(holder.parent);
                Gson gson = new Gson();
                for(int i = 0;i<detailsFixToBaskets.size();i++){
                    Log.e("change",gson.toJson(detailsFixToBaskets.get(i),DetailsFixToBasket.class));
                }

//                ((LinearLayout)holder.deleteThis.getParent()).removeAllViews();
                notifyItemRemoved(position);
                notifyDataSetChanged();
//                notifyItemRangeChanged(position, detailsFixToBaskets.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return detailsFixToBaskets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout itemName;
        LinearLayout essential;
        LinearLayout nonEssential;
        ConstraintLayout priceLinear;
        ImageButton deleteThis;
        LinearLayout parent;
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
