package com.tpn.baro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

public class ChangeEmail extends AppCompatActivity implements TopBar.OnBackPressedInParentActivity {
    String email;
    String phone;
    HashMap<String ,String> sessionUserdata;
    SharedPreferences.Editor editor;
    TextInputLayout newEmailInput;
    TextView oldEmail;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        oldEmail = findViewById(R.id.old_email);
        newEmailInput = findViewById(R.id.new_email);

        sessionManager = new SessionManager(getApplicationContext(), SessionManager.SESSION_USERSESSION);
        sessionUserdata = sessionManager.getUsersDetailFromSession();
        editor = sessionManager.getDetailEditor();

        email = sessionUserdata.get(SessionManager.KEY_EMAIL);
        phone = sessionUserdata.get(SessionManager.KEY_PHONENUMBER);
        oldEmail.setText(email);
    }

    private boolean checkInputEmail() {
        String emailString = newEmailInput.getEditText().getText().toString();
        String checkEmail = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";

        if(emailString.isEmpty()) {
            newEmailInput.setError("이메일을 입력해주세요");
            newEmailInput.requestFocus();
            return false;
        }
        if(!emailString.matches(checkEmail)) {
            newEmailInput.setError("맞는 이메일 형식을 입력해주세요");
            newEmailInput.requestFocus();
            return false;
        }
        if(emailString.matches(checkEmail)) {
            newEmailInput.setError(null);
            newEmailInput.setErrorEnabled(false);
            return true;
        }
        return true;
    }
    public void makeRequestForEmail(HashMap data) {
        String url = new UrlMaker().UrlMake("MemberEmailUpdate.do");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        jsonParsing(response);
                        try {
                            Toast.makeText(ChangeEmail.this, response.getString("message"), Toast.LENGTH_LONG).show();
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

    private void jsonParsing(JSONObject response) {
        boolean getResult = false;
        String message = null;
        try {
            getResult=response.getBoolean("result");
            message = response.getString("message");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        nextActivity(getResult, message);
    }

    private void nextActivity(boolean getResult, final String message) {
        if(getResult) {
            String newEmailString = newEmailInput.getEditText().getText().toString();
            editor.putString(SessionManager.KEY_EMAIL,newEmailString);
            editor.commit();
            startActivity(new Intent(ChangeEmail.this, ChangeEmail2.class));
            finish();
        }
        else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ChangeEmail.this, message, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    public void onClickEmail(View view) {
        if(!checkInputEmail()) {
            return;
        }
        String newEmailString = newEmailInput.getEditText().getText().toString();
        HashMap<String, String> changeEmailUser = new HashMap<>();

        changeEmailUser.put("phone", phone);
        changeEmailUser.put("email", newEmailString);
        makeRequestForEmail(changeEmailUser);
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
}