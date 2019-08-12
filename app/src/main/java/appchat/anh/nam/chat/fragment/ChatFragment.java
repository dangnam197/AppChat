package appchat.anh.nam.chat.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import appchat.anh.nam.R;
import appchat.anh.nam.common.Contact;


public class ChatFragment extends Fragment {
    private String mGroupId;
    private String mCurrentId;
    public ChatFragment() {

    }


    public static ChatFragment newInstance(String groupId,String currentId) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(Contact.KEY_GROUP_ID, groupId);
        args.putString(Contact.KEY_CURRENT_ID,currentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGroupId = getArguments().getString(Contact.KEY_GROUP_ID);
            mCurrentId = getArguments().getString(Contact.KEY_CURRENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }




}
