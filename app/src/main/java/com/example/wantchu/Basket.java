package com.example.wantchu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Adapter.BasketAdapter;
import com.example.wantchu.AdapterHelper.ExtraOrder;
import com.example.wantchu.Database.SessionManager;
import com.example.wantchu.Dialogs.OrderCancelDialog;
import com.example.wantchu.Dialogs.OrderDoneDialog;
import com.example.wantchu.Dialogs.OrderLimitExcessDialog;
import com.example.wantchu.JsonParsingHelper.ClarityIsOpenStoreParsing;
import com.example.wantchu.JsonParsingHelper.OrderInsertParsing;
import com.example.wantchu.JsonParsingHelper.OrderInsertParsingChild;
import com.example.wantchu.JsonParsingHelper.OrderInsertParsingChild2;
import com.example.wantchu.JsonParsingHelper.ReceiptRecordParsing;
import com.example.wantchu.Dialogs.CouponDialog;
import com.example.wantchu.Url.UrlMaker;
import com.example.wantchu.helperClass.DetailsFixToBasket;
import com.example.wantchu.Dialogs.StoreCloseDialog;
import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.BootpayAnalytics;
import kr.co.bootpay.BootpayBuilder;
import kr.co.bootpay.enums.Gender;
import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.listener.CancelListener;
import kr.co.bootpay.listener.CloseListener;
import kr.co.bootpay.listener.ConfirmListener;
import kr.co.bootpay.listener.DoneListener;
import kr.co.bootpay.listener.ErrorListener;
import kr.co.bootpay.listener.ReadyListener;
import kr.co.bootpay.model.BootExtra;
import kr.co.bootpay.model.BootUser;
import kr.co.bootpay.model.StatItem;
import kr.co.bootpay.rest.BootpayRest;
import kr.co.bootpay.rest.BootpayRestImplement;
import kr.co.bootpay.rest.model.RestEasyPayUserTokenData;
import kr.co.bootpay.rest.model.RestTokenData;

public class Basket extends AppCompatActivity implements BootpayRestImplement {
    public static Basket basket;
    public static final String IN_MY_BASEKT = "inMyBasket";
    public static final String BasketList = "basketList";
    ArrayList<DetailsFixToBasket> detailsFixToBaskets;
    DetailsFixToBasket detailsFixToBasket;
    RecyclerView recyclerView;
    TextView StoreName;
    LinearLayout recyclerViewShell;

