package com.example.wantchu.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wantchu.AdapterHelper.ExtraOrder;
import com.example.wantchu.AdapterHelper.OrderDetailsNonEssential;
import com.example.wantchu.R;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderDetailsNonEssentialAdapter extends BaseExpandableListAdapter {
    private Context context;
    private int groupLayout = 0;
    private int chlidLayout = 0;
    private ArrayList<OrderDetailsNonEssential> DataList;
    private LayoutInflater inflater = null;
    private TextView priceTotal;
    private TextView itemCountText;
    private ArrayList<Integer> optionPrice;
    private ArrayList<Integer> optionCounts;
    private HashMap<String, ExtraOrder> nonEssentialOptions;

    public OrderDetailsNonEssentialAdapter(Context context, int groupLayout, int chlidLayout,
                                           ArrayList<OrderDetailsNonEssential> dataList, TextView priceTotal, TextView itemCountText) {
        this.context = context;
        this.groupLayout = groupLayout;
        this.chlidLayout = chlidLayout;
        this.DataList = dataList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.priceTotal = priceTotal;
        this.itemCountText = itemCountText;
        this.optionPrice = new ArrayList<>();
        this.optionCounts = new ArrayList<>();
        this.nonEssentialOptions = new HashMap<>();
    }

    public HashMap<String,ExtraOrder> getNonEssentialOptions() {
        return nonEssentialOptions;
    }

    @Override
    public int getGroupCount() {
        return DataList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return DataList.get(i).justCount.size();
    }

    @Override
    public Object getGroup(int i) {
        return DataList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return DataList.get(i).justCount.get(i);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(this.groupLayout, parent, false);
        }

        TextView extraOptionName = (TextView)convertView.findViewById(R.id.optionName);
        extraOptionName.setText(DataList.get(i).ExtraOptionName);
        TextView extraOptionPrice = (TextView)convertView.findViewById(R.id.optionPrice);
        extraOptionPrice.setText(String.valueOf(DataList.get(i).optionPrice));
        optionPrice.add(DataList.get(i).optionPrice);
        for (int j =0;j<DataList.size();j++){
            optionCounts.add(0);
        }
        return convertView;
    }

    @Override
    public View getChildView(final int i, int i1, boolean b, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(this.chlidLayout, parent, false);
        }
        final TextView count = (TextView) convertView.findViewById(R.id.optionCount);
        final Button minus = (Button) convertView.findViewById(R.id.minus);
        count.setText(optionCounts.get(i)+"");
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemcount = Integer.parseInt(itemCountText.getText().toString());
                int optionCount = Integer.parseInt(count.getText().toString());
                int currentPrice = Integer.parseInt(priceTotal.getText().toString());
                if(optionCount!=0){
                    optionCount-=1;
                    count.setText(String.valueOf(optionCount));
                    priceTotal.setText(String.valueOf(currentPrice-(itemcount*optionPrice.get(i))));
                    ExtraOrder extraOrder = new ExtraOrder(Integer.parseInt(DataList.get(i).ExtraOption_id),DataList.get(i).optionPrice,DataList.get(i).ExtraOptionName,DataList.get(i).getMaxCount());
                    extraOrder.setExtra_count(optionCount);
                    optionCounts.set(i,optionCount);
                    nonEssentialOptions.put(DataList.get(i).ExtraOptionName,extraOrder);
                }
            }
        });
        final Button plus = (Button) convertView.findViewById(R.id.plus);
        plus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int itemcount = Integer.parseInt(itemCountText.getText().toString());
                int optionCount = Integer.parseInt(count.getText().toString());
                int currentPrice = Integer.parseInt(priceTotal.getText().toString());
                if (optionCount == DataList.get(i).getMaxCount()){
                    Toast.makeText(context, "더이상 옵션을 추가할수가 없습니다",Toast.LENGTH_LONG);
                    return;
                }
                Log.i("p",String.valueOf(optionPrice));
                optionCount+=1;
                count.setText(String.valueOf(optionCount));
                priceTotal.setText(String.valueOf(currentPrice+(itemcount*optionPrice.get(i))));
                ExtraOrder extraOrder = new ExtraOrder(Integer.parseInt(DataList.get(i).ExtraOption_id),DataList.get(i).optionPrice,DataList.get(i).ExtraOptionName,DataList.get(i).getMaxCount());
                extraOrder.setExtra_count(optionCount);
                optionCounts.set(i,optionCount);
                nonEssentialOptions.put(DataList.get(i).ExtraOptionName,extraOrder);
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}