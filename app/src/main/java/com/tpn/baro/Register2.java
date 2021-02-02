package com.tpn.baro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

//import com.example.baro.Database.SendToServer;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tpn.baro.Dialogs.CouponRegisterDialog;
import com.tpn.baro.Fragment.TopBar;
import com.tpn.baro.HelperDatabase.RegisterUser;
import com.tpn.baro.JsonParsingHelper.DefaultParsing;
import com.tpn.baro.R;
import com.tpn.baro.Url.UrlMaker;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Register2 extends AppCompatActivity implements TopBar.OnBackPressedInParentActivity, CouponRegisterDialog.OnDismiss {
    //private final static String SERVER= "http://54.180.56.44:8080/";
    private String message;

    TextInputLayout pass1, pass2, email, userName;
    private String phone;

    boolean getResult = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        userName = findViewById(R.id.user_name);
        pass1 = findViewById(R.id.login_password);
        pass2 = findViewById(R.id.login_password_confirm);
        email = findViewById(R.id.signup_email);

        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
    }

    private boolean checkInput() {
        boolean result = false;
        String userNameStr = userName.getEditText().getText().toString().trim();
        String pass1Str = pass1.getEditText().getText().toString().trim();
        String pass2Str = pass2.getEditText().getText().toString().trim();
        String emailStr = email.getEditText().getText().toString().trim();
        String checkEmail = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";

        if(userNameStr.isEmpty() ){
            userName.setError("이름을 입력하셔야 합니다.");
            userName.requestFocus();
            return result;
        }
        if(!userNameStr.isEmpty() ){
            userName.setError(null);
            userName.setErrorEnabled(false);
        }

        if(pass1Str.isEmpty()){
            pass1.setError("비밀번호를 작성하셔야 합니다.");
            pass1.requestFocus();
            return result;
        }
        if(!pass1Str.isEmpty()){
            pass1.setError(null);
            pass1.setErrorEnabled(false);
        }

        if(pass2Str.isEmpty()){
            pass2.setError("비밀번호 확인을 해야합니다.");
            pass2.requestFocus();
            return result;
        }

        if(!pass2Str.equals(pass1Str)) {
            pass2.setError("비밀번호가 일치하지 않습니다.");
            pass2.requestFocus();
            return result;
        }


        if(emailStr.isEmpty()){
            email.setError("이메일을 입력해야합니다.");
            email.requestFocus();
            return result;
        }
        if(!emailStr.isEmpty() ){
            if(!emailStr.matches(checkEmail)) {
                email.setError("형식에 맞는 이메일을 입력해주세요.");
                email.requestFocus();
                return result;
            }
            email.setError(null);
            email.setErrorEnabled(false);
        }
        getResult = true;
        return true;
    }


    public void onClickRegister(View view) {
        //회원가입 완료버튼 눌렀을때
        if(!checkInput()){ return; }
        String userNameStr = userName.getEditText().getText().toString();
        String pass1Str = pass1.getEditText().getText().toString();
        String emailStr = email.getEditText().getText().toString();
        String phoneSubString = "0" + phone.substring(3);
        RegisterUser registerUser = new RegisterUser(phoneSubString, emailStr, userNameStr, pass1Str);

        HashMap<String, String> registerUsers = new HashMap<>();
        registerUsers.put("phone", registerUser.getPhone());
        registerUsers.put("email", registerUser.getEmail());
        registerUsers.put("nick", registerUser.getNick());
        registerUsers.put("pass", registerUser.getPass());

        makeRequestForRegister(urlMaker(), registerUsers);
    }
    public String urlMaker() {
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake("");
        StringBuilder urlBuilder = new StringBuilder(url);
        urlBuilder.append(Register2.this.getString(R.string.register));

        return urlBuilder.toString();
    }
    private void jsonParsing(JSONObject jsonObject) {
        try {
            getResult = jsonObject.getBoolean("result");
            message = jsonObject.getString("message");
            if(getResult) {
                makeRequestForInsertCoupon();
            }
            
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void makeRequestForInsertAlertFirstRegister() {
        String url = new UrlMaker().UrlMake("InsertAllForNew.do?phone="+phone);
        RequestQueue requestQueue = Volley.newRequestQueue(Register2.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CouponRegisterDialog couponRegisterDialog = new CouponRegisterDialog(Register2.this, Register2.this, null);
                        couponRegisterDialog.outSideMessage = "바로와 함께하시게 되신걸 축하드립니다!\n1000원 할인 쿠폰을 드렸어요!";
                        couponRegisterDialog.callFunction();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG",error.toString());
                    }
                });
        requestQueue.add(stringRequest);
    }
    private void makeRequestForInsertCoupon() {
        String url = new UrlMaker().UrlMake("CouponInsertByNumber.do?phone="+phone+"&coupon_id=9");
        RequestQueue requestQueue = Volley.newRequestQueue(Register2.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        makeRequestForInsertAlertFirstRegister();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG",error.toString());
                    }
                });
        requestQueue.add(stringRequest);
    }

    //    private void storeNewPhoneData() {
//        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
//        DatabaseReference reference = rootNode.getReference("Users");
//        reference.child(phone).setValue("");
//    }
    public void makeRequestForRegister(String url, HashMap data) {
        RequestQueue requestQueue = Volley.newRequestQueue(Register2.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        jsonParsing(response);
                        try {
                                Toast.makeText(Register2.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        } catch(JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("login-error", error.toString());
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onBack() {
        super.onBackPressed();
    }

    @Override
    public void clickDismiss() {
        if(getResult) {
            //storeNewPhoneData();
            finish();
        }
        else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Register2.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}