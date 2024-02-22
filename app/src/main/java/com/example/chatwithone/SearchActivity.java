package com.example.chatwithone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatwithone.Adaptor.RecyclerviewAdaptorSearch;
import com.example.chatwithone.Modal.UserModal;
import com.example.chatwithone.Utrils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class SearchActivity extends AppCompatActivity {

    ImageView back_image;
    ImageView search_image_sActivity;

    EditText Search_editText;
    RecyclerView recyclerViewSearch;

    String inputDataTerm;
    RecyclerviewAdaptorSearch adaptorSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        back_image =(ImageView) findViewById(R.id.back_image);
        search_image_sActivity = (ImageView) findViewById(R.id.search_image_sActivity);
        Search_editText = (EditText) findViewById(R.id.Search_editText);
        recyclerViewSearch = (RecyclerView) findViewById(R.id.recyclerViewSearch);

        Search_editText.requestFocus();

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        search_image_sActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 inputDataTerm = Search_editText.getText().toString();
                if(inputDataTerm.isEmpty() || inputDataTerm.length() <=3){
                    Search_editText.setError("Invalid UserName");
                }
                else{
                   setUpRecyclerView(inputDataTerm);
                }
            }
        });
    }

    private void setUpRecyclerView(String inputData) {

        Query query = FirebaseUtils.allUserCollectionReference().whereGreaterThanOrEqualTo("username",inputDataTerm);

        FirestoreRecyclerOptions<UserModal> options = new FirestoreRecyclerOptions.Builder<UserModal>()
                .setQuery(query,UserModal.class).build();

        adaptorSearch = new RecyclerviewAdaptorSearch(options,getApplicationContext());
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSearch.setAdapter(adaptorSearch);
        adaptorSearch.startListening();


//        query.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful() && task.getResult() != null && task.getResult().isEmpty()) {
//                // Handle the case when no users match the criteria
//                // For example, display a message or perform other actions
//                // You can also hide or clear the RecyclerView in this case
//                // recyclerViewSearch.setVisibility(View.GONE);
//                // or
//                // adaptorSearch.stopListening();
//
//                Log.d(TAG,"no user found");
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(adaptorSearch != null){
            adaptorSearch.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adaptorSearch != null){
            adaptorSearch.stopListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adaptorSearch != null){
            adaptorSearch.startListening();
        }
    }
}