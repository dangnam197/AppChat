package appchat.anh.nam.common;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusUser {
    private static StatusUser instance;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private StatusUser() {
    }

    public static StatusUser getInstance(){
      if (instance!=null){
          return instance;
      }
      else{
          return new StatusUser();
      }
    }
    public void setUserOnline(String id){
        databaseReference.child("Users").child(id).child("status").setValue("online");
    }

    public void setUserOffline(String id){
        databaseReference.child("Users").child(id).child("status").setValue("offline");
    }
}
