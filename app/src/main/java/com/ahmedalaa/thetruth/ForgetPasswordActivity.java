package com.ahmedalaa.thetruth;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetPasswordActivity extends AppCompatActivity {

    @BindView(R.id.email_txt)
    EditText emailTxt;
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.send_password)
    Button sendPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable drawable2 = ContextCompat.getDrawable(this, R.drawable.ic_login);
        sendPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable2, null, null, null);

    }

    @OnClick(R.id.send_password)
    public void onViewClicked() {
        String email = emailTxt.getText().toString();
        if (email.isEmpty()) {
            emailTxt.setError(getString(R.string.error_field_required));
            emailTxt.requestFocus();
        } else if (isEmailValid(email)) {
            emailTxt.setError(getString(R.string.error_invalid_email));
            emailTxt.requestFocus();

        } else {
            showProgress(true);
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgetPasswordActivity.this, R.string.sent_password_done, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
                    finish();
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
