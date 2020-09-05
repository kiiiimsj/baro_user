package com.example.wantchu.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wantchu.AdapterHelper.ExtraOrder;
import com.example.wantchu.AdapterHelper.OrderDetailsNonEssential;
import com.example.wantchu.R;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderDetailsNewNonEssentailAdapter extends RecyclerView.Adapter<OrderDetailsNewNonEssentailAdapter.ViewHolder> {
    private Context context;
    private TextView priceTotal;
    private TextView itemCountText;
    private ArrayList<OrderDetailsNonEssential> DataList;
    private HashMap<String, ExtraOrder> nonEssentialOptions;

    public OrderDetailsNewNonEssentailAdapter(Context context,TextView priceTotal, TextView itemCountText, ArrayList<OrderDetailsNonEssential> dataList) {
        this.context = context;
        this.priceTotal = priceTotal;
        this.itemCountText = itemCountText;
        this.DataList = dataList;
        this.nonEssentialOptions = new HashMap<>();
    }
    public HashMap<String,ExtraOrder> getNonEssentialOptions() {
        return nonEssentialOptions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_order_details_new_nonessential,parent,false);
        ViewHolder viewHolder = new OrderDetailsNewNonEssentailAdapter.ViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.optionName.setText(DataList.get(position).getExtraOptionName());
        holder.optionPrice.setText(DataList.get(position).getOptionPrice()+"");
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemcount = Integer.parseInt(itemCountText.getText().toString());
                int optionCnt = Integer.parseInt(holder.optionCount.getText().toString());
                int currentPrice = Integer.parseInt(priceTotal.getText().toString());
                if(optionCnt!=0){
                    optionCnt-=1;
                    holder.optionCount.setText(String.valueOf(optionCnt));
                    priceTotal.setText(String.valueOf(currentPrice-(itemcount*DataList.get(position).getOptionPrice())));
                    ExtraOrder extraOrder = new ExtraOrder(Integer.parseInt(DataList.get(position).ExtraOption_id),
                            DataList.get(position).optionPrice,DataList.get(position).ExtraOptionName,DataList.get(position).getMaxCount());
                    extraOrder.setExtra_count(optionCnt);
                    nonEssentialOptions.put(DataList.get(position).ExtraOptionName,extraOrder);
                }
            }
        });
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemcount = Integer.parseInt(itemCountText.getText().toString());
                int optionCount = Integer.parseInt(holder.optionCount.getText().toString());
                int currentPrice = Integer.parseInt(priceTotal.getText().toString());
                if (optionCount == DataList.get(position).getMaxCount()){
                    Toast.makeText(context, "더이상 옵션을 추가할수가 없습니다",Toast.LENGTH_LONG);
                    return;
                }
                optionCount+=1;
                holder.optionCount.setText(String.valueOf(optionCount));
                priceTotal.setText(String.valueOf(currentPrice+(itemcount*DataList.get(position).getOptionPrice())));
                ExtraOrder extraOrder = new ExtraOrder(Integer.parseInt(DataList.get(position).ExtraOption_id),DataList.get(position).optionPrice,
                        DataList.get(position).ExtraOptionName,DataList.get(position).getMaxCount());
                extraOrder.setExtra_count(optionCount);
                nonEssentialOptions.put(DataList.get(position).ExtraOptionName,extraOrder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (DataList.size() == 0? 0: DataList.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView optionCount;
        TextView optionPrice;
        TextView optionName;
        ImageButton plus;
        ImageButton minus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            plus = (itemView).findViewById(R.id.plus);
            minus = (itemView).findViewById(R.id.minus);
            optionName = (itemView).findViewById(R.id.optionName);
            optionPrice = (itemView).findViewById(R.id.optionPrice);
            optionCount = (itemView).findViewById(R.id.optionCount);

        }
    }
}
