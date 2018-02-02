package com.ahmedalaa.Honestly;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.ahmedalaa.Honestly.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationActivity extends AppCompatActivity {

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
    @BindView(R.id.sign_up_btn)
    Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registartion);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable drawable2 = ContextCompat.getDrawable(this, R.drawable.ic_add);
        signUpBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable2, null, null, null);

        mAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.sign_up_btn)
    public void onViewClicked() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

        }

        nameTxt.setError(null);
        emailTxt.setError(null);
        passwordTxt.setError(null);
        String name = nameTxt.getText().toString().trim();
        String email = emailTxt.getText().toString().trim();
        String password = passwordTxt.getText().toString().trim();
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

            String username = email.substring(0, email.indexOf('@') - 1);
            showProgress(true);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {

                        if (task.isSuccessful()) {
                            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful()) {
                                    String ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    FirebaseDatabase.getInstance().getReference().child("users").child(ID).
                                            setValue(new User(ID, name, email, "", FirebaseInstanceId.getInstance().getToken()));
                                    startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    showProgress(false);
                                    Snackbar.make(container, task2.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            showProgress(false);
                            Snackbar.make(container, task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
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
