package com.example.amas.messenger;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

public class ContactsActivity extends AppCompatActivity {

    private DatabaseReference dRef = FirebaseDatabase.getInstance().getReference();

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        getSupportActionBar().setTitle("Select User");


        recyclerView = findViewById(R.id.recycler_view_contacts_activity);

        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GroupAdapter adapter = new GroupAdapter();
//                dataSnapshot.getChildren().forEach();
//                Iterator iterator = dataSnapshot.getChildren().iterator();
//                while (iterator.hasNext()){
////                    User user = (User) iterator.next();
////                    adapter.add(new ContactItem(user));
//                    Log.d("DataChange","entered"  );
//                    adapter.add(new ContactItem());
//                }
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    User user = snapshot.getValue(User.class);
                    Log.d("DataChange","entered"  );
                    adapter.add(new ContactItem());
                }
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        recyclerView.setAdapter(adapter);
    }

    class ContactItem extends Item<ViewHolder> {
//        private User user;
//        public ContactItem(User user){
//            this.user = user;
//        }
        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
//            Picasso.get().load(user.profilePhotoUrl).into((CircleImageView) viewHolder.itemView.findViewById(R.id.contact_profile_image));
//            (TextView)viewHolder.itemView.findViewById(R.id.contact_username).
        }
        @Override
        public int getLayout() {
            return R.layout.user_contact_row;
        }
    }
}
