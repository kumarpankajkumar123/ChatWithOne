package com.example.chatwithone;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.chatwithone.Utrils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    ImageView search_image;
    BottomNavigationView bottomNavigationView;

    ChatFragementation chatFragementation;
    ProfileFragment profileFragment;
    FrameLayout main_frame_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search_image = (ImageView) findViewById(R.id.search_image);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
        main_frame_layout = (FrameLayout)findViewById(R.id.main_frame_layout);

        search_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

//                Log.d( "onNavigationItemSelected: ");
                // Example null check for fragments
                if (getSupportFragmentManager() != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                    // Check if the fragments are not null before replacing
                    if (item.getItemId() == R.id.chat ) {
                        transaction.replace(R.id.main_frame_layout, new ChatFragementation());
                    } else if (item.getItemId() == R.id.profile ) {
                        transaction.replace(R.id.main_frame_layout, new ProfileFragment());
                    }

                    transaction.commit();
                    return true;
                }
                return false;
            }

        });

        bottomNavigationView.setSelectedItemId(R.id.chat);

//        getToken();
    }
    void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful()){
                    String token = task.getResult();
                    FirebaseUtils.currentUserDetails().update("fcmToken",token);
//                    Log.d("my token",token);
                }
            }
        });
    }
}