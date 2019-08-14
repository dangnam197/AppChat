package appchat.anh.nam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import appchat.anh.nam.R;
import appchat.anh.nam.model.User;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {
    public interface OnClickListener{
        void acceptFriendClick(User user, int position);
        void refuseFriendClick(User user, int position);
    }
    private Context mContext;
    private final ArrayList<User> mListUser = new ArrayList<>();
    private OnClickListener mOnClickListener;

    public FriendRequestAdapter(ArrayList<User> listUser,OnClickListener onClickListener) {
        if(listUser!=null&&listUser.size()>0){
            mListUser.addAll(listUser);
        }
        mOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_request,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindHoder(mListUser.get(position));
    }

    @Override
    public int getItemCount() {
        return mListUser.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvUserName;
        private ImageButton mBtnAcceptFriend;
        private ImageButton mBtnRefuseFriend;
        private ImageView mImgUserPic;
        private final View.OnClickListener mAcceptFriendClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickListener!=null){
                    mOnClickListener.acceptFriendClick(mListUser.get(getAdapterPosition()),getAdapterPosition());
                }
            }
        };

        private final View.OnClickListener mRefusetFriendClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickListener!=null){
                    mOnClickListener.refuseFriendClick(mListUser.get(getAdapterPosition()),getAdapterPosition());
                }
            }
        };
        private void bindHoder(User user){
            mTvUserName.setText(user.getFullName());
            Glide.with(mContext).load(user.getProfilePic()).placeholder(R.drawable.anh).into(mImgUserPic);

        }
        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvUserName = itemView.findViewById(R.id.tv_user_name);
            mBtnAcceptFriend = itemView.findViewById(R.id.btn_accept_friend);
            mBtnRefuseFriend = itemView.findViewById(R.id.btn_refuse_friend);
            mImgUserPic = itemView.findViewById(R.id.img_user_pic);
            mBtnAcceptFriend.setOnClickListener(mAcceptFriendClick);
            mBtnRefuseFriend.setOnClickListener(mRefusetFriendClick);
        }
    }
    public void addUser(User user){
        if(user!=null){
            mListUser.add(user);
            notifyDataSetChanged();

        }
    }
}
