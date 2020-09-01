package com.example.wantchu.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
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
    private Iterator<String> iterator;
    private TextView priceTotal;
    private TextView itemCountText;
//    private HashMap <String,Integer> selectOption;
    HashMap<String,ExtraOrder> selectOptions;
    int must;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<Integer> priceList = new ArrayList<>();
        ArrayList<Integer> option_ids = new ArrayList<>();
        Log.i("p",String.valueOf(position));
        final ArrayList<ToggleButton> toggleButtons = new ArrayList<>();
        int count = 0;
        int rowCount = 0;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        TableLayout tableLayout = (TableLayout) inflater.inflate(R.layout.activity_order_details_essential_table,null,false);
        final String text = mData.get(position);
        holder.optionName.setText(text);

        if(mTableData.containsKey(text)){
            Log.i("필수",text);
            ArrayList<ExtraOrder> extraOrders = mTableData.get(text);
            resolveExtraOrdersToArrayList(extraOrders,nameList,priceList,option_ids);
            Log.i("size",String.valueOf(nameList.size()));
            ArrayList<TableRow> tableRows = new ArrayList<>(nameList.size()/3 + 1);
            TableRow tableRow = null;
            for(int i =0;i<extraOrders.size();i++){
                tableRows.add(i, (TableRow) inflater.inflate(R.layout.activity_order_details_essential_table_child, null, false));
            }
            while(count !=extraOrders.size()){
                if(count % 3==0 ){
                    tableRow =tableRows.get(rowCount);
                    rowCount++;
                }
                LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.activity_order_details_essential_table_child2,null,false);
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
                        if(name.isChecked()){

                            for(int i =0;i<toggleButtons.size();i++){
                                if(toggleButtons.get(i) != name && toggleButtons.get(i).isChecked()){
                                    must--;
                                    deletePrice = Integer.parseInt(((TextView)(((ViewGroup) toggleButtons.get(i).getParent()).getChildAt(1))).getText().toString());
                                }
                                toggleButtons.get(i).setChecked(false);
                                toggleButtons.get(i).setBackgroundResource(R.drawable.button_border_empty);
                            }
                            must++;
                            HashMap<String,Integer> selectOption = new HashMap<>();
                            ExtraOrder select = new ExtraOrder(Integer.parseInt(id.getText().toString()),Integer.parseInt(price.getText().toString()),name.getText().toString(),1);
                            select.setExtra_count(1);
                            selectOption.put(name.getText().toString(),Integer.parseInt(price.getText().toString()));
                            selectOptions.put(text,select);
                            priceTotal.setText(String.valueOf(originPrice+(itemCount*(changePrice-deletePrice))));
                            name.setChecked(true);
                            name.setBackgroundResource(R.drawable.button_border_full);
                        }else{
                            must--;
                            priceTotal.setText(String.valueOf(originPrice-(itemCount*changePrice)));
                            name.setBackgroundResource(R.drawable.button_border_empty);
                        }
                    }
                });
                price.setText(String.valueOf(priceList.get(count)));
                id.setText(""+option_ids.get(count));
                tableRow.addView(linearLayout);
                count++;

            }
            for(int i =0;i<extraOrders.size();i++){
                tableLayout.addView(tableRows.get(i));
            }
            holder.essentialOptionTableShell.addView(tableLayout);
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
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView optionName;
        LinearLayout essentialOptionTableShell;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            optionName = itemView.findViewById(R.id.essentialOptionName);
            essentialOptionTableShell = itemView.findViewById(R.id.tableShell);
        }
    }
}
