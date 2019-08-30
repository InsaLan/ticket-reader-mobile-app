package com.insalan.ticketreader.data.model;

/**
 * Basic class, representing a response from the API.
 * Used to make the distinction between a ticket and an error
 */
public class ApiResponse {

    private ApiErr err;

    /**
     * Checks if the current error is just null, meaning no error, or if there is really an error
     */
    public boolean hasError(){
        return err != null;
    }

    /*
     * GETTER & SETTER
     */

    public ApiErr getErr() {
        return err;
    }
}
