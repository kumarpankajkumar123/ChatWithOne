package com.example.chatwithone.Utrils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.chatwithone.Modal.UserModal;

public class Utils {
    public static void showToast(Context context,String message){
        Toast.makeText(context, message ,Toast.LENGTH_SHORT).show();
    }

    public static void passAllDetailsIntent(Intent intent, UserModal modal){
        intent.putExtra("username",modal.getUsername());
        intent.putExtra("phone",modal.getPhone());
        intent.putExtra("userId",modal.getUserId());
    }

    public static UserModal setAllDetailsFromIntent(Intent intent){
        UserModal userModal = new UserModal();
        userModal.setUsername(intent.getStringExtra("username"));
        userModal.setPhone(intent.getStringExtra("phone"));
        userModal.setUserId(intent.getStringExtra("userId"));
        return userModal;
    }

    public static void setImageProfile(Context context, ImageView imageView, Uri imageUri){

        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
}
