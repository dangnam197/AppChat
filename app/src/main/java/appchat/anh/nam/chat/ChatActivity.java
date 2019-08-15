package appchat.anh.nam.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import appchat.anh.nam.R;
import appchat.anh.nam.chat.fragment.ChatFragment;
import appchat.anh.nam.common.Contact;
import appchat.anh.nam.common.StatusUser;
import appchat.anh.nam.model.Group;

public class ChatActivity extends AppCompatActivity {
    private String mUserCurrentId;
    private static final String TAG = "ketqua";
    private Group mCurrentGroup;
    private final View.OnClickListener mBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initIntent();
        initToolbar();
        setFragmentChat();
    }

    @Override
    protected void onStart() {
        super.onStart();
        StatusUser.getInstance().setUserOnline(mUserCurrentId);
    }

    @Override
    protected void onStop() {
        StatusUser.getInstance().setUserOffline(mUserCurrentId);
        super.onStop();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(mCurrentGroup.getName());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(mBackClickListener);

    }

    private void initIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mUserCurrentId = bundle.getString(Contact.KEY_CURRENT_ID);
        mCurrentGroup = bundle.getParcelable(Contact.KEY_GROUP);
    }

    private void setFragmentChat() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_chat,ChatFragment.newInstance(mCurrentGroup, mUserCurrentId)).commit();

    }
}
