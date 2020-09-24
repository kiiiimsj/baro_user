package com.example.wantchu.Fragment;

import android.content.Intent;
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

import com.example.wantchu.Login;
import com.example.wantchu.R;
import com.example.wantchu.Register1;

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
    OnBackPressedInParentActivity mListener;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.mListener = (OnBackPressedInParentActivity)getActivity();
        super.onCreate(savedInstanceState);
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
            case "MainPage" :
                break;
            case "Events" :
                break;
            case "Alerts" :
                break;
            case "MyMap" :
                break;
            case "ListStorePage" :
                break;
            case "ListStoreFavoritePage" :
                break;
            case "StoreInfoReNewer" :
                break;
            case "OrderDetail" :
                break;
            case "Basket" :
                break;
            case "OrderHistory" :
                break;
            case "OrderProgressing" :
                break;
            case "OrderStatus" :
                break;
            case "MyPage" :
                break;
            case "SideMyCard" :
                break;
            case "SideMyCoupon" :
                break;
            case "Notice" :
                break;
            case "ChangeEmail" :
                break;
            case "ChangeEmail2" :
                break;
            case "ChangePass3" :
                break;
            case "ChangePass1Logging" :
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
}
