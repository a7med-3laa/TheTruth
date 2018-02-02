package com.ahmedalaa.thetruth;

import com.ahmedalaa.thetruth.model.User;

import java.util.List;

/**
 * Created by ahmed on 13/12/2017.
 */

public interface OnLoadFinished {
    void onPostExecute(List<User> books);

    void onPreExecute();

    void onFailure();
}
