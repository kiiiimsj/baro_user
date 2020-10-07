package com.example.wantchu;

import androidx.annotation.NonNull;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Fragment.TopBar;
import com.example.wantchu.Url.UrlMaker;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class FindPass1 extends AppCompatActivity implements TopBar.OnBackPressedInParentActivity {
    private final static String KOREA = "+82";
    private TextInputLayout phoneTextInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pass1);
        phoneTextInput = findViewById(R.id.forget_password_phone_number);
    }
    public void verifyPhone(View view) {
        Log.i("getInside", "VerifyPhone_Button_Click");
        if(!validateFields()){
            return;
        }

        //Get values from fields
        String _getUserEnteredPhoneNumber = phoneTextInput.getEditText().getText().toString().trim(); //Get Phone Number
        if (_getUserEnteredPhoneNumber.charAt(0) == '0') {
            _getUserEnteredPhoneNumber = _getUserEnteredPhoneNumber.substring(1);
        } //remove 0 at the start if entered by the user
        final String _phone = KOREA + _getUserEnteredPhoneNumber;
        Log.i("FindPass1 : ", _phone);


        makeRequestForCheckPhone();
    }
    public void makeRequestForCheckPhone() {
        String url = new UrlMaker().UrlMake("MemberPhoneCheck.do?phone="+phoneTextInput.getEditText().getText().toString());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response", response);
                parsing(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }

    private void parsing(String response) {
        boolean result = false;
        try {
            JSONObject jsonObject = new JSONObject(response);
            result = jsonObject.getBoolean("result");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        if(result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(FindPass1.this, "존재하지 않는 핸드폰 입니다.", Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
            intent.putExtra("phone", phoneTextInput.getEditText().getText().toString());
            startActivity(intent);
        }
    }

    private boolean validateFields() {
        String _phone = phoneTextInput.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";

        if(_phone.isEmpty()){
            phoneTextInput.setError("휴대폰번호를 입력해주세요");
            phoneTextInput.requestFocus();
            return false;
        }
        else if(!_phone.matches(checkspaces)){
            phoneTextInput.setError("빈공간없이 입력해주세요");
            return false;
        }
        else{
            phoneTextInput.setError(null);
            phoneTextInput.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    public void onBack() {
        super.onBackPressed();
    }
}