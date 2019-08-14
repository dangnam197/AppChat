package appchat.anh.nam.login;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import appchat.anh.nam.R;
import appchat.anh.nam.model.User;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private final int CAMERA_CODE_IMG = 1, GALLERY_CODE_IMG = 2;
    private static final String TAG = "ketqua";

    //View
    private EditText mEdtEmail, mEdtPassword, mEdtConfirmPassword, mEdtName;
    private Button mBtnRegister, mBtnCancel;
    private FirebaseAuth mFireBaseAuth;
    private CircleImageView mImgAvatar;
    private ProgressBar mProgressBarRegister;

    //Firebase
    private DatabaseReference mData;
    private StorageReference mStoreReference;
    private FirebaseStorage mFireBaseStorage;
    private FirebaseUser mFireBaseUser;

    //Interface
    private ActionRegisterInterface mActionRegisterInterface;

    //Create User
    private User mUser;

    private final View.OnClickListener mImgAvatarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //camera
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivityForResult(intent, CAMERA_CODE_IMG);

            //gallery
            Intent i = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, GALLERY_CODE_IMG);
        }
    };

    private final View.OnClickListener mBtnRegisterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            register();
        }
    };

    private View.OnClickListener mBtnCancelClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mActionRegisterInterface.actionCancel();
        }
    };

    public interface ActionRegisterInterface{
        void registerSuccess();
        void actionCancel();
    }

    public RegisterFragment() {
        // Required empty public constructor
    }

    public void setInterface(ActionRegisterInterface actionRegisterInterface){
        mActionRegisterInterface = actionRegisterInterface;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        initView(view);
        initAction();
        initFireBase();


        return view;
    }

    private void initFireBase(){
        mFireBaseAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference();
        mFireBaseStorage = FirebaseStorage.getInstance();
        mStoreReference = mFireBaseStorage.getReference();
    }

    private void initView(View view) {
        mUser = new User();
        mEdtEmail = view.findViewById(R.id.edtEmail);
        mEdtPassword = view.findViewById(R.id.edtPass);
        mBtnRegister = view.findViewById(R.id.btnRegister);
        mEdtConfirmPassword = view.findViewById(R.id.edtConfirmPassword);
        mImgAvatar = view.findViewById(R.id.imgAvatar);
        mEdtName = view.findViewById(R.id.edtName);
        mBtnCancel = view.findViewById(R.id.btnCancel);
        mProgressBarRegister = view.findViewById(R.id.progressBarRegister);
    }

    private void initAction(){
        mImgAvatar.setOnClickListener(mImgAvatarClick);
        mBtnRegister.setOnClickListener(mBtnRegisterClick);
        mBtnCancel.setOnClickListener(mBtnCancelClick);
    }

    private void register() {
        final String email = mEdtEmail.getText().toString();
        String password = mEdtPassword.getText().toString();
        String confirmPassword = mEdtConfirmPassword.getText().toString();
        final String name = mEdtName.getText().toString();

        if (email.equals("") || password.equals("") || confirmPassword.equals("") || name.equals("")) {
            Toast.makeText(getActivity(), "Please Write Full Your Infor", Toast.LENGTH_SHORT).show();
        } else if (!confirmPassword.equals(password)) {
            Toast.makeText(getActivity(), "Pass word is not equals", Toast.LENGTH_SHORT).show();
        } else {
            mFireBaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                AuthResult authResult = task.getResult();
                                mFireBaseUser = authResult.getUser();
                                String uid = mFireBaseUser.getUid();
                                upLoadImage(uid);
                                mUser.setFullName(name);
                                mUser.setId(uid);
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build();
                                mFireBaseUser.updateProfile(profileUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: update successful");
                                    }
                                });
                                Toast.makeText(getActivity(), "Register Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Register fall!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_CODE_IMG && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            mImgAvatar.setImageBitmap(bitmap);
        } else if (requestCode == GALLERY_CODE_IMG && resultCode == RESULT_OK && data != null) {
            Log.d(TAG, "onActivityResult: image from gallery");
            Uri uri = data.getData();
            mImgAvatar.setImageURI(uri);
        } else {
            Toast.makeText(getActivity(), "No Image", Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void upLoadImage(String imgName) {
        final StorageReference storageReference = mStoreReference.child(imgName+".png");
        mImgAvatar.setDrawingCacheEnabled(true);
        mImgAvatar.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) mImgAvatar.getDrawable()).getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        final byte[] data = outputStream.toByteArray();

        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Upload Fall!!!", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(), "Upload Successful", Toast.LENGTH_SHORT).show();

                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(TAG, "onSuccess: "+uri);
                        mUser.setProfilePic(String.valueOf(uri));
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(uri)
                                .build();

                        mFireBaseUser.updateProfile(profileUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess: update successful");
                                mUser.setStatus("online");
                                saveUser(mUser);
                                mActionRegisterInterface.registerSuccess();
                            }
                        });
                    }
                });
            }
        });
    }


    private void saveUser(User user){
        Log.d(TAG, "saveUser: "+user.getFullName());
        mData.child("Users").child(user.getId()).setValue(user);
    }

}
