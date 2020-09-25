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
        this.mListener = (OnBackPressedInParentActivity)getActivity();
        try {
            this.clickButtonListener = (ClickButton)getActivity();
        }
        catch (ClassCastException e) {

        }
        try {
            this.clickImageListener = (ClickImage)getActivity();
        }
        catch(ClassCastException e) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.top_bar ,container, false);
        title = rootView.findViewById(R.id.title);
        backButton = rootView.findViewById(R.id.back_pressed);
        button = rootView.findViewById(R.id.when_has_button);
        etcImage = rootView.findViewById(R.id.when_has_image);
        switch (getTokenActivityName(getActivity().toString())) {
            case "Register1" :
            case "Register2" :
            case "FindPass1" :
            case "VerifyOTP" :
            case "ChangePass2" :
            case "ChangeEmail" :
            case "ChangePass1Logging" :
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
            case "SideMyCoupon" :
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
            case "Notice" :
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
            case "Events" :
            case "ListStorePage" :
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onBack();
                    }
                });
                button.setVisibility(View.INVISIBLE);
                etcImage.setVisibility(View.INVISIBLE);
                break;
            case "Alerts" :
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
            case "MyMap" :
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
            case "StoreInfoReNewer" :
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
            case "OrderDetail" :
                break;
            case "Basket" :
                break;


            case "ListStoreFavoritePage" :
                //no top bar activity
                break;
            case "OrderHistory" :
                //no top bar activity
                break;
            case "OrderProgressing" :
                //no top bar activity
                break;
            case "MyPage":
                //no top bar activity
                break;
            case "MainPage" :
                //no top bar activity
                break;
            case "ChangeEmail2" :
                //no top bar activity
                break;
            case "ChangePass3" :
                //no top bar activity
                break;

        }
        return rootView;
    }
    public String getTokenActivityName(String activityName) {
        String m = activityName.substring(20);
        StringTokenizer stringTokenizer = new StringTokenizer(m,"@");
        String getName = stringTokenizer.nextToken();

        return getName;
    }

    public void setTitleStringWhereUsedEventsAndListStore(String title_name) {
        title.setText(title_name);
        if(title.getText().toString().length() > 6) {
            title.setTextSize(20);
        }
    }
    public void setEtcImageWhereUsedStoreInfo(int image) {
        etcImage.setImageResource(image);
    }
}
