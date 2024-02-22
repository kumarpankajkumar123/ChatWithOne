package com.example.chatwithone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.hbb20.CountryCodePicker;

public class LoginActivity extends AppCompatActivity {

    CountryCodePicker login_country_code_picker;
    EditText login_mobile;
    Button login_send_Otp;
    ProgressBar login_progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_country_code_picker = (CountryCodePicker) findViewById(R.id.login_country_code_picker);
        login_mobile = (EditText) findViewById(R.id.login_mobile);
        login_send_Otp = (Button) findViewById(R.id.login_send_Otp);
        login_progressbar = (ProgressBar) findViewById(R.id.login_progressbar);

        login_progressbar.setVisibility(View.GONE);

        login_country_code_picker.registerCarrierNumberEditText(login_mobile);

        login_send_Otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(login_country_code_picker.isValidFullNumber()){
                    Intent intent = new Intent(LoginActivity.this,LoginOtpActivity.class);
                    intent.putExtra("phone",login_country_code_picker.getFullNumberWithPlus());
                    startActivity(intent);
                }else {
                    login_mobile.setError("enter valid mobile number");
                }
            }
        });

    }
}