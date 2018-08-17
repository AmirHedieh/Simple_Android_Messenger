package com.example.amas.messenger;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserMessagingActivity extends AppCompatActivity implements View.OnClickListener {

    private User user;

    private DatabaseReference dRef;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_messaging);

        dRef = FirebaseDatabase.getInstance().getReference();
        
        updateUIOnLogin();

    }



    private void updateUIOnLogin(){
            user = (User) getIntent().getSerializableExtra("user_serialized");
            getSupportActionBar().setTitle(user.username);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
    }

    class MessageToItem extends Item<ViewHolder> {
        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            if(user != null)
            Picasso.get().load(user.profilePhotoUrl).into((CircleImageView) viewHolder.itemView.findViewById(R.id.from_message_profile_image));
        }
        @Override
        public int getLayout() {
            return R.layout.to_chat_message;
        }
    }
    class MessageFromItem extends Item<ViewHolder> {
        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            if(user != null)
            Picasso.get().load(user.profilePhotoUrl).into((CircleImageView) viewHolder.itemView.findViewById(R.id.from_message_profile_image));
        }
        @Override
        public int getLayout() {
            return R.layout.to_chat_message;
        }
    }
}
