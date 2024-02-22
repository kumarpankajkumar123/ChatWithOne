package com.example.chatwithone.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatwithone.ChatActivity;
import com.example.chatwithone.Modal.ChatRoomsModals;
import com.example.chatwithone.Modal.UserModal;
import com.example.chatwithone.R;
import com.example.chatwithone.Utrils.FirebaseUtils;
import com.example.chatwithone.Utrils.Utils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class RecentChatFragmentRecyclerAdaptor extends FirestoreRecyclerAdapter<ChatRoomsModals,RecentChatFragmentRecyclerAdaptor.RecentChatViewHolder> {

    Context context;
    public RecentChatFragmentRecyclerAdaptor(@NonNull FirestoreRecyclerOptions<ChatRoomsModals> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull RecentChatViewHolder holder, int position, @NonNull ChatRoomsModals model) {

        FirebaseUtils.getOtherUserFromChatRooms(model.getUserIds()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){
                    boolean lastMessageSendByMe = model.getLastMessageSenderId().equals(FirebaseUtils.currentUserId());

                    UserModal otherModalChat = task.getResult().toObject(UserModal.class);

                    FirebaseUtils.getOtherProfileStorageRef(otherModalChat.getUserId()).getDownloadUrl()
                            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> t) {
                                    if(t.isSuccessful()){
                                        Uri uri = t.getResult();
                                        Utils.setImageProfile(context,holder.profile_image_design,uri);
                                    }
                                }
                            });

                    holder.chat_fragment_username.setText(otherModalChat.getUsername());
                    if(lastMessageSendByMe){
                        holder.chat_fragment_phoneNo.setText("you:: "+model.getLastMessage());
                    }
                    else{
                        holder.chat_fragment_phoneNo.setText(model.getLastMessage());
                    }
                    holder.timestamp.setText(FirebaseUtils.timeStampToString(model.getLastMessageTimestamp()));

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, ChatActivity.class);
                            Utils.passAllDetailsIntent(intent,otherModalChat);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    });
                }
            }
        });
    }

    @NonNull
    @Override
    public RecentChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recent_chat_recycler_design,parent,false);
        return  new RecentChatViewHolder(view);
    }
    public class RecentChatViewHolder extends RecyclerView.ViewHolder {

        TextView chat_fragment_username;
        TextView chat_fragment_phoneNo;
        TextView timestamp;
        ImageView profile_image_design;
        public RecentChatViewHolder(@NonNull View itemView) {
            super(itemView);

            chat_fragment_username = (TextView) itemView.findViewById(R.id.chat_fragment_username);
            chat_fragment_phoneNo = (TextView) itemView.findViewById(R.id.chat_fragment_phoneNo);
            timestamp = (TextView) itemView.findViewById(R.id.chat_fragment_timestamp);
            profile_image_design = (ImageView) itemView.findViewById(R.id.profile_image_design);
        }
    }
}
