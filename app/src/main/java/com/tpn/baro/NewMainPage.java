package com.tpn.baro;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;
import com.tpn.baro.Adapter.AdvertiseAdapter;
import com.tpn.baro.Adapter.StoreListAdapter;
import com.tpn.baro.Database.SessionManager;
import com.tpn.baro.Dialogs.AppStartAdDialog;
import com.tpn.baro.Dialogs.SearchDialog;
import com.tpn.baro.JsonParsingHelper.EventHelperClass;
import com.tpn.baro.JsonParsingHelper.ListStoreParsing;
import com.tpn.baro.Url.UrlMaker;
import com.tpn.baro.helperClass.BaroUtil;
import com.tpn.baro.helperClass.LowSensitiveSwipeRefreshLayout;
import com.tpn.baro.helperClass.MyGPSListener;
import com.tpn.baro.helperClass.ProgressApplication;
import com.tpn.baro.helperClass.ViewPagerCustomDuration;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import maes.tech.intentanim.CustomIntent;

public class NewMainPage extends AppCompatActivity implements StoreListAdapter.OnListItemSelectedInterface, AutoPermissionsListener, ActivityCompat.OnRequestPermissionsResultCallback  {
    Gson gson;
    SessionManager userSession;
    HashMap userData = new HashMap<>();

    private Intent serviceIntent;
    private boolean firstFlag = false;
    int getUnReadAlertCount = 0;
    private String userToken;

    LatLng latLng;
    MyGPSListener myGPSListener;

    ViewPagerCustomDuration viewPager;
    int currentPos;
    AdvertiseAdapter advertiseAdapter;
    ProgressApplication progressApplication;
    EventHelperClass eventHelperClass;

    TextView mAddress;
    TextView eventCountSet;
    TextView main_timer;

    ImageView mMapButton;
    ImageView call_search;
    ImageView alert;

    LowSensitiveSwipeRefreshLayout refreshLayout;

    RecyclerView allStoreList;
    StoreListAdapter storeListAdapter;
    ListStoreParsing listStoreParsing;

    public static boolean onPause = false;
    /////////
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BaroUtil.setStatusBarColor(NewMainPage.this, this.toString());
        }
        onPause = false;
        setContentView(R.layout.activity_new_main_page);

        progressApplication = new ProgressApplication();
        progressApplication.progressON(this);

        userSession = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        userData = userSession.getUsersDetailFromSession();

        gson = new GsonBuilder().create();

        viewPager = findViewById(R.id.info_image);
        viewPager.setNestedScrollingEnabled(false);
        main_timer = findViewById(R.id.main_timer);

        new BaroUtil().fifteenTimer(main_timer, this);
        eventCountSet = findViewById(R.id.event_count);

        alert = findViewById(R.id.alert);

        AppStartAdDialog appStartAdDialog = new AppStartAdDialog(NewMainPage.this);
        appStartAdDialog.callFunction();

        mAddress = findViewById(R.id.address);
        mMapButton = findViewById(R.id.map_button);
        ///////// 태영
        call_search = findViewById(R.id.search_dialog);
        refreshLayout = findViewById(R.id.refreshLayout);
        allStoreList = findViewById(R.id.all_store_list_recycler_view);
        allStoreList.setNestedScrollingEnabled(false);
        makeRequestForEventThread();
        makeRequestForAlerts();

        myGPSListener = new MyGPSListener(this);
        latLng = myGPSListener.startLocationService(mAddress);
        makeRequestForAllStoreList(setHashDataForStoreList());

//        if(!BaroUtil.checkGPS(this)) {
//            makeRequestUltraStore(setHashMapData());
//            makeRequestNewStore(setHashMapData());
//        }


        alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!BaroUtil.loginCheck(NewMainPage.this)) {
                    return;
                }
                startActivity(new Intent(NewMainPage.this, Alerts.class));
            }
        });
        mAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewMyMap.class);
                startActivity(intent);
                CustomIntent.customType(NewMainPage.this,"right-to-left");
            }
        });
        mMapButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewMainPage.this, NewMyMap.class);
                startActivity(intent);
                CustomIntent.customType(NewMainPage.this,"right-to-left");
            }
        });
        refreshLayout.setDistanceToTriggerSync(150);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                latLng = myGPSListener.startLocationService(mAddress);
                refreshLayout.setRefreshing(false);
            }
        });
