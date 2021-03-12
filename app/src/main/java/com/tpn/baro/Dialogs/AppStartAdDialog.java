package com.tpn.baro.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tpn.baro.R;
import com.tpn.baro.Url.UrlMaker;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class AppStartAdDialog {
    private static final String TAG = "AppStartAdDialog";
    private Context context;
    int eventId = 0;
    String imageStr;

    SharedPreferences sf;
    SharedPreferences.Editor editor;

    Button closeEver;
    Button justClose;

    ImageView eventImage;
    Dialog dlg;
    public AppStartAdDialog(Context context) {
        this.context = context;
    }
    public void callFunction() {
        dlg = new Dialog(context);
        sf = context.getSharedPreferences("saveEventId", Context.MODE_PRIVATE);
        editor = sf.edit();
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.app_start_ad_dialog);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dlg.setCanceledOnTouchOutside(false);
        dlg.setCancelable(false);

        eventImage = (ImageView) dlg.findViewById(R.id.ad_image);
        closeEver = (Button) dlg.findViewById(R.id.close_ever);
        justClose = (Button) dlg.findViewById(R.id.just_close);
        makeRequestForGetEventPicture();
    }
    public void makeRequestForGetEventPicture() {
        String url = new UrlMaker().UrlMake("EventFindAdvertising.do");
        RequestQueue requestQueue = Volley.newRequestQueue(context);
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

    private void parsing(String response) {
        boolean result = false;
        try {
            JSONObject jsonObject = new JSONObject(response);
            result =jsonObject.getBoolean("result");
            if(result) {
                eventId=jsonObject.getInt("event_id");
                imageStr=jsonObject.getString("event_image");
            }
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
        compareEventIdWithSp();
    }

    private void compareEventIdWithSp() {

        SharedPreferences sf = context.getSharedPreferences("saveEventId", Context.MODE_PRIVATE);
//        int saveEventId = sf.getInt("event_id", 0);
//        if(saveEventId == eventId) {
//            return;
//        }
//        else {
//            dlg.show();
//            makeRequestForImage();
//        }

        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = simpleDate.format(mDate);
        String prevTime = sf.getString("prevTime","1970-01-01");
        Log.e(TAG,prevTime);
        Log.e(TAG,getTime);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Date date1 = null;
            try {
                date1 = simpleDate.parse(prevTime);
                Date date2 = simpleDate.parse(getTime);
                if (!date1.equals(date2)) {
                    dlg.show();
                    makeRequestForImage();
                    setClickEvent(prevTime,getTime);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

    }
    private void setClickEvent(String prevTime, final String getTime) {
        closeEver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                editor.putInt("event_id", eventId);
                editor.putString("prevTime",getTime);
                editor.apply();
                editor.commit();
                dlg.dismiss();
            }
        });
        justClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
    }
    private void makeRequestForImage() {
        final String url = new UrlMaker().UrlMake("ImageEvent.do?image_name="+imageStr);

        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                ImageRequest request = new ImageRequest(url,
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                eventImage.setImageBitmap(response);
                            }
                        }, eventImage.getWidth(), eventImage.getHeight(), ImageView.ScaleType.FIT_XY, null,
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("error", "error");
                            }
                        });
                request.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(request);
            }
        }).start();
    }
}
