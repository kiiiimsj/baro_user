package com.tpn.baro.Fragment;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.tpn.baro.R;

import java.util.StringTokenizer;

public class TopBar extends Fragment {
    View rootView;

    TextView title;
    ImageView backButton;
    Button button;
    ImageView etcImage;

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
        rootView = inflater.inflate(R.layout.top_bar, container, false);
        title = rootView.findViewById(R.id.title);
        backButton = rootView.findViewById(R.id.back_pressed);
        button = rootView.findViewById(R.id.when_has_button);
        etcImage = rootView.findViewById(R.id.when_has_image);
        switch (getTokenActivityName(getActivity().toString())) {
            case "Register1":
            case "Register2":
            case "ChangePass2":
            case "ChangePass1Logging":
            case "FindPass1":
            case "VerifyOTP":
            case "ChangeEmail":
                rootView.setBackgroundColor(getResources().getColor(R.color.main));
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onBack();
                    }
                });
                title.setVisibility(View.INVISIBLE);
                button.setVisibility(View.INVISIBLE);
                etcImage.setVisibility(View.INVISIBLE);
                break;
            case "SideMyCoupon":
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onBack();
                    }
                });
                title.setText("내 쿠폰 리스트");
                button.setVisibility(View.INVISIBLE);
                etcImage.setVisibility(View.INVISIBLE);
                break;
            case "Notice":
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onBack();
                    }
                });
                title.setText("공지사항");
                button.setVisibility(View.INVISIBLE);
                etcImage.setVisibility(View.INVISIBLE);
                break;
            case "Events":
            case "ListStorePage":
            case "OrderDetails":
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onBack();
                    }
                });
                button.setVisibility(View.INVISIBLE);
                etcImage.setVisibility(View.INVISIBLE);
                break;
            case "Alert":
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onBack();
                    }
                });
                title.setVisibility(View.INVISIBLE);
                button.setVisibility(View.INVISIBLE);
                etcImage.setVisibility(View.INVISIBLE);
                break;
            case "Alerts":
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onBack();
                    }
                });
                title.setText("알 림");
                button.setVisibility(View.INVISIBLE);
                etcImage.setVisibility(View.INVISIBLE);
                break;
            case "MyMap":
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onBack();
                    }
                });
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickButtonListener.clickButton();
                    }
                });
                title.setText("주변 가게");
                button.setText("위치 설정");
                etcImage.setVisibility(View.INVISIBLE);
                break;
            case "NewMyMap":
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onBack();
                    }
                });
                title.setText("내 주변 가게");
                button.setVisibility(View.INVISIBLE);
                etcImage.setVisibility(View.INVISIBLE);
                break;
            case "StoreInfoReNewer":
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
                button.setVisibility(View.INVISIBLE);
                break;
            case "OrderProgressing":
                title.setText("주문현황");
                etcImage.setVisibility(View.VISIBLE);
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
                backButton.setVisibility(View.INVISIBLE);
                button.setVisibility(View.INVISIBLE);
                break;
            case "OrderHistory":
                title.setText("주문내역");
                button.setVisibility(View.INVISIBLE);
                backButton.setVisibility(View.INVISIBLE);
                etcImage.setVisibility(View.INVISIBLE);
                break;
            case "MyPage":
                title.setText("마이페이지");
                backButton.setVisibility(View.INVISIBLE);
                button.setVisibility(View.INVISIBLE);
                etcImage.setVisibility(View.INVISIBLE);
                break;
            case "Basket":
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onBack();
                    }
                });
                title.setText("장바구니");
                button.setVisibility(View.INVISIBLE);
                etcImage.setVisibility(View.INVISIBLE);
                break;
            case "ListStoreFavoritePage":
                title.setText("찜한가게");
                backButton.setVisibility(View.INVISIBLE);
                button.setVisibility(View.INVISIBLE);
                etcImage.setVisibility(View.INVISIBLE);
                break;
            case "TermsOfUse":
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onBack();
                    }
                });
                title.setText("이용약관");
                button.setVisibility(View.INVISIBLE);
                etcImage.setVisibility(View.INVISIBLE);
                break;
            case "MyInfoView":
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onBack();
                    }
                });
                title.setText("내 정보 수정");
                button.setVisibility(View.INVISIBLE);
                etcImage.setVisibility(View.INVISIBLE);
                break;
            case "MainPage":
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

    public String getTokenActivityName(String activityName) {
        StringTokenizer stringTokenizer = new StringTokenizer(activityName, ".");
        String getName = "";
        while(stringTokenizer.hasMoreTokens()) {
            getName = stringTokenizer.nextToken();
        }

        return new StringTokenizer(getName, "@").nextToken();
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
}