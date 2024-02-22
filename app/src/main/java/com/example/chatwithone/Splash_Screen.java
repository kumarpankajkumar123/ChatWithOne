package com.example.chatwithone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatwithone.Utrils.FirebaseUtils;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

//        if(FirebaseUtils.isLogged() && getIntent().getExtras() != null){
//
//            String userId = getIntent().getExtras().getString("userId");
//            FirebaseUtils.allUserCollectionReference().document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                     if(task.isSuccessful()){
//                         UserModal userModal = task.getResult().toObject(UserModal.class);
//
//                         Intent MainIntent = new Intent(getApplicationContext(),MainActivity.class);
//                         MainIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                         startActivity(MainIntent);
//
//                         Intent intent = new Intent( getApplicationContext(),ChatActivity.class);
//                         Utils.passAllDetailsIntent(intent,userModal);
//                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                         startActivity(intent);
//                         finish();
//                     }
//                }
//            });
//        }
//        else {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if(FirebaseUtils.isLogged()){
//                        Intent intent = new Intent(Splash_Screen.this, MainActivity.class);
//                        startActivity(intent);
//                    }
//                    else{
//                        Intent intent = new Intent(Splash_Screen.this, LoginActivity.class);
//                        startActivity(intent);
//                    }
//                    finish();
//                }
//            },1000);
//        }
        new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(FirebaseUtils.isLogged()){
                        Intent intent = new Intent(Splash_Screen.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(Splash_Screen.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    finish();
                }
            },1000);
        }
    }