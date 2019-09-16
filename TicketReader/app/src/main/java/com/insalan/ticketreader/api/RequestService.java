package com.insalan.ticketreader.api;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Map;

/**
 * Class implementing the requests and managing the queue for the requests.
 * Doesn't know the purpose and the data of the requests, only makes the structure and sends the requests.
 */
public final class RequestService {

    private static RequestService instance;

    private final RequestQueue requestQueue;

    private RequestService(final Context context) {
        super();
        // FINAL VERSION
        this.requestQueue = Volley.newRequestQueue(context);
        // TEST VERSION
        //this.requestQueue = Volley.newRequestQueue(context, new ProxiedHurlStack());
    }

    /**
     * Method giving the pseudo-singleton instance
     *
     * @param context will certainly be the app's context
     * @return the only instance corresponding to the context. Basically, will be the app's instance.
     */
    public static synchronized RequestService getInstance(final Context context) {
        if (instance == null) {
            instance = new RequestService(context);
        }
        return instance;
    }

    public void get(final String url, final Map<String, String> headers, final Response.Listener<String> listener, final Response.ErrorListener errorListener) {
        final StringRequest getRequest = new StringRequest(Request.Method.GET, url, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers == null ? super.getHeaders() : headers;
            }
        };
        this.requestQueue.add(getRequest);
    }

    /**
     * Basic method simulating a POST request, with JSON data. The response will be parsed into an object of type T
     *
     * @param data          Data in JSON format that will be sent to the server
     * @param clazz         Class that will be used to create an object from the parsed JSON in the response
     * @param listener      Listener that will do something with the response. You can define it with a lambda on the fly when calling this function
     * @param errorListener Listener that will do something in case of error. You can define it with a lambda on the fly when calling this function
     */
    public <T> void postJsonParsed(final String url, final JSONObject data, final Class<T> clazz, final Map<String, String> headers, final Response.Listener<T> listener, final Response.ErrorListener errorListener) {
        final GsonPostRequest<T> request = new GsonPostRequest<>(url, data, clazz, headers, listener, errorListener);

        this.requestQueue.add(request);
    }
}
