package com.example.wantchu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FindPass1 extends AppCompatActivity {

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


        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").child(_phone);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){ //만약 휴대폰정보가 firebase에 존재한다면
                    Log.i("FindPass1 : ", 1+"");
                    phoneTextInput.setError(null);
                    phoneTextInput.setErrorEnabled(false);

                    Intent intent = new Intent(getApplicationContext(), ChangePass2.class);
                    intent.putExtra("phone", _phone);
                    startActivity(intent);
                    finish();
                }
                else{ // firebase에 회원정보가 없을때 - 즉, 회원이 아닐때
                    phoneTextInput.setError("회원이 존재하지 않는 휴대폰번호입니다.");
                    phoneTextInput.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FindPass1.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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

    public void callBackFromFindPass(View view) {
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

}