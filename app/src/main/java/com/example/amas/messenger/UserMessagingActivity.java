package com.example.amas.messenger;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

public class UserMessagingActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = "UserMessaging";

    private User addressedUser;

    private DatabaseReference dRef;

    private RecyclerView recyclerView;
    private GroupAdapter adapter;

    private EditText chatBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_messaging);


        recyclerView = findViewById(R.id.recyclerView_messaging_activity);

        dRef = FirebaseDatabase.getInstance().getReference();

        adapter = new GroupAdapter();

        findViewById(R.id.send_button_mesaging_activity).setOnClickListener(this);
        chatBox = findViewById(R.id.message_messaging_activity);

        updateUIOnLogin();
    }

    private void listenToMessage(){

    }

    private void sendMessageToFirebaseDatabase(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference senderRef = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/Contacts/" + addressedUser.uid).push();

        String text = chatBox.getText().toString();

        ChatMessageItem chatMessageItem = new ChatMessageItem(text,addressedUser.uid,user.getUid(),System.currentTimeMillis());

        senderRef.setValue(chatMessageItem).addOnSuccessListener(aVoid -> {
            Log.d(TAG,"Saved message successfully for sender");
        });

        DatabaseReference receiverRef = FirebaseDatabase.getInstance().getReference("users/" + addressedUser.uid + "/Contacts/" + user.getUid()).push();
        receiverRef.setValue(chatMessageItem).addOnSuccessListener(aVoid -> {
            Log.d(TAG,"Saved message successfully for receiver");
        });
    }

    private void updateUIOnLogin(){
            addressedUser = (User) getIntent().getSerializableExtra("user_serialized");
            getSupportActionBar().setTitle(addressedUser.username);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.send_button_mesaging_activity:{
                sendMessageToFirebaseDatabase();
                chatBox.setText("");
                break;
            }
        }
    }

    private void showAddMessageToAdapter(){

    }

    class MessageToItem extends Item<ViewHolder> {
        private String messageText;

        public MessageToItem(String text){
            messageText = text;
        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            TextView tv = viewHolder.itemView.findViewById(R.id.from_message_text);
            tv.setText(messageText);
//            if(addressedUser != null)
//            Picasso.get().load(addressedUser.profilePhotoUrl).into((CircleImageView) viewHolder.itemView.findViewById(R.id.from_message_profile_image));
        }
        @Override
        public int getLayout() {
            return R.layout.to_chat_message;
        }
    }
    class MessageFromItem extends Item<ViewHolder> {
        private String messageText;

        public MessageFromItem(String text){
            messageText = text;
        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            TextView tv = viewHolder.itemView.findViewById(R.id.from_message_text);
            tv.setText(messageText);
//            if(addressedUser != null)
//            Picasso.get().load(addressedUser.profilePhotoUrl).into((CircleImageView) viewHolder.itemView.findViewById(R.id.from_message_profile_image));
        }
        @Override
        public int getLayout() {
            return R.layout.from_chat_message;
        }
    }
}
