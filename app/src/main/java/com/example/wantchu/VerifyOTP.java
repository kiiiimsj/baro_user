package com.example.wantchu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.wantchu.Fragment.TopBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyOTP extends AppCompatActivity implements TopBar.OnBackPressedInParentActivity {

    private PinView pinFromUser;
    String phoneNumber;
    String codeBySystem;
    String pageType;
    TextView timer;
    int sec = 120;
    int min;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);

        timer = findViewById(R.id.timer);
        pinFromUser = findViewById(R.id.pin_view);
        Intent intent = getIntent();
        pageType = intent.getStringExtra("pageType");
        phoneNumber = intent.getStringExtra("phone");
        sendVerificationCodeToUser(phoneNumber);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(sec != 0) {
                    try {
                        Thread.sleep(1000);
                        sec--;
                        min = sec / 60;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timer.setText(min + " : " +(sec % 60));
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    private void sendVerificationCodeToUser(String phoneNo) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNo,        // Phone number to verify
                120,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    codeBySystem = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        pinFromUser.setText(code);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(VerifyOTP.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                                okayToMoveNextStep();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(VerifyOTP.this, "핸드폰 인증에 실패 하였습니다 인증 코드가 틀립니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                });
    }

    private void okayToMoveNextStep() {
        if(pageType.equals("FindPass1")){
            Intent intent = new Intent(VerifyOTP.this, ChangePass2.class);
            intent.putExtra("phone", phoneNumber);
            startActivity(intent);
        }else if(pageType.equals("Register1")){
            Intent intent = new Intent(VerifyOTP.this, Register2.class);
            intent.putExtra("phone", phoneNumber);
            startActivity(intent);
        }

    }

    public void onClickVerify(View view) {
        String code = pinFromUser.getText().toString();
        if (!code.isEmpty()) {
            verifyCode(code);
        }
        else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(VerifyOTP.this, "입력코드가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        //인증버튼 클릭했을경우
    }

    @Override
    public void onBack() {
        super.onBackPressed();
    }
}