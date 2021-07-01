package com.tpn.baro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.tpn.baro.Fragment.TopBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.tpn.baro.helperClass.BaroUtil;
import com.tpn.baro.helperClass.ProgressApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class VerifyOTP extends AppCompatActivity implements TopBar.OnBackPressedInParentActivity {
    private boolean THREAD_END = false;
    private boolean ON_DESTORY = false;
    private PinView pinFromUser;
    String phoneNumber;
    String codeBySystem;
    String pageType;
    TextView title;
    TextView timer;
    ProgressApplication progressApplication;
    Thread auth;

    boolean buttonPressed = false;

    int sec = 120;
    int min;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BaroUtil.setStatusBarColor(VerifyOTP.this, this.toString());
        }

        title = findViewById(R.id.register_title);
        timer = findViewById(R.id.twoMintimer);
        pinFromUser = findViewById(R.id.pin_view);

        progressApplication = new ProgressApplication();

        Intent intent = getIntent();
        pageType = intent.getStringExtra("pageType");
        phoneNumber = intent.getStringExtra("phone");

        // 타이틀 설정 부분
        if(pageType.equals("FindPass1")){
            title.setText("비밀번호 찾기");
        }

        sendVerificationCodeToUser(phoneNumber);
        auth = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!THREAD_END) {
                    try {
                        if(sec == 1 ) {
                            THREAD_END = true;
                        }
                        Thread.sleep(1000);
                        Log.e("sec", sec+"");
                        sec--;
                        min = sec / 60;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timer.setText( (BaroUtil.pad(2, '0', min+"")) + " : " + (BaroUtil.pad(2, '0', (sec% 60+"")))  +" 분");
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(!ON_DESTORY) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(VerifyOTP.this, "인증시간이 지났습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }
        });

        auth.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("des?", "des?");
        THREAD_END = true;
        ON_DESTORY = true;
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
                    Log.e("error", e.toString());
                    Toast.makeText(VerifyOTP.this, "인증 메세지 전송에 실패하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
        if(credential == null ){
            progressApplication.progressOFF();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(VerifyOTP.this, "핸드폰 인증에 실패 하였습니다 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
            });
            finish();
        }else {
            signInWithPhoneAuthCredential(credential);
        }
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth == null) {
            progressApplication.progressOFF();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(VerifyOTP.this, "핸드폰 인증에 실패 하였습니다 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
            });
            finish();
        }
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
            finish();
        }else if(pageType.equals("Register1")){
            Intent intent = new Intent(VerifyOTP.this, Register2.class);
            intent.putExtra("phone", phoneNumber);
            startActivity(intent);
            finish();
        }

    }

    public void onClickVerify(View view) {
        if(buttonPressed) {
            return;
        }
        buttonPressed = true;
        progressApplication.progressON(this);
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
        progressApplication.progressOFF();
    }

    @Override
    public void onBack() {
        super.onBackPressed();
    }
}