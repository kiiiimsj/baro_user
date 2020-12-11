package com.example.wantchu.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wantchu.AdapterHelper.ExtraOrder;
import com.example.wantchu.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class OrderDetailsEssentialAdapter extends RecyclerView.Adapter<OrderDetailsEssentialAdapter.ViewHolder> {
    private ArrayList<String> mData = null ;
    private HashMap<String,ArrayList<ExtraOrder>> mTableData;
    private Context context;
    private TextView priceTotal;
    private TextView itemCountText;
    HashMap<String,ExtraOrder> selectOptions;
    int must;
    int memory;
    private static int ONE_ROW = 3;

    public int getMust() {
        return must;
    }

    public OrderDetailsEssentialAdapter(ArrayList<String> mData, HashMap<String,ArrayList<ExtraOrder>> mTableData,
                                        Context context, TextView priceTotal, TextView itemCountText) {
        this.mData = mData;
        this.mTableData =mTableData;
        this.context = context;
        this.priceTotal = priceTotal;
        this.itemCountText = itemCountText;
        selectOptions = new HashMap<>();
        must = 0;
    }

    public HashMap<String,ExtraOrder> getSelectOptions() {
        return selectOptions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.activity_order_details_essential, parent, false) ;
        ViewHolder viewHolder = new OrderDetailsEssentialAdapter.ViewHolder(view) ;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        ArrayList<String> nameList = new ArrayList<>();
        final ArrayList<Integer> priceList = new ArrayList<>();
        ArrayList<Integer> option_ids = new ArrayList<>();
        LinearLayout parent = null;
        final ArrayList<ToggleButton> toggleButtons = new ArrayList<>();
        int count = 0;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.activity_order_details_essential_table,null,false);
        LinearLayout reChild = (LinearLayout) relativeLayout.getChildAt(0);
        LinearLayout reChildRadio = (LinearLayout) relativeLayout.getChildAt(1);
        final String text = mData.get(position);
        holder.optionName.setText(text);

        if(mTableData.containsKey(text)){
            Log.i("필수",text);
            final ArrayList<ExtraOrder> extraOrders = mTableData.get(text);
            resolveExtraOrdersToArrayList(extraOrders,nameList,priceList,option_ids);
            Log.i("size",String.valueOf(nameList.size()));

            if(extraOrders.size() > ONE_ROW){
                parent = (LinearLayout) inflater.inflate(R.layout.activity_order_details_radio, null, false);
                for(int i = 0;i<extraOrders.size();i++){
                    final ExtraOrder extraOrder = extraOrders.get(i);
                    final RadioButton radioButton = (RadioButton) inflater.inflate(R.layout.activity_order_details_radio_child,null,false);
                    radioButton.setText( extraOrder.getExtra_name()+"  (+"+extraOrder.getExtra_price()+"원)");
                    radioButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            must++;
                            if(must > 1){
                                must = 1;
                            }
                            holder.selectedOptions.setText(radioButton.getText());
                            int itemCount = Integer.parseInt(itemCountText.getText().toString());
                            int changePrice = extraOrder.getExtra_price();
                            int originPrice = Integer.valueOf(priceTotal.getText().toString());
                            ExtraOrder select = new ExtraOrder(extraOrder.getExtra_id(), extraOrder.getExtra_price(), extraOrder.getExtra_name(), 1);
                            select.setExtra_count(1);
                            priceTotal.setText(String.valueOf(originPrice + (itemCount * (changePrice - memory))));
                            selectOptions.put(text, select);
                            memory = changePrice;
                        }
                    });
                    ((LinearLayout)parent.getChildAt(0)).addView(radioButton);
                }
            }else {
                parent = (LinearLayout) inflater.inflate(R.layout.activity_order_details_essential_table_child, null, false);


                while (count != extraOrders.size()) {

                    LinearLayout linearLayout = (LinearLayout) parent.getChildAt(count);
                    final ToggleButton name = (ToggleButton) linearLayout.getChildAt(0);
                    final TextView price = (TextView) linearLayout.getChildAt(1);
                    final TextView id = (TextView) linearLayout.getChildAt(2);

                    name.setText(nameList.get(count));
                    name.setTextOn(nameList.get(count));
                    name.setTextOff(nameList.get(count));
                    toggleButtons.add(name);
                    name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int itemCount = Integer.parseInt(itemCountText.getText().toString());
                            int changePrice = Integer.parseInt(price.getText().toString());
                            int originPrice = Integer.valueOf(priceTotal.getText().toString());
                            int deletePrice = 0;
                            if (name.isChecked()) {

                                for (int i = 0; i < toggleButtons.size(); i++) {
                                    if (toggleButtons.get(i) != name && toggleButtons.get(i).isChecked()) {
                                        must--;
                                        deletePrice = Integer.parseInt(((TextView) (((ViewGroup) toggleButtons.get(i).getParent()).getChildAt(1))).getText().toString());
                                    }
                                    toggleButtons.get(i).setTextColor(ContextCompat.getColor(context, R.color.lightGray));
                                    toggleButtons.get(i).setChecked(false);
                                    toggleButtons.get(i).setBackgroundResource(R.drawable.menu_non_select);
                                }
                                holder.selectedOptions.setText(name.getText().toString() + "(+" + changePrice + "원)");
                                must++;
                                HashMap<String, Integer> selectOption = new HashMap<>();
                                ExtraOrder select = new ExtraOrder(Integer.parseInt(id.getText().toString()), Integer.parseInt(price.getText().toString()), name.getText().toString(), 1);
                                select.setExtra_count(1);
                                selectOption.put(name.getText().toString(), Integer.parseInt(price.getText().toString()));
                                selectOptions.put(text, select);
                                priceTotal.setText(String.valueOf(originPrice + (itemCount * (changePrice - deletePrice))));
                                name.setChecked(true);
                                name.setBackgroundResource(R.drawable.menu_select);
                                name.setTextColor(ContextCompat.getColor(context, R.color.white));
                            } else {
                                must--;
                                holder.selectedOptions.setText("");
                                priceTotal.setText(String.valueOf(originPrice - (itemCount * changePrice)));
                                name.setBackgroundResource(R.drawable.button_border_empty);
                                name.setTextColor(ContextCompat.getColor(context, R.color.lightGray));
                            }
                        }
                    });
                    price.setText(String.valueOf(priceList.get(count)));
                    id.setText("" + option_ids.get(count));
                    count++;
                }
                for(int i = count;i < ONE_ROW;i++){
                    LinearLayout trash = (LinearLayout) parent.getChildAt(i);
                    trash.setVisibility(View.GONE);
                }
            }
            if(extraOrders.size() > ONE_ROW){
                reChildRadio.addView(parent);
            }else {
                reChild.addView(parent);
            }
            holder.essentialOptionTableShell.addView(relativeLayout);
        }
        else{
            return;
        }
    }


    public void resolveExtraOrdersToArrayList(ArrayList<ExtraOrder> extraOrders,ArrayList<String> nameList,ArrayList<Integer> priceList,ArrayList<Integer> option_ids){
        for(int i = 0;i<extraOrders.size();i++){
            ExtraOrder extraOrder = extraOrders.get(i);
            String optionName = extraOrder.getExtra_name();
            int option_price = extraOrder.getExtra_price();
            int option_id = extraOrder.getExtra_id();
            nameList.add(optionName);
            priceList.add(option_price);
            option_ids.add(option_id);

        }
    }
    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }
    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull OrderDetailsEssentialAdapter.ViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull OrderDetailsEssentialAdapter.ViewHolder  holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewRecycled(@NonNull OrderDetailsEssentialAdapter.ViewHolder  holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull OrderDetailsEssentialAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView optionName;
        TextView selectedOptions;
        LinearLayout essentialOptionTableShell;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            selectedOptions = itemView.findViewById(R.id.selectedOptions);
            optionName = itemView.findViewById(R.id.essentialOptionName);
            essentialOptionTableShell = itemView.findViewById(R.id.tableShell);
        }
    }
}
