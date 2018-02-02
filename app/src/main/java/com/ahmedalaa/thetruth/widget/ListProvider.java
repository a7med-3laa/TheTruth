package com.ahmedalaa.thetruth.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.ahmedalaa.thetruth.R;
import com.ahmedalaa.thetruth.model.Msg;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 19/11/2017.
 */

class ListProvider implements RemoteViewsService.RemoteViewsFactory {
    Context context;
    List<Msg> msgsList;

    Query msgs;

    public ListProvider(Context applicationContext) {
        context = applicationContext;

        msgsList = new ArrayList<>();

    }

    @Override
    public void onCreate() {
        String ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        msgs = FirebaseDatabase.getInstance().getReference().child("msgs").orderByChild("receiverID").
                equalTo(ID);
        ValueEventListener valueEventListener = msgs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                } else {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        msgsList.add(snapshot.getValue(Msg.class));
                    }
                    updateWidget();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateWidget() {
        Intent i = new Intent(context, MessagesWidget.class);
        i.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName componentName = new ComponentName(context, MessagesWidget.class);
        int[] widgetId = appWidgetManager.getAppWidgetIds(componentName);
        appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.appwidget_list);


    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return msgsList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.msg_item_widget);
        Msg msg = msgsList.get(i);
        remoteViews.setTextViewText(R.id.msg, msg.getMsg());
        return remoteViews;


    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return (long) i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
