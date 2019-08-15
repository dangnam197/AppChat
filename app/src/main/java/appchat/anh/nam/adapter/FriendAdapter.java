package appchat.anh.nam.adapter;

import android.content.Context;
import android.util.Log;
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

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    public interface OnClickListener{
        void addFriendClick(User user,int position);
    }
    private Context mContext;
    private final ArrayList<User> mListUser = new ArrayList<>();
    private OnClickListener mOnClickListener;

    public FriendAdapter(ArrayList<User> listUser, OnClickListener onClickListener) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_seach,parent,false);
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
        private ImageButton mBtnAddFriend;
        private ImageView mImgUserPic;
        private final View.OnClickListener mAddFriendClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickListener!=null){
                    mOnClickListener.addFriendClick(mListUser.get(getAdapterPosition()),getAdapterPosition());
                    mBtnAddFriend.setVisibility(View.GONE);
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
            mBtnAddFriend = itemView.findViewById(R.id.btn_add_friend);
            mImgUserPic = itemView.findViewById(R.id.img_user_pic);
            mBtnAddFriend.setOnClickListener(mAddFriendClick);
        }
    }
    public void addUser(User user){
        if(user!=null){
            mListUser.add(user);
            notifyDataSetChanged();
            Log.d("FriendFragment", "addUser: "+mListUser.size());
        }
    }
    public void addAllUser(ArrayList<User> listUser){
        if(listUser!=null&&listUser.size()>0){
            mListUser.clear();
            mListUser.addAll(listUser);
            notifyDataSetChanged();

        }
    }
}
