package appchat.anh.nam.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import appchat.anh.nam.R;
import appchat.anh.nam.chat.fragment.ChatFragment;
import appchat.anh.nam.common.Contact;

public class ChatActivity extends AppCompatActivity {
    private String groupId;
    private String userCurrentId;
    private static final String TAG = "ketqua";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initIntent();
        setFragmentChat();
    }

    private void initIntent() {
        Intent intent = getIntent();
        groupId = intent.getStringExtra(Contact.KEY_GROUP_ID);
        userCurrentId = intent.getStringExtra(Contact.KEY_CURRENT_ID);
        Log.d(TAG, "initIntent: "+userCurrentId);
        Log.d(TAG, "initIntent: "+groupId);
    }

    private void setFragmentChat() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_chat,ChatFragment.newInstance(groupId,userCurrentId)).commit();
    }
}
