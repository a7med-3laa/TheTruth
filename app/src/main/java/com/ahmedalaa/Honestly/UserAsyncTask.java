package com.ahmedalaa.Honestly;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.ahmedalaa.Honestly.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 13/12/2017.
 */

public class UserAsyncTask extends AsyncTask<String, Void, List<User>> {
    OnLoadFinished onLoadFinished;

    public UserAsyncTask(OnLoadFinished onLoadFinished, Context context) {
        this.onLoadFinished = onLoadFinished;

    }

    @Override
    protected void onCancelled(List<User> users) {
        super.onCancelled(users);
        this.cancel(true);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

        this.cancel(true);
    }

    public void executeTask(String search) {
        this.execute("https://us-central1-thetruth-9b237.cloudfunctions.net/search?name=" + search);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        onLoadFinished.onPreExecute();
    }

    @Override
    protected List<User> doInBackground(String... strings) {
        String link = strings[0];
        List<User> users = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        Gson gson = new Gson();

        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            Type listType = new TypeToken<List<User>>() {
            }.getType();
            return gson.fromJson(sb.toString(), listType);

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Async", e.getMessage());
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> onLoadFinished.onFailure());
        }
        return users;


    }

    @Override
    protected void onPostExecute(List<User> Users) {
        super.onPostExecute(Users);
        onLoadFinished.onPostExecute(Users);

    }
}
