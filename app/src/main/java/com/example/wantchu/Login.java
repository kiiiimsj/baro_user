package com.example.wantchu;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Database.SessionManager;
import com.example.wantchu.Url.UrlMaker;
import com.example.wantchu.helperClass.CheckInternet;
import com.example.wantchu.helperClass.myGPSListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Login extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, AutoPermissionsListener{

    TextInputLayout phone, password;
    CheckBox rememberUser;
    TextInputEditText phoneEditText, passwordEditText;
    boolean check;
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
        //firebase messaging을 이용하기위해 가져오는 회원 기기의 토큰값
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(!task.isSuccessful()){
                            Log.e("token failed", String.valueOf(task.getException()));
                            return;
                        }
                        userToken = task.getResult().getToken();
                        Log.e("aaa", userToken);
                    }
                });
        startLocationService();

        SharedPreferences shf = getApplicationContext().getSharedPreferences("basketList", MODE_PRIVATE);
        SharedPreferences.Editor editor = shf.edit();
        editor.clear().commit();

        AutoPermissions.Companion.loadAllPermissions(this, 101);

        userSession = new SessionManager(getApplicationContext(), SessionManager.SESSION_USERSESSION);
        phone = findViewById(R.id.login_phone);
        password = findViewById(R.id.login_password);
        rememberUser = findViewById(R.id.remember_user);
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
                    Log.i("ddfa","i");
                    sessionManager.clearRememberMeSession();
                    sessionManager.getEditor().putBoolean("IsRememberMe",false);
                    sessionManager.getEditor().commit();
                }
            }
        });
        progressApplication.progressOFF();
    }
    private void startLocationService() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        long minTime = 10000;
        float minDistance = 0;
        myGPSListener gpsListener = new myGPSListener(this);
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,minTime,minDistance, gpsListener);
        manager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,minTime,minDistance,gpsListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //권한이 없을경우 최초권한요청 또는 사용자에 의한 재요청 확인하는 코드
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) &&
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)){
                //권한 재요청
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            }
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }
    public void onClickLogin(View view){
        CheckInternet checkInternet = new CheckInternet();
        if (!checkInternet.isConnected(this)) {
            showCustomDialog();
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
    
    public synchronized void makeRequest(String url, HashMap data) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.i("login", "request made to " + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // {"result":true, afs}
                        applyJson(response);
                        try {
                            if (response.getBoolean("result")) {

                                startActivity(new Intent(getApplicationContext(), MainPage.class));
                            }
                            else {
                                Toast.makeText(Login.this, response.getString("message"), Toast.LENGTH_LONG).show();
                            }
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
    public void applyJson(final JSONObject result) {
        String name = null;
        String phone = null;
        String createdDate = null;
        String email = null;
        boolean result2 = false;
        try {
            result2 = result.getBoolean("result");
            name = result.getString("nick");
            phone = result.getString("phone");
            createdDate = result.getString("created_date");
            email = result.getString("email");

        }
        catch(JSONException e ) {
            e.printStackTrace();
        }
        if(result2) {
            userSession.createLoginSession(name, phone, createdDate, email, userToken);
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
        startActivity(new Intent(getApplicationContext(), FindPass1.class));
    }

    public void onClickRegister(View view){
        startActivity(new Intent(getApplicationContext(), Register1.class));
    }
    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("인터넷이 연결되어있는지 확인해주세요")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        startActivity(new Intent(getApplicationContext(), RetailerStartUpScreen.class));
//                        finish();
                    }
                });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public void onDenied(int i, String[] strings) {

    }

    @Override
    public void onGranted(int i, String[] strings) {

    }


}
