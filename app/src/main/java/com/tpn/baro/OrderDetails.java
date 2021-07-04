package com.tpn.baro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tpn.baro.Adapter.OrderDetailsEssentialAdapter;
import com.tpn.baro.Adapter.OrderDetailsNonEssentialAdapter;
import com.tpn.baro.AdapterHelper.ExtraOrder;
import com.tpn.baro.AdapterHelper.OrderDetailsNonEssential;
import com.tpn.baro.Fragment.TopBar;
import com.tpn.baro.JsonParsingHelper.OrderDetailsListParsing;
import com.tpn.baro.JsonParsingHelper.OrderDetailsParsing;
import com.tpn.baro.Dialogs.BasketDialog;
import com.tpn.baro.Url.UrlMaker;
import com.tpn.baro.helperClass.BaroUtil;
import com.tpn.baro.helperClass.DetailsFixToBasket;
import com.tpn.baro.Dialogs.RefreshBasketByStoreIdDialog;
import com.google.gson.Gson;
import com.tpn.baro.helperClass.ProgressApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import maes.tech.intentanim.CustomIntent;

public class OrderDetails extends AppCompatActivity implements TopBar.OnBackPressedInParentActivity, OrderDetailsEssentialAdapter.ChangeDefaultPriceInEssential, OrderDetailsNonEssentialAdapter.ChangeDefaultPriceNonEssential {
    public static OrderDetails orderDetails;
    ImageView imageView;
    LinearLayout expandListViewShell;
    LinearLayout recyclerViewShell;
    ExpandableListView expandableListView;
    ImageButton itemPlus;
    ImageButton itemMinus;
    TextView itemName;
    TextView itemCount;
    TextView totalPriceText;
    TextView ifDiscountRate;
    ImageView arrowRight;

    String menu_name;
    String menu_code;
    String menu_image;
    String store_name;
    String store_number;
    int menu_count;
    int totalPrice;
    int discountRate;
    int store_id;
    int menuDefaultPrice;

    int defaultPrice = 0;

    HashMap<String, HashMap<String, ArrayList<ExtraOrder>>> essentialOrNot;
    HashMap<String, ArrayList<ExtraOrder>> extraOptions;
    ArrayList<String> arrayList;
    ArrayList<String> arrayList2;
    HashMap<String, ArrayList<ExtraOrder>> essentialOptions;
    HashMap<String, ArrayList<ExtraOrder>> nonEssentialOptions;
    DetailsFixToBasket detailsFixToBasket;
    OrderDetailsEssentialAdapter adapter;
    OrderDetailsNonEssentialAdapter nonEssentialAdapter;
    TextView getIfDiscountRate;
//    View v;
    Button fix;
//    Bitmap bitmap = null;
    ProgressApplication progressApplication;
    TopBar topBar;
    FragmentManager fm;
    public static boolean onPause = false;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("onCreate", "true");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BaroUtil.setStatusBarColor(OrderDetails.this, this.toString());
        }
        onPause = false;
        setContentView(R.layout.activity_order_details);

        progressApplication = new ProgressApplication();
        progressApplication.progressON(this);
        orderDetails = OrderDetails.this;
        Intent intent = getIntent();

        fm = getSupportFragmentManager();
        topBar = (TopBar) fm.findFragmentById(R.id.top_bar);
        menuDefaultPrice = intent.getExtras().getInt("menuDefaultPrice");
        menu_code = String.valueOf(intent.getExtras().getInt("menuId"));
        menu_name = intent.getExtras().getString("menuName");
        store_id = intent.getExtras().getInt("storeId");
        store_name = intent.getExtras().getString("storeName");
        store_number = intent.getExtras().getString("storeNumber");
        menu_image = intent.getStringExtra("menuImage");
        discountRate = intent.getIntExtra("discount_rate", 0);

        topBar.setTitleStringWhereUsedEventsAndListStore(store_name);
        topBar.storeId = store_id;

        defaultPrice = menuDefaultPrice;

        arrayList = new ArrayList<>(); // 세부 항목 넣는곳
        arrayList2 = new ArrayList<>(); // 세부 항목 넣는곳
        essentialOrNot = new HashMap<>();
        essentialOptions = new HashMap<>();
        nonEssentialOptions = new HashMap<>();
