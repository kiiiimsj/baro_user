package com.example.wantchu.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wantchu.R;

import java.util.ArrayList;

public class MyPageButtonAdapter extends RecyclerView.Adapter<MyPageButtonAdapter.MyPageViewHolder> {
    static ArrayList<String> buttonList;
    static Context context;
    public interface OnItemCilckListener {
        void itemClick(int position);
    }
    OnItemCilckListener mListener;
    public MyPageButtonAdapter(Context context, ArrayList<String> buttons, OnItemCilckListener mListener) {
        this.context = context;
        this.buttonList = buttons;
        this.mListener = mListener;
    }
    @NonNull
    @Override
    public MyPageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_page_parent_view, parent, false);
        MyPageViewHolder myPageViewHolder = new MyPageViewHolder(view, viewType);
        return myPageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyPageViewHolder holder, int position) {
        String str = buttonList.get(position);
        holder.buttonText.setText(str);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return buttonList == null? 0 : buttonList.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onViewRecycled(@NonNull MyPageViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull MyPageViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyPageViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MyPageViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    public class MyPageViewHolder extends RecyclerView.ViewHolder {
        View bottomLine;
        TextView buttonText;
        RelativeLayout backSize;
        public MyPageViewHolder(@NonNull View itemView, final int po) {
            super(itemView);
            buttonText = itemView.findViewById(R.id.my_page_button);
            bottomLine = itemView.findViewById(R.id.bottom_line);
            backSize = itemView.findViewById(R.id.my_page_background);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.itemClick(po);
                }
            });
            if(po == 3) {
                View addLine = new View(context);
                addLine.setMinimumHeight((int) 0.5);
                addLine.setMinimumWidth(backSize.getWidth());
                addLine.setBackgroundColor(context.getResources().getColor(R.color.home_background));
                addLine.setBottom(backSize.getBottom());

                int resizeHeight = backSize.getHeight();
                resizeHeight += addLine.getHeight();


            }
            if(po == 5) {
                View addLine = new View(context);
                addLine.setMinimumHeight((int) 0.5);
                addLine.setMinimumWidth(backSize.getWidth());
                addLine.setBackgroundColor(context.getResources().getColor(R.color.home_background));
                addLine.setBottom(backSize.getBottom());
            }
//            if(buttonList.size() == po + 1) {
//                bottomLine.setVisibility(View.GONE);
//                LinearLayout.MarginLayoutParams vl = new LinearLayout.MarginLayoutParams(backSize.getLayoutParams());
//                vl.setMargins(0,0,0, 20);
//                backSize.setLayoutParams(new LinearLayout.LayoutParams(vl));
//            }
        }
    }
}
