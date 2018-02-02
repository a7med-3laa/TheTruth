package com.ahmedalaa.thetruth.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by ahmed on 19/11/2017.
 */

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return (new ListProvider(this.getApplicationContext()));
    }


}
