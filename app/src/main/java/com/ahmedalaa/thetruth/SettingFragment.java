package com.ahmedalaa.thetruth;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.ahmedalaa.thetruth.model.User;
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
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {


    @BindView(R.id.circleImageView)
    CircleImageView circleImageView;
    @BindView(R.id.user_name2)
    TextView userName2;
    @BindView(R.id.user_name)
    TextView userName;

    @BindView(R.id.push_notification)
    Switch pushNotification;
    @BindView(R.id.email_notification)
    Switch emailNotification;
    @BindView(R.id.audio_notification)
    Switch audioNotification;
    Unbinder unbinder;
    private Query users;
    private ValueEventListener valueEventListener;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        unbinder = ButterKnife.bind(this, view);
        users = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        valueEventListener = users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userName.setText(user.getName());
                userName2.setText(user.getUserName());
                if (!user.getPhotoURL().isEmpty())
                    Picasso.with(getContext()).load(user.getPhotoURL()).into(circleImageView);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        users.removeEventListener(valueEventListener);
    }

    @OnClick(R.id.circleImageView)
    public void onCircleImageViewClicked() {
    }

    @OnClick(R.id.user_name2)
    public void onUserName2Clicked() {
    }
}
