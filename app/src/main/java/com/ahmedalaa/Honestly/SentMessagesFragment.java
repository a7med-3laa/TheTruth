package com.ahmedalaa.Honestly;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ahmedalaa.Honestly.Util.NetworkUtil;
import com.ahmedalaa.Honestly.model.Msg;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class SentMessagesFragment extends Fragment {


    @BindView(R.id.msgs_list)
    RecyclerView msgsList;
    @BindView(R.id.progressBar2)
    ProgressBar progressBar;
    @BindView(R.id.no_msgs)
    LinearLayout noMsgs;
    Unbinder unbinder;
    Query msgs;

    SentMessagesAdapter msgsAdapter;

    ValueEventListener valueEventListener;

    List<Msg> msgsItem;
    View view;
    private boolean isOffline;

    public SentMessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null || isOffline) {
            view = inflater.inflate(R.layout.fragment_out_msg, container, false);
            unbinder = ButterKnife.bind(this, view);
            setRetainInstance(true);
            msgsAdapter = new SentMessagesAdapter(getActivity());
            msgsList.setLayoutManager(new LinearLayoutManager(getActivity()));
            msgsList.setAdapter(msgsAdapter);
            showProgress(true);
            String ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            msgs = FirebaseDatabase.getInstance().getReference().child("msgs").orderByChild("senderID").
                    equalTo(ID);
            if (NetworkUtil.isConnected(getActivity())) {

                valueEventListener = msgs.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        showProgress(false);
                        if (dataSnapshot.getChildrenCount() == 0) {
                            noMsgs.setVisibility(View.VISIBLE);
                        } else {
                            noMsgs.setVisibility(View.INVISIBLE);

                            msgsItem = new ArrayList<>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                msgsItem.add(snapshot.getValue(Msg.class));
                            }
                            msgsAdapter.add(msgsItem);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        showProgress(false);
                        noMsgs.setVisibility(View.VISIBLE);
                        Snackbar.make(container, databaseError.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });
            } else {
                showProgress(false);
                TextView textView = noMsgs.findViewById(R.id.alert_msg);
                textView.setText(R.string.error_connection);
                noMsgs.setVisibility(View.VISIBLE);
                isOffline = true;
            }
            showProgress(false);
        }
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }

    private void showProgress(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        msgsList.setVisibility(show ? View.GONE : View.VISIBLE);
        msgsList.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                msgsList.setVisibility(show ? View.GONE : View.VISIBLE);
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

}
