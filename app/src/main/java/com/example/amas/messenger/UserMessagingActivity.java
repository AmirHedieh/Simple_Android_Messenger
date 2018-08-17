package com.example.amas.messenger;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserMessagingActivity extends AppCompatActivity implements View.OnClickListener {

    private User user;

    private DatabaseReference dRef;

    private RecyclerView recyclerView;
    private GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_messaging);

        findViewById(R.id.send_button_mesaging_activity).setOnClickListener(this);

        recyclerView = findViewById(R.id.recyclerView_messaging_activity);
        dRef = FirebaseDatabase.getInstance().getReference();
        adapter = new GroupAdapter();

        updateUIOnLogin();

    }



    private void updateUIOnLogin(){
            user = (User) getIntent().getSerializableExtra("user_serialized");
            getSupportActionBar().setTitle(user.username);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.send_button_mesaging_activity:{
                adapter.add(new MessageToItem());
                adapter.add(new MessageFromItem());
                recyclerView.setAdapter(adapter);
                break;
            }
        }
    }

    class MessageToItem extends Item<ViewHolder> {
        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
//            if(user != null)
//            Picasso.get().load(user.profilePhotoUrl).into((CircleImageView) viewHolder.itemView.findViewById(R.id.from_message_profile_image));
        }
        @Override
        public int getLayout() {
            return R.layout.to_chat_message;
        }
    }
    class MessageFromItem extends Item<ViewHolder> {
        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
//            if(user != null)
//            Picasso.get().load(user.profilePhotoUrl).into((CircleImageView) viewHolder.itemView.findViewById(R.id.from_message_profile_image));
        }
        @Override
        public int getLayout() {
            return R.layout.from_chat_message;
        }
    }
}