//        makeRequestForDiscountRate(store_id);
        //--------------------------------------------------------
        imageView = findViewById(R.id.menu_image);
        expandableListView = findViewById(R.id.menuExpand_NotEssential);
        expandableListView.setNestedScrollingEnabled(false);
        itemName = findViewById(R.id.menuName);
        itemMinus = findViewById(R.id.itemMinus);
        itemPlus = findViewById(R.id.itemPlus);
        itemCount = findViewById(R.id.itemCount);
        arrowRight = findViewById(R.id.arrow_right);
        arrowRight.setVisibility(View.GONE);
        ifDiscountRate = findViewById(R.id.if_discount_rate);
        ifDiscountRate.setVisibility(View.GONE);
        getIfDiscountRate = findViewById(R.id.discount_rate);
        getIfDiscountRate.setVisibility(View.GONE);
        totalPriceText = findViewById(R.id.totalPrice);

        expandListViewShell = findViewById(R.id.expandListViewShell);
        recyclerViewShell = findViewById(R.id.essentialOptionShell);
        fix = findViewById(R.id.fix);

        BaroUtil.storeId = store_id;

        //--------------------------------------------------------
        itemName.setText(menu_name);

        // 이벤트 심는곳
        fix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!BaroUtil.loginCheck(OrderDetails.this)) {
                    return;
                }
                int must;
                SharedPreferences sharedPreferences = getSharedPreferences("basketList", MODE_PRIVATE);
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                final Gson gson = new Gson();
                if (adapter == null) {
                    must = 0;
                } else {
                    must = adapter.getMust();
                }
                if (arrayList.size() != must) {
                    Toast.makeText(OrderDetails.this, "필수 옵션을 모두 선택하셔야 합니다", Toast.LENGTH_LONG).show();
                    return;
                }
                menu_count = Integer.parseInt(itemCount.getText().toString());
                totalPrice = Integer.parseInt(totalPriceText.getText().toString());
                HashMap<String, ExtraOrder> essentialOptionFixed = new HashMap<>();
                if (adapter != null) {
                    essentialOptionFixed = adapter.getSelectOptions();
                }
                HashMap<String, ExtraOrder> nonEssentialOptionsFixed = new HashMap<>();

                if (nonEssentialAdapter != null) {
                    nonEssentialOptionsFixed = nonEssentialAdapter.getNonEssentialOptions();
                }

                detailsFixToBasket = new DetailsFixToBasket(menu_name, Integer.parseInt(menu_code), menu_count, menuDefaultPrice, totalPrice, discountRate, essentialOptionFixed, nonEssentialOptionsFixed);


                String inMyBasket = sharedPreferences.getString(Basket.IN_MY_BASEKT, "");
                final ArrayList<DetailsFixToBasket> detailsFixToBaskets = new ArrayList<>();
                if (!(inMyBasket.equals(""))) {
                    try {
                        JSONArray jsonArray = new JSONArray(inMyBasket);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String jsonChild = jsonArray.optString(i);
                            DetailsFixToBasket detailsFixToBasket = gson.fromJson(jsonChild, DetailsFixToBasket.class);
                            if (detailsFixToBasket.getName().equals("")) continue;
                            detailsFixToBaskets.add(detailsFixToBasket);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                final BasketDialog basketDialog = new BasketDialog(OrderDetails.this, store_id, discountRate);


                if (!(sharedPreferences.getString("currentStoreId", "").equals(String.valueOf(store_id)))) {
                    if (!(sharedPreferences.getString("currentStoreId", "").equals(""))) {

                        RefreshBasketByStoreIdDialog refreshBasketByStoreIdDialog =
                                new RefreshBasketByStoreIdDialog(OrderDetails.this, store_id + "", new RefreshBasketByStoreIdDialog.RefreshBasketByStoreIdDialogListener() {
                                    @Override
                                    public void clickBtn(Boolean OkOrNo) {

                                        if(OkOrNo==true){
                                            basketDialog.callFunction();
                                            ArrayList<DetailsFixToBasket> detailsFixToBaskets = new ArrayList<>();
                                            addNewMenuToBasket(detailsFixToBaskets,editor);
                                        }
                                        else{
                                            ;
                                        }
                                    }
                                });
                        refreshBasketByStoreIdDialog.callFunction();

                    }else{;
                        basketDialog.callFunction();
                        addNewMenuToBasket(detailsFixToBaskets,editor);
                    }

                }else{
                    basketDialog.callFunction();
                    addNewMenuToBasket(detailsFixToBaskets,editor);
                }
                return;
            }
        });
        itemMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int count = Integer.parseInt(itemCount.getText().toString());
                int currentPrice = Integer.parseInt(totalPriceText.getText().toString()) / count;
