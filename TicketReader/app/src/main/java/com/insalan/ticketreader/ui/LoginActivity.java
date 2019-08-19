package com.insalan.ticketreader.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.insalan.ticketreader.R;
import com.insalan.ticketreader.data.Storage;

public final class LoginActivity extends AppCompatActivity {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);

        final EditText usernameField = findViewById(R.id.username);
        final EditText passwordField = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        final TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
                // Needed because we override, can be useful in further upgrades
            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                // Needed because we override, can be useful in further upgrades
            }

            @Override
            public void afterTextChanged(final Editable s) {
                final String username = usernameField.getText().toString();
                final String password = passwordField.getText().toString();
                boolean loginEnabled = false;
                // Tests to check credentials format
                if (username.isEmpty()) {
                    usernameField.setError(getString(R.string.username_required));
                } else if (password.isEmpty()) {
                    passwordField.setError(getString(R.string.password_required));
                } else {
                    loginEnabled = true;
                }
                loginButton.setEnabled(loginEnabled);
            }
        };
        usernameField.addTextChangedListener(afterTextChangedListener);
        passwordField.addTextChangedListener(afterTextChangedListener);
        // FIXME seems useless
//        passwordField.setOnEditorActionListener((v, actionId, event) -> {
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                LoginActivity.this.login(usernameField, passwordField, loadingProgressBar);
//                return true;
//            }
//            return false;
//        });

        loginButton.setOnClickListener(v -> this.login(usernameField, passwordField, loadingProgressBar));
    }

    private void login(final EditText usernameField, final EditText passwordField, final ProgressBar loadingProgressBar) {
        loadingProgressBar.setVisibility(View.VISIBLE);
        Storage.storeUserCredentials(getApplicationContext(), usernameField.getText().toString(), passwordField.getText().toString());
        Intent menuActivity = new Intent(LoginActivity.this, MenuActivity.class);
        startActivity(menuActivity);
        finish();
    }
}
