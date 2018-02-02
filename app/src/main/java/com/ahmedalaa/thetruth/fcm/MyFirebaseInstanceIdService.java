package com.ahmedalaa.thetruth.fcm;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by ahmed on 01/02/2018.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            String ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference("users").child(ID)
                    .child("token")
                    .setValue(refreshedToken);
        }
    }
}
