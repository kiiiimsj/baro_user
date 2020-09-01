package com.example.wantchu.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.wantchu.R;

public class AdvertiseAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public AdvertiseAdapter(Context context){this.context = context;}

    int images[] = {
            R.drawable.categories_restaurant_background,
            R.drawable.categories_hospitals_background,
            R.drawable.categories_car_background,
            R.drawable.categories_rent_car_icon
    };


    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.advertise_design, container, false);

        ImageView imageView = view.findViewById(R.id.slider_image);
        imageView.setImageResource(images[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
}

