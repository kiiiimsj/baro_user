package com.example.wantchu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

//import com.example.wantchu.Database.SendToServer;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Fragment.TopBar;
import com.example.wantchu.HelperDatabase.RegisterUser;
import com.example.wantchu.Url.UrlMaker;
import com.example.wantchu.helperClass.CheckInternet;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Register2 extends AppCompatActivity implements TopBar.OnBackPressedInParentActivity {
    //private final static String SERVER= "http://54.180.56.44:8080/";
    private String message;

    TextInputLayout pass1, pass2, email, userName;
    private String phone;

    boolean getResult = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
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
        if(getResult) {
            //storeNewPhoneData();
            startActivity(new Intent(Register2.this, Login.class));
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
            Log.i("register2", getResult+"");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    private void storeNewPhoneData() {
//        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
//        DatabaseReference reference = rootNode.getReference("Users");
//        reference.child(phone).setValue("");
//    }
    public void makeRequestForRegister(String url, HashMap data) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.i("login", "request made to " + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Register2", response.toString());
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
}