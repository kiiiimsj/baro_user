package com.example.wantchu.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wantchu.AdapterHelper.ListMenuHelperClass;
import com.example.wantchu.R;

import java.util.ArrayList;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.MenuViewHolder> {
    public interface OnListItemLongSelectedInterfaceForMenu {
        void onItemLongSelectedForMenu(View v, int adapterPosition);
    }

    public interface OnListItemSelectedInterfaceForMenu {
        void onItemSelectedForMenu(View v, int position);
    }

    private static OnListItemSelectedInterfaceForMenu mListener;
    private static OnListItemLongSelectedInterfaceForMenu mLongListener;

    static ArrayList<ListMenuHelperClass> listMenuHelperClasses;
    public MenuListAdapter(ArrayList<ListMenuHelperClass> list, OnListItemSelectedInterfaceForMenu listener, OnListItemLongSelectedInterfaceForMenu longListener) {
        listMenuHelperClasses = list;
        mListener = listener;
        mLongListener = longListener;
    }
    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.menu_layout, parent, false);
        MenuViewHolder menuViewHolder = new MenuViewHolder(view, viewType);
        return menuViewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        ListMenuHelperClass listMenuHelperClass = listMenuHelperClasses.get(position);

        holder.menuName.setText(listMenuHelperClass.menus);
        holder.menuPrice.setText(Integer.toString(listMenuHelperClass.menuPrice)+" 원");
        holder.menuId.setText(Integer.toString(listMenuHelperClass.menuId));
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return listMenuHelperClasses == null ? 0 : listMenuHelperClasses.size();
    }
    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull MenuListAdapter.MenuViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MenuListAdapter.MenuViewHolder  holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewRecycled(@NonNull MenuListAdapter.MenuViewHolder  holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MenuListAdapter.MenuViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        public TextView menuName;
        public TextView menuPrice;
        public TextView menuId;
        public RelativeLayout background;
        public MenuViewHolder(@NonNull View itemView, int po) {
            super(itemView);
            menuName = itemView.findViewById(R.id.menu_button);
            menuPrice = itemView.findViewById(R.id.menu_price);
            menuId = itemView.findViewById(R.id.menu_id);
            background = itemView.findViewById(R.id.background);

            if(listMenuHelperClasses.size() == po + 1) {
                ViewGroup.LayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 480);
                itemView.setLayoutParams(layoutParams);
            }

            background.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelectedForMenu(v, getAdapterPosition());
                }
            });
            background.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongListener.onItemLongSelectedForMenu(v, getAdapterPosition());
                    return false;
                }
            });
//            menuPrice.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//            menuName.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });

        }
    }
}
