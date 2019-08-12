package appchat.anh.nam.chat.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import appchat.anh.nam.R;
import appchat.anh.nam.adapter.ChatAdapter;
import appchat.anh.nam.common.Contact;
import appchat.anh.nam.model.Message;
import appchat.anh.nam.model.User;


public class ChatFragment extends Fragment {
    private String mGroupId;
    private String mCurrentId;
    private RecyclerView mRvChat;
    private ChatAdapter mChatAdapter;
    private ArrayList<Message> mListMessages;
    private HashMap<String, User> mHashMapUsers;
    DatabaseReference mReference ;
    public ChatFragment() {

    }


    public static ChatFragment newInstance(String groupId,String currentId) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(Contact.KEY_GROUP_ID, groupId);
        args.putString(Contact.KEY_CURRENT_ID,currentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReference = FirebaseDatabase.getInstance().getReference();
        if (getArguments() != null) {
            mGroupId = getArguments().getString(Contact.KEY_GROUP_ID);
            mCurrentId = getArguments().getString(Contact.KEY_CURRENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        initView(view);
        initRvChat();
        initData();
        return view;
    }

    private void initData() {

        mReference.child("Groups").child("xxx").child("Members").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getUser(String id){
        mReference.child("Users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String key;
                User user = new User();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    key = snapshot.getKey();
                    if(key.equals("id")){
                     
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initRvChat() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRvChat.setLayoutManager(linearLayoutManager);
        mChatAdapter = new ChatAdapter(new ArrayList<Message>(),new HashMap<String, User>(),mCurrentId);
        mRvChat.setAdapter(mChatAdapter);

    }

    private void initView(View view) {
        mRvChat = view.findViewById(R.id.rv_chat);

    }


}
