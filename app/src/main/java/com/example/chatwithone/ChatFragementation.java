package com.example.chatwithone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatwithone.Adaptor.RecentChatFragmentRecyclerAdaptor;
import com.example.chatwithone.Modal.ChatRoomsModals;
import com.example.chatwithone.Utrils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;


public class ChatFragementation extends Fragment {


    public ChatFragementation() {
        // Required empty public constructor
    }
    RecyclerView recyclerView;
    RecentChatFragmentRecyclerAdaptor recentChatFragmentRecyclerAdaptor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_chat_fragementation, container, false);
        recyclerView = view.findViewById(R.id.recyclerview_chat_fragment);

        setUpChatFragmentRecyclerView();
        return view;
    }

    void setUpChatFragmentRecyclerView(){

        Query query = FirebaseUtils.getAllChatsRoomsReference()
                .whereArrayContains("userIds",FirebaseUtils.currentUserId())
                .orderBy("lastMessageTimestamp",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatRoomsModals> options = new FirestoreRecyclerOptions.Builder<ChatRoomsModals>()
                .setQuery(query,ChatRoomsModals.class).build();

        recentChatFragmentRecyclerAdaptor = new RecentChatFragmentRecyclerAdaptor(options,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recentChatFragmentRecyclerAdaptor);
        recentChatFragmentRecyclerAdaptor.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(recentChatFragmentRecyclerAdaptor != null){
            recentChatFragmentRecyclerAdaptor.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(recentChatFragmentRecyclerAdaptor != null){
            recentChatFragmentRecyclerAdaptor.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(recentChatFragmentRecyclerAdaptor != null){
            recentChatFragmentRecyclerAdaptor.notifyDataSetChanged();
        }
    }
}