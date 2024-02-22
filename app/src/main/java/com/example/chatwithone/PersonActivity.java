package com.example.chatwithone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatwithone.Modal.UserModal;
import com.example.chatwithone.Utrils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

public class PersonActivity extends AppCompatActivity {

    EditText login_username;
    Button login_let_me;
    ProgressBar login_progressbar;

    String phone_number;
    UserModal userModal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        login_let_me = (Button) findViewById(R.id.login_let_me);
        login_username = (EditText) findViewById(R.id.login_username);
        login_progressbar = (ProgressBar) findViewById(R.id.login_progressbar);

        phone_number = getIntent().getExtras().getString("phone");
        getUserName();

        login_let_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUserName();
            }
        });
    }

    void setUserName() {

        String username = login_username.getText().toString();
        if (username.isEmpty() || username.length() <= 3) {
            login_username.setError("the username should be smaller than 3 characters");
        }
        else {
            login_progressbar.setVisibility(View.VISIBLE);
            login_let_me.setVisibility(View.GONE);
            if (userModal != null) {
                userModal.setUsername(username);
            } else {
                userModal = new UserModal(phone_number, username, Timestamp.now(),FirebaseUtils.currentUserId());
            }

            FirebaseUtils.currentUserDetails().set(userModal).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        Intent intent = new Intent(PersonActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        setProgress(false);
//                        login_progressbar.setVisibility(View.GONE);
//                        login_let_me.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

    }

    void getUserName() {
        setProgress(true);
        FirebaseUtils.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    userModal = task.getResult().toObject(UserModal.class);
                    if (userModal != null) {
                        login_username.setText(userModal.getUsername());
                    }
                }
                setProgress(false);
            }
        });

    }

    void setProgress(boolean inProgress) {
        if (inProgress) {
            login_progressbar.setVisibility(View.VISIBLE);
            login_let_me.setVisibility(View.GONE);
        } else {
            login_progressbar.setVisibility(View.GONE);
            login_let_me.setVisibility(View.VISIBLE);
        }
    }
}