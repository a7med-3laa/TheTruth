package com.ahmedalaa.thetruth;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ahmedalaa.thetruth.model.Msg;
import com.ahmedalaa.thetruth.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class SendFeedBackActivity extends AppCompatActivity {

    @BindView(R.id.txt)
    EditText txt;
    String receiverID;
    @BindView(R.id.user_img)
    CircleImageView userImg;
    @BindView(R.id.name_txt)
    TextView nameTxt;
    String name = null;
    String picURl = null;
    @BindView(R.id.send_btn)
    Button sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feed_back);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_send2);
        sendBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);

        if (getIntent().hasExtra("user")) {
            User user = Parcels.unwrap(getIntent().getParcelableExtra("user"));
            name = user.getName();
            picURl = user.getPhotoURL();
            receiverID = user.getID();
            nameTxt.setText(name);

            if (!picURl.isEmpty())
                Picasso.with(SendFeedBackActivity.this).load(picURl).into(userImg);


        } else {
            receiverID = getIntent().getStringExtra("receiverID");
            Query user = FirebaseDatabase.getInstance().getReference().child("users").child(receiverID);
            user.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user1 = dataSnapshot.getValue(User.class);
                    nameTxt.setText(user1.getName());
                    picURl = user1.getPhotoURL();
                    if (!user1.getPhotoURL().isEmpty())
                        Picasso.with(SendFeedBackActivity.this).load(user1.getPhotoURL()).into(userImg);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @OnClick(R.id.send_btn)
    public void onViewClicked() {
        String text = txt.getText().toString().trim();
        String senderID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (!TextUtils.isEmpty(text)) {
            txt.setText("");
            String messagesID = FirebaseDatabase.getInstance().getReference().child("msgs").push().getKey();
            FirebaseDatabase.getInstance().getReference().child("msgs").child(messagesID).setValue(new Msg(text
                    , senderID, receiverID, messagesID)).addOnCompleteListener(task -> {

            });

        }
    }
}
