package appchat.anh.nam;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import appchat.anh.nam.login.LoginFragment;
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FragmentTransaction fragmentTransaction;
    //private FirebaseAuth firebaseAuth;
    //private FirebaseUser mFireBaseUser;
    //private DatabaseReference mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference();

        //firebaseAuth = FirebaseAuth.getInstance();
        //mData = FirebaseDatabase.getInstance().getReference();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        LoginFragment loginFragment = new LoginFragment();
        fragmentTransaction.replace(R.id.frame, loginFragment);
        fragmentTransaction.commit();
    }
}
