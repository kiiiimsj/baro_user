package com.tpn.baro;

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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.tpn.baro.Adapter.AdvertiseAdapter;
import com.tpn.baro.Adapter.NewStoreListAdapter;
import com.tpn.baro.Adapter.TypeAdapter;
import com.tpn.baro.Adapter.UltraStoreListAdapter;
import com.tpn.baro.AdapterHelper.TypeHelperClass;
import com.tpn.baro.Database.SessionManager;
import com.tpn.baro.Dialogs.AppStartAdDialog;
import com.tpn.baro.Dialogs.SearchDialog;
import com.tpn.baro.JsonParsingHelper.EventHelperClass;
import com.tpn.baro.JsonParsingHelper.TypeListParsing;
import com.tpn.baro.JsonParsingHelper.TypeParsing;
import com.tpn.baro.JsonParsingHelper.ViewPagersListStoreParsing;
import com.tpn.baro.Url.UrlMaker;
import com.tpn.baro.helperClass.BaroUtil;
import com.tpn.baro.helperClass.ProgressApplication;
import com.tpn.baro.helperClass.ViewPagerCustomDuration;
import com.tpn.baro.helperClass.MyGPSListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import maes.tech.intentanim.CustomIntent;

public class MainPage extends AppCompatActivity implements TypeAdapter.OnListItemLongSelectedInterface, TypeAdapter.OnListItemSelectedInterface, UltraStoreListAdapter.OnListItemLongSelectedInterface, UltraStoreListAdapter.OnListItemSelectedInterface,
        NewStoreListAdapter.OnListItemSelectedInterface, NewStoreListAdapter.OnListItemLongSelectedInterface, AutoPermissionsListener, ActivityCompat.OnRequestPermissionsResultCallback {
    final private String TAG = this.getClass().getSimpleName();
    private boolean firstFlag = false;
    RecyclerView mRecyclerView;
    private Intent serviceIntent;
    //울트라store recycler
    RecyclerView ultraStoreRecyclerView;
    ViewPagersListStoreParsing listStoreParsing;
    UltraStoreListAdapter ultraAdapter;
    //New Store recycler
    RecyclerView newStoreRecyclerView;
    ViewPagersListStoreParsing newListStoreParsing;
    NewStoreListAdapter newStoreListAdapter;

    RecyclerView.Adapter adapter;

    TextView eventCountSet;
    ViewPagerCustomDuration viewPager;
    int currentPos;
    AdvertiseAdapter advertiseAdapter;

    Gson gson;

    ProgressApplication progressApplication;
    EventHelperClass eventHelperClass;

    TextView mAddress;
    ImageView mMapButton;

    LatLng latLng;
    MyGPSListener myGPSListener;
    ///////// 태영
    ImageView call_search;
    SwipyRefreshLayout refreshLayout;

    ImageView alert;

    SessionManager userSession;
    HashMap userData = new HashMap<>();
    int getUnReadAlertCount = 0;
    private String userToken;
    /////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        progressApplication = new ProgressApplication();
        progressApplication.progressON(this);

        userSession = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        userData = userSession.getUsersDetailFromSession();

        gson = new GsonBuilder().create();
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        ultraStoreRecyclerView = findViewById(R.id.ultra_store);
        newStoreRecyclerView = findViewById(R.id.new_store);

        viewPager = findViewById(R.id.info_image);
        eventCountSet = findViewById(R.id.event_count);

        alert = findViewById(R.id.alert);

        AppStartAdDialog appStartAdDialog = new AppStartAdDialog(MainPage.this);
        appStartAdDialog.callFunction();

        mAddress = findViewById(R.id.address);
        mMapButton = findViewById(R.id.map_button);
        ///////// 태영
        call_search = findViewById(R.id.search_dialog);
        refreshLayout = findViewById(R.id.refreshLayout);

        makeRequestForEventThread();
        makeRequest();
        makeRequestForAlerts();

        myGPSListener = new MyGPSListener(this);
        latLng = myGPSListener.startLocationService(mAddress);


//        if(!BaroUtil.checkGPS(this)) {
//            makeRequestUltraStore(setHashMapData());
//            makeRequestNewStore(setHashMapData());
//        }


        alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BaroUtil.loginCheck(MainPage.this)) {
                    return;
                }
                startActivity(new Intent(MainPage.this, Alerts.class));
            }
        });
        mAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewMyMap.class);
                startActivity(intent);
                CustomIntent.customType(MainPage.this, "right-to-left");
            }
        });
        mMapButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, NewMyMap.class);
                startActivity(intent);
                CustomIntent.customType(MainPage.this, "right-to-left");
            }
        });
        refreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                latLng = myGPSListener.startLocationService(mAddress);
                makeRequestUltraStore(setHashMapData());
                makeRequestNewStore(setHashMapData());
                refreshLayout.setRefreshing(false);
            }
        });
        call_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchDialog searchDialog = new SearchDialog(MainPage.this);
                searchDialog.callFunction();
            }
        });
        progressApplication.progressOFF();
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    return;
                }
                userToken = task.getResult().getToken();
                Log.e("qqqqqq",userToken);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        userSession = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        userData = userSession.getUsersDetailFromSession();
        if (userData.get(SessionManager.KEY_PHONENUMBER) == null) {
            alert.setBackground(getResources().getDrawable(R.drawable.alert_off));
        } else {
            makeRequestForAlerts();
        }
        if (!BaroUtil.checkGPS(this)) {
            makeRequestUltraStore(setHashMapData());
            makeRequestNewStore(setHashMapData());
        } else {

        }
        Log.e("qqqqqq","ewer");
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    return;
                }
                final String temp_token = task.getResult().getToken();
                Log.e("qqqqqqqq",temp_token);
                if (!temp_token.equals(userToken)){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HashMap<String,String> data = new HashMap<>();
                            data.put("phone",""+userData.get(SessionManager.KEY_PHONENUMBER));
                            data.put("device_token",temp_token);
                            String url = new UrlMaker().UrlMake("UpdateToken.do");
                            RequestQueue requestQueue = Volley.newRequestQueue(MainPage.this);
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data),
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.e("qqqqqqqqqqqq","qqqqqq");
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                            requestQueue.add(jsonObjectRequest);
                        }
                    }).start();
                }else{

                }
            }
        });
        latLng = myGPSListener.startLocationService(mAddress);
        makeRequestUltraStore(setHashMapData());
        makeRequestNewStore(setHashMapData());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceIntent != null) {
            stopService(serviceIntent);
            serviceIntent = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void makeRequestForAlerts() {
        UrlMaker urlMaker = new UrlMaker();
        final String url = urlMaker.UrlMake("GetNewAlertCount.do?phone=" + userData.get(SessionManager.KEY_PHONENUMBER));
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(MainPage.this);
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
        } catch (JSONException e) {

        }
        if (getUnReadAlertCount == 0) {
            alert.setBackground(getResources().getDrawable(R.drawable.alert_off));

        } else if (getUnReadAlertCount > 0) {
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
        eventCountSet.setText((position % eventHelperClass.event.size() + 1) + "  /  " + eventHelperClass.event.size());
    }

    public HashMap setHashMapData() {
        HashMap<String, String> data = new HashMap<>();
        data.put("latitude", latLng.latitude + "");
        data.put("longitude", latLng.longitude + "");
        return data;
    }

    public void makeRequestUltraStore(final HashMap data) {
        UrlMaker urlMaker = new UrlMaker();
        String lastUrl = "StoreFindByUltra.do";
        final String url = urlMaker.UrlMake(lastUrl);
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(MainPage.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                jsonParsingUltraStore(response.toString());
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueue.add(jsonObjectRequest);
            }
        }).start();
    }

    public void makeRequest() {
        UrlMaker urlMaker = new UrlMaker();
        String lastUrl = "TypeFindAll.do";
        final String url = urlMaker.UrlMake(lastUrl);
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(MainPage.this);
                StringRequest request = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
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
        }).start();
    }

    public void makeRequestNewStore(final HashMap data) {
        UrlMaker urlMaker = new UrlMaker();
        String lastUrl = "StoreFindByNew.do";
        final String url = urlMaker.UrlMake(lastUrl);
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(MainPage.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                jsonParsingNewStore(response.toString());
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueue.add(jsonObjectRequest);
            }
        }).start();
    }

    private void jsonParsingNewStore(String toString) {
        Gson gson = new Gson();
        newListStoreParsing = gson.fromJson(toString, ViewPagersListStoreParsing.class);
        setNewListStoreRecyclerView();
    }

    private void setNewListStoreRecyclerView() {
        newStoreRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        newStoreListAdapter = new NewStoreListAdapter(newListStoreParsing, this, this, this);
        newStoreRecyclerView.setAdapter(newStoreListAdapter);
    }

    private void mRecyclerView(String result) {
        ArrayList<TypeHelperClass> DataList = new ArrayList<>();
        HashMap<String, TypeHelperClass> hashMap = new HashMap<>();
        hashMap.put("TypeHelperClass", new TypeHelperClass("", "", ""));
        TypeParsing typeParsing = new TypeParsing();
        jsonParsing(result, typeParsing);
        adapter = new TypeAdapter(DataList, this, this, MainPage.this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        for (int i = 0; i < typeParsing.getTypeList().size(); i++) {
            TypeListParsing typeListParsing = typeParsing.getTypeList().get(i);
            TypeHelperClass typeHelperClass = new TypeHelperClass(typeListParsing.getTypeName(), typeListParsing.getTypeCode(), typeListParsing.getTypeImage());
            typeHelperClass.typeNames.add(typeListParsing.getTypeName());
            typeHelperClass.typeCodes.add(typeListParsing.getTypeCode());
            DataList.add(typeHelperClass);
        }
        mRecyclerView.setAdapter(adapter);
    }

    //ultrastore 가져오기 위한 recyclerview
    private void ultraStoreRecyclerView() {
        ultraStoreRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ultraAdapter = new UltraStoreListAdapter(listStoreParsing, this, this, MainPage.this);
        ultraStoreRecyclerView.setAdapter(ultraAdapter);
    }

    private void jsonParsingUltraStore(String result) {
        Gson gson = new Gson();
        listStoreParsing = gson.fromJson(result, ViewPagersListStoreParsing.class);
        ultraStoreRecyclerView();
    }


    private synchronized void jsonParsing(String result, TypeParsing typeParsing) {
        try {
            Boolean result2 = (Boolean) new JSONObject(result).getBoolean("result");
            typeParsing.setMessage(new JSONObject(result).getString("message"));
            JSONArray jsonArray = new JSONObject(result).getJSONArray("type");
            ArrayList<TypeListParsing> typeListParsings = new ArrayList<TypeListParsing>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObject = jsonArray.getJSONObject(i);
                String type_name = jObject.optString("type_name");
                String type_code = jObject.optString("type_code");
                String type_image = jObject.optString("type_image");
                TypeListParsing typeListParsing = new TypeListParsing(type_name, type_code, type_image);
                typeListParsings.add(typeListParsing);
            }
            typeParsing.setTypeList(typeListParsings);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void makeRequestForEventThread() {
        final String url = new UrlMaker().UrlMake("EventFindAll.do");
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(MainPage.this);
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
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.design_advertise, null, false);
        ImageView imageView = view.findViewById(R.id.slider_image);
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        for (int i = 0; i < eventHelperClass.event.size(); i++) {
            makeRequestForgetImage(eventHelperClass.event.get(i).event_image, imageView, MainPage.this, bitmaps, i, eventHelperClass.event.size());
        }

    }

    public void makeRequestForgetImage(String type_image, final ImageView imageView, Context context, final ArrayList<Bitmap> bitmaps, final int pos, final int max) {
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
                        bitmaps.add(pos, response);
                        if (max - 1 == pos) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    advertiseAdapter = new AdvertiseAdapter(MainPage.this, eventHelperClass, bitmaps);
                                    viewPager.setAdapter(advertiseAdapter);
                                    viewPager.setCurrentItem(eventHelperClass.event.size() * 500);
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
                                                        viewPager.setCurrentItem((currentPos + 1), true);
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

    @Override
    public void onItemLongSelected(View v, int adapterPosition) {
    }

    @Override
    public void onItemSelected(View v, int position) {
        TypeAdapter.TypeViewHolder viewHolder = (TypeAdapter.TypeViewHolder) mRecyclerView.findViewHolderForAdapterPosition(position);
        Intent intent = new Intent(this, ListStorePage.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        int i = 0;
        intent.putExtra("type_code", viewHolder.code.getText().toString());
        intent.putExtra("type_name", viewHolder.title.getText().toString());
        intent.putExtra("list_type", "find_type");
        startActivity(intent);
        CustomIntent.customType(this, "left-to-right");
//        startActivity(new Intent(this, ListStorePage.class));
//        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
//
//        overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_left);

    }

    @Override
    public void onNewStoreItemSelected(View v, int position) {
        NewStoreListAdapter.NewStoreViewHolder viewHolder = (NewStoreListAdapter.NewStoreViewHolder) newStoreRecyclerView.findViewHolderForAdapterPosition(position);
        Intent intent = new Intent(MainPage.this, StoreInfoReNewer.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("store_id", viewHolder.storeId.getText().toString());
        startActivity(intent);
        CustomIntent.customType(this, "left-to-right");
    }

    @Override
    public void onLongNewStoreItemSelected(View v, int adapterPosition) {

    }

    @Override
    public void onUltraStoreLongSelected(View v, int adapterPosition) {

    }

    @Override
    public void onUltraStoreSelected(View v, int position) {
        UltraStoreListAdapter.UltraStoreListViewHolder viewHolder = (UltraStoreListAdapter.UltraStoreListViewHolder) ultraStoreRecyclerView.findViewHolderForAdapterPosition(position);
        Intent intent = new Intent(MainPage.this, StoreInfoReNewer.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("store_id", viewHolder.storeId.getText().toString());
        startActivity(intent);
        CustomIntent.customType(this, "left-to-right");
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
