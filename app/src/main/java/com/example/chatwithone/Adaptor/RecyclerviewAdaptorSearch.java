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
import com.example.chatwithone.Modal.UserModal;
import com.example.chatwithone.R;
import com.example.chatwithone.Utrils.FirebaseUtils;
import com.example.chatwithone.Utrils.Utils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class RecyclerviewAdaptorSearch extends FirestoreRecyclerAdapter<UserModal,RecyclerviewAdaptorSearch.RecyclerexpendsViewHolder> {

    Context context;
    public RecyclerviewAdaptorSearch(@NonNull FirestoreRecyclerOptions<UserModal> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull RecyclerexpendsViewHolder holder, int position, @NonNull UserModal model) {
        holder.user_recycler_design_text.setText(model.getUsername());
        holder.phoneNo_recycler_design_text.setText(model.getPhone());
        String newId = model.getUserId();
        if(newId != null && newId.equals(FirebaseUtils.currentUserId())){
            holder.user_recycler_design_text.setText(model.getUsername()+" (Me)");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent = new Intent(context, ChatActivity.class);
              Utils.passAllDetailsIntent(intent,model);
              intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              context.startActivity(intent);
            }
        });

        FirebaseUtils.getOtherProfileStorageRef(model.getUserId()).getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> t) {
                        if(t.isSuccessful()){
                            Uri uri = t.getResult();
                            Utils.setImageProfile(context,holder.profile_image_design,uri);
                        }
                    }
                });

    }

    @NonNull
    @Override
    public RecyclerexpendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.design_recyclersearch,parent,false);
        return  new RecyclerexpendsViewHolder(view);
    }
    public class RecyclerexpendsViewHolder extends RecyclerView.ViewHolder {

        TextView user_recycler_design_text;
        TextView phoneNo_recycler_design_text;
        ImageView profile_image_design;
        public RecyclerexpendsViewHolder(@NonNull View itemView) {
            super(itemView);

            user_recycler_design_text = (TextView) itemView.findViewById(R.id.user_recycler_design_text);
            phoneNo_recycler_design_text = (TextView) itemView.findViewById(R.id.phoneNo_recycler_design_text);
            profile_image_design = (ImageView) itemView.findViewById(R.id.profile_image_design);
        }
    }
}
