package com.ahmedalaa.Honestly;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.ahmedalaa.Honestly.model.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    @BindView(R.id.search)
    SearchView search;
    @BindView(R.id.user_list)
    RecyclerView userList;
    Unbinder unbinder;
    @BindView(R.id.container)
    LinearLayout container;
    @BindView(R.id.progress)
    ProgressBar progressbar;
    View view = null;
    AsyncTask mtask;
    @BindView(R.id.no_users)
    LinearLayout noUsers;
    Unbinder unbinder1;
    private boolean isOffline = false;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null || isOffline) {
            view = inflater.inflate(R.layout.fragment_search, container, false);
            unbinder = ButterKnife.bind(this, view);
            UsersAdapter usersAdapter = new UsersAdapter(getActivity());
            userList.setLayoutManager(new LinearLayoutManager(getActivity()));
            userList.setAdapter(usersAdapter);
            search.setOnSearchClickListener(view -> noUsers.setVisibility(View.GONE));
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    if (!TextUtils.isEmpty(s.trim())) {
                        usersAdapter.clear();
                        UserAsyncTask userAsyncTask = new UserAsyncTask(new OnLoadFinished() {
                            @Override
                            public void onFailure() {
                                showProgress(false);
                                Snackbar.make(container, R.string.error_connection, Snackbar.LENGTH_LONG).show();
                                isOffline = true;
                            }


                            @Override
                            public void onPostExecute(List<User> books) {
                                showProgress(false);
                                if (books.size() == 0) {
                                    noUsers.setVisibility(View.VISIBLE);
                                } else {
                                    noUsers.setVisibility(View.GONE);

                                    usersAdapter.add(books);

                                }
                            }

                            @Override
                            public void onPreExecute() {
                                showProgress(true);
                            }
                        }, getActivity());
                        userAsyncTask.executeTask(s.trim());
                        View view = getActivity().getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            if (imm != null) {
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }

                        }
                        return true;

                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });
        }
        unbinder1 = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder1.unbind();
    }

    private void showProgress(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        container.setVisibility(show ? View.GONE : View.VISIBLE);
        container.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                container.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        progressbar.setVisibility(show ? View.VISIBLE : View.GONE);
        progressbar.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressbar.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });

    }

}
