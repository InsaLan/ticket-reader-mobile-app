package com.insalan.ticketreader.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.insalan.ticketreader.R;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.error_view);

        final String errorExplanation = (String) getIntent().getSerializableExtra("errorExplanation");
        final String errorMessage = (String) getIntent().getSerializableExtra("errorMessage");

        // Getting all the objects in the view
        final Button menuButton = findViewById(R.id.ticket_view_menu_button);
        TextView textErrorExplanation = findViewById(R.id.error_view_explanation);
        TextView textErrorMessage = findViewById(R.id.error_view_message);

        // Displaying the ticket's informations
        textErrorExplanation.setText(errorExplanation);
        textErrorMessage.setText("ERROR: " + errorMessage);

        // Buttons interactions
        menuButton.setOnClickListener(v -> backToMenu());

    }

    public void backToMenu() {
        final Intent intent = new Intent(ErrorActivity.this, MenuActivity.class);
        startActivity(intent);
    }
}
