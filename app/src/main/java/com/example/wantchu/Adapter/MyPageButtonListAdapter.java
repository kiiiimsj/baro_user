package com.example.wantchu.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wantchu.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MyPageButtonListAdapter extends RecyclerView.Adapter<MyPageButtonListAdapter.MyPageButtonsViewHolder>{
    SharedPreferences shf;
    Context context;
    public interface OnListItemLongSelectedInterfaceForMyPage {
        void onItemLongSelectedForMyPage(View v, int adapterPosition);
    }

    public interface OnListItemSelectedInterfaceForMyPage {
        void onItemSelectedForMyPage(View v, int position);
    }

    private static MyPageButtonListAdapter.OnListItemSelectedInterfaceForMyPage mListener;
    private static MyPageButtonListAdapter.OnListItemLongSelectedInterfaceForMyPage mLongListener;

    int[] setCount;
    ArrayList<String> list;
    public MyPageButtonListAdapter(MyPageButtonListAdapter.OnListItemSelectedInterfaceForMyPage mListener, ArrayList<String> list, int[] setCount, Context context) {
        this.mListener = mListener;
        this.setCount = setCount;
        this.list = list;
        this.context =context;
    }
    public MyPageButtonListAdapter(ArrayList<String> list) {
        this.list = list;
    }
    @NonNull
    @Override
    public MyPageButtonsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        View view = LayoutInflater.from(context).inflate(R.layout.mypage_button, parent, false);
        MyPageButtonsViewHolder myPageButtonsViewHolder = new MyPageButtonsViewHolder(view);
        return myPageButtonsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyPageButtonsViewHolder holder, int position) {
        String strS = list.get(position);
        holder.buttonName.setText(strS);
        if(strS.equals("주문내역")) {
            holder.count.setText(Integer.toString(setCount[0]));
        }
        if(strS.equals("내 쿠폰")) {
            holder.count.setText(Integer.toString(setCount[1]));
        }
        if(strS.equals("장바구니")) {
            shf = context.getSharedPreferences("basketList", MODE_PRIVATE);
            int count = shf.getInt("orderCnt", 0);
            holder.count.setText(Integer.toString(count));
        }
    }

    @Override
    public int getItemCount() {
        return list == null? 0 : list.size();
    }
    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull MyPageButtonsViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyPageButtonsViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewRecycled(@NonNull MyPageButtonsViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MyPageButtonsViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public class MyPageButtonsViewHolder extends RecyclerView.ViewHolder {
        public TextView buttonName;
        public TextView count;
        public MyPageButtonsViewHolder(@NonNull View itemView) {
            super(itemView);
            buttonName = itemView.findViewById(R.id.button_string);
            count = itemView.findViewById(R.id.itemCount);

            buttonName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelectedForMyPage(v, getAdapterPosition());
                }
            });
        }
    }
}
