package com.ahmedalaa.Honestly;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    MessagesFragment messagesFragment;
    SentMessagesFragment sentMessagesFragment;
    SettingFragment settingFragment;

    SearchFragment searchFragment;
    Toolbar toolbar;
    FragmentTransaction ft;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_inbox:
                    ft = getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

                    toolbar.setTitle(R.string.msgs_Fragment);
                    ft.replace(R.id.container_fragment, messagesFragment).addToBackStack(null).commit();
                    return true;
                case R.id.navigation_outbox:
                    ft = getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

                    toolbar.setTitle(R.string.send_messages_fragment);
                    ft.replace(R.id.container_fragment, sentMessagesFragment).addToBackStack(null).commit();
                    return true;
                case R.id.navigation_search:
                    ft = getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

                    toolbar.setTitle(R.string.search_fragment);
                    ft.replace(R.id.container_fragment, searchFragment).addToBackStack(null).commit();
                    return true;
                case R.id.navigation_setting:
                    ft = getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

                    toolbar.setTitle(R.string.setting_fragment_title);
                    ft.replace(R.id.container_fragment, settingFragment).addToBackStack(null).commit();
                    return true;
            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        if (savedInstanceState == null)
            toolbar.setTitle(R.string.msgs_Fragment);
        ft = getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

        setSupportActionBar(toolbar);


        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        messagesFragment = new MessagesFragment();

        sentMessagesFragment = new SentMessagesFragment();
        searchFragment = new SearchFragment();
        settingFragment = new SettingFragment();
        messagesFragment.setRetainInstance(true);

        sentMessagesFragment.setRetainInstance(true);

        searchFragment.setRetainInstance(true);

        settingFragment.setRetainInstance(true);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        if (savedInstanceState == null) {
            navigation.setSelectedItemId(R.id.navigation_inbox);
            ft = getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

            ft.add(R.id.container_fragment, messagesFragment).addToBackStack(null).commit();

        }
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        navigation.setOnNavigationItemReselectedListener(item -> {
//            switch (item.getItemId()) {
//
//                case R.id.navigation_inbox:
//                    ft = getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
//
//                    toolbar.setTitle(R.string.msgs_Fragment);
//
//                    ft.show(messagesFragment).commit();
//                    if (sentMessagesFragment.isAdded())
//                        ft.hide(sentMessagesFragment);
//                    if (searchFragment.isAdded())
//                        ft.hide(searchFragment);
//                    if (settingFragment.isAdded())
//                        ft.hide(settingFragment);
//
//                    break;
//                case R.id.navigation_outbox:
//                    ft = getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
//
//                    toolbar.setTitle(R.string.send_messages_fragment);
//
//                    ft.show(sentMessagesFragment).addToBackStack(null).commit();
//                    if (messagesFragment.isAdded())
//                        ft.hide(messagesFragment);
//                    if (searchFragment.isAdded())
//                        ft.hide(searchFragment);
//                    if (settingFragment.isAdded())
//                        ft.hide(settingFragment);
//
//                    break;
//                case R.id.navigation_search:
//                    ft = getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
//
//                    if (sentMessagesFragment.isAdded())
//                        ft.hide(sentMessagesFragment);
//                    if (messagesFragment.isAdded())
//                        ft.hide(messagesFragment);
//                    if (settingFragment.isAdded())
//                        ft.hide(settingFragment);
//
//                    toolbar.setTitle(R.string.search_fragment);
//
//                    ft.show(searchFragment).addToBackStack(null).commit();
//                    break;
//                case R.id.navigation_setting:
//                    ft = getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
//
//                    toolbar.setTitle(R.string.setting_fragment_title);
//                    ft.show(settingFragment).addToBackStack(null).commit();
//                    if (sentMessagesFragment.isAdded())
//                        ft.hide(sentMessagesFragment);
//                    if (searchFragment.isAdded())
//                        ft.hide(searchFragment);
//                    if (messagesFragment.isAdded())
//                        ft.hide(messagesFragment);
//
//                    break;
//            }
//        });

    }

}
