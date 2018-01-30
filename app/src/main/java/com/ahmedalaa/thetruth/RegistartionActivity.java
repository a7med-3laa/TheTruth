package com.ahmedalaa.thetruth;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ahmedalaa.thetruth.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistartionActivity extends AppCompatActivity {

    @BindView(R.id.name_txt)
    EditText nameTxt;
    @BindView(R.id.email_txt)
    EditText emailTxt;
    @BindView(R.id.password_txt)
    EditText passwordTxt;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.container)
    ConstraintLayout container;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registartion);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.sign_up_btn)
    public void onViewClicked() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }

        nameTxt.setError(null);
        emailTxt.setError(null);
        passwordTxt.setError(null);
        String name = nameTxt.getText().toString().trim();
        String email = emailTxt.getText().toString().trim();
        String password = passwordTxt.getText().toString().trim();
        String username = email.substring(0, email.indexOf('@') - 1);
        if (!isValidName(name)) {
            nameTxt.setError(getString(R.string.error_invalid_name));
            nameTxt.requestFocus();
        } else if (!isEmailValid(email)) {
            emailTxt.setError(getString(R.string.error_invalid_email));
            emailTxt.requestFocus();
        } else if (!isPasswordValid(password)) {
            passwordTxt.setError(getString(R.string.error_incorrect_password));
            passwordTxt.requestFocus();
        } else {
            showProgress(true);


            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task2) {

                                        if (task2.isSuccessful()) {
                                            String ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                            FirebaseDatabase.getInstance().getReference().child("users").child(ID).
                                                    setValue(new User(ID, name, email, username, ""));
                                            startActivity(new Intent(RegistartionActivity.this, MainActivity.class));

                                            finish();
                                        } else {
                                            // If sign in fails, display a message to the user.

                                            showProgress(false);
                                            Snackbar.make(container, task2.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
                                showProgress(false);
                                Snackbar.make(container, task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private boolean isValidName(String name) {
        return name.length() > 3;
    }

    private boolean isExistUserName(String name) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query users = database.getReference("users").child("userName").equalTo(name);
        final boolean[] isExist = {true};
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0)
                    isExist[0] = false;
                Toast.makeText(getApplication(), isExist + " ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return isExist[0];
    }


    public void return_toLogin(View view) {
        startActivity(new Intent(this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
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

        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        progressBar.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });

    }
}