//                int currentPrice = defaultPrice / count;
                if (count != 1) {
                    count -= 1;
                    defaultPrice = (currentPrice * count);
                    itemCount.setText(String.valueOf(count));
//                    totalPriceText.setText(String.valueOf(currentPrice * count));
                    totalPriceText.setText(String.valueOf(defaultPrice));
                    ifDiscountRate.setText(String.valueOf(defaultPrice * 100 / (100 - discountRate))+"원");
                    Log.e("priceMinus", defaultPrice +"");
                }
            }
        });

        itemPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(itemCount.getText().toString()) + 1;
                final int currentPrice = Integer.parseInt(totalPriceText.getText().toString()) / (count - 1);
//                final int currentPrice = defaultPrice / (count - 1);
                defaultPrice = (currentPrice * count);

                itemCount.setText(String.valueOf(count));
//                totalPriceText.setText(String.valueOf(currentPrice * count));
                totalPriceText.setText(String.valueOf(defaultPrice));
                ifDiscountRate.setText(String.valueOf(defaultPrice * 100 / (100 - discountRate))+"원");
                Log.e("pricePlus", defaultPrice +":"+discountRate);
            }
        });
        progressApplication.progressOFF();
    }
    @Override
    protected void onRestart() {
        onPause = false;
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        setDiscountTextView(BaroUtil.getDiscountRateInt());
        discountRate = BaroUtil.getDiscountRateInt();
        setDiscountTextView(discountRate);
    }

    @Override
    protected void onPause() {
        onPause = true;
        super.onPause();
    }
