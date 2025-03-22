package com.example.moviesapp.utils;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Utility class for making HTTP requests to external APIs.
 * This class uses OkHttpClient to handle GET requests.
 */
public class ApiClient {
    private static final OkHttpClient client = new OkHttpClient();

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public static void get(String url, Callback callback) {

        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);

    }
}