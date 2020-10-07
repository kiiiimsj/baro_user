package com.example.wantchu.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wantchu.R;

import java.util.ArrayList;

public class MyPageButtonAdapter extends RecyclerView.Adapter<MyPageButtonAdapter.MyPageViewHolder> {
    ArrayList<String> buttonList;
    static Context context;
    public MyPageButtonAdapter(Context context, ArrayList<String> buttons) {
        this.context = context;
        this.buttonList = buttons;
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
    public int getItemCount() {
        return buttonList == null? 0 : buttonList.size();
    }

    public class MyPageViewHolder extends RecyclerView.ViewHolder {
        TextView buttonText;
        public MyPageViewHolder(@NonNull View itemView, int po) {
            super(itemView);
            buttonText = itemView.findViewById(R.id.my_page_button);
            if(buttonText.getText().toString().equals("1:1 문의")) {
                View view = new View(context);
                view.setMinimumWidth(itemView.getWidth());
                view.setMinimumHeight(2);
                view.setBackgroundColor(context.getResources().getColor(R.color.main));
                view.setX(buttonText.getBottom());
            }
        }
    }
}
