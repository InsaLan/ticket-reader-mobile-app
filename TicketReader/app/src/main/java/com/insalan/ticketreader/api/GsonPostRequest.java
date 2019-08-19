package com.insalan.ticketreader.api;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.insalan.ticketreader.api.json.GsonService;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Class used to create a POST request, from which we retrieve the JSON data returned from the server.
 * This JSON data is parsed by Gson and serialized into 2 classes : Ticket and ApiErr
 */
public class GsonPostRequest<T> extends Request<T> {

    private final GsonService gson;

    private final Class<T> clazz;
    private final Map<String, String> headers;
    private final Response.Listener<T> listener;
    private final JSONObject data;

    /**
     * Make a POST request and return a parsed object from JSON.
     *
     * @param uri     URL of the request to make
     * @param data    JSON data sent to the server
     * @param clazz   Relevant class object, for Gson's reflection : Ticket
     * @param headers Map of request headers
     */
    public GsonPostRequest(final String uri, final JSONObject data, final Class<T> clazz, final Map<String, String> headers, final Response.Listener<T> listener, final Response.ErrorListener errorListener) {
        super(Method.POST, uri, errorListener);
        this.gson = GsonService.getInstance();
        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
        this.data = data;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers == null ? super.getHeaders() : headers;
    }

    @Override
    protected void deliverResponse(final T response) {
        listener.onResponse(response);
    }

    // Sets the data for the post request
    @Override
    public byte[] getBody() {
        return data.toString().getBytes();
    }

    @Override
    protected Response<T> parseNetworkResponse(final NetworkResponse response) {
        try {
            final String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(this.gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (final JsonSyntaxException | UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

}
