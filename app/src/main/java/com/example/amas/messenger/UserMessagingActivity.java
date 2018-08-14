package com.example.amas.messenger;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserMessagingActivity extends AppCompatActivity {

    private User user;

    private DatabaseReference firebaseDatabaseRef;

    private TextView usernameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_messaging);

        usernameTextView = findViewById(R.id.username_user_messaging_activity);

        firebaseDatabaseRef = FirebaseDatabase.getInstance().getReference();
        updateUIOnLogin();
    }

    private void updateUIOnLogin(){
        DatabaseReference myRef = firebaseDatabaseRef.child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user =  dataSnapshot.getValue(User.class);
                if(user == null) {
                    Log.d("User_Messaging","User was returned as null");
                    return;
                }
                    usernameTextView.setText(user.username);
                getSupportActionBar().setTitle(user.username);
//                Toast.makeText(UserMessagingActivity.this, ""+dataSnapshot.getValue(User.class).email, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
