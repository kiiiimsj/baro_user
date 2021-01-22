package com.tpn.baro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tpn.baro.Database.SessionManager;
import com.tpn.baro.R;
import com.tpn.baro.Url.UrlMaker;
import com.tpn.baro.helperClass.BaroUtil;
import com.tpn.baro.helperClass.CheckInternet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    TextInputLayout phone, password;
    CheckBox rememberUser;
    TextInputEditText phoneEditText, passwordEditText;
    SessionManager sessionManager;
    SessionManager userSession;
    String userToken;
    ProgressApplication progressApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressApplication = new ProgressApplication();
        progressApplication.progressON(this);
        FirebaseInstanceId.getInstance().getInstanceId()
        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(!task.isSuccessful()){
                    return;
                }
                userToken = task.getResult().getToken();
            }
        });

        userSession = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        phone = findViewById(R.id.login_phone);
        password = findViewById(R.id.login_password);
        rememberUser = findViewById(R.id.map_permission);
        phoneEditText = findViewById(R.id.login_phone_editText);
        passwordEditText =findViewById(R.id.login_password_editText);

        phoneEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone.setError(null);
            }
        });
        passwordEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.setError(null);
            }
        });

        sessionManager = new SessionManager(Login.this, SessionManager.SESSION_REMEMMBERME);
        if (sessionManager.checkRememberMe()) {
            HashMap<String, String> rememberMeDetails = sessionManager.getRemeberMeDetailsFromSession();
            rememberUser.setChecked(true);
            phoneEditText.setText(rememberMeDetails.get(SessionManager.KEY_SESSIONPHONENUMBER));
            passwordEditText.setText(rememberMeDetails.get(SessionManager.KEY_SESSIONPASSWORD));
        }
        rememberUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(rememberUser.isChecked())) {
                    sessionManager.clearRememberMeSession();
                    sessionManager.getEditor().putBoolean("IsRememberMe",false);
                    sessionManager.getEditor().commit();
                }
            }
        });
        progressApplication.progressOFF();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    public void onClickLogin(View view){
        CheckInternet checkInternet = new CheckInternet();

        if (!checkInternet.isConnected(this)) {
            BaroUtil.showCustomDialog(Login.this);
            return;
        }
        if(!validateFields()){
            return;
        }

        final String _phone = phone.getEditText().getText().toString().trim();
        final String _pass = password.getEditText().getText().toString().trim();

        if (rememberUser.isChecked()) {
            sessionManager.createRememberMeSession(_phone, _pass);
        }
        else{
            sessionManager.createRememberMeSession(_phone, _pass);
            sessionManager.getEditor().putBoolean("IsRememberMe",false);
            sessionManager.getEditor().commit();
            sessionManager.clearRememberMeSession();
        }

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("phone", _phone);
        hashMap.put("pass", _pass);
        hashMap.put("device_token", userToken);
        String lastUrl = "MemberLogin.do";
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake(lastUrl);
        makeRequest(url, hashMap);
    }
    
    public void makeRequest(String url, HashMap data) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        applyJson(response);
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
    public void applyJson(final JSONObject result) {
        String name = null;
        String phone = null;
        String createdDate = null;
        String email = null;
        String message = null;
        boolean result2 = false;
        try {
            result2 = result.getBoolean("result");
            name = result.getString("nick");
            phone = result.getString("phone");
            createdDate = result.getString("created_date");
            email = result.getString("email");
            message = result.getString("message");
        }
        catch(JSONException e ) {
            e.printStackTrace();
        }
        if(result2) {
            userSession.createLoginSession(name, phone, createdDate, email, userToken);
            finish();
        }
        else {
            Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();
        }
    }
    private boolean validateFields() {
        String _phone = phone.getEditText().getText().toString().trim();
        String _pass = password.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{0,20}\\z";
        if(_phone.isEmpty()){
            phone.setError("휴대폰 번호를 입력해주세요");
            phone.requestFocus();
            return false;
        }
        else if(_pass.isEmpty()){
            password.setError("비밀번호를 입력해주세요");
            password.requestFocus();
            return false;
        }
        else if(!_phone.matches(checkspaces)){
            phone.setError("빈공간없이 작성해주세요");
            return false;
        }
        else{
            phone.setError(null);
            password.setError(null);
            phone.setErrorEnabled(false);
            password.setErrorEnabled(false);
            return true;
        }
    }
    public void onClickFindPass(View view){
        startActivity(new Intent(Login.this, FindPass1.class));
    }

    public void onClickRegister(View view){
        startActivity(new Intent(Login.this, GetPermissionBeforeRegister.class));
    }
}
