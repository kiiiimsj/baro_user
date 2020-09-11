package com.example.wantchu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Database.SessionManager;
import com.example.wantchu.JsonParsingHelper.TypeParsing;
import com.example.wantchu.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Inquiry extends AppCompatActivity {
    TextInputLayout title;
    TextInputLayout content;
    TypeParsing getResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);
        title = findViewById(R.id.title_input);
        content = findViewById(R.id.content_input);
    }

    public void onClickBack(View view) {
        startActivity(new Intent(getApplicationContext(), InquiryList.class));
        finish();
    }
    public boolean checkOutFill() {
        String titleCotent = title.getEditText().getText().toString();
        String contentContent = content.getEditText().getText().toString();

        if(titleCotent.isEmpty()) {
           title.setError("제목을 입력해주세요");
           title.requestFocus();
           return false;
        }
        if(!titleCotent.isEmpty()) {
            title.setError("null");
            title.setErrorEnabled(false);
        }
        if(contentContent.isEmpty()) {
            content.setError("내용을 입력해주세요");
            content.requestFocus();
            return false;
        }
        if(!contentContent.isEmpty()) {
            content.setError("null");
            content.requestFocus();
        }

        return true;
    }
    public void onClickSend(View view) {
        if(!checkOutFill()) {
            return;
        }
        SessionManager sessionManager = new SessionManager(getApplicationContext(), SessionManager.SESSION_USERSESSION);
        sessionManager.getUsersDetailSession();
        HashMap<String, String> sendUserData = new HashMap<>();

        HashMap<String, String> userData =sessionManager.getUsersDetailFromSession();

        sendUserData.put("email", userData.get(SessionManager.KEY_EMAIL));
        sendUserData.put("title", title.getEditText().getText().toString());
        sendUserData.put("content", title.getEditText().getText().toString());
        makeRequestForInquiryWrite(urlMaker(), sendUserData);
    }
    public String urlMaker() {
        StringBuilder urlBuilder = new StringBuilder(getApplicationContext().getString(R.string.SERVER));
        urlBuilder.append(getApplicationContext().getString(R.string.InquirySave));

        return urlBuilder.toString();
    }
    public void makeRequestForInquiryWrite(String url, HashMap data) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.i("login", "request made to " + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Register2", response.toString());
                        Parsing(response.toString());
                        try {
                            Toast.makeText(Inquiry.this, response.getString("message"), Toast.LENGTH_LONG).show();
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

    private void Parsing(String response) {
        Gson gson = new Gson();
        getResult = gson.fromJson(response, TypeParsing.class);
        getActionByResult(getResult);
    }

    private void getActionByResult(final TypeParsing result) {
        if(result.isResult()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Inquiry.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            startActivity(new Intent(getApplicationContext(), InquiryList.class));
        }
        else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Inquiry.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}