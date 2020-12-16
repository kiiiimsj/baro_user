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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderListCart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderListCart extends Fragment {
    ViewGroup rootView;
    TextView orderCount;
    ImageView image;
    int count = 0;

    Context context;
    SharedPreferences shf;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderListCart() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderListCart.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderListCart newInstance(String param1, String param2) {
        OrderListCart fragment = new OrderListCart();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        shf = context.getSharedPreferences("basketList", MODE_PRIVATE);
        count = shf.getInt("orderCnt", 0);
        Log.i("onAttach", count+"");
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
        Log.i("ONDESTROYVIEW", "true");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("ONCREATEVIEW", "true");
        context = getActivity();
        shf = context.getSharedPreferences("basketList", MODE_PRIVATE);
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_order_list_cart, container, false);
        count = shf.getInt("orderCnt", 0);

        Log.i("ONCREATEFRAGMENT", count+"");
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
                String actName=getActivity().toString().substring(20);
                StringTokenizer tokenizer = new StringTokenizer(actName, "@");
                Log.i("ACTIVITYNAME", tokenizer.nextToken());
                startActivity(new Intent(getActivity(), Basket.class));
                CustomIntent.customType(context,"left-to-right");
            }
        });
        return rootView;
    }
}