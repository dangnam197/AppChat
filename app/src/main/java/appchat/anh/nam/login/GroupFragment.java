package appchat.anh.nam.login;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import appchat.anh.nam.FriendFragment;
import appchat.anh.nam.R;
import appchat.anh.nam.adapter.GroupChatAdapter;
import appchat.anh.nam.common.Contact;
import appchat.anh.nam.model.Group;
import appchat.anh.nam.model.Message;
import appchat.anh.nam.model.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends Fragment {
    private RecyclerView mRecyclerViewGroupChat;
    private ArrayList<String> mArrIdGroup = new ArrayList<>();
    private DatabaseReference mData;
    private String mIdUser;
    private CallBackGroupChatFragment mCallBackGroupChatFragment;
    private GroupChatAdapter.ItemGroupChatClick mItemGroupChatClick = new GroupChatAdapter.ItemGroupChatClick() {
        @Override
        public void onGroupClick(Group group) {
            mCallBackGroupChatFragment.onItemGroupClick(mIdUser, group);
        }
    };
    private final GroupChatAdapter mGroupChatAdapter = new GroupChatAdapter(new ArrayList<Group>(), new HashMap<String, Message>(), mItemGroupChatClick);
    private static final String TAG = "ketqua";

    public GroupFragment() {
        // Required empty public constructor
    }

    public interface CallBackGroupChatFragment{
        void onItemGroupClick(String idUser, Group group);
    }

    public void setInterface(CallBackGroupChatFragment callBackGroupChatFragment){
        mCallBackGroupChatFragment = callBackGroupChatFragment;
    }


    public static GroupFragment newInstance(String currentId) {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putString(Contact.KEY_CURRENT_ID, currentId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        initView(view);
        initBundle();
        initRecyclerView();
        initData();
        return view;
    }

    private void initView(View view) {
        mRecyclerViewGroupChat = view.findViewById(R.id.recyclerViewGroupChat);
    }

    private void initBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mIdUser = bundle.getString(Contact.KEY_CURRENT_ID);
        }
    }

    private void initData() {
        mData = FirebaseDatabase.getInstance().getReference();
        if (mIdUser != null) {
            final DatabaseReference dataGroup = mData.child("Groups");
            mData.child(Contact.TABLE_FRIENDS_GROUPS).child(mIdUser).child(Contact.TABLE_GROUP).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        final String id = (String) dataSnapshot1.getValue();
                        mArrIdGroup.add(id);
                        dataGroup.child(id).child("recentMessage").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Message message = dataSnapshot.getValue(Message.class);
                                mGroupChatAdapter.updateRecentMessage(id, message);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        dataGroup.child(id).child("GroupDetail").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Group group = dataSnapshot.getValue(Group.class);
                                Log.d(TAG, "onDataChange: "+group.getName());
                                mGroupChatAdapter.addGroupChat(group);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewGroupChat.setAdapter(mGroupChatAdapter);
        mRecyclerViewGroupChat.setLayoutManager(layoutManager);
    }

}