//    public void makeRequestForDiscountRate(int storeId) {
//        UrlMaker urlMaker = new UrlMaker();
//        String lastUrl = "GetStoreDiscount.do?store_id="+storeId;
//        String url = urlMaker.UrlMake(lastUrl);
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//        StringRequest request = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(final String response) {
//                        Log.e("response", response);
//                        setDiscountTextView(response);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        progressApplication.progressOFF();
//                    }
//                });
//        requestQueue.add(request);
//    }
    public void setDiscountTextView(int discountRate) {
        getIfDiscountRate.setText("SALE "+discountRate+"%");
        if(discountRate == 0) {
            totalPriceText.setText(String.valueOf(defaultPrice));
        }else {
            ifDiscountRate.setVisibility(View.VISIBLE);
            getIfDiscountRate.setVisibility(View.VISIBLE);
            arrowRight.setVisibility(View.VISIBLE);
            ifDiscountRate.setText(defaultPrice+"원");
            totalPriceText.setText(String.valueOf(defaultPrice - (int)(defaultPrice * (discountRate / 100.0))));
        }
        makeRequest();
        progressApplication.progressOFF();
    }
    private void addNewMenuToBasket(ArrayList<DetailsFixToBasket> detailsFixToBaskets, SharedPreferences.Editor editor){
        Gson gson = new Gson();
        detailsFixToBaskets.add(detailsFixToBasket);
        String json = gson.toJson(detailsFixToBaskets);
        int count = 0;
        for(int i =0;i<detailsFixToBaskets.size();i++){
            count += detailsFixToBaskets.get(i).getCount();
        }
        editor.putString(Basket.IN_MY_BASEKT, json);
        editor.putInt("orderCnt", count);
        editor.putString("currentStoreName", gson.toJson(store_name));
        editor.putString("currentStoreId", gson.toJson(store_id));
        editor.putString("currentStoreNumber", gson.toJson(store_id));
        editor.putString("menuId", gson.toJson(menu_code));
        editor.commit();
    }

    private ArrayList<OrderDetailsNonEssential> ConvertNonEssential(HashMap<String, ArrayList<ExtraOrder>> nonEssentialOptions) {
        if (nonEssentialOptions == null) {
            return new ArrayList<OrderDetailsNonEssential>();
        }
        ArrayList<OrderDetailsNonEssential> convertMapList = new ArrayList<>();
        ArrayList<ExtraOrder> data = nonEssentialOptions.get("null");
        for (int i = 0; i < data.size(); i++) {
            ExtraOrder extraOrder = data.get(i);
            OrderDetailsNonEssential orderDetailsNonEssential = new OrderDetailsNonEssential(extraOrder.getExtra_name(), String.valueOf(extraOrder.getExtra_id()), extraOrder.getExtra_price(), extraOrder.getExtra_maxcount());
            orderDetailsNonEssential.justCount.add(0);
            convertMapList.add(orderDetailsNonEssential);
        }
        return convertMapList;
    }

    private synchronized void applyAdapters(final String result) {
        final OrderDetailsParsing orderDetailsParsing = new OrderDetailsParsing();
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonParsing(result, orderDetailsParsing);
            }
        }).run();
        ArrayList<OrderDetailsListParsing> orderDetailsListParsings = orderDetailsParsing.getExtraList();
        String extraCategory = "";
        extraOptions = new HashMap<>();
        ExtraOrder extraOption = null;
        ArrayList<ExtraOrder> extraOrders = null;
        for (int i = 0; i < orderDetailsListParsings.size(); i++) {
            if (orderDetailsListParsings.get(i).getExtra_group().equals("null")) {
                // 비 필수 옵션
                if (!(orderDetailsListParsings.get(i).getExtra_group().equals(extraCategory))) {
                    if (!(extraCategory.equals(""))) {
                        arrayList.add(extraCategory);
                        extraOptions.put(extraCategory, extraOrders);
                        essentialOrNot.put("essential", extraOptions);
                    }
                    extraOrders = new ArrayList<>();
                    extraOptions = new HashMap<>();
                }
                extraOption = new ExtraOrder(orderDetailsListParsings.get(i).getExtra_id(), orderDetailsListParsings.get(i).getExtra_price(), orderDetailsListParsings.get(i).getExtra_name(), orderDetailsListParsings.get(i).getExtra_maxcount());
                extraOrders.add(extraOption);
                extraCategory = orderDetailsListParsings.get(i).getExtra_group();
                if (i == orderDetailsListParsings.size() - 1) {
                    extraOptions.put("null", extraOrders);
                    essentialOrNot.put("nonEssential", extraOptions);
                }
            } else {
                //필수 옵션
                if (!(orderDetailsListParsings.get(i).getExtra_group().equals(extraCategory))) {
                    if (!(extraCategory.equals(""))) {
                        arrayList.add(extraCategory);
                        extraOptions.put(extraCategory, extraOrders);
                    }
                    extraOrders = new ArrayList<>();
                }
                extraOption = new ExtraOrder(orderDetailsListParsings.get(i).getExtra_id(), orderDetailsListParsings.get(i).getExtra_price(), orderDetailsListParsings.get(i).getExtra_name(), orderDetailsListParsings.get(i).getExtra_maxcount());
                extraOrders.add(extraOption);
                extraCategory = orderDetailsListParsings.get(i).getExtra_group();
                if (i == orderDetailsListParsings.size() - 1) {
                    extraOptions.put(extraCategory, extraOrders);
                    essentialOrNot.put("essential", extraOptions);
                    arrayList.add(extraCategory);
                }
            }

        }

        essentialOptions = essentialOrNot.get("essential");
        nonEssentialOptions = essentialOrNot.get("nonEssential");

        ArrayList<OrderDetailsNonEssential> NonEssentialOptionList = ConvertNonEssential(nonEssentialOptions);
        RecyclerView essentailRecyclerView = findViewById(R.id.menuRecyler_Essential);
        essentailRecyclerView.setNestedScrollingEnabled(false);
        essentailRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (arrayList.size() != 0) {
//            adapter = new OrderDetailsEssentialAdapter(arrayList, essentialOptions, this, totalPriceText, itemCount);
            adapter = new OrderDetailsEssentialAdapter(arrayList, essentialOptions, this, totalPriceText, itemCount, defaultPrice, discountRate, this, ifDiscountRate);
            essentailRecyclerView.setAdapter(adapter);
        } else {
            recyclerViewShell.setVisibility(View.GONE);
        }
        if (NonEssentialOptionList.size() != 0) {
//            nonEssentialAdapter =
//                    new OrderDetailsNonEssentialAdapter(OrderDetails.this, R.layout.activity_order_details_nonessential_group_parent,
//                            R.layout.activity_order_details_nonessential_group_child, NonEssentialOptionList, totalPriceText, itemCount);
            nonEssentialAdapter =
                    new OrderDetailsNonEssentialAdapter(OrderDetails.this, R.layout.design_order_details_nonessential_group_parent,
                            R.layout.design_order_details_nonessential_group_child, NonEssentialOptionList, totalPriceText, itemCount, defaultPrice , discountRate, this, ifDiscountRate);
            expandableListView.setAdapter(nonEssentialAdapter);
            setListIndicator();
            setExpandableListViewHeight(expandableListView,-1);
            expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int position, long id) {
                    setExpandableListViewHeight(parent, position);
                    return false;
                }
            });
        } else {
            expandListViewShell.setVisibility(View.GONE);
        }
        progressApplication.progressOFF();
    }
    private void setListIndicator()
    {

        DisplayMetrics metrics = new DisplayMetrics();
        OrderDetails.this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
            expandableListView.setIndicatorBounds(width - GetPixelFromDips(70), width - GetPixelFromDips(40));
        else
            expandableListView.setIndicatorBoundsRelative(width - GetPixelFromDips(70), width - GetPixelFromDips(40));
    }


    public int GetPixelFromDips(float pixels)


    {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pixels * scale + 0.5f);
    }


    private synchronized void makeRequest() {
        UrlMaker urlMaker = new UrlMaker();
        String lastUrl = "ExtraFindByMenuId.do?menu_id=" + menu_code;
        String url = urlMaker.UrlMake(lastUrl);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        applyAdapters(response);
//                        getMenuPicture();
                        getMenuPicture(menu_image, OrderDetails.this, imageView);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("OrderDetails", "error");
                        progressApplication.progressOFF();
                    }
                });
        requestQueue.add(request);
    }
    public void getMenuPicture(String menu_image, Context context, final ImageView image) {
        UrlMaker urlMaker = new UrlMaker();
        String lastUrl = "ImageMenu.do?store_id=" + store_id + "&image_name=";
        String url = urlMaker.UrlMake(lastUrl);
        StringBuilder urlBuilder = new StringBuilder()
                .append(url)
                .append(menu_image);
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        ImageRequest request = new ImageRequest(urlBuilder.toString(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        image.setImageBitmap(response);
                    }
                }, image.getWidth(), image.getHeight(), ImageView.ScaleType.FIT_XY, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("menuimageerror", "error");
                    }
                });
        requestQueue.add(request);
    }
