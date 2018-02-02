package com.ahmedalaa.Honestly.fcm;

import com.ahmedalaa.Honestly.Util.Uti;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by ahmed on 01/02/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            if (Uti.isAppIsInBackground(this)) {
                String content = remoteMessage.getNotification().getBody();
                Uti.notify(content, this);

            }

        }
    }
}
