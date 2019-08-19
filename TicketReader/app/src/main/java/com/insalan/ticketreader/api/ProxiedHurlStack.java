package com.insalan.ticketreader.api;

import com.android.volley.toolbox.HurlStack;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class ProxiedHurlStack  extends HurlStack {

    private static final String PROXY_ADDRESS = ConfigServer.AUTHORITY;
    private static final int PROXY_PORT = ConfigServer.PORT;//change with the port of the proxy

    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException {
        Proxy proxy = new Proxy(Proxy.Type.HTTP,
                InetSocketAddress.createUnresolved(PROXY_ADDRESS, PROXY_PORT));
        return (HttpURLConnection) url.openConnection(proxy);
    }
}