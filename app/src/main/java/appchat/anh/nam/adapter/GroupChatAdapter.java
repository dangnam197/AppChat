package appchat.anh.nam.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import appchat.anh.nam.R;
import appchat.anh.nam.model.Group;
import appchat.anh.nam.model.Message;
import de.hdodenhof.circleimageview.CircleImageView;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.GroupChatHolder> {

    private final ArrayList<Group> mArrGroupChat = new ArrayList<>();
    private final HashMap<String, Message> mHashMapMessage = new HashMap<>();
    private Context mContext;
    private ItemGroupChatClick mItemGroupChatClick;


    public interface ItemGroupChatClick{
        void onGroupClick(Group group);
    }

    public GroupChatAdapter(ArrayList<Group> arrGroupChat, HashMap<String, Message> hashMapMessage, ItemGroupChatClick itemGroupChatClick) {
        mItemGroupChatClick = itemGroupChatClick;
        if (arrGroupChat != null) {
            mArrGroupChat.addAll(arrGroupChat);
        }
        if (hashMapMessage != null) {
            mHashMapMessage.putAll(hashMapMessage);
        }
    }

    @NonNull
    @Override
    public GroupChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext = parent.getContext();
        }
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_group_chat, parent, false);
        return new GroupChatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupChatHolder holder, int position) {
        holder.setData(mArrGroupChat.get(position));
    }

    @Override
    public int getItemCount() {
        return mArrGroupChat.size();
    }

    class GroupChatHolder extends RecyclerView.ViewHolder {
        private CircleImageView mImageAvatar;
        private TextView mTxtNameGroup, mTxtContentMessage, mTxtTimeMessage;

        GroupChatHolder(@NonNull View itemView) {
            super(itemView);
            mImageAvatar = itemView.findViewById(R.id.imgAvatar);
            mTxtContentMessage = itemView.findViewById(R.id.txtContentMessage);
            mTxtTimeMessage = itemView.findViewById(R.id.txtTimeMessage);
            mTxtNameGroup = itemView.findViewById(R.id.txtGroupName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemGroupChatClick.onGroupClick(mArrGroupChat.get(getAdapterPosition()));
                }
            });
        }

        private void setData(Group group) {
            if(mContext!=null){
                Glide.with(mContext).load(group.getGroupIcon()).placeholder(R.drawable.default_user).into(mImageAvatar);
            }
            mTxtNameGroup.setText(group.getName());
            if(mHashMapMessage.get(group.getId())!=null) {
                mTxtContentMessage.setText(mHashMapMessage.get(group.getId()).getMessage());
                mTxtTimeMessage.setText(convertTime((mHashMapMessage.get(group.getId()).getTime())));
            }
        }
    }

    public void addGroupChat(Group group) {
        boolean check = true;
        for(Group group1 : mArrGroupChat){
            if(group1.getId().equals(group.getId())){
                group1.setId(group.getId());
                check = false;
                break;
            }
        }
        if(check){
            mArrGroupChat.add(group);
        }
        notifyDataSetChanged();
    }

    public void updateRecentMessage(String idGroup, Message message) {
        if(message!=null) {
            mHashMapMessage.put(idGroup, message);
            mHashMapMessage.get(idGroup).setMessage(message.getMessage());
            mHashMapMessage.get(idGroup).setTime(message.getTime());
            if (message.getContentType().equals("image")) {
                mHashMapMessage.get(idGroup).setMessage("Đã nhận một hình ảnh");
            }
            notifyDataSetChanged();
        }
    }

    private String convertTime(long time){
        long differentTime = System.currentTimeMillis()/1000-time;
        if(differentTime>86400){
            Date date = new Date(time*1000);
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMM");
            String textTime = sdf.format(date);
            return textTime;
        }
        Date date = new Date(time*1000);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.CHINA);
        String textTime = sdf.format(date);
        return textTime;
    }
}
