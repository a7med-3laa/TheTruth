package com.ahmedalaa.thetruth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    MyMsgFragment myMsgFragment;
    OutMsgFragment outMsgFragment;
    SettingFragment settingFragment;

    SearchFragment searchFragment;
    Toolbar toolbar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_inbox:
                    toolbar.setTitle("Inbox msgs");
                    getSupportFragmentManager().beginTransaction().
                            replace(R.id.container_fragment, myMsgFragment).addToBackStack("tag").commit();
                    return true;
                case R.id.navigation_outbox:

                    toolbar.setTitle("OutBox msgs");
                    getSupportFragmentManager().beginTransaction().
                            replace(R.id.container_fragment, outMsgFragment).addToBackStack("tag1").commit();
                    return true;
                case R.id.navigation_search:

                    toolbar.setTitle("Search");
                    getSupportFragmentManager().beginTransaction().
                            replace(R.id.container_fragment, searchFragment).addToBackStack("tag2").commit();
                    return true;
                case R.id.navigation_setting:

                    toolbar.setTitle("Settings");
                    getSupportFragmentManager().beginTransaction().
                            replace(R.id.container_fragment, settingFragment).addToBackStack("tag3").commit();
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        myMsgFragment = new MyMsgFragment();

        outMsgFragment = new OutMsgFragment();
        searchFragment = new SearchFragment();
        settingFragment = new SettingFragment();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_inbox);

    }

}
