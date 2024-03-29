package appchat.anh.nam.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

import appchat.anh.nam.R;
import appchat.anh.nam.common.Contact;
import appchat.anh.nam.model.Message;
import appchat.anh.nam.model.User;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private final ArrayList<Message> mListMessages = new ArrayList<>();
    private String mCurrentUserId;
    private final HashMap<String, User> mHashMapUsers = new HashMap<>();
    private Context mContext;

    public ChatAdapter(ArrayList<Message> listMessages, HashMap<String, User> hashMapUsers, String currentUserId) {
        mCurrentUserId = currentUserId;
        if (hashMapUsers != null) {
            mHashMapUsers.putAll(hashMapUsers);
        }
        if (listMessages != null && listMessages.size() != 0) {
            mListMessages.addAll(listMessages);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view;
        if (viewType == Contact.TYPE_CURRENT_USER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_send, parent, false);
            return new SendViewHolder(view);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_receive, parent, false);
        return new ReceiveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindViewHolder(mListMessages.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mListMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mListMessages.get(position).getFromId().equals(mCurrentUserId)) {
            return Contact.TYPE_CURRENT_USER;
        }
        return Contact.TYPE_USER;
    }

    public class SendViewHolder extends ViewHolder {
        private TextView mTvMessage;

        public void bindViewHolder(Message message, int position) {
            mTvMessage.setText(message.getMessage());
            Message messageTop = null;
            Message messageBottom = null;
            if (position > 0) {
                messageTop = mListMessages.get(position - 1);
            }
            if (position < mListMessages.size() - 1) {
                messageBottom = mListMessages.get(position + 1);
            }
            if (messageTop != null && messageBottom != null) {
                if (message.getFromId().equals(messageTop.getFromId())) {

                    if (message.getFromId().equals(messageBottom.getFromId())) {

                        mTvMessage.setBackgroundResource(R.drawable.message_send_center_backgound);
                    } else {

                        mTvMessage.setBackgroundResource(R.drawable.message_send_bottom_backgound);
                    }
                } else {

                    if (message.getFromId().equals(messageBottom.getFromId())) {

                        mTvMessage.setBackgroundResource(R.drawable.message_send_top_backgound);
                    } else {
                       mTvMessage.setBackgroundResource(R.drawable.message_send_backgound);
                    }

                }

            }else {
                if(messageTop!=null){
                    if(message.getFromId().equals(messageTop.getFromId())){

                        mTvMessage.setBackgroundResource(R.drawable.message_send_bottom_backgound);
                    }else {

                        mTvMessage.setBackgroundResource(R.drawable.message_send_backgound);
                    }
                }else if(messageBottom!=null){
                    if (message.getFromId().equals(messageBottom.getFromId())) {

                        mTvMessage.setBackgroundResource(R.drawable.message_send_top_backgound);
                    } else {

                        mTvMessage.setBackgroundResource(R.drawable.message_send_backgound);
                    }
                }
            }
        }

        private SendViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvMessage = itemView.findViewById(R.id.messenger_send);
        }
    }

    public class ReceiveViewHolder extends ViewHolder {
        private TextView mTvMessage;
        private TextView mTvUserName;
        private ImageView mUserPic;
        private ImageView mStatus;

        public void bindViewHolder(Message message, int position) {
            mTvMessage.setText(message.getMessage());
            if (mHashMapUsers.get(message.getFromId()) != null) {
                mTvUserName.setText(Objects.requireNonNull(mHashMapUsers.get(message.getFromId())).getFullName());
                if (Contact.STATUS_ONLINE.equals(Objects.requireNonNull(mHashMapUsers.get(message.getFromId())).getStatus())) {
                    Glide.with(mContext).load(R.drawable.status_online).into(mStatus);
                } else {
                    Glide.with(mContext).load(R.drawable.status_offline).into(mStatus);
                }
                Glide.with(mContext).load(Objects.requireNonNull(mHashMapUsers.get(message.getFromId())).getProfilePic()).into(mUserPic);
            }
            Message messageTop = null;
            Message messageBottom = null;
            if (position > 0) {
                messageTop = mListMessages.get(position - 1);
            }
            if (position < mListMessages.size() - 1) {
                messageBottom = mListMessages.get(position + 1);
            }
            if (messageTop != null && messageBottom != null) {
                if (message.getFromId().equals(messageTop.getFromId())) {
                    mTvUserName.setVisibility(View.GONE);
                    if (message.getFromId().equals(messageBottom.getFromId())) {
                        mUserPic.setVisibility(View.GONE);
                        mStatus.setVisibility(View.GONE);
                        mTvMessage.setBackgroundResource(R.drawable.message_receive_center_background);
                    } else {
                        mUserPic.setVisibility(View.VISIBLE);
                        mStatus.setVisibility(View.VISIBLE);
                        mTvMessage.setBackgroundResource(R.drawable.message_receive_bottom_background);
                    }
                } else {
                    mTvUserName.setVisibility(View.VISIBLE);
                    if (message.getFromId().equals(messageBottom.getFromId())) {
                        mUserPic.setVisibility(View.GONE);
                        mStatus.setVisibility(View.GONE);
                        mTvMessage.setBackgroundResource(R.drawable.message_receive_top_background);
                    } else {
                        mUserPic.setVisibility(View.VISIBLE);
                        mStatus.setVisibility(View.VISIBLE);
                    }

                }

            }else {
                if(messageTop!=null){
                    if(message.getFromId().equals(messageTop.getFromId())){
                        mTvUserName.setVisibility(View.GONE);
                        mTvMessage.setBackgroundResource(R.drawable.message_receive_bottom_background);
                    }else {
                        mTvUserName.setVisibility(View.VISIBLE);
                        mTvMessage.setBackgroundResource(R.drawable.message_receive_background);
                    }
                }else {
                    mTvUserName.setVisibility(View.VISIBLE);
                }

                if(messageBottom!=null){
                    if (message.getFromId().equals(messageBottom.getFromId())) {
                        mUserPic.setVisibility(View.GONE);
                        mStatus.setVisibility(View.GONE);
                        mTvMessage.setBackgroundResource(R.drawable.message_receive_top_background);
                    } else {
                        mUserPic.setVisibility(View.VISIBLE);
                        mStatus.setVisibility(View.VISIBLE);
                        mTvMessage.setBackgroundResource(R.drawable.message_receive_background);
                    }
                }else {
                    mUserPic.setVisibility(View.VISIBLE);
                }
            }

        }

        private ReceiveViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvMessage = itemView.findViewById(R.id.tv_messenger_receive);
            mTvUserName = itemView.findViewById(R.id.tv_user_name);
            mUserPic = itemView.findViewById(R.id.img_user_pic);
            mStatus = itemView.findViewById(R.id.status);
        }
    }

    abstract class ViewHolder extends RecyclerView.ViewHolder {
        abstract void bindViewHolder(Message message, int position);

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void addAllItem(ArrayList<Message> list) {
        if (list != null && list.size() > 0) {
            mListMessages.addAll(list);
            sort();
            Log.d("ketqua", "onDataChange: " + list.size());
            Log.d("onClick", "addllItem: ");
            notifyDataSetChanged();
        }
    }

    public void addItem(Message message) {
        if(message!=null){
            mListMessages.add(message);
            notifyDataSetChanged();
        }
        Log.d("onClick", "addItem: ");

    }


    public void addUser(String key, User user) {
        if (user != null) {
            if (mHashMapUsers.get(key) != null) {
                Objects.requireNonNull(mHashMapUsers.get(key)).setStatus(user.getStatus());
            } else {
                mHashMapUsers.put(key, user);
            }
            notifyDataSetChanged();
        }
    }
    private void sort(){
        Collections.sort(mListMessages, new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                if(o1.getTime()>o2.getTime()){
                    return 1;
                }else if (o1.getTime()==o2.getTime()){
                    return 0;
                }
                return -1;
            }
        });

    }
    public ArrayList<Message> getListMessages() {
        return mListMessages;
    }


}
