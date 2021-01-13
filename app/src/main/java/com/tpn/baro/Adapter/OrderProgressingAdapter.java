package com.tpn.baro.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.tpn.baro.Dialogs.ProgressingDetailDialog;
import com.tpn.baro.JsonParsingHelper.OrderProgressingParsingHelper;
import com.tpn.baro.R;
import com.tpn.baro.Url.UrlMaker;
import com.google.gson.Gson;

import java.util.ArrayList;

public class OrderProgressingAdapter extends RecyclerView.Adapter<OrderProgressingAdapter.ViewHolder> {
    private static String TAG = "OrderProgressingAdapter";
    private static String ACCEPT = "ACCEPT";
    private static String PREPARING = "PREPARING";

    static ArrayList<OrderProgressingParsingHelper> mData;
    public Context context;

    public OrderProgressingAdapter(ArrayList<OrderProgressingParsingHelper> mData, Context context) {
        this.mData = mData;
        this.context = context;
    }
    public void onResume(){

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_order_progressing_list,parent,false);
        ViewHolder viewHolder = new OrderProgressingAdapter.ViewHolder(view, viewType);

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final OrderProgressingParsingHelper reverse = mData.get(mData.size()-position-1);
        holder.order_date.setText(reverse.getOrder_date());
        holder.stores_name.setText(reverse.getStore_name());
        holder.store_phone.setText(reverse.getStore_phone());
        holder.total_prices.setText(reverse.getTotal_price()+"");
        holder.showDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressingDetailDialog progressingDetailDialog = ProgressingDetailDialog.newInstance(context);
                progressingDetailDialog.show(((AppCompatActivity)context).getSupportFragmentManager(),"dialog");
                Gson gson = new Gson();
                String json = gson.toJson(reverse);
                Bundle bundle = progressingDetailDialog.getArguments();
                bundle.putString("json",json);
            }
        });

        if(reverse.getOrder_state().equals(ACCEPT)){
            holder.order_state.setText("제 조 중");
            holder.chageViewColor(2);

        }
        else if(reverse.getOrder_state().equals(PREPARING)){
            holder.order_state.setText("접 수 대 기");
            holder.chageViewColor(1);
        }else{
            holder.chageViewColor(3);
        }

//        FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(holder.map.getId(),new OrderProgressingMap(reverse.getStore_latitude(),reverse.getStore_longitude())).commit();
    }

    @Override
    public int getItemCount() {
        return (mData == null ? 0 : mData.size());
    }
    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

    }

    @Override
    public boolean onFailedToRecycleView(@NonNull OrderProgressingAdapter.ViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull OrderProgressingAdapter.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewRecycled(@NonNull OrderProgressingAdapter.ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull OrderProgressingAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView order_state;
        TextView order_date;
        TextView stores_name;
        TextView total_prices;
        ImageView store_image;
        TextView store_phone;
        LinearLayout shell;
        Button showDetails;
        Button callStore;
        ImageView firstState;
        ImageView secondState;
        ImageView thirdState;
        View firstLine;
        View secondLine;
        View thirdLine;
        TextView payComplete;
        TextView accept;
        TextView making;
        TextView takeout;
        ArrayList<Status> statuses = new ArrayList<>();
        public ViewHolder(@NonNull View itemView, int po) {
            super(itemView);
            order_state = (itemView).findViewById(R.id.order_state);
            order_date = (itemView).findViewById(R.id.ordered_dates);
            stores_name = (itemView).findViewById(R.id.store_names);
            total_prices = (itemView).findViewById(R.id.totalPrices);
            shell = (itemView).findViewById(R.id.shellProgressingItem);
            store_image = (itemView).findViewById(R.id.store_image);
            store_phone = (itemView).findViewById(R.id.store_phone);
            showDetails = (itemView).findViewById(R.id.show_details);
            callStore = (itemView).findViewById(R.id.call_store);
            firstState = (itemView).findViewById(R.id.firstState);
            secondState = (itemView).findViewById(R.id.secondState);
            thirdState = (itemView).findViewById(R.id.thirdState);
            firstLine = (itemView).findViewById(R.id.firstLine);
            secondLine = (itemView).findViewById(R.id.secondLine);
            thirdLine = (itemView).findViewById(R.id.thridLine);
            payComplete = (itemView).findViewById(R.id.payComplete);
            accept = (itemView).findViewById(R.id.accpept);
            making = (itemView).findViewById(R.id.making);
            takeout = (itemView).findViewById(R.id.takeout);



            makeStatusData();


            makeRequest( mData.get(mData.size()-po-1).getStore_image(),context,store_image);

            callStore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("asdf", store_phone.getText().toString());
                    Log.e("asdff", "click");
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + store_phone.getText().toString()));
                    context.startActivity(intent);
                }
            });
//            }
        }
        private void makeStatusData(){
            Status status = new Status(firstState,firstLine,payComplete);
            statuses.add(status);
            status = new Status(secondState,secondLine,accept);
            statuses.add(status);
            status = new Status(secondState,secondLine,making);
            statuses.add(status);
        }

        private void chageViewColor(int num) {
            for (int i = 0 ;i<num;i++){
                statuses.get(i).changeColor();
                if (i == num - 1){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        statuses.get(i).state.setTextColor(context.getColor(R.color.black));
                    }
                }
            }
        }

    }
    public static void makeRequest(String imageName, Context context, final ImageView image) {
        UrlMaker urlMaker = new UrlMaker();
        String url = new UrlMaker().UrlMake("ImageStore.do?image_name=" + imageName);
        Log.d(TAG,url);
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        ImageRequest request = new ImageRequest(url,
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


    private class Status {
        ImageView imageView;
        View line;
        TextView state;

        public Status(ImageView imageView, View line,TextView state) {
            this.imageView = imageView;
            this.line = line;
            this.state = state;
        }
        private void changeColor() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageView.setBackground(context.getDrawable(R.drawable.radio_on));

            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                line.setBackgroundColor(context.getColor(R.color.main));
            }
        }
    }
}
