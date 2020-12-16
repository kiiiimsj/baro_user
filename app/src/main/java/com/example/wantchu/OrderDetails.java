package com.example.wantchu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Adapter.OrderDetailsEssentialAdapter;
import com.example.wantchu.Adapter.OrderDetailsNonEssentialAdapter;
import com.example.wantchu.AdapterHelper.ExtraOrder;
import com.example.wantchu.AdapterHelper.OrderDetailsNonEssential;
import com.example.wantchu.JsonParsingHelper.OrderDetailsListParsing;
import com.example.wantchu.JsonParsingHelper.OrderDetailsParsing;
import com.example.wantchu.Dialogs.CustomDialog;
import com.example.wantchu.Url.UrlMaker;
import com.example.wantchu.helperClass.DetailsFixToBasket;
import com.example.wantchu.Dialogs.RefreshBasketByStoreIdDialog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderDetails extends AppCompatActivity {
    public static OrderDetails orderDetails;
    ImageView imageView;
    TextView store_name_text;
    LinearLayout expandListViewShell;
    LinearLayout recyclerViewShell;
    ExpandableListView expandableListView;
    ImageButton itemPlus;
    ImageButton itemMinus;
    TextView itemName;
    TextView itemCount;
    TextView totalPriceText;
    ToggleButton toggleButton;
    String menu_name;
    String menu_code;
    int store_id;
    String store_name;
    String store_number;
    int menu_count;
    int totalPrice;
    HashMap<String, HashMap<String, ArrayList<ExtraOrder>>> essentialOrNot;
    HashMap<String, ArrayList<ExtraOrder>> extraOptions;
    ArrayList<String> arrayList;
    ArrayList<String> arrayList2;
    HashMap<String, ArrayList<ExtraOrder>> essentialOptions;
    HashMap<String, ArrayList<ExtraOrder>> nonEssentialOptions;
    DetailsFixToBasket detailsFixToBasket;
    OrderDetailsEssentialAdapter adapter;
    OrderDetailsNonEssentialAdapter nonEssentialAdapter;
    View v;
    Button fix;
    Bitmap bitmap = null;
    ProgressApplication progressApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        progressApplication = new ProgressApplication();
        progressApplication.progressON(this);
        orderDetails = OrderDetails.this;
        Intent intent = getIntent();
        final int defaultPrice = intent.getExtras().getInt("menuDefaultPrice");
        menu_code = String.valueOf(intent.getExtras().getInt("menuId"));
        menu_name = intent.getExtras().getString("menuName");
        store_id = intent.getExtras().getInt("storeId");
        store_name = intent.getExtras().getString("storeName");
        store_number = intent.getExtras().getString("storeNumber");
        arrayList = new ArrayList<>(); // 세부 항목 넣는곳
        arrayList2 = new ArrayList<>(); // 세부 항목 넣는곳
        essentialOrNot = new HashMap<>();
        essentialOptions = new HashMap<>();
        nonEssentialOptions = new HashMap<>();
        makeRequest();
        //--------------------------------------------------------
        store_name_text = findViewById(R.id.store_name);
        imageView = findViewById(R.id.baro_logo);
        expandableListView = findViewById(R.id.menuExpand_NotEssential);
        itemName = findViewById(R.id.menuName);
        itemMinus = findViewById(R.id.itemMinus);
        itemPlus = findViewById(R.id.itemPlus);
        itemCount = findViewById(R.id.itemCount);
        totalPriceText = findViewById(R.id.totalPrice);
        totalPriceText.setText(String.valueOf(defaultPrice));
        expandListViewShell = findViewById(R.id.expandListViewShell);
        recyclerViewShell = findViewById(R.id.essentialOptionShell);
        fix = findViewById(R.id.fix);
        //--------------------------------------------------------
        itemName.setText(menu_name);
        store_name_text.setText(store_name);
        // 이벤트 심는곳
        fix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                detailsFixToBasket = new DetailsFixToBasket(menu_name, Integer.parseInt(menu_code), menu_count, defaultPrice, totalPrice, essentialOptionFixed, nonEssentialOptionsFixed);


                String inMyBasket = sharedPreferences.getString(Basket.IN_MY_BASEKT, "");
                final ArrayList<DetailsFixToBasket> detailsFixToBaskets = new ArrayList<>();
                if (!(inMyBasket.equals(""))) {
                    try {
                        JSONArray jsonArray = new JSONArray(inMyBasket);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String jsonChild = jsonArray.optString(i);
                            Log.i("Zzzzz", jsonChild);
                            DetailsFixToBasket detailsFixToBasket = gson.fromJson(jsonChild, DetailsFixToBasket.class);
                            if (detailsFixToBasket.getName().equals("")) continue;
                            detailsFixToBaskets.add(detailsFixToBasket);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                final CustomDialog customDialog = new CustomDialog(OrderDetails.this, store_id);


                if (!(sharedPreferences.getString("currentStoreId", "").equals(String.valueOf(store_id)))) {
                    if (!(sharedPreferences.getString("currentStoreId", "").equals(""))) {

                        RefreshBasketByStoreIdDialog refreshBasketByStoreIdDialog =
                                new RefreshBasketByStoreIdDialog(OrderDetails.this, store_id + "", new RefreshBasketByStoreIdDialog.RefreshBasketByStoreIdDialogListener() {
                                    @Override
                                    public void clickBtn(Boolean OkOrNo) {

                                        if(OkOrNo==true){
                                            customDialog.callFunction();
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
                        customDialog.callFunction();
                        addNewMenuToBasket(detailsFixToBaskets,editor);
                    }

                }else{
                    customDialog.callFunction();
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
                if (count != 1) {
                    count -= 1;
                    itemCount.setText(String.valueOf(count));
                    totalPriceText.setText(String.valueOf(currentPrice * count));
                }

            }
        });

        itemPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(itemCount.getText().toString()) + 1;
                final int currentPrice = Integer.parseInt(totalPriceText.getText().toString()) / (count - 1);

                itemCount.setText(String.valueOf(count));
                totalPriceText.setText(String.valueOf(currentPrice * count));
            }
        });

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
        editor.putString("munuId", gson.toJson(menu_code));
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
        Log.i("asdfadfs", String.valueOf(orderDetailsListParsings.size()));
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
        essentailRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (arrayList.size() != 0) {
            adapter = new OrderDetailsEssentialAdapter(arrayList, essentialOptions, this, totalPriceText, itemCount);
            essentailRecyclerView.setAdapter(adapter);
        } else {
            recyclerViewShell.setVisibility(View.GONE);
        }
        if (NonEssentialOptionList.size() != 0) {
            Log.i("sizeeeeeeeeeeeee", NonEssentialOptionList.size() + "");
            nonEssentialAdapter =
                    new OrderDetailsNonEssentialAdapter(OrderDetails.this, R.layout.activity_order_details_nonessential_group_parent,
                            R.layout.activity_order_details_nonessential_group_child, NonEssentialOptionList, totalPriceText, itemCount);
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
                        getMenuPicture();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("OrderDetails", "error");
                    }
                });
        requestQueue.add(request);
    }

    private void getMenuPicture() {
        String url = new UrlMaker().UrlMake("ImageMenu.do?image_name="+menu_code+".png&store_id="+store_id);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        ImageRequest imageRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                    }
                },1000,1000,ImageView.ScaleType.FIT_CENTER,null,
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error", "error");
                    }
                });
        requestQueue.add(imageRequest);

//        Glide.with(this).load("http://celebe.ohmyapp.io/app-assets/images/portrait/small/avatar-a-1.png").into(imageView);
//        Glide.with(this).load("http://celebe.ohmyapp.io/app-assets/images/portrait/small/avatar-a-1.png").into(imageView);


    }

    private void jsonParsing(String result, OrderDetailsParsing orderDetailsParsing) {
        try {
            Boolean isSuccess = (Boolean) new JSONObject(result).getBoolean("result");
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
        super.onBackPressed();
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
        params.height = totalHeight +100+ (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