//        refreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh(SwipyRefreshLayoutDirection direction) {
//
//            }
//        });
        call_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchDialog searchDialog = new SearchDialog(NewMainPage.this);
                searchDialog.callFunction();
            }
        });
        progressApplication.progressOFF();

    }

    @Override
    protected void onRestart() {
        onPause = false;
        new BaroUtil().fifteenTimer(main_timer, this);
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        userSession = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        Log.e("userSession", userSession.toString());
        userData = userSession.getUsersDetailFromSession();
        if(userData.get(SessionManager.KEY_PHONENUMBER) == null) {
            alert.setBackground(getResources().getDrawable(R.drawable.alert_off));
        }else {
            makeRequestForAlerts();
        }
        if (!BaroUtil.checkGPS(this)) {

        } else {

        }
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    return;
                }
                final String temp_token = task.getResult().getToken();
                String phone = String.valueOf(userData.get(SessionManager.KEY_PHONENUMBER));
                if (!temp_token.equals(userToken) || phone !=null){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HashMap<String,String> data = new HashMap<>();
                            data.put("phone",""+userData.get(SessionManager.KEY_PHONENUMBER));
                            data.put("device_token",temp_token);
                            String url = new UrlMaker().UrlMake("UpdateToken.do");
                            RequestQueue requestQueue = Volley.newRequestQueue(NewMainPage.this);
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data),
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("UPDATEDEVICETOKEN",error.toString());
                                }
                            });
                            requestQueue.add(jsonObjectRequest);
                        }
                    }).start();
                }else{

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceIntent!=null) {
            stopService(serviceIntent);
            serviceIntent = null;
        }
    }

    @Override
    protected void onPause() {
        onPause = true;
        super.onPause();
    }
    private void makeRequestForAlerts() {
        UrlMaker urlMaker = new UrlMaker();
        final String url = urlMaker.UrlMake("GetNewAlertCount.do?phone=" + userData.get(SessionManager.KEY_PHONENUMBER));
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(NewMainPage.this);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parsing(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueue.add(stringRequest);
            }
        });
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private void parsing(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean("result")) {
                getUnReadAlertCount = jsonObject.getInt("count");
            }
        }catch (JSONException e) {

        }
        if(getUnReadAlertCount == 0) {
            alert.setBackground(getResources().getDrawable(R.drawable.alert_off));

        }
        else if (getUnReadAlertCount > 0){
            alert.setBackground(getResources().getDrawable(R.drawable.alert_on));
        }
    }
    private void startLocation() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("설정");
        builder.setCancelable(true);
        builder.setMessage("어플을 사용하기위해선 위치서비스를 켜주세요");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int i) {
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
        //Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                dialog.dismiss();
            }
        });
    }

    private void setEventCountSet(int position) {
        eventCountSet.setText((position % eventHelperClass.event.size() + 1)+"  /  "+eventHelperClass.event.size());
    }
    public HashMap setHashMapData() {
        HashMap<String, String> data = new HashMap<>();
        data.put("latitude",latLng.latitude+"");
        data.put("longitude",latLng.longitude+"");
        return data;
    }

    public void makeRequestForEventThread() {
        final String url = new UrlMaker().UrlMake("EventFindAll.do");
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(NewMainPage.this);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        eventParsing(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueue.add(stringRequest);
            }
        }).start();
    }

    private void setAdvertiseAdapter() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentPos = position;
            }
            @Override
            public void onPageSelected(int position) {
                setEventCountSet(position);
                currentPos = position;
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    private void eventParsing(String response) {
        Gson gson = new Gson();
        eventHelperClass = gson.fromJson(response, EventHelperClass.class);
        LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.design_advertise, null, false);
        ImageView imageView = view.findViewById(R.id.slider_image);
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        if(eventHelperClass == null || eventHelperClass.event == null || eventHelperClass.event.size() == 0 ) {
            return;
        }
        for (int i = 0;i<eventHelperClass.event.size();i++){
            makeRequestForgetImage(eventHelperClass.event.get(i).event_image,imageView,NewMainPage.this,bitmaps,eventHelperClass.event.size());
        }

    }
    public void makeRequestForgetImage(String type_image, final ImageView imageView, Context context, final ArrayList<Bitmap> bitmaps, final int max) {
        String lastUrl = "ImageEvent.do?image_name=";
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake(lastUrl);
        StringBuilder urlBuilder = new StringBuilder()
                .append(url)
                .append(type_image);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        ImageRequest request = new ImageRequest(urlBuilder.toString(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        bitmaps.add(response);
                        if (max == bitmaps.size()){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    advertiseAdapter = new AdvertiseAdapter(NewMainPage.this, eventHelperClass,bitmaps);
                                    viewPager.setAdapter(advertiseAdapter);
                                    viewPager.setCurrentItem(eventHelperClass.event.size());
                                    viewPager.setOffscreenPageLimit(5);
                                    viewPager.setScrollDurationFactor(3);
                                    setEventCountSet(0);

                                    setAdvertiseAdapter();

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
                                                        viewPager.setCurrentItem(((currentPos) + 1 % bitmaps.size()),true);
                                                    }
                                                });
                                            }

                                        }
                                    }).start();
                                }
                            });
                        }
                    }
                }, imageView.getWidth(), imageView.getHeight(), ImageView.ScaleType.CENTER, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error", "error");
                    }
                });
        requestQueue.add(request);
    }
    public HashMap setHashDataForStoreList() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("latitude",latLng.latitude);
        data.put("longitude",latLng.longitude);

        return data;
    }
    public void makeRequestForAllStoreList(HashMap data) {
        String lastUrl = "StoreFindAll.do";
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake(lastUrl);
        RequestQueue requestQueue = Volley.newRequestQueue(NewMainPage.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        jsonParsingStoreList(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("storeList", "error");
                    }
                });
        requestQueue.add(request);
    }

    private void jsonParsingStoreList(String toString) {
        listStoreParsing = new Gson().fromJson(toString, ListStoreParsing.class);
        allStoreList.setLayoutManager(new LinearLayoutManager(this));
        storeListAdapter = new StoreListAdapter(listStoreParsing, this, this);
        allStoreList.setAdapter(storeListAdapter);
    }
    @Override
    public void onItemSelected(View v, int position) {
        Intent intent = new Intent(NewMainPage.this, StoreInfoReNewer.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        Log.e("id ", listStoreParsing.store.get(position).getStore_id()+"");
        if(listStoreParsing.store.get(position) == null ||listStoreParsing.store.get(position).getStore_id() == 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(NewMainPage.this, "해당 가게가 존재하지 않습니다 재접속 해주세요", Toast.LENGTH_LONG);
                }
            });
        }
        intent.putExtra("store_id", listStoreParsing.store.get(position).getStore_id()+"");
        //intent.putExtra("discount_rate", Integer.parseInt(listStoreViewHolder.discountRate.getText().toString().substring(1, listStoreViewHolder.discountRate.getText().toString().lastIndexOf("%"))));
        intent.putExtra("discount_rate", listStoreParsing.store.get(position).getDiscount_rate());
        startActivity(intent);
        CustomIntent.customType(this,"left-to-right");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        finishAndRemoveTask();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }
    @Override
    public void onDenied(int i, @NotNull String[] strings) {
        firstFlag = true;
    }

    @Override
    public void onGranted(int i, @NotNull String[] strings) {

    }
}