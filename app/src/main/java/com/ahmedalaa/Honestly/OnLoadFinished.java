package com.ahmedalaa.Honestly;

import com.ahmedalaa.Honestly.model.User;

import java.util.List;

/**
 * Created by ahmed on 13/12/2017.
 */

public interface OnLoadFinished {
    void onPostExecute(List<User> books);

    void onPreExecute();

    void onFailure();
}
