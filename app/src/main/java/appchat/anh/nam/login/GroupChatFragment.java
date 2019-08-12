package appchat.anh.nam.login;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import appchat.anh.nam.R;
import appchat.anh.nam.common.Contact;
import appchat.anh.nam.model.Group;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupChatFragment extends Fragment {

    private RecyclerView mRecyclerViewGroupChat;
    private ArrayList<Group> mArrGroup;
    private ArrayList<String> mArrIdGroup = new ArrayList<>();
    private DatabaseReference mData;
    private String mIdUser;

    public GroupChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_chat, container, false);
        initView(view);


        return view;
    }

    private void initView(View view){
        mRecyclerViewGroupChat = view.findViewById(R.id.recyclerViewGroupChat);
    }

    private void initBundle(){
        Bundle bundle = getArguments();
        if(bundle!=null){
            mIdUser = bundle.getString(Contact.KEY_CURRENT_ID);
        }
    }

    private void initData(){
        mData = FirebaseDatabase.getInstance().getReference();
        if(mIdUser!=null){
            mData.child("Users").child(mIdUser).child("Groups").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        mArrIdGroup.add((String) dataSnapshot1.getValue());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            DatabaseReference dataGroup = mData.child("Groups");
            for(String idGroup : mArrIdGroup){
                dataGroup.child(idGroup).child("GroupDetail").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Group group = dataSnapshot.getValue(Group.class);
                        mArrGroup.add(group);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        }
    }

}
