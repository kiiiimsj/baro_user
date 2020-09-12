package com.example.wantchu.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wantchu.AdapterHelper.InquiryData;
import com.example.wantchu.AdapterHelper.InquiryDataList;
import com.example.wantchu.R;

public class InquiryListAdapter extends RecyclerView.Adapter<InquiryListAdapter.InquiryViewHolder> {

    public interface OnListItemLongSelectedInterfaceForInquiry {
        void onItemLongSelectedForInquiry(View v, int adapterPosition);
    }

    public interface OnListItemSelectedInterfaceForInquiry {
        void onItemSelectedForInquiry(View v, int position);
    }

    private static OnListItemSelectedInterfaceForInquiry mListener;
    private static OnListItemLongSelectedInterfaceForInquiry mLongListener;

    public InquiryDataList inquiryDataList;

    public InquiryListAdapter(InquiryDataList inquiryDataList, OnListItemSelectedInterfaceForInquiry mListener, OnListItemLongSelectedInterfaceForInquiry mLongListener) {
        this.inquiryDataList = inquiryDataList;
        this.mListener = mListener;
        this.mLongListener = mLongListener;
    }
    @NonNull
    @Override
    public InquiryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.inquiry_list, parent, false);
        InquiryViewHolder inquiryViewHolder = new InquiryViewHolder(view);
        return inquiryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InquiryViewHolder holder, int position) {
        InquiryData inquiryData = inquiryDataList.inquiry.get(position);
        holder.inquiryTitle.setText(inquiryData.getTitle());
        if(inquiryData.getIs_replied().equals("N")) {
            holder.inquiryReplied.setText("미답변");
        }
        if(inquiryData.getIs_replied().equals("Y")) {
            holder.inquiryReplied.setText("답변완료");
        }
        holder.inquiryDate.setText(inquiryData.getInquiry_date());
        holder.inquiryId.setText(Integer.toString(inquiryData.getInquiry_id()));
        Log.i("INQUIRYID", inquiryData.getInquiry_id()+"");
    }

    @Override
    public int getItemCount() {
        return inquiryDataList== null? 0: inquiryDataList.inquiry.size();
    }
    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull InquiryListAdapter.InquiryViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull InquiryListAdapter.InquiryViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewRecycled(@NonNull InquiryListAdapter.InquiryViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull InquiryListAdapter.InquiryViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public class InquiryViewHolder extends RecyclerView.ViewHolder {
        public TextView inquiryTitle;
        public TextView inquiryDate;
        public TextView inquiryReplied;
        public TextView inquiryId;
        RelativeLayout background;
        public InquiryViewHolder(@NonNull View itemView) {
            super(itemView);
            inquiryTitle = itemView.findViewById(R.id.inquiry_title);
            inquiryDate = itemView.findViewById(R.id.inquiry_date);
            inquiryReplied = itemView.findViewById(R.id.is_replied);
            inquiryId = itemView.findViewById(R.id.inquiry_id);
            background = itemView.findViewById(R.id.background);

            background.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelectedForInquiry(v, getAdapterPosition());
                }
            });
            inquiryTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelectedForInquiry(v, getAdapterPosition());
                }
            });
        }
    }
}
