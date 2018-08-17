package com.example.amas.messenger;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LatestMessagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_messages);

        getSupportActionBar().setTitle("Connecting...");
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Intent intent = new Intent(LatestMessagesActivity.this, SignInActivity.class);
            startActivity(intent);

            this.finish();
            return;
        }

        updateUIOnLogin();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.latest_messages_activity_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out_user_mesaging_activity: {
                Log.d("signout","id:"+item.getItemId());
                Log.d("signout","sign outing");
                FirebaseAuth.getInstance().signOut();
                if(FirebaseAuth.getInstance().getCurrentUser() != null){
                    Toast.makeText(this, "Couldn't Signed out", Toast.LENGTH_SHORT).show();
                }
                else{
                startActivity(new Intent(LatestMessagesActivity.this,SignInActivity.class));
                this.finish();
                }
                break;
            }
            case R.id.add_user_messaging_activity:{
                Log.d("signout","id:"+item.getItemId());
                Log.d("signout","add user");
                Intent intent = new Intent(LatestMessagesActivity.this,ContactsActivity.class);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUIOnLogin(){
        DatabaseReference firebaseDatabaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference loggedInUserRef = firebaseDatabaseRef.child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        loggedInUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user =  dataSnapshot.getValue(User.class);
                if(user == null) {
                    return;
                }

                getSupportActionBar().setTitle(user.username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
