package com.example.wantchu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Database.SessionManager;
import com.example.wantchu.Fragment.TopBar;
import com.example.wantchu.Url.UrlMaker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

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

        sessionManager = new SessionManager(ChangeEmail.this, SessionManager.SESSION_USERSESSION);
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
        Log.i("login", "request made to " + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Register2", response.toString());
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
            startActivity(new Intent(getApplicationContext(), ChangeEmail2.class));
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
        super.onBackPressed();
    }
}