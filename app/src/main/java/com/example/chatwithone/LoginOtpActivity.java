package com.example.chatwithone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatwithone.Utrils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class LoginOtpActivity extends AppCompatActivity {

    String phone_number;

    String verification_code;
    PhoneAuthProvider.ForceResendingToken ResendingToken;
    Long TimeOut = 60L;
    EditText login_otp_text;
    Button next_button;
    ProgressBar login_progressbar;
    TextView resend_otp;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        login_otp_text = (EditText) findViewById(R.id.login_otp_text);
        next_button = (Button) findViewById(R.id.next_button);
        login_progressbar = (ProgressBar) findViewById(R.id.login_progressbar);
        resend_otp = (TextView) findViewById(R.id.resend_otp);
        phone_number = getIntent().getStringExtra("phone");

        sendOtp(phone_number, false);

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enterOtp = login_otp_text.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification_code, enterOtp);
                signIn(credential);
                setProgress(true);
            }
        });

        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOtp(phone_number, true);
            }
        });
    }

    void sendOtp(String phone_number, boolean isResend) {
        setOtpTimer();
        setProgress(true);
        PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber(phone_number)
                .setTimeout(TimeOut, TimeUnit.SECONDS).setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signIn(phoneAuthCredential);
                        setProgress(false);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Utils.showToast(getApplicationContext(), "OTP verification failed");
                        setProgress(false);
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);

                        verification_code = s;
                        ResendingToken = forceResendingToken;
                        Utils.showToast(getApplicationContext(), "OTP sent successfully");
                        setProgress(false);

                    }
                });

        if (isResend) {
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(ResendingToken).build());
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());

        }
    }

    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        setProgress(true);
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
//                setProgress(false);
                if (task.isSuccessful()) {
                    Intent intent = new Intent(LoginOtpActivity.this, PersonActivity.class);
                    intent.putExtra("phone", phone_number);
                    startActivity(intent);

                } else {
                    Utils.showToast(getApplicationContext(), " otp verification failed");
                }
            }
        });
    }

    void setProgress(boolean inProgress) {
        if (inProgress) {
            login_progressbar.setVisibility(View.VISIBLE);
            next_button.setVisibility(View.GONE);
        } else {
            login_progressbar.setVisibility(View.GONE);
            next_button.setVisibility(View.VISIBLE);
        }
    }

    void setOtpTimer() {
        resend_otp.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                TimeOut--;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resend_otp.setText("Resend Otp After " + TimeOut + " seconds");
                    }
                });

                if (TimeOut <= 0) {
                    TimeOut = 60L;
                    timer.cancel();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resend_otp.setEnabled(true);
                        }
                    });
                }
            }
        }, 0, 1000);
    }

}