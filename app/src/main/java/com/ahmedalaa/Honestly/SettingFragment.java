package com.ahmedalaa.Honestly;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.ahmedalaa.Honestly.Util.NetworkUtil;
import com.ahmedalaa.Honestly.Util.SessionManager;
import com.ahmedalaa.Honestly.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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
    @BindView(R.id.audio_notification)
    Switch audioNotification;

    Unbinder unbinder;
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.logout_btn)
    Button logoutBtn;
    SessionManager sessionManager;
    View view;
    private Query users;
    private ValueEventListener valueEventListener;
    private int GALLERY_REQUEST = 0;
    private boolean isOffline = false;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null || isOffline) {

            view = inflater.inflate(R.layout.fragment_setting, container, false);
            unbinder = ButterKnife.bind(this, view);
            showProgress(true);
            sessionManager = new SessionManager(getActivity());
            pushNotification.setChecked(sessionManager.isNotficationEnable());
            audioNotification.setChecked(sessionManager.isNotficationSoundEnable());
            Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.ic_power_settings_new_black_24dp);
            logoutBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);
            users = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            if (sessionManager.getUserData() == null && NetworkUtil.isConnected(getActivity())) {
                valueEventListener = users.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        showProgress(false);
                        User user = dataSnapshot.getValue(User.class);
                        userName.setText(user.getName());
                        userName2.setText(user.getUserName());
                        if (!user.getPhotoURL().isEmpty())
                            Picasso.with(getContext()).load(user.getPhotoURL()).into(circleImageView);
                        sessionManager.setUserData(user);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        showProgress(false);
                        Snackbar.make(container, databaseError.getMessage(), Snackbar.LENGTH_LONG).show();

                    }
                });
            } else if (sessionManager.getUserData() != null) {
                User user = sessionManager.getUserData();
                showProgress(false);
                userName.setText(user.getName());
                userName2.setText(user.getUserName());
                if (!user.getPhotoURL().isEmpty())
                    Picasso.with(getContext()).load(user.getPhotoURL()).into(circleImageView);

            } else {
                Snackbar.make(container, R.string.error_connection, Snackbar.LENGTH_LONG).show();
                isOffline = true;
            }


            pushNotification.setOnCheckedChangeListener((compoundButton, b) -> sessionManager.setNotfication(b));
            audioNotification.setOnCheckedChangeListener((compoundButton, b) -> sessionManager.setNotficationSound(b));
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @OnClick(R.id.circleImageView)
    public void onCircleImageViewClicked() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            showProgress2(true);
            Uri selectedImage = data.getData();
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference riversRef = null;
            if (selectedImage != null) {
                riversRef = storageRef.child("images/" + selectedImage.getLastPathSegment());
                UploadTask uploadTask = riversRef.putFile(selectedImage);

                uploadTask.addOnFailureListener(exception -> {
                    showProgress2(false);
                    Snackbar.make(container, exception.getMessage(), Snackbar.LENGTH_LONG).show();

                    // Handle unsuccessful uploads
                }).addOnSuccessListener(taskSnapshot -> {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    users = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    users.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                    child("photoURL").setValue(String.valueOf(downloadUrl));
                            User user = sessionManager.getUserData();
                            user.setPhotoURL(String.valueOf(downloadUrl));
                            sessionManager.setUserData(user);
                            Picasso.with(getContext()).load(downloadUrl).into(circleImageView);
                            showProgress2(false);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            showProgress2(false);
                            Snackbar.make(container, databaseError.getMessage(), Snackbar.LENGTH_LONG).show();

                        }
                    });


                });
            } else {
                showProgress2(false);

            }
        }
        showProgress2(false);

    }

    private void showProgress(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        container.setVisibility(show ? View.GONE : View.VISIBLE);
        container.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                container.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        progressBar.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });

    }

    private void showProgress2(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        if (!show) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


        }
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        progressBar.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                if (show) {
                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }
        });

    }

    @OnClick(R.id.user_name2)
    public void onUserName2Clicked() {

    }

    @OnClick(R.id.logout_btn)
    public void onViewClicked() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }
}
