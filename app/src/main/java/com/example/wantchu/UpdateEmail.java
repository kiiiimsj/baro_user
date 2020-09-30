package com.example.wantchu;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.wantchu.Database.SessionManager;
import com.example.wantchu.Url.UrlMaker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class UpdateEmail extends AppCompatActivity {
    //private final static String SERVER = "http://54.180.56.44:8080/";
    //emailUpdate
    TextView currentEmailView;
    SessionManager userSession;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        currentEmailView = findViewById(R.id.current_email);

        HashMap<String, String> updateEmailUser = new HashMap<>();
        userSession = new SessionManager(UpdateEmail.this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> userInfo = userSession.getUsersDetailFromSession();

        Log.i("UPDATE_EMAIL", userInfo.toString());

        updateEmailUser.put("phone", userInfo.get(SessionManager.KEY_PHONENUMBER));
        updateEmailUser.put("email", userInfo.get(SessionManager.KEY_EMAIL));

        makeRequestForEmail(urlMaker(), updateEmailUser);
    }

    public String urlMaker() {
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake("");
        StringBuilder urlBuilder = new StringBuilder(url);
        urlBuilder.append(getApplicationContext().getString(R.string.emailUpdate));

        return urlBuilder.toString();
    }
    public void makeRequestForEmail(String url, HashMap data) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.i("login", "request made to " + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, new JSONObject(data),
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
        try {
            boolean result =response.getBoolean("result");
            final String message = response.getString("message");
            if(result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UpdateEmail.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UpdateEmail.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        catch (JSONException e) {

        }
    }
}