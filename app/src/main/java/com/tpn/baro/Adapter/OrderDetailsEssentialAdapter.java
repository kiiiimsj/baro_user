package com.tpn.baro.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tpn.baro.AdapterHelper.ExtraOrder;
import com.tpn.baro.R;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderDetailsEssentialAdapter extends RecyclerView.Adapter<OrderDetailsEssentialAdapter.ViewHolder> {
    private ArrayList<String> mData = null ;
    private HashMap<String,ArrayList<ExtraOrder>> mTableData;
    private Context context;
    private TextView priceTotal;
    private TextView itemCountText;
    private TextView ifDiscountRate;
    private int discountRate;
    private int defaultPrice;
    HashMap<String,ExtraOrder> selectOptions;
    int must;
    int memory;
    private static int ONE_ROW = 3;

    public int getMust() {
        return must;
    }
    public interface ChangeDefaultPriceInEssential {
        void changeEssentialValue(int newDefaultPrice);
    }
    public ChangeDefaultPriceInEssential changeDefaultPriceInEssential;
//    public OrderDetailsEssentialAdapter(ArrayList<String> mData, HashMap<String,ArrayList<ExtraOrder>> mTableData,
//                                        Context context, TextView priceTotal, TextView itemCountText) {
//        this.mData = mData;
//        this.mTableData =mTableData;
//        this.context = context;
//        this.priceTotal = priceTotal;
//        this.itemCountText = itemCountText;
//        selectOptions = new HashMap<>();
//        must = 0;
//    }
    public OrderDetailsEssentialAdapter(ArrayList<String> mData, HashMap<String,ArrayList<ExtraOrder>> mTableData,
                                        Context context, TextView priceTotal, TextView itemCountText, int defaultPrice, int discountRate, ChangeDefaultPriceInEssential changeDefaultPriceInEssential, TextView ifDiscountRate) {
        this.mData = mData;
        this.mTableData =mTableData;
        this.context = context;
        this.defaultPrice = defaultPrice;
        this.discountRate = discountRate;
        this.priceTotal = priceTotal;
        this.ifDiscountRate = ifDiscountRate;
        this.itemCountText = itemCountText;
        this.changeDefaultPriceInEssential = changeDefaultPriceInEssential;
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

        View view = inflater.inflate(R.layout.design_order_details_essential, parent, false) ;
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
        final ArrayList<Integer> radioFlag = new ArrayList<>();
        int count = 0;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.design_order_details_essential_table,null,false);
        LinearLayout reChild = (LinearLayout) relativeLayout.getChildAt(0);
        LinearLayout reChildRadio = (LinearLayout) relativeLayout.getChildAt(1);
        final String text = mData.get(position);
        holder.optionName.setText(" · " + text);

        if(mTableData.containsKey(text)){
//            Log.i("필수",text);
            final ArrayList<ExtraOrder> extraOrders = mTableData.get(text);
            resolveExtraOrdersToArrayList(extraOrders,nameList,priceList,option_ids);
//            Log.i("size",String.valueOf(nameList.size()));

            if(extraOrders.size() > ONE_ROW){

                Log.e("Essential", 1+"");
                parent = (LinearLayout) inflater.inflate(R.layout.design_order_details_radio, null, false);
                for(int i = 0;i<extraOrders.size();i++){
                    final ExtraOrder extraOrder = extraOrders.get(i);
                    final RadioButton radioButton = (RadioButton) inflater.inflate(R.layout.design_order_details_radio_child,null,false);
                    radioButton.setText( extraOrder.getExtra_name()+"  (+"+extraOrder.getExtra_price()+"원)");
                    radioFlag.add(0);
                    final int finalI = i;
                    radioButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            /// 라디오 버튼일때 필수 옵션 선택 갯수 결정.
                            int temp = 0;
                            for (int i = 0;i<radioFlag.size();i++) {
                                temp += radioFlag.get(i);
                            }
                            must -= temp;
                            temp = 0;
                            radioFlag.set(finalI,1);
                            for (int i = 0;i<radioFlag.size();i++) {
                                temp += radioFlag.get(i);
                            }
                            must += temp;
                            ///////

                            holder.selectedOptions.setText(radioButton.getText());
                            int itemCount = Integer.parseInt(itemCountText.getText().toString());
                            int changePrice = extraOrder.getExtra_price();
                            int originPrice = Integer.parseInt(priceTotal.getText().toString());
                            originPrice = (originPrice * 100) / (100 - discountRate);

                            ExtraOrder select = new ExtraOrder(extraOrder.getExtra_id(), extraOrder.getExtra_price(), extraOrder.getExtra_name(), 1);
                            select.setExtra_count(1);
                            defaultPrice = originPrice + (itemCount * (changePrice - memory));
                            changeDefaultPriceInEssential.changeEssentialValue(defaultPrice);
                            ifDiscountRate.setText(/*(int)((defaultPrice * 100) / (100 -discountRate))+""*/defaultPrice+"");
                            priceTotal.setText(String.valueOf(defaultPrice - (int)(defaultPrice * (discountRate / 100.0))));

                            selectOptions.put(text, select);
                            memory = changePrice;

                        }
                    });
                    ((LinearLayout)parent.getChildAt(0)).addView(radioButton);
                }
            }else {
                Log.e("Essential", 2+"");
                parent = (LinearLayout) inflater.inflate(R.layout.design_order_details_essential_table_child, null, false);

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
                            int originPrice = Integer.parseInt(priceTotal.getText().toString());
                            originPrice = (originPrice * 100) / (100 - discountRate);
                            int deletePrice = 0;
                            if (name.isChecked()) {
                                Log.e("Essential", "toggle");
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

                                defaultPrice = originPrice + (itemCount * (changePrice - deletePrice));
                                changeDefaultPriceInEssential.changeEssentialValue(defaultPrice);
//                                ifDiscountRate.setText((int)((defaultPrice * 100) / (100 -discountRate))+"");
                                ifDiscountRate.setText(defaultPrice+"");
                                priceTotal.setText(String.valueOf(defaultPrice - (int)(defaultPrice * (discountRate / 100.0))));

                                name.setChecked(true);
                                name.setBackgroundResource(R.drawable.menu_select);
                                name.setTextColor(ContextCompat.getColor(context, R.color.white));
                            } else {
                                Log.e("Essential", "NotToggle");
                                must--;
                                holder.selectedOptions.setText("");

                                defaultPrice = originPrice - (itemCount * changePrice);
                                changeDefaultPriceInEssential.changeEssentialValue(defaultPrice);
//                                ifDiscountRate.setText((int)((defaultPrice * 100) / (100 -discountRate))+"");
                                ifDiscountRate.setText(defaultPrice+"");
                                priceTotal.setText(String.valueOf(defaultPrice - (int)(defaultPrice * (discountRate / 100.0))));

                                name.setBackgroundResource(R.drawable.menu_non_select);
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
