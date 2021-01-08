package com.example.wantchu.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.wantchu.Basket;
import com.example.wantchu.NewMyMap;
import com.example.wantchu.R;

import java.util.StringTokenizer;

import maes.tech.intentanim.CustomIntent;

import static android.content.Context.MODE_PRIVATE;

public class OrderListCart extends Fragment {
    ViewGroup rootView;
    TextView orderCount;
    ImageView image;
    int count = 0;

    Context context;
    SharedPreferences shf;

    public OrderListCart() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.e("OrderListCart", "onResume");
        shf = context.getSharedPreferences("basketList", Context.MODE_PRIVATE);
        count = shf.getInt("orderCnt", 0);
        setText(count);
    }
    private void setText(int count) {
        if(orderCount == null ) {
            return;
        }
        if(count == 0 ) {
            rootView.setVisibility(ViewGroup.INVISIBLE);
            return;
        }
        //orderCount.setText(Integer.toString(1));
        orderCount.setText(Integer.toString(count));
        rootView.setVisibility(ViewGroup.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        shf = context.getSharedPreferences("basketList", MODE_PRIVATE);
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_order_list_cart, container, false);
        count = shf.getInt("orderCnt", 0);

        if(count == 0) {
            rootView.setVisibility(ViewGroup.INVISIBLE);
        }
        else {
            rootView.setVisibility(ViewGroup.VISIBLE);
        }

        orderCount =rootView.findViewById(R.id.get_cart_session);
        image = rootView.findViewById(R.id.cart);
        orderCount.setText(Integer.toString(count));
        image.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Basket.class));
                CustomIntent.customType(context,"left-to-right");
            }
        });
        return rootView;
    }
}