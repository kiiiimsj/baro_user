package com.example.wantchu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Fragment.TopBar;
import com.example.wantchu.Url.UrlMaker;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ChangePass2 extends AppCompatActivity implements TopBar.OnBackPressedInParentActivity {
    TextInputLayout pass1, pass2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass2);
        pass1 = findViewById(R.id.newPassword);
        pass2 = findViewById(R.id.newPasswordConfirm);

    }
    public void onClickPassword(View view) {
        //새로 변경할 비밀번호2개를 클릭하고 확인버튼 누를시
        if (!validatePassword() | !validateConfirmPassword()) {
            return;
        }
        String _newPass = pass1.getEditText().getText().toString().trim();
        String _phone = getIntent().getStringExtra("phone");

        HashMap<String, String> changePassUser = new HashMap<>();
        changePassUser.put("phone", _phone);
        changePassUser.put("pass", _newPass);

        makeRequestForFindPass(changePassUser);
    }

    private boolean validatePassword() {
        String _pass = pass1.getEditText().getText().toString();
        String passPolicy = "[0-9a-zA-Z].{4,}$";

        if(_pass.isEmpty()){
            pass1.setError("비밀번호를 입력해주세요");
            return false;
        }
        else if(!_pass.matches(passPolicy)){
            pass1.setError("적어도 4글자이상 입력해주세요");
            return false;
        }
        else{
            pass1.setError(null);
            pass1.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        String _pass1 = pass1.getEditText().getText().toString();
        String _pass2 = pass2.getEditText().getText().toString();

        if(_pass2.isEmpty()){
            pass2.setError("비밀번호를 입력해주세요");
            return false;
        }
        else if(!_pass1.equals(_pass2)){
            pass2.setError("비밀번호와 비밀번호확인이 일치하지 않습니다.");
            return false;
        }
        else{
            pass2.setError(null);
            pass2.setErrorEnabled(false);
            return true;
        }
    }

    public void callBackFromFindPass(View view) { super.onBackPressed(); }
    public String urlMaker() {
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake("");
        StringBuilder urlBuilder = new StringBuilder(url);
        urlBuilder.append(ChangePass2.this.getString(R.string.passUpdate));

        return urlBuilder.toString();
    }
    public void makeRequestForFindPass(HashMap data) {
        String url = new UrlMaker().UrlMake("MemberPassUpdate.do");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.i("login", "request made to " + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("FindPass1", response.toString());
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
        boolean result = false;
        String message = null;
        try {
            result =response.getBoolean("result");
            message = response.getString("message");
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
        check(result, message);
    }
    public boolean check (boolean result, final String message) {
        if(result) {
            Toast.makeText(ChangePass2.this, message, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ChangePass2.this, Login.class));
            finish();
        }
        Toast.makeText(ChangePass2.this, message, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onBack() {
        super.onBackPressed();
    }
}