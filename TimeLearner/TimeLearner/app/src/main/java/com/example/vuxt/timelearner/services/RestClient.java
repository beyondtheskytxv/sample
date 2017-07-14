package com.example.vuxt.timelearner.services;

import com.loopj.android.http.*;

/**
 * Created by Vu Tran on 16/5/2017.
 */

public class RestClient {

    private static final String BASE_URL = "https://time-learner.herokuapp.com";

    private static SyncHttpClient syncHttpClient = new SyncHttpClient();
    private static AsyncHttpClient asyncHttpClient = new SyncHttpClient();

    public static void syncGet(String url, RequestParams params, TextHttpResponseHandler responseHandler) {
        syncHttpClient.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void asyncGet(String url, RequestParams params, TextHttpResponseHandler responseHandler) {
        asyncHttpClient.get(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
