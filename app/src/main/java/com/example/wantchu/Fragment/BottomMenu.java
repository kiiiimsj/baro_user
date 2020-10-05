package com.example.wantchu.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
    BottomNavigationView realBottom;
    public BottomMenu() {}


    public static BottomMenu newInstance(String param1, String param2) {
        BottomMenu fragment = new BottomMenu();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

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
        return rootView;
    }
    public void clickBottomMenu() {
        realBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottomIconHome:
                        item.
                        if (getTokenActivityName(getActivity().toString()).equals("MainPage")) {
                            break;
                        }
                        startActivity(new Intent(getActivity(), MainPage.class));
                        break;
                    case R.id.bottomIconClock:
                        if (getTokenActivityName(getActivity().toString()).equals("OrderProgressing")) {
                            break;
                        }
                        startActivity(new Intent(getActivity(), OrderProgressing.class));
                        break;
                    case R.id.bottomIconMyStore:
                        if (getTokenActivityName(getActivity().toString()).equals("ListStoreFavoritePage")) {
                            break;
                        }
                        Intent intent = new Intent(getActivity(), ListStoreFavoritePage.class);
                        startActivity(intent);
                        break;
                    case R.id.bottomIconOrderList:
                        if (getTokenActivityName(getActivity().toString()).equals("OrderHistory")) {
                            break;
                        }
                        startActivity(new Intent(getActivity(), OrderHistory.class));
                        break;
                    case R.id.bottomIconMyPage:
                        if (getTokenActivityName(getActivity().toString()).equals("MyPage")) {
                            break;
                        }
                        startActivity(new Intent(getActivity(), MyPage.class));
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
