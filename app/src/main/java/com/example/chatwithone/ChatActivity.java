package com.example.chatwithone;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatwithone.Adaptor.ChatRecyclerViewAdaptor;
import com.example.chatwithone.Modal.ChatMessageModal;
import com.example.chatwithone.Modal.ChatRoomsModals;
import com.example.chatwithone.Modal.UserModal;
import com.example.chatwithone.Utrils.FirebaseUtils;
import com.example.chatwithone.Utrils.Utils;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.Arrays;

public class ChatActivity extends AppCompatActivity {

    ImageView chat_back;
    ImageView chat_image;
    TextView chat_username;

    RecyclerView recyclerChatView;
    ImageView send_chat_image;
    EditText send_edit_text;
    UserModal otherModal;

    ChatRecyclerViewAdaptor chatAdaptor;

    ChatRoomsModals chatRoomsModals;
    String chatroomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chat_back = (ImageView) findViewById(R.id.chat_back);
        chat_image = (ImageView) findViewById(R.id.chat_image);
        chat_username = (TextView) findViewById(R.id.chat_username);
        send_chat_image = (ImageView) findViewById(R.id.send_chat_image);
        send_edit_text = (EditText)findViewById(R.id.send_edit_text);
        recyclerChatView = (RecyclerView)findViewById(R.id.recyclerChatView);

        otherModal = Utils.setAllDetailsFromIntent(getIntent());
        chat_username.setText(otherModal.getUsername());


        FirebaseUtils.getOtherProfileStorageRef(otherModal.getUserId()).getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> t) {
                        if(t.isSuccessful()){
                            Uri uri = t.getResult();
                            Utils.setImageProfile(getApplicationContext(),chat_image,uri);
                        }
                    }
                });

        chatroomId = FirebaseUtils.getChatroomId(FirebaseUtils.currentUserId(),otherModal.getUserId());

        chat_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });

        send_chat_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = send_edit_text.getText().toString().trim();
                if(message.isEmpty()){
                    Utils.showToast(getApplicationContext(),"please write something");
                }
                else{
                    sendMessageToUser(message);
                }
            }
        });
        getOrCreatedChatroom();
        setUpChatRecyclerView();
    }

    void setUpChatRecyclerView(){
        Query query = FirebaseUtils.getChatRoomsMessageReference(chatroomId).orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ChatMessageModal> options = new FirestoreRecyclerOptions.Builder<ChatMessageModal>()
                .setQuery(query,ChatMessageModal.class).build();

        chatAdaptor = new ChatRecyclerViewAdaptor(options,getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerChatView.setLayoutManager(manager);
        recyclerChatView.setAdapter(chatAdaptor);
        chatAdaptor.startListening();
        chatAdaptor.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerChatView.smoothScrollToPosition(0);
            }
        });
    }
    void getOrCreatedChatroom(){
        FirebaseUtils.getChatRoomReference(chatroomId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                   chatRoomsModals = task.getResult().toObject(ChatRoomsModals.class);
                   if(chatRoomsModals == null){
                      chatRoomsModals = new ChatRoomsModals(chatroomId,
                              Arrays.asList(FirebaseUtils.currentUserId(),otherModal.getUserId()),
                                      Timestamp.now(),"");
                      FirebaseUtils.getChatRoomReference(chatroomId).set(chatRoomsModals);
                   }
                }
            }
        });
    }

    void sendMessageToUser(String message){
        chatRoomsModals.setLastMessageTimestamp(Timestamp.now());
        chatRoomsModals.setLastMessageSenderId(FirebaseUtils.currentUserId());
        chatRoomsModals.setLastMessage(message);
        FirebaseUtils.getChatRoomReference(chatroomId).set(chatRoomsModals);
        ChatMessageModal chatMessageModal= new ChatMessageModal(message,FirebaseUtils.currentUserId(),Timestamp.now());

        FirebaseUtils.getChatRoomsMessageReference(chatroomId).add(chatMessageModal)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    send_edit_text.setText("");
                }
            }
        });
    }
}