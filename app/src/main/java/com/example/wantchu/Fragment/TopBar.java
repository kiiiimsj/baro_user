package com.example.wantchu.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.example.wantchu.R;

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
            case "FindPass1":
            case "VerifyOTP":
            case "ChangePass2":
            case "ChangeEmail":
            case "ChangePass1Logging":
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
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onBack();
                    }
                });
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
                title.setText("소 식");
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
                button.setVisibility(View.INVISIBLE);
                backButton.setVisibility(View.INVISIBLE);
                etcImage.setVisibility(View.INVISIBLE);
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
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onBack();
                    }
                });
                title.setText("찜한 가게");
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
            case "OrderDetail":

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
        String m = activityName.substring(20);
        StringTokenizer stringTokenizer = new StringTokenizer(m, "@");
        String getName = stringTokenizer.nextToken();

        return getName;
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
