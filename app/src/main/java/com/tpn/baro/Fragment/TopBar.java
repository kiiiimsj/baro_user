package com.tpn.baro.Fragment;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tpn.baro.OrderHistory;
import com.tpn.baro.R;
import com.tpn.baro.Url.UrlMaker;
import com.tpn.baro.helperClass.BaroUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.StringTokenizer;

public class TopBar extends Fragment /*implements BaroUtil.ReloadActivity*/ {
    View rootView;

    TextView title;
    ImageView backButton;
//    Button button;
    ImageView etcImage;
    TextView timer;
//    TextView discountRate;
    RelativeLayout timerLayout;

    Activity activity;

    public int storeId;
    public int discountRateInt;

//    @Override
//    public void reload() {
//
//    }

    public interface OnBackPressedInParentActivity {
        void onBack();
    }

    public interface ClickButton {
        void clickButton();
    }

    public interface ClickImage {
        void clickImage();
    }


    OnBackPressedInParentActivity mListener;
    ClickButton clickButtonListener;
    ClickImage clickImageListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.mListener = (OnBackPressedInParentActivity) getActivity();
        } catch (ClassCastException e) {

        }
        try {
            this.clickButtonListener = (ClickButton) getActivity();
        } catch (ClassCastException e) {

        }
        try {
            this.clickImageListener = (ClickImage) getActivity();
        } catch (ClassCastException e) {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        rootView = inflater.inflate(R.layout.top_bar, container, false);
        title = rootView.findViewById(R.id.title);
        backButton = rootView.findViewById(R.id.back_pressed);
//        button = rootView.findViewById(R.id.when_has_button);
        etcImage = rootView.findViewById(R.id.when_has_image);
        timer = rootView.findViewById(R.id.fifteenTimer);
        timerLayout = rootView.findViewById(R.id.main_timer_layout);
//        discountRate = rootView.findViewById(R.id.discount_rate);

        backButton.setVisibility(View.GONE);
        timer.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
//        button.setVisibility(View.GONE);
        etcImage.setVisibility(View.GONE);
//        discountRate.setVisibility(View.GONE);
        timerLayout.setVisibility(View.GONE);

        switch (BaroUtil.getTokenActivityName(getActivity().toString())) {
            case "Register1":
            case "Register2":
            case "ChangePass2":
            case "ChangePass1Logging":
            case "FindPass1":
            case "VerifyOTP":
            case "ChangeEmail":
                rootView.setBackgroundColor(getResources().getColor(R.color.main));
                backButton.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);

                backButton.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
                backButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onBack();
                    }
                });
                break;
            case "Events":
            case "Terms":
            case "Alert":
                backButton.setVisibility(View.VISIBLE);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onBack();
                    }
                });
                break;
            case "ListStorePage":
            case "OrderDetails":
                backButton.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);
                timer.setVisibility(View.VISIBLE);
                timerLayout.setVisibility(View.VISIBLE);

                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onBack();
                    }
                });
                break;

            case "SideMyCoupon":
                backButton.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onBack();
                    }
                });
                title.setText("내 쿠폰 리스트");
                break;
            case "Notice":
                backButton.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onBack();
                    }
                });
                title.setText("공지사항");
                break;
            case "Alerts":
                backButton.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);

                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onBack();
                    }
                });
                title.setText("알 림");
                break;
            case "MyMap":
                backButton.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);
//                button.setVisibility(View.VISIBLE);

                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onBack();
                    }
                });
//                button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        clickButtonListener.clickButton();
//                    }
//                });
                title.setText("주변 가게");
//                button.setText("위치 설정");
                break;
            case "NewMyMap":
                backButton.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);

                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onBack();
                    }
                });
                title.setText("내 주변 가게");
                break;
            case "StoreInfoReNewer":
                backButton.setVisibility(View.VISIBLE);
                etcImage.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);
                timer.setVisibility(View.VISIBLE);
                timerLayout.setVisibility(View.VISIBLE);

                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onBack();
                    }
                });
                etcImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickImageListener.clickImage();
                    }
                });
                break;
            case "OrderProgressing":
                title.setVisibility(View.VISIBLE);
                etcImage.setVisibility(View.VISIBLE);

                title.setText("주문현황");
                etcImage.setBackgroundResource(R.drawable.icon_refresh);
                etcImage.setBackgroundTintList(ColorStateList.valueOf(getActivity().getResources().getColor(R.color.main)));
                etcImage.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
                etcImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Animation rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_rotate_360);
                        etcImage.startAnimation(rotate);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                clickButtonListener.clickButton();
                            }
                        }, 1000);
                    }
                });
                break;
            case "OrderHistory":
                title.setVisibility(View.VISIBLE);
                title.setText("주문내역");
                break;
            case "MyPage":
                title.setVisibility(View.VISIBLE);
                title.setText("마이페이지");
                break;
            case "Basket":
                backButton.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);
                timer.setVisibility(View.VISIBLE);
                timerLayout.setVisibility(View.VISIBLE);

                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onBack();
                    }
                });
                title.setText("장바구니");
                break;
            case "ListStoreFavoritePage":
                title.setVisibility(View.VISIBLE);
                title.setText("찜한가게");
                break;
            case "MyInfoView":
                backButton.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);

                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onBack();
                    }
                });
                title.setText("내 정보 수정");
                break;
            case "NewMainPage":
                //no top bar activity
                break;
            case "ChangeEmail2":
                //no top bar activity
                break;
            case "ChangePass3":
                //no top bar activity
                break;
        }
        return rootView;
    }

    @Override
    public void onResume() {
        new BaroUtil().fifteenTimer(timer, activity);
//        makeRequestForDiscountRate(storeId);
        super.onResume();
    }
    public void setTitleStringWhereUsedEventsAndListStore(String title_name) {
        title.setText(title_name);
        if (title.getText().toString().length() > 6) {
            title.setTextSize(20);
        }
    }

    public void setEtcImageWhereUsedStoreInfo(int image) {
        etcImage.setImageResource(image);
    }
    public int getDiscountRate() {
        return discountRateInt;
    }
//    public void setDiscountTextView(String result) {
//        try {
//            JSONObject jsonObject = new JSONObject(result);
//            if(jsonObject.getBoolean("result")) {
//                discountRate.setVisibility(View.VISIBLE);
//                discountRateInt = jsonObject.getInt("discount_rate");
//                discountRate.setText("-"+discountRateInt+"%");
//            }else {
//                discountRate.setVisibility(View.GONE);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//    public void setDiscountTextView(int discountRateInt) {
//        if(discountRateInt == 0 ){
//            discountRate.setVisibility(View.GONE);
//        }else {
//            discountRate.setVisibility(View.VISIBLE);
//            discountRate.setText(discountRateInt+"%");
//        }
//    }
//    public void makeRequestForDiscountRate(int storeId) {
//        //GetStoreDiscount.do?store_id=
//        UrlMaker urlMaker = new UrlMaker();
//        String lastUrl = "GetStoreDiscount.do?store_id="+storeId;
//        String url = urlMaker.UrlMake(lastUrl);
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//
//        StringRequest request = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(final String response) {
//                        Log.e("response", response);
//                        setDiscountTextView(response);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                });
//        requestQueue.add(request);
//    }
}
