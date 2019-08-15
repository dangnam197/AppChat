package appchat.anh.nam;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import appchat.anh.nam.chat.ChatActivity;
import appchat.anh.nam.common.Contact;
import appchat.anh.nam.common.StatusUser;
import appchat.anh.nam.login.GroupChatFragment;
import appchat.anh.nam.login.LoginFragment;
import appchat.anh.nam.login.RegisterFragment;
import appchat.anh.nam.model.Group;
import appchat.anh.nam.model.User;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private FragmentTransaction mFragmentTransaction;
    private FirebaseAuth mFireBaseAuth;
    private DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
    private Toolbar mToolbar;
    private TextView mTxtCurrentUserName;
    private AppBarLayout mAppBarLayout;
    private CircleImageView mImgAvatar;

    private RegisterFragment.ActionRegisterInterface mActionRegisterInterface = new RegisterFragment.ActionRegisterInterface() {
        @Override
        public void registerSuccess() {
            User user = checkUser();
            if(user!=null){
                setToolbar(user);
                initGroupChatFragment(user);
                DatabaseReference data = FirebaseDatabase.getInstance().getReference();
                data.child("Users").child(user.getId()).child("status").setValue("online");
            }
            else{
                initLoginFragment();
            }
        }

        @Override
        public void actionCancel() {
            initLoginFragment();
        }

    };

    private LoginFragment.LoginFragmentInterface mLoginFragmentInterface = new LoginFragment.LoginFragmentInterface() {
        @Override
        public void onBtnRegisterClick() {
            initRegisterFragment();
        }

        @Override
        public void onLoginSuccess(User user) {
            setToolbar(user);
            initGroupChatFragment(user);
        }

        @Override
        public void onLoginFall() {
            Toast.makeText(MainActivity.this, "Login fall!", Toast.LENGTH_SHORT).show();
        }
    };

    private GroupChatFragment.GroupChatCallBackActivity mCallBack = new GroupChatFragment.GroupChatCallBackActivity() {

        @Override
        public void actionCallChatActivity(String idUser, Group group) {
            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(Contact.KEY_GROUP, group);
            bundle.putString(Contact.KEY_CURRENT_ID, idUser);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        initView();
        User user = checkUser();
        if(user!=null){
            setToolbar(user);
            initGroupChatFragment(user);
            DatabaseReference data = FirebaseDatabase.getInstance().getReference();
            data.child("Users").child(user.getId()).child("status").setValue("online");
        }
        else{
            initLoginFragment();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        User user = checkUser();
        if(user!=null){
            StatusUser.getInstance().setUserOnline(user.getId());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.item_logout){
            actionSignOut();
            mAppBarLayout.setVisibility(View.GONE);
            return true;
        }
        return false;

    }

    private void initView(){
        mToolbar = findViewById(R.id.chat_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mImgAvatar = findViewById(R.id.img_avatar);
        mAppBarLayout = findViewById(R.id.appbar_layout);
        mTxtCurrentUserName = findViewById(R.id.txt_current_user_name);
    }

    private User checkUser(){
        User user = new User();
        mFireBaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mFireBaseAuth.getCurrentUser();
        if(currentUser!=null){
            user.setId(currentUser.getUid());
            user.setFullName(currentUser.getDisplayName());
            user.setProfilePic(String.valueOf(currentUser.getPhotoUrl()));
            user.setStatus("online");
            Log.d("ketqua", "checkUser: auth"+user.getProfilePic());
            return user;
        }

        return null;
    }

    private void setToolbar(User user){
        mAppBarLayout.setVisibility(View.VISIBLE);
        mTxtCurrentUserName.setText(user.getFullName());
        Glide.with(getApplicationContext()).load(user.getProfilePic()).into(mImgAvatar);
    }

    private void initGroupChatFragment(User user){
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        GroupChatFragment groupChatFragment = new GroupChatFragment();
        groupChatFragment.setInterface(mCallBack);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Contact.KEY_CURRENT_USER, user);
        groupChatFragment.setArguments(bundle);
        mFragmentTransaction.replace(R.id.frame, groupChatFragment);
        mFragmentTransaction.commit();
    }

    private void initLoginFragment(){
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setInterface(mLoginFragmentInterface);
        mFragmentTransaction.replace(R.id.frame, loginFragment);
        mFragmentTransaction.commit();
    }

    private void initRegisterFragment(){
        RegisterFragment registerFragment = new RegisterFragment();
        registerFragment.setInterface(mActionRegisterInterface);
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.frame, registerFragment);
        mFragmentTransaction.commit();
    }

    public void actionSignOut() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        initLoginFragment();
    }

    @Override
    protected void onStop() {
        User user = checkUser();
        if(user!=null){
            StatusUser.getInstance().setUserOffline(user.getId());
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
