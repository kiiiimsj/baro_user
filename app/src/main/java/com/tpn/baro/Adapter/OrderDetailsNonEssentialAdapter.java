package com.tpn.baro.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tpn.baro.AdapterHelper.ExtraOrder;
import com.tpn.baro.AdapterHelper.OrderDetailsNonEssential;
import com.tpn.baro.R;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderDetailsNonEssentialAdapter extends BaseExpandableListAdapter {
    private Context context;
    private int groupLayout = 0;
    private int chlidLayout = 0;
    private int defaultPrice;
    private int discountRate;
    private ArrayList<OrderDetailsNonEssential> DataList;
    private LayoutInflater inflater = null;
    private TextView priceTotal;
    private TextView itemCountText;
    private ArrayList<Integer> optionPrice;
    private ArrayList<Integer> optionCounts;
    private HashMap<String, ExtraOrder> nonEssentialOptions;
    public interface ChangeDefaultPriceNonEssential {
        void changeNonEssentialValue(int newDefaultPrice);
    }

    public ChangeDefaultPriceNonEssential changeDefaultPriceNonEssential;
//    public OrderDetailsNonEssentialAdapter(Context context, int groupLayout, int chlidLayout,
//                                           ArrayList<OrderDetailsNonEssential> dataList, TextView priceTotal, TextView itemCountText) {
//        this.context = context;
//        this.groupLayout = groupLayout;
//        this.chlidLayout = chlidLayout;
//        this.DataList = dataList;
//        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        this.priceTotal = priceTotal;
//        this.itemCountText = itemCountText;
//        this.optionPrice = new ArrayList<>();
//        this.optionCounts = new ArrayList<>();
//        this.nonEssentialOptions = new HashMap<>();
//    }
    public OrderDetailsNonEssentialAdapter(Context context, int groupLayout, int chlidLayout,
                                           ArrayList<OrderDetailsNonEssential> dataList, TextView priceTotal
            , TextView itemCountText, int defaultPrice , int discountRate, ChangeDefaultPriceNonEssential changeDefaultPriceNonEssential) {
        this.context = context;
        this.groupLayout = groupLayout;
        this.chlidLayout = chlidLayout;
        this.DataList = dataList;
        this.defaultPrice = defaultPrice;
        this.discountRate = discountRate;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.priceTotal = priceTotal;
        this.itemCountText = itemCountText;
        this.optionPrice = new ArrayList<>();
        this.optionCounts = new ArrayList<>();
        this.nonEssentialOptions = new HashMap<>();
        this.changeDefaultPriceNonEssential = changeDefaultPriceNonEssential;
    }

    public HashMap<String,ExtraOrder> getNonEssentialOptions() {
        return nonEssentialOptions;
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int i) {
        return DataList.size();
    }

    @Override
    public Object getGroup(int i) {
        return DataList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return DataList.get(i);
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
        TextView title = (TextView)convertView.findViewById(R.id.personal_option);
//        TextView extraOptionPrice = (TextView)convertView.findViewById(R.id.optionPrice);
        title.setText("퍼스널 옵션");
//        TextView extraOptionName = (TextView)convertView.findViewById(R.id.optionName);
//        extraOptionName.setText(DataList.get(i).ExtraOptionName);
//        TextView extraOptionPrice = (TextView)convertView.findViewById(R.id.optionPrice);
//        extraOptionPrice.setText(String.valueOf(DataList.get(i).optionPrice));
//        optionPrice.add(DataList.get(i).optionPrice);
//        for (int j =0;j<DataList.size();j++){
//            optionCounts.add(0);
//        }
        return convertView;
    }

    @Override
    public View getChildView(final int i, final int childPosition, boolean b, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(this.chlidLayout, parent, false);
        }
        TextView optionName = convertView.findViewById(R.id.optionName);
        TextView price = convertView.findViewById(R.id.optionPrice);
        final TextView optionCount = convertView.findViewById(R.id.optionCount);
        ImageButton minus = convertView.findViewById(R.id.minus);
        ImageButton plus = convertView.findViewById(R.id.plus);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemcount = Integer.parseInt(itemCountText.getText().toString());
                int optionCnt = Integer.parseInt(optionCount.getText().toString());
                int currentPrice = Integer.parseInt(priceTotal.getText().toString());
                currentPrice = (currentPrice * 100) / (100 - discountRate);
                if(optionCnt!=0){
                    optionCnt-=1;
                    optionCount.setText(String.valueOf(optionCnt));
                    defaultPrice = currentPrice-(itemcount*DataList.get(childPosition).getOptionPrice());
                    changeDefaultPriceNonEssential.changeNonEssentialValue(defaultPrice);
                    Log.e("price ", defaultPrice + "discount_rate " + discountRate);
                    priceTotal.setText(String.valueOf(defaultPrice - (int)(defaultPrice * (discountRate / 100.0))));

                    ExtraOrder extraOrder = new ExtraOrder(Integer.parseInt(DataList.get(childPosition).ExtraOption_id),
                            DataList.get(childPosition).optionPrice,DataList.get(childPosition).ExtraOptionName,DataList.get(childPosition).getMaxCount());
                    extraOrder.setExtra_count(optionCnt);
                    nonEssentialOptions.put(DataList.get(childPosition).ExtraOptionName,extraOrder);
                }
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemcount = Integer.parseInt(itemCountText.getText().toString());
                int optionCnt = Integer.parseInt(optionCount.getText().toString());
                int currentPrice = Integer.parseInt(priceTotal.getText().toString());
                currentPrice = (currentPrice * 100) / (100 - discountRate);
                if (optionCnt == DataList.get(childPosition).getMaxCount()){
                    Toast.makeText(context, "더이상 옵션을 추가할수가 없습니다",Toast.LENGTH_LONG);
                    return;
                }
                optionCnt+=1;
                optionCount.setText(String.valueOf(optionCnt));
                defaultPrice = currentPrice+(itemcount*DataList.get(childPosition).getOptionPrice());
                changeDefaultPriceNonEssential.changeNonEssentialValue(defaultPrice);
                Log.e("price ", defaultPrice + "discount_rate " + discountRate);
                priceTotal.setText(String.valueOf(defaultPrice - (int)(defaultPrice * (discountRate / 100.0))));

                ExtraOrder extraOrder = new ExtraOrder(Integer.parseInt(DataList.get(childPosition).ExtraOption_id),DataList.get(childPosition).optionPrice,
                        DataList.get(childPosition).ExtraOptionName,DataList.get(childPosition).getMaxCount());
                extraOrder.setExtra_count(optionCnt);
                nonEssentialOptions.put(DataList.get(childPosition).ExtraOptionName,extraOrder);
            }
        });
        optionName.setText(" · " +DataList.get(childPosition).getExtraOptionName());
        price.setText(DataList.get(childPosition).getOptionPrice()+"");
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}