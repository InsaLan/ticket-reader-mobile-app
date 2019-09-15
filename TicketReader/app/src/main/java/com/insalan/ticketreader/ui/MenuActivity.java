package com.insalan.ticketreader.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.insalan.ticketreader.R;
import com.insalan.ticketreader.api.ApiRequests;
import com.insalan.ticketreader.data.Storage;
import com.insalan.ticketreader.data.model.ApiErr;
import com.insalan.ticketreader.data.model.Ticket;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public final class MenuActivity extends AppCompatActivity {

    private static final String TAG = MenuActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_view);

        final TextView infoUser = findViewById(R.id.text_infos_user);
        final Button scanButton = findViewById(R.id.scan_button);
        final ImageButton logoutButton = findViewById(R.id.logout_button);

        infoUser.setText("Connecté comme : " + Storage.getUsername(getApplicationContext()));

        scanButton.setOnClickListener(v -> {
            final IntentIntegrator integrator = new IntentIntegrator(MenuActivity.this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            integrator.setBeepEnabled(true);
            integrator.setBarcodeImageEnabled(true);
            integrator.initiateScan();
        });

        logoutButton.setOnClickListener(v -> askLogout());
    }

    // Function called when finishing a scan (scan activity auto returns in this activity)
    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result == null) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            final String token = result.getContents();
            if (token == null) {
                Log.d(TAG, "No qrcode in result");
            } else {
                Toast.makeText(getApplicationContext(), token, Toast.LENGTH_LONG).show();
                ApiRequests.getInstance().getTicket(getApplicationContext(), token,
                        // Callback creating an activity with ticket received
                        apiResponse -> {

                            if (apiResponse.getClass().equals(Ticket.class)) {
                                Ticket ticket = (Ticket) apiResponse;
                                ticket.setToken(token);
                                final Intent intent = new Intent(this, TicketDisplayActivity.class);
                                intent.putExtra("ticket", ticket);
                                startActivity(intent);
                            } else if (apiResponse.hasError()) {
                                ApiErr apiErr = apiResponse.getErr();
                                final Intent intent = new Intent(this, ErrorActivity.class);
                                intent.putExtra("errorExplanation", apiErr.getErrorExplanation(getApplicationContext()));
                                intent.putExtra("errorMessage", apiErr.getErrorMessage());
                                startActivity(intent);
                            }

                        });
            }
        }
    }

    @Override
    public void onBackPressed() {
        askLogout();
    }

    public void askLogout() {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.logout_question))
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    ApiRequests.getInstance().logout(getApplicationContext(),
                            res -> {
                                Log.d("LOGOUT", "Accepted");
                                Log.d("LOGOUT", "Before delete");
                                Storage.clear(this.getApplicationContext());
                                Log.d("LOGOUT", "After delete");
                                final Intent loginActivity = new Intent(this, LoginActivity.class);
                                this.startActivity(loginActivity);
                                this.finish();
                            });
                })
                .setNegativeButton(R.string.no, (dialog, which) -> {
                    // no action
                })
                .show();
    }

}



