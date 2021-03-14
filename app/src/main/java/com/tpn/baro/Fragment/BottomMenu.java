package com.tpn.baro.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.tpn.baro.ListStoreFavoritePage;
import com.tpn.baro.NewMainPage;
import com.tpn.baro.MyPage;
import com.tpn.baro.OrderHistory;
import com.tpn.baro.OrderProgressing;
import com.tpn.baro.R;
import com.tpn.baro.helperClass.BaroUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.StringTokenizer;

import maes.tech.intentanim.CustomIntent;

public class BottomMenu extends Fragment {
    BottomNavigationView realBottom;
    public BottomMenu() {}


//    public static BottomMenu newInstance(String param1, String param2) {
//        BottomMenu fragment = new BottomMenu();
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_bottom_menu, container, false);
        realBottom = rootView.findViewById(R.id.real_bottom);
        clickBottomMenu();
        setBottomButtonColor();
        return rootView;
    }
    private void setBottomButtonColor() {
        switch (getTokenActivityName(getActivity().toString())) {
            case "NewMainPage" :
                realBottom.getMenu().getItem(0).setChecked(true);
//                realBottom.setSelectedItemId(realBottom.getMenu().getItem(0).getItemId());
                break;
            case "ListStoreFavoritePage" :
                realBottom.getMenu().getItem(1).setChecked(true);
//                realBottom.setSelectedItemId(realBottom.getMenu().getItem(1).getItemId());
                break;
            case "OrderProgressing" :
                realBottom.getMenu().getItem(2).setChecked(true);
//                realBottom.setSelectedItemId(realBottom.getMenu().getItem(2).getItemId());
                break;
            case "OrderHistory" :
                realBottom.getMenu().getItem(3).setChecked(true);
//                realBottom.setSelectedItemId(realBottom.getMenu().getItem(3).getItemId());
                break;
            case "MyPage" :
                realBottom.getMenu().getItem(4).setChecked(true);
//                realBottom.setSelectedItemId(realBottom.getMenu().getItem(4).getItemId());
                break;
            default:
                for (int i = 0; i < realBottom.getMenu().size(); i++) {
                    if(realBottom.getMenu().getItem(i).isChecked()) {
                        Log.e("default", "checked :" + i);
                        realBottom.getMenu().getItem(i).setCheckable(false);
                    }
                }
                break;
        }
    }

    public void clickBottomMenu() {
        realBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottomIconHome:
                        if (getTokenActivityName(getActivity().toString()).equals("NewMainPage")) {
                            break;
                        }
                        Intent intent = new Intent(getActivity(), NewMainPage.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        CustomIntent.customType(getActivity(),"right-to-left");
                        break;
                    case R.id.bottomIconClock:
                        if(!BaroUtil.loginCheck(getActivity())) {
                            break;
                        }
                        if (getTokenActivityName(getActivity().toString()).equals("OrderProgressing")) {
                            break;
                        }
                        Intent intent2 = new Intent(getActivity(), OrderProgressing.class);
//                        intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent2);
                        CustomIntent.customType(getActivity(),"right-to-left");
                        break;
                    case R.id.bottomIconMyStore:
                        if(!BaroUtil.loginCheck(getActivity())) {
                            break;
                        }
                        if (getTokenActivityName(getActivity().toString()).equals("ListStoreFavoritePage")) {
                            break;
                        }
                        Intent intent3 = new Intent(getActivity(), ListStoreFavoritePage.class);
//                        intent3.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent3);
                        CustomIntent.customType(getActivity(),"right-to-left");
                        break;
                    case R.id.bottomIconOrderList:
                        if(!BaroUtil.loginCheck(getActivity())) {
                            break;
                        }
                        if (getTokenActivityName(getActivity().toString()).equals("OrderHistory")) {
                            break;
                        }
                        Intent intent4 = new Intent(getActivity(), OrderHistory.class);
//                        intent4.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent4);
                        CustomIntent.customType(getActivity(),"right-to-left");
                        break;
                    case R.id.bottomIconMyPage:
                        if(!BaroUtil.loginCheck(getActivity())) {
                            break;
                        }
                        if (getTokenActivityName(getActivity().toString()).equals("MyPage")) {
                            break;
                        }
                        Intent intent5 = new Intent(getActivity(), MyPage.class);
//                        intent5.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent5);
                        CustomIntent.customType(getActivity(),"right-to-left");
                        break;
                }
                return false;
            }
        });
    }
    public String getTokenActivityName(String activityName) {
        StringTokenizer stringTokenizer = new StringTokenizer(activityName, ".");
        String getName = "";
        while(stringTokenizer.hasMoreTokens()) {
            getName = stringTokenizer.nextToken();
        }

        return new StringTokenizer(getName, "@").nextToken();
    }
}
