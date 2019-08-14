package appchat.anh.nam.login;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import appchat.anh.nam.R;
import appchat.anh.nam.model.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private EditText mEdtEmail, mEdtPassword;
    private Button mBtnLogin, mBtnRegister;
    private FirebaseAuth mFireBaseAuth;
    private FragmentTransaction mFragmentTransaction;
    private DatabaseReference mDatabaseReference;
    private LoginFragmentInterface mLoginFragmentInterface;

    private final View.OnClickListener mBtnLoginClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            login();
        }
    };

    private final View.OnClickListener mBtnRegisterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mLoginFragmentInterface.onBtnRegisterClick();
        }
    };


    public interface LoginFragmentInterface {
        void onBtnRegisterClick();

        void onLoginSuccess(User user);

        void onLoginFall();
    }


    public LoginFragment() {
        // Required empty public constructor
    }

    public void setInterface(LoginFragmentInterface buttonRegisterClickInterface) {
        mLoginFragmentInterface = buttonRegisterClickInterface;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        initView(view);
        initFireBase();
        initAction();

        return view;
    }


    private void initView(View view) {
        mEdtEmail = view.findViewById(R.id.edtEmail);
        mEdtPassword = view.findViewById(R.id.edtPass);
        mBtnLogin = view.findViewById(R.id.btnLogin);
        mBtnRegister = view.findViewById(R.id.btnRegister);
    }

    private void initFireBase() {
        mFireBaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void initAction() {
        mBtnLogin.setOnClickListener(mBtnLoginClick);
        mBtnRegister.setOnClickListener(mBtnRegisterClick);
    }

    private void login() {
        String email, password;
        email = mEdtEmail.getText().toString();
        password = mEdtPassword.getText().toString();

        if (email.equals("") || password.equals("")) {
            Toast.makeText(getActivity(), "Vui lòng nhập đẩy đủ thông tin", Toast.LENGTH_SHORT).show();
        } else {
            mFireBaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = task.getResult().getUser();
                        mDatabaseReference.child("Users").child(currentUser.getUid()).child("status").setValue("online");
                        User user = new User(currentUser.getDisplayName(), currentUser.getUid(), String.valueOf(currentUser.getPhotoUrl()), "online");
                        mLoginFragmentInterface.onLoginSuccess(user);
                    } else {
                        mLoginFragmentInterface.onLoginFall();
                    }
                }
            });
        }


    }
}
