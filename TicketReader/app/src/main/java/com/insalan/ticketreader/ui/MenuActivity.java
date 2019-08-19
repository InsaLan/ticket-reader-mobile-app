package com.insalan.ticketreader.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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

        final Button scanButton = findViewById(R.id.scan_button);
        final Button logoutButton = findViewById(R.id.logout_button);

        scanButton.setOnClickListener(v -> {
            final IntentIntegrator integrator = new IntentIntegrator(MenuActivity.this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(true);
            integrator.initiateScan();
        });

        logoutButton.setOnClickListener(v -> askLogout());
    }

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
                ApiRequests.getInstance().getTicket(getApplicationContext(), token,
                        // Callback creating an activity with ticket received
                        apiResponse -> {

                            if (apiResponse.getClass().equals(Ticket.class)) {
                                Ticket ticket = (Ticket) apiResponse;
                                ticket.setToken(token);
                                final Intent intent = new Intent(this, TicketDisplayActivity.class);
                                intent.putExtra("ticket", ticket);
                                startActivity(intent);
                            } else if (apiResponse.getClass().equals(ApiErr.class)) {
                                ApiErr apiErr = (ApiErr) apiResponse;
                                final Intent intent = new Intent(this, ErrorActivity.class);
                                intent.putExtra("errorMessage", apiErr.getErrorMessage());
                                startActivity(intent);
                            }

                        },
                        // Error callback if we cannot scan any qrcode
                        apiError -> {
                            Log.e("API REQUEST", apiError.getMessage(), apiError);
                            Toast.makeText(getApplicationContext(), "Error while searching for qrcode, token = " + token, Toast.LENGTH_LONG).show();
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
                    Log.e("Logout", "Before delete");
                    Storage.deleteUsername(MenuActivity.this.getApplicationContext());
                    Storage.deletePassword(MenuActivity.this.getApplicationContext());

                    Log.e("Logout", "After delete");
                    final Intent loginActivity = new Intent(this, LoginActivity.class);
                    this.startActivity(loginActivity);
                    this.finish();
                })
                .setNegativeButton(R.string.no, (dialog, which) -> {
                    // no action
                })
                .show();
    }

}



