package com.example.wantchu.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wantchu.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class OrderDetailsEssentialTableAdapter extends RecyclerView.Adapter<OrderDetailsEssentialTableAdapter.ViewHolder> {

    private final static int TABLE_ROW_SIZE = 3;


    private HashMap<String, Integer> mData;
    private Context context;
    private Iterator<String> iterator;
    private ArrayList<String> nameList;
    private ArrayList<Integer> priceList;
    public OrderDetailsEssentialTableAdapter(Context context , HashMap<String, Integer> mData) {
        this.context = context;
        this.mData = mData;
        iterator = mData.keySet().iterator();
        this.nameList = new ArrayList<>();
        this.priceList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.activity_order_details_essential_table, parent, false) ;
        ViewHolder viewHolder = new ViewHolder(view) ;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int count =0;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        resolveHashMapToArrayList(mData);
        Log.i("size",String.valueOf((mData.size()/TABLE_ROW_SIZE)+1));
        TableRow tableRow = null;
        while(count !=mData.size()){

            if(count%3 ==0){
                tableRow = (TableRow) inflater.inflate(R.layout.activity_order_details_essential_table_child,null,false);
            }
            Log.i("count",String.valueOf(count));
            LinearLayout optionElement = (LinearLayout) inflater.inflate(R.layout.activity_order_details_essential_table_child2,null,false);
            Button button = (Button) optionElement.getChildAt(0);
            TextView textView = (TextView) optionElement.getChildAt(1);
            button.setText(nameList.get(count));
            textView.setText(String.valueOf(priceList.get(count)));
            tableRow.addView(optionElement);
            count++;
            if(count %3 == 0 || count == mData.size()){
                holder.essentialOptionTable.addView(tableRow);
            }
        }





//        TableRow tableRow = new TableRow(context);
//        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT);
//        tableRow.setLayoutParams(params);
//        TextView textView = new TextView(context);
//        textView.setText("SDFasdfafdfadfadf");
//        tableRow.addView(textView);
//        holder.essentialOptionTable.addView(tableRow);
    }

    @Override
    public int getItemCount() {
        return mData.size() == 0? null : mData.size();

    }

    public void resolveHashMapToArrayList(HashMap<String,Integer> mData){
        while(iterator.hasNext()){
            String optionName = iterator.next();
            int optionPrice = mData.get(optionName);
            nameList.add(optionName);
            priceList.add(optionPrice);
            Log.i("name",optionName);
            Log.i("price",String.valueOf(optionPrice));
        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TableLayout essentialOptionTable;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
            essentialOptionTable =itemView.findViewById(R.id.optionTable);
        }
    }
}
