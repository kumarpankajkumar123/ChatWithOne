package com.example.chatwithone.Utrils;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.List;

public class FirebaseUtils {

    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }

    public static boolean isLogged(){
         if(currentUserId() != null){
             return  true;
         }
         return false;
    }
    public static DocumentReference currentUserDetails(){
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }

    public static CollectionReference allUserCollectionReference(){
        return FirebaseFirestore.getInstance().collection("users");
    }

    public static DocumentReference getChatRoomReference(String chatroomId){
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId);
    }

    public static CollectionReference getChatRoomsMessageReference(String chatroomId){
        return FirebaseUtils.getChatRoomReference(chatroomId).collection("chats");
    }
    public static String getChatroomId(String userId1,String userId2){
        if(userId1.hashCode() < userId2.hashCode()){
            return userId1+"_"+userId2;
        }
        else{
            return userId2+"_"+userId1;
        }
    }
    public static CollectionReference getAllChatsRoomsReference(){
        return FirebaseFirestore.getInstance().collection("chatrooms");
    }

    public static DocumentReference getOtherUserFromChatRooms(List<String> userIds){
        if(userIds.get(0).equals(FirebaseUtils.currentUserId())){
            return allUserCollectionReference().document(userIds.get(1));
        }
        else{
            return allUserCollectionReference().document(userIds.get(0));
        }
    }

    public static String timeStampToString(Timestamp timestamp){
        return new SimpleDateFormat("HH:MM").format(timestamp.toDate());
    }
    public static void loggedOut(){
        FirebaseAuth.getInstance().signOut();
    }

    public static StorageReference getCurrentProfileStorageRef(){
        return FirebaseStorage.getInstance().getReference()
                .child("profile_pic").child(FirebaseUtils.currentUserId());
    }

    public static StorageReference getOtherProfileStorageRef(String otherUserId){
        return FirebaseStorage.getInstance().getReference()
                .child("profile_pic").child(otherUserId);
    }
}
