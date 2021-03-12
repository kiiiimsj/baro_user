package com.tpn.baro;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tpn.baro.Fragment.TopBar;
import com.tpn.baro.Url.UrlMaker;
import com.google.android.material.textfield.TextInputLayout;
import com.tpn.baro.helperClass.BaroUtil;
import com.tpn.baro.helperClass.ProgressApplication;

import org.json.JSONException;
import org.json.JSONObject;

public class Register1 extends AppCompatActivity implements TopBar.OnBackPressedInParentActivity {
    private final static String KOREA = "+82";
    public TextInputLayout phoneTextInput;
    public Button nextButton;
    ProgressApplication progressApplication;

    String _phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BaroUtil.setStatusBarColor(Register1.this, this.toString());
        }
            setContentView(R.layout.activity_register1);
        progressApplication = new ProgressApplication();

        phoneTextInput = findViewById(R.id.register_phone_number);
        nextButton = findViewById(R.id.next_button);
    }

    public void verifyPhone(View view) {
        if(!validateFields()){
            return;
        }
        progressApplication.progressON(this);

        String _getUserEnteredPhoneNumber = phoneTextInput.getEditText().getText().toString().trim(); //Get Phone Number
        if (_getUserEnteredPhoneNumber.charAt(0) == '0') {
            _getUserEnteredPhoneNumber = _getUserEnteredPhoneNumber.substring(1);
        } //remove 0 at the start if entered by the user
        _phone = KOREA + _getUserEnteredPhoneNumber;

        makeRequestForCheckPhone();

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
    public void makeRequestForCheckPhone() {
        String url = new UrlMaker().UrlMake("MemberPhoneCheck.do?phone="+phoneTextInput.getEditText().getText().toString());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
        if(!result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Register1.this, "이미 존재하는 핸드폰 입니다.", Toast.LENGTH_LONG).show();
                }
            });
            progressApplication.progressOFF();
        }
        else {
            Intent intent = new Intent(Register1.this, VerifyOTP.class);
            intent.putExtra("phone", _phone);
            intent.putExtra("pageType",Register1.this.getClass().getSimpleName());
            startActivity(intent);
            finish();
            progressApplication.progressOFF();
        }
    }

    @Override
    public void onBack() {
        super.onBackPressed();
    }
}