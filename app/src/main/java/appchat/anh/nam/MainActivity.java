package appchat.anh.nam;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import appchat.anh.nam.chat.ChatActivity;
import appchat.anh.nam.common.Contact;
import appchat.anh.nam.login.LoginFragment;
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ketqua";
    private FragmentTransaction fragmentTransaction;
    //private FirebaseAuth firebaseAuth;
    //private FirebaseUser mFireBaseUser;
    //private DatabaseReference mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference();
//        myRef.child("Groups").child("xxx").child("Members").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
//                    Log.d(TAG, "onDataChange: "+dataSnapshot1.getKey());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        //firebaseAuth = FirebaseAuth.getInstance();
        //mData = FirebaseDatabase.getInstance().getReference();

//        fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        LoginFragment loginFragment = new LoginFragment();
//        fragmentTransaction.replace(R.id.frame, loginFragment);
//        fragmentTransaction.commit();
        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
        intent.putExtra(Contact.KEY_GROUP_ID,"xxx");
        intent.putExtra(Contact.KEY_CURRENT_ID,"SAii3UQTFKaE403hEajkNIOz0dz2");
        startActivity(intent);
    }
}
