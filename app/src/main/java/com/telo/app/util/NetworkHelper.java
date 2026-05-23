package com.telo.app.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

public class NetworkHelper {

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
            context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        android.net.Network network = cm.getActiveNetwork();
        if (network == null) return false;

        NetworkCapabilities caps = cm.getNetworkCapabilities(network);
        if (caps == null) return false;

        return caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            || caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            || caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
    }

    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
            context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        android.net.Network network = cm.getActiveNetwork();
        if (network == null) return false;

        NetworkCapabilities caps = cm.getNetworkCapabilities(network);
        return caps != null &&
               caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
    }
}