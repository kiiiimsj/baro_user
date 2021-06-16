package com.tpn.baro.helperClass;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tpn.baro.Basket;
import com.tpn.baro.Database.SessionManager;
import com.tpn.baro.Dialogs.NeedLoginDialog;
import com.tpn.baro.ListStoreFavoritePage;
import com.tpn.baro.ListStorePage;
import com.tpn.baro.NewMainPage;
import com.tpn.baro.OrderDetails;
import com.tpn.baro.R;
import com.tpn.baro.StoreInfoReNewer;
import com.tpn.baro.Url.UrlMaker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

public class BaroUtil {
    public static int discountRateInt;
    public static int storeId;

    static SharedPreferences sf;

    public static void setDiscountRateInt(int discountRateInt, Context context) {
        sf = context.getSharedPreferences("shared_discount", Context.MODE_PRIVATE);
        Log.e("setDiscount", discountRateInt+"");
        SharedPreferences.Editor sf_editor = sf.edit();
        sf_editor.putInt("discount_rate", discountRateInt);
        sf_editor.apply();
        sf_editor.commit();
    }
    public static int getDiscountRateInt() {
        int discount_rate = sf.getInt("discount_rate", 0);
        return discount_rate;
    }

    public static void printLog(String TAG, String message) {
        int maxLogSize = 2000;
        for(int i = 0; i <= message.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > message.length() ? message.length() : end;
            android.util.Log.d(TAG, message.substring(start, end));
        }
    }

    public static boolean loginCheck(final Activity activity){

        SessionManager sm = new SessionManager(activity, SessionManager.SESSION_USERSESSION);
        SharedPreferences sf = sm.getUsersDetailSession();
        String nick = sf.getString(SessionManager.KEY_USERNAME,"");
        if(nick == null || nick.equals("")){
            NeedLoginDialog needLoginDialog = new NeedLoginDialog(activity);
            needLoginDialog.callFunction();
            return false;
        }
        return true;
    }
    public static void showCustomDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("인터넷이 연결되어있는지 확인해주세요")
            .setCancelable(false)
            .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    activity.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).show();
    }
    public static boolean checkGPS(final Context context) {
        boolean isClose = false;
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("설정");
            builder.setCancelable(true);
            builder.setMessage("어플을 사용하기위해선 위치서비스를 켜주세요");
            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(intent);
                }
            });
            builder.show();
            isClose = true;
        }else {
            isClose = false;
        }
        return isClose;
    }
    public static String pad(int fieldWidth, char padChar, String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = s.length(); i < fieldWidth; i++) {
            sb.append(padChar);
        }
        sb.append(s);
        return sb.toString();
    }
    public boolean getActivityOnPause(Activity activity) {
        if(getTokenActivityName(activity.toString()).equals("NewMainPage")) {
            return NewMainPage.onPause;
        }
        if(getTokenActivityName(activity.toString()).equals("StoreInfoReNewer")) {
            return StoreInfoReNewer.onPause;
        }
        if(getTokenActivityName(activity.toString()).equals("OrderDetails")) {
            return OrderDetails.onPause;
        }
        if(getTokenActivityName(activity.toString()).equals("Basket")) {
            return Basket.onPause;
        }
        if(getTokenActivityName(activity.toString()).equals("ListStoreFavoritePage")) {
            return ListStoreFavoritePage.onPause;
        }
        if(getTokenActivityName(activity.toString()).equals("ListStorePage")) {
            return ListStorePage.onPause;
        }

        return false;
    }
    public boolean checkTopBarTimeThreadActivity(Activity activity) {
        switch (getTokenActivityName(activity.toString())) {
            case "NewMainPage":
            case "StoreInfoReNewer":
            case "OrderDetails":
            case "Basket":
            case "ListStoreFavoritePage":
            case "ListStorePage":
                return true;
            default:
                return false;
        }
    }
    public void fifteenTimer(final TextView timerTextView, final Activity activity) {
        if(!checkTopBarTimeThreadActivity(activity)) {
            return;
        }
        new Thread((new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                while (!getActivityOnPause(activity)) {
                    Calendar calendar = GregorianCalendar.getInstance();
                    String minuteString = BaroUtil.pad(2, '0', calendar.get(Calendar.MINUTE) + "");
                    String secondString = BaroUtil.pad(2, '0', calendar.get(Calendar.SECOND) +"");
                    Log.i("activity : ", activity.toString()+secondString);
                    try {
                        Thread.sleep(1000);
                        final int minuteFinal = 59 - (Integer.parseInt(minuteString) % 60);
                        final int secondFinal = 59 - Integer.parseInt(secondString);
                        if(minuteFinal==0 && secondFinal == 1) {
                            if(storeId != 0) {
                                Log.e("BaroUtil_store_id : ", storeId+"");
                                makeRequestForDiscountRate(storeId, activity);
                            }else {
//                                activity.overridePendingTransition(0, 0);
//                                activity.startActivity(activity.getIntent());
//                                activity.finish();
//                                activity.overridePendingTransition(0, 0);
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        activity.recreate();
                                    }
                                });
                            }
                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timerTextView.setText(BaroUtil.pad(2,'0', minuteFinal+"")+":"+BaroUtil.pad(2, '0',secondFinal+""));
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        })).start();
    }
    public static String getTokenActivityName(String activityName) {
        StringTokenizer stringTokenizer = new StringTokenizer(activityName, ".");
        String getName = "";
        while(stringTokenizer.hasMoreTokens()) {
            getName = stringTokenizer.nextToken();
        }
        return new StringTokenizer(getName, "@").nextToken();
    }
    public void makeRequestForDiscountRate(int storeId, final Activity activity) {
        UrlMaker urlMaker = new UrlMaker();
        String lastUrl = "GetStoreDiscount.do?store_id="+storeId;
        String url = urlMaker.UrlMake(lastUrl);
        RequestQueue requestQueue = Volley.newRequestQueue(activity);

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        Log.e("response", response);
                        setDiscountTextView(response, activity);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(request);
    }
    public void setDiscountTextView(String result, Activity activity) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.getBoolean("result")) {
                discountRateInt = jsonObject.getInt("discount_rate");
                Log.e("discountRateInt : ", discountRateInt+"");
                setDiscountRateInt(discountRateInt, activity);
//                activity.overridePendingTransition(0, 0);
//                activity.getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                //
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    activity.finishAndRemoveTask();
//                }else {
//                    activity.finish();
//                }
//                activity.startActivity(activity.getIntent());
//                activity.overridePendingTransition(0, 0);
                activity.recreate();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * UseAge :
     * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
     *    BaroUtil.setStatusBarColor(Activity.this, this.toString());
     * }
     * @param activity
     * @param activityToString
     */
    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarColor(Activity activity, String activityToString) {
        int setColor = activity.getResources().getColor(R.color.white);
        switch (getTokenActivityName(activityToString)) {
            case "FirstPage":
            case "ChangeEmail":
            case "ChangeEmail2":
            case "ChangePass1Logging":
            case "ChangePass2":
            case "ChangePass3":
            case "FindPass1":
            case "Register1":
            case "Register2":
            case "VerifyOTP":
            case "UpdateEmail":
            case "Terms":
            case "Login":
                activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.main));
                break;
            default:
                activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
                break;
        }
    }
}

