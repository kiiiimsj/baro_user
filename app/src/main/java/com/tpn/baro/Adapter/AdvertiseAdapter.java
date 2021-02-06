package com.tpn.baro.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.tpn.baro.Events;
import com.tpn.baro.JsonParsingHelper.EventHelperClass;
import com.tpn.baro.R;
import com.tpn.baro.Url.UrlMaker;

import java.util.ArrayList;

public class AdvertiseAdapter extends PagerAdapter {

    private final ArrayList<Bitmap> bitmaps;
    Context context;
    LayoutInflater layoutInflater;
    public EventHelperClass eventHelperClass;
    int posi;
    public AdvertiseAdapter(Context context, EventHelperClass eventHelperClass, ArrayList<Bitmap> bitmaps){
        this.context = context;
        this.eventHelperClass = eventHelperClass;
        this.bitmaps = bitmaps;
    }

    @Override
    public int getCount() {
        return eventHelperClass.event.size() * 1000;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        posi = position;
        final int realIndex = position % eventHelperClass.event.size();

        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.advertise_design, container, false);
        ImageView imageView = view.findViewById(R.id.slider_image);
        EventHelperClass.EventHelperParsingClass eventHelperParsingClass =eventHelperClass.event.get(realIndex);
        if(!eventHelperParsingClass.event_image.equals("")) {
            imageView.setImageBitmap(bitmaps.get(realIndex));
        }
        container.addView(view);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EventHelperClass.EventHelperParsingClass eventHelperParsingClass =eventHelperClass.event.get(realIndex);
//                Intent intent = new Intent(context, Events.class);
//                intent.putExtra("event_id", eventHelperParsingClass.event_id);
//                context.startActivity(intent);
//            }
//        });

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
    public void makeRequestForgetImage(String type_image, final ImageView imageView, Context context ) {
        String lastUrl = "ImageEvent.do?image_name=";
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake(lastUrl);
        StringBuilder urlBuilder = new StringBuilder()
                .append(url)
                .append(type_image);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        ImageRequest request = new ImageRequest(urlBuilder.toString(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                    }
                }, imageView.getWidth(), imageView.getHeight(), ImageView.ScaleType.FIT_XY, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error", "error");
                    }
                });
        requestQueue.add(request);
    }
    public void setClickListener() {

    }
}

