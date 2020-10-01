package com.example.wantchu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Adapter.AdvertiseAdapter;
import com.example.wantchu.Adapter.TypeAdapter;
import com.example.wantchu.AdapterHelper.TypeHelperClass;
import com.example.wantchu.Database.StoreSessionManager;
import com.example.wantchu.Dialogs.SearchDialog;
import com.example.wantchu.JsonParsingHelper.EventHelperClass;
import com.example.wantchu.JsonParsingHelper.TypeListParsing;
import com.example.wantchu.JsonParsingHelper.TypeParsing;
import com.example.wantchu.Url.UrlMaker;
import com.example.wantchu.helperClass.myGPSListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, TypeAdapter.OnListItemLongSelectedInterface, TypeAdapter.OnListItemSelectedInterface {

    RecyclerView mRecyclerView;
    RecyclerView.Adapter adapter;
    ImageView menu, glasses;
    EditText mSearch;

    Context context;

    TextView[] dots;
    LinearLayout dotsLayout;
    ViewPager viewPager;
    int currentPos;
    AdvertiseAdapter advertiseAdapter;

    SharedPreferences sp;
    Gson gson;

    RelativeLayout recycleBack;

    StoreSessionManager storeSessionManager;
    ProgressApplication progressApplication;
    EventHelperClass eventHelperClass;
    ViewPager.OnPageChangeListener changeListener;

    RelativeLayout mapBar;
    TextView mAddress;

    LatLng latLng;
    myGPSListener myGPSListener;
    ///////// 태영
    Button call_search;
    /////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        progressApplication = new ProgressApplication();
        progressApplication.progressON(this);
        sp = getSharedPreferences("favorite", MODE_PRIVATE);
        gson = new GsonBuilder().create();
        mRecyclerView = findViewById(R.id.recyclerView);

//        glasses = findViewById(R.id.glasses);
//        mSearch = findViewById(R.id.search);
        viewPager = findViewById(R.id.info_image);

        recycleBack = findViewById(R.id.background1);
        mapBar = findViewById(R.id.map_bar);
        //Fragment 생성

        dotsLayout = findViewById(R.id.dots);
        context = this;
        mAddress = findViewById(R.id.address);
        // 왼쪽 사이드바
        storeSessionManager = new StoreSessionManager(getApplicationContext(), StoreSessionManager.STORE_SESSION);
        storeSessionManager.setIsFavorite(false);
        ///////// 태영
        call_search = findViewById(R.id.search_dialog);
        /////////
        startLocation();

        // 타입 버튼 동적으로 만드는 메소드
        makeRequest();
        myGPSListener = new myGPSListener(this);
        latLng = myGPSListener.startLocationService(mAddress);
        if(latLng == null) {
            mAddress.setText("GPS를 설정 해 주세요");
        }
        mapBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyMap.class);
                startActivity(intent);
            }
        });
        ///////// 태영
        call_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchDialog searchDialog = new SearchDialog(MainPage.this);
                searchDialog.callFunction();
            }
        });
        makeRequestForEventThread();
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);

        latLng = myGPSListener.startLocationService(mAddress);
        if(latLng == null) {
            mAddress.setText("GPS를 설정 해 주세요");
        }
    }

    private void startLocation() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("설정");
            builder.setMessage("어플을 사용하기위해선 위치서비스를 켜주세요");
            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }


    private void addDots(int position) {
        dots = new TextView[advertiseAdapter.getCount()];
        dotsLayout.removeAllViews();

        for(int i = 0; i< dots.length;i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);

            dotsLayout.addView(dots[i]);
        }
        if(dots.length > 0){
            dots[position].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }


    public void makeRequest() {
        UrlMaker urlMaker = new UrlMaker();
        String lastUrl = "TypeFindAll.do";
        String url = urlMaker.UrlMake(lastUrl);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.i("type", "request made to " + url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("type", response);
                        mRecyclerView(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("type", "error");
                    }
                });
        requestQueue.add(request);
    }


    private void mRecyclerView(String result) {
        mRecyclerView.setHasFixedSize(true);
        ArrayList<TypeHelperClass> DataList = new ArrayList<TypeHelperClass>();
        HashMap<String, TypeHelperClass> hashMap = new HashMap<>();
        hashMap.put("TypeHelperClass", new TypeHelperClass("", "", ""));
        TypeParsing typeParsing = new TypeParsing();
        jsonParsing(result, typeParsing);
        adapter = new TypeAdapter(DataList, this, this, getApplicationContext());
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        for(int i = 0; i < typeParsing.getTypeList().size();i++){
            TypeListParsing typeListParsing = typeParsing.getTypeList().get(i);
            TypeHelperClass typeHelperClass = new TypeHelperClass(typeListParsing.getTypeName(), typeListParsing.getTypeCode(), typeListParsing.getTypeImage());
            typeHelperClass.typeNames.add(typeListParsing.getTypeName());
            typeHelperClass.typeCodes.add(typeListParsing.getTypeCode());
            DataList.add(typeHelperClass);
        }
        mRecyclerView.setAdapter(adapter);
        progressApplication.progressOFF();
    }

    //왼쪽 사이드바 메뉴 클릭시
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.left_coupon:
                startActivity(new Intent(getApplicationContext(), SideMyCoupon.class));
                break;
            case R.id.left_noti:
                Intent intent = new Intent(getApplicationContext(), Notice.class);
                intent.putExtra("type", "NOTICE");
                startActivity(intent);
                break;
            case R.id.left_entry_request:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://pf.kakao.com/_bYeuK/chat")));
                break;
            case R.id.left_one_to_one:

                break;
            case R.id.left_events:
                startActivity(new Intent(getApplicationContext(), Alerts.class));
                break;
        }
        return true;
    }




    private synchronized void jsonParsing(String result, TypeParsing typeParsing){
        try {
            Boolean result2 = (Boolean) new JSONObject(result).getBoolean("result");
            typeParsing.setMessage(new JSONObject(result).getString("message"));
            JSONArray jsonArray = new JSONObject(result).getJSONArray("type");
            ArrayList<TypeListParsing> typeListParsings = new ArrayList<TypeListParsing>();
            for(int i = 0; i<jsonArray.length();i++){
                JSONObject jObject = jsonArray.getJSONObject(i);
                String type_name = jObject.optString("type_name");
                String type_code = jObject.optString("type_code");
                String type_image = jObject.optString("type_image");
                TypeListParsing typeListParsing = new TypeListParsing(type_name, type_code, type_image);
                typeListParsings.add(typeListParsing);
            }
            typeParsing.setTypeList(typeListParsings);
            Log.i("MainPage", result2.toString());
        }
        catch(JSONException e){
            e.printStackTrace();
        }

    }
    public void makeRequestForEventThread() {
        String url = new UrlMaker().UrlMake("EventFindAll.do");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("GET_EVENT_PICTURE", response);
                eventParsing(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }
    private void setAdvertiseAdapter() {
        changeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position == eventHelperClass.event.size()) {
                    position = 0;
                }
            }

            @Override
            public void onPageSelected(int position) {
                Log.i("onPageSelected", position+"");
                addDots(position);
                currentPos = position;
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        };
    }
    private void eventParsing(String response) {
        Gson gson = new Gson();
        eventHelperClass = gson.fromJson(response, EventHelperClass.class);
        advertiseAdapter = new AdvertiseAdapter(context, eventHelperClass);
        viewPager.setAdapter(advertiseAdapter);

        addDots(0);

        setAdvertiseAdapter();
        viewPager.addOnPageChangeListener(changeListener);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem((currentPos + 1)%eventHelperClass.event.size());
                        }
                    });
                }

            }
        }).start();
    }

    @Override
    public void onItemLongSelected(View v, int adapterPosition) {
    }

    @Override
    public void onItemSelected(View v, int position) {
        TypeAdapter.TypeViewHolder viewHolder = (TypeAdapter.TypeViewHolder)mRecyclerView.findViewHolderForAdapterPosition(position);
        Intent intent = new Intent(getApplicationContext(), ListStorePage.class);
        intent.putExtra("type_code", viewHolder.code.getText().toString());
        intent.putExtra("type_name", viewHolder.title.getText().toString());
        intent.putExtra("list_type", "find_type");
        startActivity(intent);
    }
}
