package appchat.anh.nam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

import appchat.anh.nam.R;
import appchat.anh.nam.common.Contact;
import appchat.anh.nam.model.Message;
import appchat.anh.nam.model.User;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<Message> mListMessages = new ArrayList<>();
    private String mCurrentUserId;
    private final HashMap<String, User> mHashMapUsers = new HashMap<>();
    private Context mContext;
    public ChatAdapter(ArrayList<Message>listMessages,HashMap<String,User> hashMapUsers, String currentUserId) {
        mCurrentUserId = currentUserId;
        if(hashMapUsers!=null) {
            mHashMapUsers.putAll(hashMapUsers);
        }
        if(listMessages!=null){
            mListMessages.addAll(listMessages);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext = parent.getContext();
        }
        View view;
        if(viewType==Contact.TYPE_CURRENT_USER){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_send,parent,false);
            return new SendViewHolder(view);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_receive,parent,false);
        return new ReceiveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(mListMessages.get(position).getFromId().equals(mCurrentUserId)){
            ((SendViewHolder)holder).bindViewHolder(mListMessages.get(position));
        }else {
            ((ReceiveViewHolder)holder).bindViewHolder(mListMessages.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mListMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(getItemViewType(position)==Contact.TYPE_CURRENT_USER){
            return Contact.TYPE_CURRENT_USER;
        }
        return Contact.TYPE_USER;
    }

    public class SendViewHolder extends RecyclerView.ViewHolder{
        private TextView mTvMessage;
        private void bindViewHolder(Message message){
            mTvMessage.setText(message.getMessage());
        }
        private SendViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvMessage = itemView.findViewById(R.id.messenger_send);
        }
    }
    public class ReceiveViewHolder extends RecyclerView.ViewHolder{
        private TextView mTvMessage;
        private TextView mTvUserName;
        private ImageView mUserPic;
        private ImageView mStatus;
        private void bindViewHolder(Message message){
            mTvMessage.setText(message.getMessage());
            mTvUserName.setText(mHashMapUsers.get(message.getFromId()).getFullName());
            if(mHashMapUsers.get(message.getFromId()).getStatus().equals(Contact.STATUS_ONLINE)){
                Glide.with(mContext).load(R.drawable.status_online).into(mStatus);
            }else {
                Glide.with(mContext).load(R.drawable.status_offline).into(mStatus);
            }
            Glide.with(mContext).load(mHashMapUsers.get(message.getFromId()).getProfilePic()).into(mUserPic);
        }
        private ReceiveViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvMessage = itemView.findViewById(R.id.tv_messenger_receive);
            mTvUserName = itemView.findViewById(R.id.tv_user_name);
            mUserPic = itemView.findViewById(R.id.img_user_pic);
            mStatus = itemView.findViewById(R.id.status);
        }
    }
    public void addAllItem(ArrayList<Message> list){
        mListMessages.addAll(list);
        notifyDataSetChanged();
    }
    public void addItem(Message message){
        mListMessages.add(message);
        notifyDataSetChanged();
    }
    public void addAllUser(HashMap<String,User> userHashMap){
        mHashMapUsers.putAll(userHashMap);
        notifyDataSetChanged();
    }
    public void addUser(String key,User user){
        mHashMapUsers.put(key,user);
        notifyDataSetChanged();
    }
    public ArrayList<Message> getListMessages(){
        return mListMessages;
    }
}
