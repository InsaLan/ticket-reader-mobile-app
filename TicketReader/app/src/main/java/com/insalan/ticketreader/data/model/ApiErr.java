package com.insalan.ticketreader.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Class representing an Error message received from the API
 */
public class ApiErr extends ApiResponse implements Serializable {

    // SerializedName represents the name of the key in the JSON received, which is match with the attribute name in Java
    @SerializedName("err")
    private String errorMessage;

    /**
     * Checks if the current error is just null, meaning no error, or if there is really an error
     */
    public boolean hasError(){
        return errorMessage != null;
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
}
