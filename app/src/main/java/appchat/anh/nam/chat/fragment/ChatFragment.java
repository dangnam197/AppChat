package appchat.anh.nam.chat.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

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

import static androidx.core.content.ContextCompat.getSystemService;


public class ChatFragment extends Fragment {
    private String mGroupId;
    private String mCurrentId;
    private RecyclerView mRvChat;
    private ChatAdapter mChatAdapter;
    private ImageButton btnSendMessage;
    private EditText mEditChat;
    DatabaseReference mReference ;

    private static final String TAG = "ketqua";
    private final View.OnClickListener sendMessageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String chat = mEditChat.getText().toString();
            if(!chat.equals("")){
                mEditChat.setText("");
                Message message = new Message(Contact.CONTENT_TYPE_TEXT,mCurrentId,"",chat,System.currentTimeMillis()/1000);
                mChatAdapter.addItem(message);
                Log.d(TAG, "onClick: "+chat);
                mRvChat.smoothScrollToPosition(mChatAdapter.getItemCount()-1);
                mReference.child("GroupMessager").child(mGroupId).push().setValue(message);
                mReference.child("Groups").child(mGroupId).child("recentMessage").setValue(message);
            }


        }
    };
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
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mReference = firebaseDatabase.getReference();

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
        initAction();
        initRvChat();
        initData();
        return view;
    }

    private void initAction() {
        btnSendMessage.setOnClickListener(sendMessageListener);
    }

    private void initRvChat() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        mRvChat.setLayoutManager(linearLayoutManager);
        mChatAdapter = new ChatAdapter(new ArrayList<Message>(),new HashMap<String, User>(),mCurrentId);
        mRvChat.setAdapter(mChatAdapter);

    }

    private void initView(View view) {
        mRvChat = view.findViewById(R.id.rv_chat);
        btnSendMessage = view.findViewById(R.id.btn_send_message);
        mEditChat = view.findViewById(R.id.edit_chat);

    }
    private void initData() {

        mReference.child("Groups").child(mGroupId).child("Members").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                    getUser(dataSnapshot1.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final ArrayList<Message> list = new ArrayList<>();
        mReference.child("GroupMessager").child(mGroupId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){


                    Message message = snapshot.getValue(Message.class);
                    Log.d(TAG, "onDataChange: "+snapshot.getValue().toString());
                    list.add(message);

                }
                if(list.size()>0) {
                    list.remove(list.size()-1);
                }
               mChatAdapter.addAllItem(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mReference.child("Groups").child(mGroupId).child("recentMessage").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange +a: "+dataSnapshot.getValue().toString());
                Message message = dataSnapshot.getValue(Message.class);
                if(message!=null) {
                    if(message.getFromId()!=mCurrentId){
                        mChatAdapter.addItem(message);
                        mRvChat.post(new Runnable() {
                            @Override
                            public void run() {
                                mRvChat.smoothScrollToPosition(mChatAdapter.getItemCount()-1);
                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getUser(final String id){
        mReference.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mChatAdapter.addUser(id,user);
                Log.d(TAG, "member: "+dataSnapshot.toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
