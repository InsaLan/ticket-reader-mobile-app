package com.insalan.ticketreader.data.model;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.insalan.ticketreader.R;

import java.io.Serializable;

/**
 * Class representing an Error message received from the API
 */
public class ApiErr extends ApiResponse implements Serializable {


    // SerializedName represents the name of the key in the JSON received, which is match with the attribute name in Java
    @SerializedName("no")
    private int errorNumber;

    // SerializedName represents the name of the key in the JSON received, which is match with the attribute name in Java
    @SerializedName("msg")
    private String errorMessage;


    public String getErrorExplanation(final Context context) {
        try {
            switch (this.errorNumber) {
                case 1:
                    return context.getResources().getString(R.string.error_ticket_not_found);
                case 2:
                    return context.getResources().getString(R.string.error_participant_not_found);
                case 3:
                    return context.getResources().getString(R.string.error_already_scanned);
                default:
                    return "Erreur de l'API inconnue : numéro d'erreur ("+this.errorNumber+") inconnu.";
            }
        } catch (NullPointerException e) {
            return "Numéro d'erreur NULL, erreur inconnue ou pas d'erreur";
        }
    }


    /*
     * GETTER & SETTER
     */

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorNumber() {
        return errorNumber;
    }

    public void setErrorNumber(int errorNumber) {
        this.errorNumber = errorNumber;
    }
}
