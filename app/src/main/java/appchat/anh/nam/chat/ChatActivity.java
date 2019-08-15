package appchat.anh.nam.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import appchat.anh.nam.R;
import appchat.anh.nam.chat.fragment.ChatFragment;
import appchat.anh.nam.common.Contact;
import appchat.anh.nam.model.Group;

public class ChatActivity extends AppCompatActivity {
    private String userCurrentId;
    private static final String TAG = "ketqua";
    private Group mCurrentGroup;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initIntent();
        setFragmentChat();
    }

    private void initIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userCurrentId = bundle.getString(Contact.KEY_CURRENT_ID);
        mCurrentGroup = bundle.getParcelable(Contact.KEY_GROUP);
    }

    private void setFragmentChat() {
        getSupportFragmentManager().beginTransaction().add(R.id.frame_chat,ChatFragment.newInstance(mCurrentGroup,userCurrentId)).addToBackStack("").commit();
    }
}
