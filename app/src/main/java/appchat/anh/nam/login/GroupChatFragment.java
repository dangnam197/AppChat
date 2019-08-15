package appchat.anh.nam.login;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import appchat.anh.nam.FriendFragment;
import appchat.anh.nam.FriendRequestFragment;
import appchat.anh.nam.R;
import appchat.anh.nam.adapter.GroupFriendPagerAdapter;
import appchat.anh.nam.common.Contact;
import appchat.anh.nam.model.User;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupChatFragment extends Fragment {

    private CircleImageView mImgAvatarCurrentUser;
    private TextView mTxtCurrentUserName, mTxtLogOut;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private DatabaseReference mData;
    private GroupChatCallBackActivity mCallBackActivity;
    private User mCurrentUser;
    private static final String TAG = "ketqua";

    private View.OnClickListener mTxtLogOutClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallBackActivity.actionSignOut();
        }
    };

    private GroupFragment.CallBackGroupChatFragment mCallBackGroupChatFragment = new GroupFragment.CallBackGroupChatFragment() {
        @Override
        public void onItemGroupClick(String idUser, String idGroup) {
            mCallBackActivity.actionCallChatActivity(idUser, idGroup);
        }
    };

    public interface GroupChatCallBackActivity{
        void actionSignOut();
        void actionCallChatActivity(String idUser, String idGroup);
    }

    public GroupChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_chat, container, false);
        initView(view);
        initBundle();
        initViewPager();
        initAction();
        initData();
        return view;
    }

    public void setInterface(GroupChatCallBackActivity callBack){
        mCallBackActivity = callBack;
    }

    private void initView(View view) {
        mImgAvatarCurrentUser = view.findViewById(R.id.imgAvatarCurrentUser);
        mTxtCurrentUserName = view.findViewById(R.id.txtCurrentUserName);
        mTxtLogOut = view.findViewById(R.id.txtLogOut);
        mViewPager = view.findViewById(R.id.viewPager);
        mTabLayout = view.findViewById(R.id.tabLayout);
    }

    private void initAction(){
        mTxtLogOut.setOnClickListener(mTxtLogOutClick);
    }

    private void initBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCurrentUser = bundle.getParcelable(Contact.KEY_CURRENT_USER);
        }
    }

    private void initData() {
        mData = FirebaseDatabase.getInstance().getReference();

        if (mCurrentUser != null) {
            mData.child(Contact.TABLE_USER).child(mCurrentUser.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange: "+dataSnapshot.toString());
                    User user = dataSnapshot.getValue(User.class);
                    if(user.getProfilePic()!=null){
                        Glide.with(getContext()).load(user.getProfilePic()).placeholder(R.drawable.default_user).into(mImgAvatarCurrentUser);
                    }
                    if(user.getFullName()!=null){
                        mTxtCurrentUserName.setText(user.getFullName());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void initViewPager(){
        GroupFriendPagerAdapter mPagerAdapter = new GroupFriendPagerAdapter(getFragmentManager());
        GroupFragment groupFragment = GroupFragment.newInstance(mCurrentUser.getId());
        groupFragment.setInterface(mCallBackGroupChatFragment);
        FriendFragment friendFragment = FriendFragment.newInstance(mCurrentUser.getId());
        FriendRequestFragment friendRequestFragment = FriendRequestFragment.newInstance(mCurrentUser.getId(),mCurrentUser.getFullName());
        mPagerAdapter.addFragment(groupFragment);
        mPagerAdapter.addFragment(friendFragment);
        mPagerAdapter.addFragment(friendRequestFragment);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).setIcon(R.drawable.group);
        mTabLayout.getTabAt(1).setIcon(R.drawable.search);
        mTabLayout.getTabAt(2).setIcon(R.drawable.user);
    }
}