    private WebSocketClient webSocketClient;
    int store_id;
    String storeName;
    String storeNumber;
    String phone;
    String email;
    String user_name;
    String receiptId;
    OrderInsertParsing orderInsertParsing;
    Boolean pay =false;
    private int stuck = 1;
    private String application_id = "5f28e2c002f57e0033305756";
    Long expired_unixtime = null;
    String expired_localtime = null;
    String user_token = null;
    int realTotalPrice;
    int totalPrice;
    int used_coupon_id;
    int discount_price ;
    boolean isOpen = false;
    BasketAdapter basketAdapter =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        if(getFragmentManager().isDestroyed()){
            Log.e("시발사라졌다고 ","mm");
        }
        basket = Basket.this;
        recyclerView = findViewById(R.id.basketList);
        recyclerViewShell = findViewById(R.id.linearLayout2);
        StoreName = findViewById(R.id.store_name);
        SharedPreferences sharedPreferences = getSharedPreferences(BasketList, MODE_PRIVATE);
        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> userData = sessionManager.getUsersDetailFromSession();
        phone = userData.get(SessionManager.KEY_PHONENUMBER);
        Log.e("phoneeeeee", phone);
        email = userData.get(SessionManager.KEY_EMAIL);
        user_name = userData.get(SessionManager.KEY_USERNAME);
        Gson gson = new Gson();
        storeName = sharedPreferences.getString("currentStoreName", "");
        store_id = Integer.parseInt(sharedPreferences.getString("currentStoreId", "0"));
        storeNumber = sharedPreferences.getString("currentStoreNumber", "");
        if (store_id == 0) {
            Toast.makeText(this, "잘못된 접근 요청입니다", Toast.LENGTH_LONG);
        }
        String inMyBasket = sharedPreferences.getString(IN_MY_BASEKT, "");
        Log.i("inmy", inMyBasket);
        StoreName.setText(storeName);
        detailsFixToBaskets = new ArrayList<>();
        if (!(inMyBasket.equals(""))) {
            try {
                JSONArray jsonArray = new JSONArray(inMyBasket);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String jsonChild = jsonArray.optString(i);
                    detailsFixToBasket = gson.fromJson(jsonChild, DetailsFixToBasket.class);
                    if (detailsFixToBasket.getName().equals("")) continue;
                    detailsFixToBaskets.add(detailsFixToBasket);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < detailsFixToBaskets.size(); i++) {
            if((detailsFixToBaskets.get(i).getName().equals(""))){
                continue;
            }
            totalPrice += detailsFixToBaskets.get(i).getPrice();
        }
        basketAdapter = new BasketAdapter(detailsFixToBaskets, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(basketAdapter);


        // 초기설정 - 해당 프로젝트(안드로이드)의 application id 값을 설정합니다. 결제와 통계를 위해 꼭 필요합니다.
        BootpayAnalytics.init(this, application_id);

        // 통계 - 유저 로그인 시점에 호출
        BootpayAnalytics.login(
                phone, // bootUser 고유 id 혹은 로그인 아이디
                email, // bootUser email
                user_name, // bootUser 이름
                Gender.UNKNOWN, //1: 남자, 0: 여자
                null, // bootUser 생년월일 앞자리
                phone, // bootUser 휴대폰 번호
                null); //  서울|인천|대구|대전|광주|부산|울산|경기|강원|충청북도|충북|충청남도|충남|전라북도|전북|전라남도|전남|경상북도|경북|경상남도|경남|제주|세종 중 택 1
        startTrace();
        //결제=========================================
    }

    public void orderInsert(){

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String formatDate = sdfNow.format(date);
        int eachCount = 0;
        Gson gson = new Gson();
        pay = true;
        orderInsertParsing = new OrderInsertParsing();
        orderInsertParsing.setPhone(phone);
        orderInsertParsing.setStore_id(store_id);
        orderInsertParsing.setReceipt_id(receiptId);
        orderInsertParsing.setTotal_price(totalPrice);
        orderInsertParsing.setDiscount_price(discount_price);
        orderInsertParsing.setCoupon_id(used_coupon_id);
        orderInsertParsing.setOrder_date(formatDate);

        for (int i = 0; i < detailsFixToBaskets.size(); i++) {
            OrderInsertParsingChild orderInsertParsingChild = new OrderInsertParsingChild();
            DetailsFixToBasket detailsFixToBasket = detailsFixToBaskets.get(i);
            orderInsertParsingChild.setMenu_id(detailsFixToBasket.getMenu_id());
            orderInsertParsingChild.setMenu_defaultprice(detailsFixToBasket.getDefaultPrice());
            orderInsertParsingChild.setOrder_count(detailsFixToBasket.getCount());
            orderInsertParsingChild.setMenu_name(detailsFixToBasket.getName());
            eachCount+=detailsFixToBasket.getCount();
            OrderInsertParsingChild2 orderInsertParsingChild2 = null;
            ArrayList<OrderInsertParsingChild2> orderInsertParsingChild2s = new ArrayList<>();
            HashMap<String, ExtraOrder> essentials = detailsFixToBasket.getEssentialOptions();
            Iterator<String> iterator = essentials.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                ExtraOrder data = essentials.get(key);

                orderInsertParsingChild2 = new OrderInsertParsingChild2(data.getExtra_id(), data.getExtra_price(), data.getExtra_name(),data.getExtra_count());
                orderInsertParsingChild2s.add(orderInsertParsingChild2);
            }
            HashMap<String, ExtraOrder> nonEssentials = detailsFixToBasket.getNonEssentialoptions();
            iterator = nonEssentials.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                ExtraOrder data = nonEssentials.get(key);
                int id = data.getExtra_id();
                String name = data.getExtra_name();
                int price = data.getExtra_price();
                int count = data.getExtra_count();
                orderInsertParsingChild2 = new OrderInsertParsingChild2(id, price,name, count);
                orderInsertParsingChild2s.add(orderInsertParsingChild2);
            }
            orderInsertParsingChild.setExtras(orderInsertParsingChild2s);
            orderInsertParsing.getOrders().add(orderInsertParsingChild);
            orderInsertParsing.setEach_count(eachCount);
        }

