package com.example.amas.messenger;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserMessagingActivity extends AppCompatActivity implements View.OnClickListener {

    private User user;

    private DatabaseReference firebaseDatabaseRef;

    private TextView usernameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_messaging);

        getSupportActionBar().setTitle("Connecting...");
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Intent intent = new Intent(UserMessagingActivity.this, SignInActivity.class);
            startActivity(intent);
            this.finish();
        }

        usernameTextView = findViewById(R.id.username_user_messaging_activity);

        firebaseDatabaseRef = FirebaseDatabase.getInstance().getReference();


        updateUIOnLogin();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_messaging_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out_user_mesaging_activity: {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserMessagingActivity.this,SignInActivity.class));
                this.finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUIOnLogin(){
        DatabaseReference myRef = firebaseDatabaseRef.child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user =  dataSnapshot.getValue(User.class);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();

    }
}
