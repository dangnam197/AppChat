package appchat.anh.nam;


import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private int a;



    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("test").setValue("aaa");
        Log.d("ketqua", "onCreate: ");

        a=10;
        a++;
        Log.d(TAG, "onCreate: push 09/08");

        int b=a;
        Log.d(TAG, "onCreate: hoanganhktd");
        int c = b;
        Log.d(TAG, "onCreate: "+c);
        Log.d(TAG, "onCreate: xyz");
    }
}
