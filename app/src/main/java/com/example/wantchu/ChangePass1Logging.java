package com.example.wantchu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Database.SessionManager;
import com.example.wantchu.Url.UrlMaker;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ChangePass1Logging extends AppCompatActivity {
    //private final static String SERVER = "http://54.180.56.44:8080/";
    private TextInputLayout passInput;
    private String phone;

    private HashMap<String, String> sessionUserdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass1_logging);
        passInput = findViewById(R.id.input_current_pass);

        SessionManager sessionManager = new SessionManager(getApplicationContext(), SessionManager.SESSION_USERSESSION);
        sessionUserdata = sessionManager.getUsersDetailFromSession();

        phone = sessionUserdata.get(SessionManager.KEY_PHONENUMBER);

    }
    public String urlMaker() {
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake("");
        StringBuilder urlBuilder = new StringBuilder(url);
        urlBuilder.append(getApplicationContext().getString(R.string.login));

        return urlBuilder.toString();
    }

    public void callBackFromUpdatePass(View view) {
        super.onBackPressed();
    }

    public void verifyPass(View view) {
        checkPassRight();
    }
    public void makeRequestForCheckPass(String url, HashMap data) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.i("login", "request made to " + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("FindPass1Logging", response.toString());
                        jsonParsing(response);
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

    private void jsonParsing(JSONObject response) {
        Boolean getLogin = false;
        try {
            getLogin = response.getBoolean("result");

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        checkPass(getLogin);
        Log.i("JSONPARSING", getLogin+"");
    }
    public void checkPassRight() {
        final HashMap<String ,String > userData = new HashMap<>();
        userData.put("phone", phone);
        userData.put("pass", passInput.getEditText().getText().toString());

        makeRequestForCheckPass(urlMaker(), userData);
    }
    public boolean checkPass(boolean getBool) {
        String inputPass = passInput.getEditText().getText().toString();
        if(!getBool) {
            passInput.setError("비밀번호가 틀립니다.");
            passInput.requestFocus();
            return false;
        }
        if(inputPass.isEmpty()) {
            passInput.setError("비밀번호를 입력해주세요");
            passInput.requestFocus();
            return false;
        }
        else {
            Intent intent = new Intent(getApplicationContext(), ChangePass2.class);
            intent.putExtra("phone", phone);
            startActivity(intent);
            finish();
        }
        return true;
    }
}