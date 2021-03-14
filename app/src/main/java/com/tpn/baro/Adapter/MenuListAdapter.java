package com.tpn.baro.Adapter;

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
import com.tpn.baro.AdapterHelper.ListMenuHelperClass;
import com.tpn.baro.R;
import com.tpn.baro.Url.UrlMaker;

import java.util.ArrayList;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.MenuViewHolder> {

    private Context context;
    private String store_id;
    private int discountRate;

    public interface OnListItemSelectedInterfaceForMenu {
        void onItemSelectedForMenu(View v, int position);
    }

    private static OnListItemSelectedInterfaceForMenu mListener;

    private ArrayList<ListMenuHelperClass> listMenuHelperClasses;

    public MenuListAdapter(ArrayList<ListMenuHelperClass> list, OnListItemSelectedInterfaceForMenu listener, Context context, String store_id, int discountRate) {
        listMenuHelperClasses = list;
        mListener = listener;
        this.context = context;
        this.store_id = store_id;
        this.discountRate = discountRate;
    }
    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.design_menu_list, parent, false);
        MenuViewHolder menuViewHolder = new MenuViewHolder(view);
        return menuViewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, final int position) {
        final ListMenuHelperClass listMenuHelperClass = listMenuHelperClasses.get(position);
        if(discountRate != 0) {
            holder.menuPrice.setText((listMenuHelperClass.menuPrice - (int)(listMenuHelperClass.menuPrice * (discountRate / 100.0))) +" 원");
            holder.menuDefaultPrice.setText(listMenuHelperClass.menuPrice+"원");
            holder.menuDefaultPrice.setVisibility(View.VISIBLE);
            holder.priceCancelImage.setVisibility(View.VISIBLE);
            holder.arrowRight.setVisibility(View.VISIBLE);
        }else {
            holder.menuPrice.setText(listMenuHelperClass.menuPrice +" 원");
            holder.menuDefaultPrice.setVisibility(View.GONE);
            holder.priceCancelImage.setVisibility(View.GONE);
            holder.arrowRight.setVisibility(View.GONE);
        }

        holder.menuName.setText(listMenuHelperClass.menus);
        holder.menuId.setText(Integer.toString(listMenuHelperClass.menuId));
        holder.menuImage.setBackground(Drawable.createFromPath(listMenuHelperClass.menuImage));
        makeRequest(listMenuHelperClass.menuImage, context, holder.menuImage);
        holder.subscription.setText(listMenuHelperClass.menu_info);

        if(listMenuHelperClass.is_soldout.equals("Y")){
            holder.sold_out.setVisibility(View.VISIBLE);
            holder.background.setClickable(false);
            holder.background.setEnabled(false);
        }
        holder.background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemSelectedForMenu(v, position);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
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
        public TextView menuDefaultPrice;
        public ImageView priceCancelImage;
        public TextView menuId;
        public RelativeLayout background;
        public ImageView menuImage;
        public TextView subscription;
        public TextView sold_out;
        public ImageView arrowRight;
        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            menuName = itemView.findViewById(R.id.menu_name);
            menuPrice = itemView.findViewById(R.id.menu_price);
            priceCancelImage = itemView.findViewById(R.id.price_cancel_cross_line);
            menuDefaultPrice = itemView.findViewById(R.id.menu_default_price);
            arrowRight = itemView.findViewById(R.id.arrow_right);
            priceCancelImage.setVisibility(View.GONE);
            menuDefaultPrice.setVisibility(View.GONE);
            arrowRight.setVisibility(View.GONE);

            menuId = itemView.findViewById(R.id.menu_id);
            background = itemView.findViewById(R.id.background);
            menuImage = itemView.findViewById(R.id.menu_image);
            subscription = itemView.findViewById(R.id.subscription);
            sold_out = itemView.findViewById(R.id.sold_out);
        }
    }

    public void makeRequest(String menu_image, Context context, final ImageView image) {
        UrlMaker urlMaker = new UrlMaker();
        String lastUrl = "ImageMenu.do?store_id=" + store_id + "&image_name=";
        String url = urlMaker.UrlMake(lastUrl);
        StringBuilder urlBuilder = new StringBuilder()
                .append(url)
                .append(menu_image);
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
