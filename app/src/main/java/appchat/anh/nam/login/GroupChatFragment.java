package appchat.anh.nam.login;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import appchat.anh.nam.R;
import appchat.anh.nam.adapter.GroupChatAdapter;
import appchat.anh.nam.common.Contact;
import appchat.anh.nam.model.Group;
import appchat.anh.nam.model.Message;
import appchat.anh.nam.model.User;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupChatFragment extends Fragment {

    private RecyclerView mRecyclerViewGroupChat;
    private CircleImageView mImgAvatarCurrentUser;
    private TextView mTxtCurrentUserName, mTxtLogOut;
    private final GroupChatAdapter mGroupChatAdapter = new GroupChatAdapter(new ArrayList<Group>(), new HashMap<String, Message>());
    private ArrayList<String> mArrIdGroup = new ArrayList<>();
    private DatabaseReference mData;
    private String mIdUser;
    private GroupChatCallBackActivity mCallBack;
    private static final String TAG = "ketqua";

    private View.OnClickListener mTxtLogOutClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallBack.actionSignOut();
        }
    };

    public interface GroupChatCallBackActivity{
        void actionSignOut();
    }

    public GroupChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_chat, container, false);
        initView(view);
        initAction();
        initBundle();
        initRecyclerView();
        initData();
        return view;
    }

    public void setInterface(GroupChatCallBackActivity callBack){
        mCallBack = callBack;
    }

    private void initView(View view) {
        mRecyclerViewGroupChat = view.findViewById(R.id.recyclerViewGroupChat);
        mImgAvatarCurrentUser = view.findViewById(R.id.imgAvatarCurrentUser);
        mTxtCurrentUserName = view.findViewById(R.id.txtCurrentUserName);
        mTxtLogOut = view.findViewById(R.id.txtLogOut);

    }

    private void initAction(){
        mTxtLogOut.setOnClickListener(mTxtLogOutClick);
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
            mData.child(Contact.TABLE_USER).child(mIdUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange: "+dataSnapshot.toString());
                    User user = dataSnapshot.getValue(User.class);
                    if(user.getProfilePic()!=null){
                        Glide.with(getContext()).load(user.getProfilePic()).placeholder(R.drawable.anh).into(mImgAvatarCurrentUser);
                    }
                    if(user.getFullName()!=null){
                        mTxtCurrentUserName.setText(user.getFullName());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
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