//    private void getMenuPicture() {
//        String url = new UrlMaker().UrlMake("ImageMenu.do?image_name="+menu_code+".png&store_id="+store_id);
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        ImageRequest imageRequest = new ImageRequest(url,
//                new Response.Listener<Bitmap>() {
//                    @Override
//                    public void onResponse(Bitmap response) {
//                        imageView.setImageBitmap(response);
//                    }
//                },1000,1000,ImageView.ScaleType.FIT_CENTER,null,
//                new Response.ErrorListener(){
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.i("error", "error");
//                    }
//                });
//        requestQueue.add(imageRequest);
//
////        Glide.with(this).load("http://celebe.ohmyapp.io/app-assets/images/portrait/small/avatar-a-1.png").into(imageView);
////        Glide.with(this).load("http://celebe.ohmyapp.io/app-assets/images/portrait/small/avatar-a-1.png").into(imageView);
//
//
//    }
    private void jsonParsing(String result, OrderDetailsParsing orderDetailsParsing) {
        try {
//            Boolean isSuccess = (Boolean) new JSONObject(result).getBoolean("result");
            orderDetailsParsing.setMessage(new JSONObject(result).getString("message"));
            JSONArray jsonArray = new JSONObject(result).getJSONArray("extra");
            ArrayList<OrderDetailsListParsing> orderDetailsListParsings = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObject = jsonArray.getJSONObject(i);
                String extra_group = jObject.optString("extra_group");
                int extra_id = jObject.optInt("extra_id");
                int extra_price = jObject.optInt("extra_price");
                String extra_name = jObject.optString("extra_name");
                int extra_maxcount = jObject.optInt("extra_maxcount");
                OrderDetailsListParsing orderDetailsListParsing = new OrderDetailsListParsing(extra_group, extra_id, extra_price, extra_name, extra_maxcount);
                orderDetailsListParsings.add(orderDetailsListParsing);
                if (orderDetailsListParsings == null) {
                    Log.i("asdf", "Zcv");
                }
            }
            orderDetailsParsing.setExtraList(orderDetailsListParsings);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onClickBack(View view) {
        onBackPressed();
    }

    private void setExpandableListViewHeight(ExpandableListView listView, int group) {
        ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        if (listAdapter == null) {
            return;
        }

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            view = listAdapter.getGroupView(i, false, view, listView);
            if (i == 0) {
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
            if(((listView.isGroupExpanded(i)) && (i != group)) || ((!listView.isGroupExpanded(i)) && (i == group))) {
                View listItem = null;
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    listItem = listAdapter.getChildView(i, j, false, listItem, listView);
                    listItem.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, View.MeasureSpec.UNSPECIFIED));
                    listItem.measure(
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + 100 + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    @Override
    public void onBack() {
        super.onBackPressed();
        CustomIntent.customType(this,"right-to-left");
    }

    @Override
    public void changeNonEssentialValue(int newDefaultPrice) {
//        defaultPrice = newDefaultPrice;
    }

    @Override
    public void changeEssentialValue(int newDefaultPrice) {
//        defaultPrice = newDefaultPrice;
    }
}
