package appchat.anh.nam;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import appchat.anh.nam.adapter.FriendAdapte;
import appchat.anh.nam.common.Contact;
import appchat.anh.nam.model.User;


public class FriendFragment extends Fragment {

    private String mCurrentId;
    
    private RecyclerView mRvFriend;
    
    private EditText mEditSearchFriend;
    
    private ImageButton mBtnSearchFriend;

    private FriendAdapte mFriendAdapte;

    private DatabaseReference mReference ;

    private static final String TAG = "FriendFragment";

    private final View.OnClickListener mSearchFriendClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String search = mEditSearchFriend.getText().toString();
            mReference.child("Users").orderByChild("fullName").startAt(search).endAt(search+"\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<User> list = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    User user = snapshot.getValue(User.class);
                    Log.d(TAG, "onDataChange: "+user.getFullName());
                    if(user!=null){
                        list.add(user);
                    }
                }
                if(list.size()>0){
                    mFriendAdapte.addAllUser(list);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        }
    };
    private final FriendAdapte.OnClickListener mItemFriendClick = new FriendAdapte.OnClickListener() {
        @Override
        public void addFriendClick(User user, int position) {
            String id = mReference.child("FriendsGroups").child(mCurrentId).child("AddFriends").push().getKey();
            mReference.child("FriendsGroups").child(mCurrentId).child("AddFriends").child(id).
                    child("time").setValue(System.currentTimeMillis()/1000);
            mReference.child("FriendsGroups").child(mCurrentId).child("AddFriends").child(id).
                    child("userId").setValue(user.getId());
        }
    };
    public FriendFragment() {
    }

    public static FriendFragment newInstance(String currentId) {
        FriendFragment fragment = new FriendFragment();
        Bundle args = new Bundle();
        args.putString(Contact.KEY_CURRENT_ID, currentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mReference = firebaseDatabase.getReference();
        if (getArguments() != null) {
            mCurrentId = getArguments().getString(Contact.KEY_CURRENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friend, container, false);
        initView(view);
        initAction();
        initRvFriend();
        return view;
    }

    private void initRvFriend() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRvFriend.setLayoutManager (linearLayoutManager);
        mFriendAdapte = new FriendAdapte(new ArrayList<User>(),mItemFriendClick);
        mRvFriend.setAdapter(mFriendAdapte);

    }

    private void initAction() {
        mBtnSearchFriend.setOnClickListener(mSearchFriendClick);
    }

    private void initView(View view) {
        mRvFriend = view.findViewById(R.id.rv_search_friend);
        mEditSearchFriend = view.findViewById(R.id.ed_search_friend);
        mBtnSearchFriend = view.findViewById(R.id.btn_search_friend);
    }

    private void getUser(String id){
        mReference.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mFriendAdapte.addUser(user);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
