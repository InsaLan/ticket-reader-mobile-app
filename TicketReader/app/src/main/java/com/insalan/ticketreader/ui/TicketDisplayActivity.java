package com.insalan.ticketreader.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.insalan.ticketreader.R;
import com.insalan.ticketreader.api.ApiRequests;
import com.insalan.ticketreader.data.model.Ticket;

import org.w3c.dom.Text;

public class TicketDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticket_view);

        final Ticket ticket = (Ticket) getIntent().getSerializableExtra("ticket");

        // Getting all the objects in the view
        final Button validateButton = findViewById(R.id.ticket_view_validate_btn);
        final Button menuButton = findViewById(R.id.ticket_view_menu_button);

        TextView nameText = findViewById(R.id.ticket_view_name);
        TextView gameNameText = findViewById(R.id.ticket_view_gameName);
        TextView phoneText = findViewById(R.id.ticket_view_phone);
        TextView tournamentText = findViewById(R.id.ticket_view_tournament);
        TextView stateText = findViewById(R.id.ticket_view_state);
        TextView errorText = findViewById(R.id.ticket_view_error);

        // Displaying the ticket's informations
        nameText.setText(ticket.getName());
        gameNameText.setText(ticket.getGameName());
        phoneText.setText(ticket.getPhone());
        tournamentText.setText(ticket.getTournament());
        stateText.setText(Boolean.toString(ticket.isTicketScanned()));

        // Buttons interactions
        menuButton.setOnClickListener(v -> backToMenu());

        if (ticket.isTicketScanned()) {
            validateButton.setVisibility(View.GONE);
        } else {
            // Preparing the validation of the ticket
            validateButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    clearError(errorText);

                    ApiRequests.getInstance().validateTicket(getApplicationContext(), ticket.getToken(),
                            // Callback creating an activity with ticket received
                            apiResponse -> {
                                if (!apiResponse.hasError()) {
                                    // No error during validation, we go back to menu
                                    Toast.makeText(getApplicationContext(), "Ticket validé !", Toast.LENGTH_LONG).show();
                                    backToMenu();
                                } else {
                                    errorText.setText(apiResponse.getErr().getErrorMessage());
                                }
                            });

                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        backToMenu();
    }

    public void backToMenu() {
        final Intent intent = new Intent(TicketDisplayActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    private void clearError(final TextView errorText){
        errorText.setText("");
    }
}
