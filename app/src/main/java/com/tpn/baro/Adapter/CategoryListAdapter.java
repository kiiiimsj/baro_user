package com.tpn.baro.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tpn.baro.AdapterHelper.ListCategoryHelperClass;
import com.tpn.baro.R;

import java.util.ArrayList;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {

    public interface OnListItemLongSelectedInterfaceForCategory {
        void onItemLongSelectedForCategory(View v, int adapterPosition);
    }

    public interface OnListItemSelectedInterfaceForCategroy {
        void onItemSelectedForCategory(View v, int position);
    }

    private static OnListItemSelectedInterfaceForCategroy mListener;
    private static OnListItemLongSelectedInterfaceForCategory mLongListener;

    //생성자를 통해 데이터 받아오기
    ArrayList<ListCategoryHelperClass> listStoreHelperClassArrayList;

    public CategoryListAdapter(ArrayList<ListCategoryHelperClass> list, OnListItemSelectedInterfaceForCategroy listener, OnListItemLongSelectedInterfaceForCategory longListener){
        this.listStoreHelperClassArrayList = list;
        this.mListener = listener;
        this.mLongListener = longListener;
    }

    @NonNull
    @Override
    public CategoryListAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        View view = LayoutInflater.from(context).inflate(R.layout.category_layout, parent, false);
        CategoryViewHolder categoryViewHolder = new CategoryViewHolder(view);
        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        ListCategoryHelperClass listCategoryHelperClass = listStoreHelperClassArrayList.get(position);
        holder.category.setText(listCategoryHelperClass.categoryName);
        holder.categoryId.setText(Integer.toString(listCategoryHelperClass.categoryId));
    }

    @Override
    public int getItemCount() { return listStoreHelperClassArrayList.size(); }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        public TextView category;
        public TextView categoryId;
        public RelativeLayout clickBack;
        public CategoryViewHolder(@NonNull final View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.category_button);
            categoryId = itemView.findViewById(R.id.category_id);
            clickBack = itemView.findViewById(R.id.click_back);

            clickBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemSelectedForCategory(view, getAdapterPosition());
                }
            });

            category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemSelectedForCategory(view, getAdapterPosition());
                }
            });

            category.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongListener.onItemLongSelectedForCategory(v, getAdapterPosition());
                    return false;
                }
            });

            clickBack.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongListener.onItemLongSelectedForCategory(v, getAdapterPosition());
                    return false;
                }
            });
        }
    }
}
