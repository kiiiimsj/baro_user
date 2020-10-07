package com.example.wantchu.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.AdapterHelper.ListMenuHelperClass;
import com.example.wantchu.Database.SessionManager;
import com.example.wantchu.R;
import com.example.wantchu.Url.UrlMaker;

import java.util.ArrayList;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.MenuViewHolder> {

    static public Context context;
    static public String store_id;

    public interface OnListItemLongSelectedInterfaceForMenu {
        void onItemLongSelectedForMenu(View v, int adapterPosition);
    }

    public interface OnListItemSelectedInterfaceForMenu {
        void onItemSelectedForMenu(View v, int position);
    }

    private static OnListItemSelectedInterfaceForMenu mListener;
    private static OnListItemLongSelectedInterfaceForMenu mLongListener;

    static ArrayList<ListMenuHelperClass> listMenuHelperClasses;
    public MenuListAdapter(ArrayList<ListMenuHelperClass> list, OnListItemSelectedInterfaceForMenu listener, OnListItemLongSelectedInterfaceForMenu longListener, Context context, String store_id) {
        listMenuHelperClasses = list;
        mListener = listener;
        mLongListener = longListener;
        this.context = context;
        this.store_id = store_id;
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
        holder.menuImage.setBackground(Drawable.createFromPath(listMenuHelperClass.menuImage));
        holder.subscription.setText(listMenuHelperClass.menu_info);
        if(listMenuHelperClass.is_soldout.equals("Y")){
            holder.sold_out.setVisibility(View.VISIBLE);
            holder.background.setClickable(false);
            holder.background.setEnabled(false);
            holder.background.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
        }
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
        public ImageView menuImage;
        public TextView subscription;
        public TextView sold_out;
        public MenuViewHolder(@NonNull View itemView, int po) {
            super(itemView);
            menuName = itemView.findViewById(R.id.menu_button);
            menuPrice = itemView.findViewById(R.id.menu_price);
            menuId = itemView.findViewById(R.id.menu_id);
            background = itemView.findViewById(R.id.background);
            menuImage = itemView.findViewById(R.id.menu_image);
            subscription = itemView.findViewById(R.id.subscription);
            sold_out = itemView.findViewById(R.id.sold_out);

            ListMenuHelperClass list = listMenuHelperClasses.get(po);
            makeRequest(list.menuImage, context, menuImage);

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
        }
    }

    public static void makeRequest(String menu_image, Context context, final ImageView image) {
        UrlMaker urlMaker = new UrlMaker();
        String lastUrl = "ImageMenu.do?store_id=" + store_id + "&image_name=";
        String url = urlMaker.UrlMake(lastUrl);
        StringBuilder urlBuilder = new StringBuilder()
                .append(url)
                .append(menu_image);
        Log.e("menu", urlBuilder.toString());
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        ImageRequest request = new ImageRequest(urlBuilder.toString(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        image.setImageBitmap(response);
                    }
                }, image.getWidth(), image.getHeight(), ImageView.ScaleType.FIT_XY, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("menuimageerror", "error");
                    }
                });
        requestQueue.add(request);
    }
}
