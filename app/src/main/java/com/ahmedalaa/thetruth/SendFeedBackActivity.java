package com.ahmedalaa.thetruth;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.ahmedalaa.thetruth.model.Msg;
import com.ahmedalaa.thetruth.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class SendFeedBackActivity extends AppCompatActivity {

    @BindView(R.id.txt)
    EditText txt;
    String ID;
    @BindView(R.id.user_img)
    CircleImageView userImg;
    @BindView(R.id.name_txt)
    TextView nameTxt;
    String name = null;
    String picURl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feed_back);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ID = getIntent().getStringExtra("ID");
        Query user = FirebaseDatabase.getInstance().getReference().child("users").child(ID);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user1 = dataSnapshot.getValue(User.class);
                name = user1.getName();
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

    @OnClick(R.id.send_btn)
    public void onViewClicked() {
        String text = txt.getText().toString().trim();
        if (!TextUtils.isEmpty(text)) {
            txt.setText("");
            FirebaseDatabase.getInstance().getReference().child("msgs").push().setValue(new Msg(text
                    , ID, FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(), name,
                    picURl, "", 0)).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });

        }
    }
}
