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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import appchat.anh.nam.adapter.FriendRequestAdapter;
import appchat.anh.nam.common.Contact;
import appchat.anh.nam.model.Group;
import appchat.anh.nam.model.User;


public class FriendRequestFragment extends Fragment {

    private String mCurrentId;

    private RecyclerView mRvFriend;


    private FriendRequestAdapter mFriendRequestAdapter;

    private DatabaseReference mReference ;


    private static final String TAG = "FriendFragment";

    private String mCurrrentUserName;
    private final FriendRequestAdapter.OnClickListener mItemFriendClick = new FriendRequestAdapter.OnClickListener() {
        @Override
        public void acceptFriendClick(User user, int position) {
            Log.d(TAG, "acceptFriendClick: "+user.getId());
            long currenTime = System.currentTimeMillis()/1000;
            //
            mReference.child("FriendsGroups").child(mCurrentId).child("Friends").child(user.getId()).child("userId").setValue(user.getId());
            mReference.child("FriendsGroups").child(mCurrentId).child("Friends").child(user.getId()).child("time").setValue(currenTime);

            //
            mReference.child("FriendsGroups").child(user.getId()).child("Friends").child(mCurrentId).child("userId").setValue(mCurrentId);
            mReference.child("FriendsGroups").child(user.getId()).child("Friends").child(mCurrentId).child("time").setValue(currenTime);

            //remove friend request
            mReference.child("FriendsGroups").child(mCurrentId).child("AddFriends").child(user.getId()).removeValue();
            String groupId = mReference.child("Groups").push().getKey();
            Group group = new Group(currenTime,"",groupId,mCurrrentUserName+","+user.getFullName());

           //add group
            mReference.child("Groups").child(groupId).child("GroupDetail").setValue(group);
            mReference.child("Groups").child(groupId).child("Members").push().setValue(mCurrentId);
            mReference.child("Groups").child(groupId).child("Members").push().setValue(user.getId());
            mReference.child("FriendsGroups").child(mCurrentId).child("Groups").push().setValue(groupId);
            mReference.child("FriendsGroups").child(user.getId()).child("Groups").push().setValue(groupId);
        }

        @Override
        public void refuseFriendClick(User user, int position) {
            mReference.child("FriendsGroups").child(mCurrentId).child("AddFriends").child(user.getId()).removeValue();
        }
    };
    public FriendRequestFragment() {
    }

    public static FriendRequestFragment newInstance(String currentId, String currentUserName) {
        FriendRequestFragment fragment = new FriendRequestFragment();
        Bundle args = new Bundle();
        args.putString(Contact.KEY_CURRENT_ID, currentId);
        args.putString(Contact.KEY_CURRENT_USER_NAME, currentUserName);
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
            mCurrrentUserName = getArguments().getString(Contact.KEY_CURRENT_USER_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friend_request, container, false);
        initView(view);
        initAction();
        initData();
        initRvFriend();
        return view;
    }

    private void initData() {
        mReference.child("FriendsGroups").child(mCurrentId).child("AddFriends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    for(DataSnapshot data:snapshot.getChildren()){
                        if(data.getKey().equals("userId")) {
                            getUser(data.getValue().toString());
                        }
                        Log.d(TAG, "onDataChange: "+snapshot.getValue().toString());
                    }
                    getUser(snapshot.getValue().toString());
                    Log.d(TAG, "onDataChange: "+snapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initRvFriend() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRvFriend.setLayoutManager (linearLayoutManager);
        mFriendRequestAdapter = new FriendRequestAdapter(new ArrayList<User>(),mItemFriendClick);
        mRvFriend.setAdapter(mFriendRequestAdapter);

    }

    private void initAction() {

    }

    private void initView(View view) {
        mRvFriend = view.findViewById(R.id.rv_friend_request);

    }

    private void getUser(String id){
        if(id!=null) {
            mReference.child("Users").child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if(user!=null) {
                        mFriendRequestAdapter.addUser(user);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }


}
