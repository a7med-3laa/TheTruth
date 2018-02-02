package com.ahmedalaa.thetruth;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.email_txt)
    EditText emailTxt;
    @BindView(R.id.password_txt)
    EditText passwordTxt;
    @BindView(R.id.container)
    CoordinatorLayout container;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.container2)
    ConstraintLayout container2;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.sign_up)
    Button signUp;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_login);
        loginBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);
        Drawable drawable2 = ContextCompat.getDrawable(this, R.drawable.ic_add);
        signUp.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable2, null, null, null);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.login_btn)
    public void onLoginBtnClicked() {
        emailTxt.setError(null);
        passwordTxt.setError(null);
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

        }
        String email = emailTxt.getText().toString().trim();
        String password = passwordTxt.getText().toString().trim();
        if (email.isEmpty()) {
            emailTxt.setError(getString(R.string.error_field_required));
            emailTxt.requestFocus();
        } else if (!isEmailValid(email)) {
            emailTxt.setError(getString(R.string.error_invalid_email));
            emailTxt.requestFocus();

        } else if (!isPasswordValid(password)) {
            passwordTxt.setError(getString(R.string.error_incorrect_password));
            passwordTxt.requestFocus();
        } else {
            showProgress(true);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));

                            finish();
                        } else {

                            showProgress(false);
                            if (task.getException() != null)
                                Snackbar.make(container, task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                        }

                    });
        }
    }

    @OnClick(R.id.sign_up)
    public void onSignUpClicked() {
        startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));

    }

    @OnClick(R.id.forgetPassword_txt)
    public void onForgetPasswordTxtClicked() {
        startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));

    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 6;
    }

    private void showProgress(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        container2.setVisibility(show ? View.GONE : View.VISIBLE);
        container2.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                container2.setVisibility(show ? View.GONE : View.VISIBLE);
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
