package com.example.wantchu.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.wantchu.ListStoreFavoritePage;
import com.example.wantchu.MainPage;
import com.example.wantchu.MyPage;
import com.example.wantchu.OrderHistory;
import com.example.wantchu.OrderProgressing;
import com.example.wantchu.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.StringTokenizer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BottomMenu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BottomMenu extends Fragment {
    Bundle saveButton;
    BottomNavigationView realBottom;
    public BottomMenu() {}


    public static BottomMenu newInstance(String param1, String param2) {
        BottomMenu fragment = new BottomMenu();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        clickBottomMenu();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveButton = savedInstanceState;
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
            case "MainPage" :
                realBottom.getMenu().getItem(0).setIcon(R.drawable.bottom_home_on);
                break;
            case "OrderProgressing" :
                realBottom.getMenu().getItem(1).setIcon(R.drawable.bottom_order_status_on);
                break;
            case "ListStoreFavoritePage" :
                realBottom.getMenu().getItem(2).setIcon(R.drawable.bottom_favorite_on);
                break;
            case "OrderHistory" :
                realBottom.getMenu().getItem(3).setIcon(R.drawable.bottom_history_on);
                break;
            case "MyPage" :
                realBottom.getMenu().getItem(4).setIcon(R.drawable.bottom_my_page_on);
                break;
        }
    }

    public void clickBottomMenu() {
        realBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottomIconHome:
                        if (getTokenActivityName(getActivity().toString()).equals("MainPage")) {
                            break;
                        }
                        Intent intent = new Intent(getActivity(), MainPage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        break;
                    case R.id.bottomIconClock:
                        if (getTokenActivityName(getActivity().toString()).equals("OrderProgressing")) {
                            break;
                        }
                        Intent intent2 = new Intent(getActivity(), OrderProgressing.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent2);
                        break;
                    case R.id.bottomIconMyStore:
                        if (getTokenActivityName(getActivity().toString()).equals("ListStoreFavoritePage")) {
                            break;
                        }
                        Intent intent3 = new Intent(getActivity(), ListStoreFavoritePage.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent3);
                        break;
                    case R.id.bottomIconOrderList:
                        if (getTokenActivityName(getActivity().toString()).equals("OrderHistory")) {
                            break;
                        }
                        Intent intent4 = new Intent(getActivity(), OrderHistory.class);
                        intent4.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent4);
                        break;
                    case R.id.bottomIconMyPage:
                        if (getTokenActivityName(getActivity().toString()).equals("MyPage")) {
                            break;
                        }
                        Intent intent5 = new Intent(getActivity(), MyPage.class);
                        intent5.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent5);
                        break;
                }
                return false;
            }
        });
    }
    public String getTokenActivityName(String activityName) {
        String m = activityName.substring(20);
        StringTokenizer stringTokenizer = new StringTokenizer(m,"@");
        String getName = stringTokenizer.nextToken();

        return getName;
    }
}
