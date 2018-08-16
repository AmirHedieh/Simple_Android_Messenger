package com.example.amas.messenger;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserMessagingActivity extends AppCompatActivity implements View.OnClickListener {

    private User user;

    private DatabaseReference firebaseDatabaseRef;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_messaging);

        getSupportActionBar().setTitle("Connecting...");
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Intent intent = new Intent(UserMessagingActivity.this, SignInActivity.class);
            startActivity(intent);

            this.finish();
            return;
        }

        firebaseDatabaseRef = FirebaseDatabase.getInstance().getReference();
        
        updateUIOnLogin();



//        new Handler().postDelayed(new Runnable() { //delay actions till app gets data from server and init user
//            @Override
//            public void run() {
//
//            }
//        },5000);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_messaging_activity_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out_user_mesaging_activity: {
                FirebaseAuth.getInstance().signOut();
                if(FirebaseAuth.getInstance().getCurrentUser() == null){
                    Log.d("User_Messaging","User signed out successfully");
                }
                else{
                    Log.d("User_Messaging","User could not sign out");
                }
                startActivity(new Intent(UserMessagingActivity.this,SignInActivity.class));
                this.finish();
            }
            case R.id.add_user_messaging_activity:{
                Intent intent = new Intent(UserMessagingActivity.this,ContactsActivity.class);
                startActivity(intent);
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
                GroupAdapter adapter = new GroupAdapter();

                recyclerView = findViewById(R.id.recyclerView_messaging_activity);

                adapter.add(new MessageItem());
                adapter.add(new MessageItem());
                adapter.add(new MessageItem());
                adapter.add(new MessageItem());

                recyclerView.setAdapter(adapter);

                getSupportActionBar().setTitle(user.username);
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

    class MessageItem extends Item<ViewHolder> {
        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            if(user != null)
            Picasso.get().load(user.profilePhotoUrl).into((CircleImageView) viewHolder.itemView.findViewById(R.id.profile_image_messaging_activity));
        }
        @Override
        public int getLayout() {
            return R.layout.to_chat_message;
        }
    }
}
