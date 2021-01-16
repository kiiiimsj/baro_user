package com.tpn.baro;

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
import com.tpn.baro.Database.SessionManager;
import com.tpn.baro.Fragment.TopBar;
import com.tpn.baro.R;
import com.tpn.baro.Url.UrlMaker;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import maes.tech.intentanim.CustomIntent;

public class ChangePass1Logging extends AppCompatActivity implements TopBar.OnBackPressedInParentActivity {
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
    public void verifyPass(View view) {
        checkPassRight();
    }

    public void makeRequestForCheckPass(HashMap data) {
        String url = new UrlMaker().UrlMake("MemberLogin.do");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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
    }
    public void checkPassRight() {
        final HashMap<String ,String > userData = new HashMap<>();
        userData.put("phone", phone);
        userData.put("pass", passInput.getEditText().getText().toString());

        makeRequestForCheckPass(userData);
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
            Intent intent = new Intent(ChangePass1Logging.this, ChangePass2.class);
            intent.putExtra("phone", phone);
            startActivity(intent);
            finish();
        }
        return true;
    }

    @Override
    public void onBack() {
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CustomIntent.customType(this,"right-to-left");
    }
    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this,"right-to-left");
    }
}