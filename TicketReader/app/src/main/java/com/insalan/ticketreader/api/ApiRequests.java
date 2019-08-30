package com.insalan.ticketreader.api;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.insalan.ticketreader.R;
import com.insalan.ticketreader.data.Storage;
import com.insalan.ticketreader.data.model.ApiErr;
import com.insalan.ticketreader.data.model.ApiResponse;
import com.insalan.ticketreader.ui.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Singleton class
 * Specifies every request that can be made to the InsaLan API
 * Handles specific data related with the requests and the server connection
 */
public class ApiRequests {

    private static volatile ApiRequests instance;

    private ApiRequests() {
        super();
    }

    public static synchronized ApiRequests getInstance() {
        if (instance == null) {
            instance = new ApiRequests();
        }
        return instance;
    }

    /*
     * API REQUESTS
     */

    public static void getTicket(final Context context, final String ticketToken, final Consumer<ApiResponse> callback) {
        // Creating the URL for the request
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ConfigServer.SCHEME)
                .authority(ConfigServer.AUTHORITY)
                .appendPath(ConfigServer.BASE_API)
                .appendPath("get");
        String uri = builder.build().toString();

        // Creating the request
        RequestService.getInstance(context).postJsonParsed(uri, createTokenData(ticketToken), ApiResponse.class, createHeaders(context),
                // Calls the callback, that will handle the ticket, display it or do actions with it
                new Response.Listener<ApiResponse>() {
                    @Override
                    public void onResponse(final ApiResponse apiRes) {
                        callback.accept(apiRes);
                    }
                },
                // Manages the VolleyError
                volleyError -> handleVolleyError(context, volleyError));
    }


    public static void validateTicket(final Context context, final String ticketToken, final Consumer<ApiResponse> callback) {
        // Creating the URL for the request
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ConfigServer.SCHEME)
                .authority(ConfigServer.AUTHORITY)
                .appendPath(ConfigServer.BASE_API)
                .appendPath("validate");
        String uri = builder.build().toString();

        // Creating the request
        RequestService.getInstance(context).postJsonParsed(uri, createTokenData(ticketToken), ApiResponse.class, createHeaders(context),
                // Calls the callback, that will handle the ticket, display it or do actions with it
                new Response.Listener<ApiResponse>() {
                    @Override
                    public void onResponse(final ApiResponse response) {
                        callback.accept(response);
                    }
                },
                // Manages the VolleyError
                volleyError -> handleVolleyError(context, volleyError));
    }



    /*
     * VolleyError MANAGEMENT
     */

    private static void handleVolleyError(final Context context, final VolleyError volleyError) {
        if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
            Toast.makeText(context,
                    context.getString(R.string.error_network_timeout),
                    Toast.LENGTH_LONG).show();

        } else if (volleyError instanceof AuthFailureError) {
            int httpCode = volleyError.networkResponse.statusCode;
            if (httpCode == 401) {
                Toast.makeText(context,
                        "Identifiants incorrects ! Utilisateur inconnu.",
                        Toast.LENGTH_LONG).show();
            } else if (httpCode == 403) {
                Toast.makeText(context,
                        "Vous n'avez pas les droits suffisants !",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context,
                        "Erreur d'authentification ! message : " + volleyError.getMessage() + "code = " + httpCode,
                        Toast.LENGTH_LONG).show();
            }

            // Launches the login activity again, since there is a problem with the connected user
            final Intent loginActivity = new Intent(context, LoginActivity.class);
            context.startActivity(loginActivity);

        } else {
            Toast.makeText(context,
                    "Erreur survenue ! message = " + volleyError.getMessage() + ", code = " + volleyError.networkResponse.statusCode,
                    Toast.LENGTH_LONG).show();
        }
    }

    /*
     * UTILS AND COMMON METHODS
     */

    private static JSONObject createTokenData(final String token) {
        JSONObject data = new JSONObject();
        try {
            data.put("token", token);
        } catch (JSONException e) {
            Log.e("Exception", "JSON");
        }
        return data;
    }

    private static Map<String, String> createHeaders(final Context context) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic " + Base64.encodeToString((Storage.getUsername(context) + ":" + Storage.getPassword(context)).getBytes(), Base64.DEFAULT));

        return headers;
    }

}

