package com.ahmedalaa.Honestly.Util;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.ahmedalaa.Honestly.MainActivity;
import com.ahmedalaa.Honestly.R;

import java.util.List;

/**
 * Created by ahmed on 01/02/2018.
 */

public class Uti {

    SessionManager sessionManager;

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = null;
            if (am != null) {
                runningProcesses = am.getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        for (String activeProcess : processInfo.pkgList) {
                            if (activeProcess.equals(context.getPackageName())) {
                                isInBackground = false;
                            }
                        }
                    }
                }
            }

        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public static void notify(String data, Context context) {

        SessionManager sessionManager = new SessionManager(context);
        if (sessionManager.isNotficationEnable()) {
            android.support.v4.app.NotificationCompat.Builder notificationBuilder;

            Intent t = new Intent(context, MainActivity.class);
            t.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, t,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notificationBuilder = new android.support.v4.app.NotificationCompat.Builder(context, "5")
                    .setSmallIcon(R.drawable.ic_notfication)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setAutoCancel(true)
                    .setContentText(data)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent);
            if (sessionManager.isNotficationSoundEnable())
                notificationBuilder.setSound(defaultSoundUri);
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.notify(0, notificationBuilder.build());
            }
        }
    }


}
