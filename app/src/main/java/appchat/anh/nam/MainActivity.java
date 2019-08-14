package appchat.anh.nam;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import appchat.anh.nam.chat.ChatActivity;
import appchat.anh.nam.common.Contact;
import appchat.anh.nam.login.GroupChatFragment;
import appchat.anh.nam.login.LoginFragment;
import appchat.anh.nam.login.RegisterFragment;
import appchat.anh.nam.model.User;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FragmentTransaction mFragmentTransaction;
    private FirebaseAuth mFireBaseAuth;
    private DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();

    private RegisterFragment.ActionRegisterInterface mActionRegisterInterface = new RegisterFragment.ActionRegisterInterface() {
        @Override
        public void registerSuccess() {
            String currentUserId = checkUser();
            if(currentUserId.equals("NoUser")){
                initLoginFragment();
            }
            else{
                initGroupChatFragment(currentUserId);
                mDatabaseReference.child(Contact.TABLE_USER).child(currentUserId).child("status").setValue("online");
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
            initGroupChatFragment(user.getId());
        }

        @Override
        public void onLoginFall() {
            Toast.makeText(MainActivity.this, "Login fall!", Toast.LENGTH_SHORT).show();
        }
    };

    private GroupChatFragment.GroupChatCallBackActivity mCallBack = new GroupChatFragment.GroupChatCallBackActivity() {
        @Override
        public void actionSignOut() {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            initLoginFragment();
        }

        @Override
        public void actionCallChatActivity(String idUser, String idGroup) {
            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            intent.putExtra(Contact.KEY_CURRENT_ID, idUser);
            intent.putExtra(Contact.KEY_GROUP_ID, idGroup);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String currentUserId = checkUser();
        Log.d(TAG, "onCreate: "+checkUser());
        if(currentUserId.equals("NoUser")){
            initLoginFragment();
        }
        else{
            initGroupChatFragment(currentUserId);
            DatabaseReference data = FirebaseDatabase.getInstance().getReference();
            data.child("Users").child(currentUserId).child("status").setValue("online");
        }


    }

    private String checkUser(){
        mFireBaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mFireBaseAuth.getCurrentUser();
        if(currentUser!=null){
            Log.d(TAG, "checkUser: đã đăng nhập");
            return currentUser.getUid();
        }
        Log.d(TAG, "checkUser: chưa đăng nhập");
        return "NoUser";
    }

    private void initGroupChatFragment(String userId){
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        GroupChatFragment groupChatFragment = new GroupChatFragment();
        groupChatFragment.setInterface(mCallBack);
        Bundle bundle = new Bundle();
        bundle.putString(Contact.KEY_CURRENT_ID, userId);
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

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