        String result = gson.toJson(orderInsertParsing, OrderInsertParsing.class);
        Log.e("result", result);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("result", result);
        try {
            makeRequest(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onClickBack(View view) {
//        if (!pay) {
//            SharedPreferences sharedPreferences = getSharedPreferences("basketList", MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            Gson gson = new Gson();
//            String json = gson.toJson(detailsFixToBaskets);
//            Log.i("json", json);
//            editor.putString(IN_MY_BASEKT, json);
//            editor.putInt("orderCnt", detailsFixToBaskets.size());
//            editor.commit();
//        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(Basket.BasketList,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.i("onresDETAIL_SIZE", detailsFixToBaskets.size()+"");
        editor.putInt("orderCnt", detailsFixToBaskets.size());
        editor.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences(Basket.BasketList,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(!isOpen){
            editor.remove(Basket.IN_MY_BASEKT);
            editor.commit();
            Log.e("ddddddddddd",sharedPreferences.getString(Basket.IN_MY_BASEKT,"")+"귀로");
        }
        else if(!pay) {
            Gson gson = new Gson();
            String json = gson.toJson(detailsFixToBaskets);
            Log.i("json", json);
            editor.putString(IN_MY_BASEKT, json);
            editor.commit();
        }else{
            ;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!pay) {
            SharedPreferences sharedPreferences = getSharedPreferences("basketList", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(detailsFixToBaskets);
            Log.i("json", json);
            editor.putString(IN_MY_BASEKT, json);
            Log.i("ondesDETAIL_SIZE", detailsFixToBaskets.size()+"");
            editor.putInt("orderCnt", (detailsFixToBaskets.size()));
            editor.commit();
        }
    }

    private void findUserForm() {
        final String phoneNumber = phone;
        final String user_id = phone;

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("user_id", user_id);
        hashMap.put("phone", phoneNumber);
        String lastUrl = "BillingGetUserToken.do";
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake(lastUrl);
        Log.i("qerw", phoneNumber);
        Log.i("qerw", user_id);
        makeRequest2(url, hashMap);
    }

    private void makeRequest2(String url, HashMap<String, String> hashMap) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hashMap),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        applyJson(response);
                        Log.i("qerw", response.toString());
                        Log.i("pn", phone);
                        startBootPay(user_token);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }
    public synchronized void applyJson(final JSONObject result) {
        jsonParsing(result);
    }

    private void jsonParsing(JSONObject result) {
        try {
            expired_unixtime = result.getLong("expired_unixtime");
            expired_localtime = result.getString("expired_localtime");
            user_token = result.getString("user_token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void recalculateTotalPrice(){
        totalPrice = 0;
        for (int i = 0; i < detailsFixToBaskets.size(); i++) {
            if((detailsFixToBaskets.get(i).getName().equals(""))){
                continue;
            }
            totalPrice += detailsFixToBaskets.get(i).getPrice();
        }
    }

    public void onClick_onestore(View v) {
        Toast.makeText(this, "왜안되노", Toast.LENGTH_LONG).show();

        recalculateTotalPrice();
        CouponDialog couponDialog = CouponDialog.newInstance(Basket.this, new CouponDialog.CouponDialogListener() {
            @Override
            public void clickBtn(int discountTotal, int dc, int coupon_id) {
                realTotalPrice = discountTotal;
                discount_price = dc;
                used_coupon_id = coupon_id;
                clarityIsOpenStore();
            }
        });
        Bundle bundle = couponDialog.getArguments();
        bundle.putInt("totalPrice", totalPrice);
        bundle.putString("phone", phone);
        couponDialog.show(getSupportFragmentManager(), "couponDialog");
    }
    private void clarityIsOpenStore(){
        String lastUrl = "StoreCheckIsOpen.do?store_id="+store_id;
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake(lastUrl);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("clarityIsOpenStore",response);
                        Gson gson = new Gson();
                        ClarityIsOpenStoreParsing clarityIsOpenStoreParsing = gson.fromJson(response,ClarityIsOpenStoreParsing.class);
                        isOpen = clarityIsOpenStoreParsing.getResult();
                        if(isOpen==true) {
                            findUserForm();
                        }else{
                            SharedPreferences sharedPreferences = getSharedPreferences(Basket.BasketList, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove(Basket.IN_MY_BASEKT);
                            editor.commit();
                            StoreCloseDialog storeCloseDialog = new StoreCloseDialog(Basket.this);
                            storeCloseDialog.callFunction();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("clarityIsOpenStore",error.toString());
                    }
                });
        requestQueue.add(request);
    }
    private synchronized void makeRequest(final String json) throws JSONException {
        String lastUrl = "OrderInsert.do";
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake(lastUrl);
        Log.e("jsonNNN",json);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("OrderDetails", response);

                        connectToWebSocket(phone, store_id, json);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("OrderDetails", "error");
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return json == null ? null : json.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", json, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        requestQueue.add(request);
    }

    private void connectToWebSocket(final String phnone, final int store_id, final String message) {
        Log.i("webSocket", "connectToWebSocket() called.");
        URI uri;
        try {
            uri = new URI("ws://15.165.22.64:8080/websocket");
        } catch(Exception e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri, new Draft_17()) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.i("webSocket Open", "opened.");
                webSocketClient.send("connect:::" + phone);
                webSocketClient.send("message:::" + store_id + ":::" + message);
            }

            @Override
            public void onMessage(String message) {
                // TODO
                Log.i("webSocket Message", message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.i("webSocket Close", "closed. reason : " + reason);
            }

            @Override
            public void onError(Exception ex) {
                Log.i("webSocket Error", ex.getMessage());
            }
        };
        webSocketClient.connect();
    }
    public void startTrace() {
        List<StatItem> items = new ArrayList<>();
        for (int i = 0; i < detailsFixToBaskets.size(); i++) {
            items.add(new StatItem(detailsFixToBaskets.get(i).getName(), null, detailsFixToBaskets.get(i).getMenu_id() + "",
                    null, null, null));
        }
        Log.i("zzz","" + items.size());
        BootpayAnalytics.start("ItemListActivity", "item_list", items);
    }

    void startBootPay(String userToken) {
        Log.e("1234",1+"");
        if(getFragmentManager().isDestroyed()){
            Log.e("de","de");
            return;
        }
        if(getFragmentManager() ==null){
            Log.e("사라","사라");
        }
        BootUser bootUser = new BootUser().setPhone(phone);
        BootExtra bootExtra = new BootExtra().setQuotas(new int[]{0, 2, 3});
        BootpayBuilder bootpayBuilder = Bootpay.init(getFragmentManager());
        bootpayBuilder.setContext(this)
                .setApplicationId(application_id) // 해당 프로젝트(안드로이드)의 application id 값
                .setPG(PG.NICEPAY) // 결제할 PG 사
                .setContext(this)
                .setEasyPayUserToken(user_token)
                .setMethodList(Arrays.asList(Method.EASY_CARD, Method.PHONE, Method.BANK, Method.CARD, Method.VBANK))
                .setBootExtra(bootExtra)
                .setBootUser(bootUser)
                //               .setUserPhone("010-1234-5678") // 구매자 전화번호
                .setUX(UX.PG_DIALOG)
                .setMethod(Method.EASY_CARD) // 결제수단
                //.isShowAgree(true)
                .setName(storeName) // 결제할 상품명
                .setOrderId(expired_localtime + user_name) // 결제 고유번호
                .setPrice(realTotalPrice) // 결제할 금액
//                .setAccountExpireAt("2019-07-16")
                .onConfirm(new ConfirmListener() { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
                    @Override
                    public void onConfirm(@Nullable String message) {
                        if (0 < stuck) Bootpay.confirm(message); // 재고가 있을 경우.
                        else Bootpay.removePaymentWindow(); // 재고가 없어 중간에 결제창을 닫고 싶을 경우
                        Log.d("confirm", message);
                    }
                })
                .onDone(new DoneListener() { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
                    @Override
                    public void onDone(@Nullable String message) {
                        Log.d("done", message);
                        SharedPreferences shf = getApplicationContext().getSharedPreferences("basketList", MODE_PRIVATE);
                        SharedPreferences.Editor editor = shf.edit();
                        editor.clear().commit();
                        HashMap<String, String> hashMap = new HashMap<>();
                        String price = null;
                        String receipt_id = null;
                        Gson gson = new Gson();
                        ReceiptRecordParsing receiptRecordParsing = gson.fromJson(message,ReceiptRecordParsing.class);
                        price = receiptRecordParsing.getPrice();
                        receipt_id = receiptRecordParsing.getReceipt_id();
                        receiptId = receipt_id;
                        hashMap.put("price", price);
                        hashMap.put("receipt_id", receipt_id);
                        String url = "";
                        Log.e("price", price);
                        Log.e("rece", receipt_id);
                        String lastUrl2 = "BillingVerify.do";
                        UrlMaker urlMaker = new UrlMaker();
                        String url2 = urlMaker.UrlMake(lastUrl2);
                        makeRequest3(url2, hashMap);
                        OrderDoneDialog orderDoneDialog = new OrderDoneDialog(Basket.this);
                        orderDoneDialog.callFunction();

                    }
                })
                .onReady(new ReadyListener() { // 가상계좌 입금 계좌번호가 발급되면 호출되는 함수입니다.
                    @Override
                    public void onReady(@Nullable String message) {
                        Log.d("ready", message);
                    }
                })
                .onCancel(new CancelListener() { // 결제 취소시 호출
                    @Override
                    public void onCancel(@Nullable String message) {
                        Log.d("cancel", message);
                        OrderCancelDialog orderCancelDialog = new OrderCancelDialog(Basket.this);
                        orderCancelDialog.callFunction();
                    }
                })
                .onError(new ErrorListener() { // 에러가 났을때 호출되는 부분
                    @Override
                    public void onError(@Nullable String message) {
                        Log.d("error", message);
                        String getMessage ="";
                        try {
                            JSONObject jsonObject = new JSONObject(message);
                            getMessage = jsonObject.getString("msg");
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(!getMessage.equals("")) {
                            OrderLimitExcessDialog orderLimitExcessDialog = new OrderLimitExcessDialog(Basket.this, getMessage);
                            orderLimitExcessDialog.callFunction();
                        }
                        else {
                            return;
                        }
                        //오류
                    }
                })
                .onClose(
                        new CloseListener() { //결제창이 닫힐때 실행되는 부분
                            @Override
                            public void onClose(String message) {
                                Log.d("close", "close");
                            }
                        });
        for (int i = 0; i < detailsFixToBaskets.size(); i++) {
            if((detailsFixToBaskets.get(i).getName().equals(""))){
                continue;
            }
            bootpayBuilder.addItem(detailsFixToBaskets.get(i).getName(), detailsFixToBaskets.get(i).getCount(),
                    "" + detailsFixToBaskets.get(i).getMenu_id(), detailsFixToBaskets.get(i).getPrice());
        }
        bootpayBuilder.request();
    }

    private synchronized void makeRequest3(String url, HashMap hashMap) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        Log.i("aaaa", url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(hashMap),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("success", response.toString());
                        SharedPreferences sharedPreferences = getSharedPreferences(BasketList, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(IN_MY_BASEKT,"");
                        editor.commit();
                        String inMyBasket = sharedPreferences.getString(IN_MY_BASEKT, "");
                        Log.i("inmyyyyyyyyyy", inMyBasket);
                        Log.i("inmyyyyyyyyy",inMyBasket+"22");
                        orderInsert();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("e", error.toString());
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void callbackRestToken(RestTokenData acessToken) {
        String user_id = String.valueOf(System.currentTimeMillis()); //고유 user_id로 고객별로 유니크해야하며, 다른 고객과 절대로 중복되어서는 안됩니다

        BootUser user = new BootUser();
        user.setID(phone);
        user.setArea(null);
        user.setGender(Gender.UNKNOWN); //1: 남자, 0: 여자
        user.setEmail(email);
        user.setPhone(phone);
        user.setBirth(null);
        user.setUsername(user_name);


        BootpayRest.getEasyPayUserToken(this, this, acessToken.token, user);
    }

    @Override
    public void callbackEasyPayUserToken(RestEasyPayUserTokenData userToken) {
        Log.e("2222222","2222222");
        startBootPay(user_token);
    }
    //결제==================================
}
