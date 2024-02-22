package com.example.chatwithone.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatwithone.Modal.ChatMessageModal;
import com.example.chatwithone.R;
import com.example.chatwithone.Utrils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ChatRecyclerViewAdaptor extends FirestoreRecyclerAdapter<ChatMessageModal,ChatRecyclerViewAdaptor.ChatRecyclerViewHolder> {

    Context context;
    public ChatRecyclerViewAdaptor(@NonNull FirestoreRecyclerOptions<ChatMessageModal> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatRecyclerViewHolder holder, int position, @NonNull ChatMessageModal model) {
        if(model.getSenderId().equals(FirebaseUtils.currentUserId())){
           holder.left_layout.setVisibility(View.GONE);
           holder.right_layout.setVisibility(View.VISIBLE);
           holder.right_chat_text.setText(model.getMessage());
        }
        else{
            holder.right_layout.setVisibility(View.GONE);
            holder.left_layout.setVisibility(View.VISIBLE);
            holder.left_chat_text.setText(model.getMessage());
        }
    }


    @NonNull
    @Override
    public ChatRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_recycler_view_messages,parent,false);
        return  new ChatRecyclerViewHolder(view);
    }
    public class ChatRecyclerViewHolder extends RecyclerView.ViewHolder {

        LinearLayout left_layout,right_layout;
        TextView left_chat_text;
        TextView right_chat_text;

        public ChatRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            left_layout = (LinearLayout)itemView.findViewById(R.id.left_layout);
            right_layout = (LinearLayout) itemView.findViewById(R.id.right_layout);
            left_chat_text = (TextView) itemView.findViewById(R.id.left_chat_text);
            right_chat_text = (TextView) itemView.findViewById(R.id.right_chat_text);

        }
    }
}
