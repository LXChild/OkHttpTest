package com.example.okhttptest.http;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by LXChild on 9/13/16.
 */
public class OkHttpHelper {
    private static OkHttpClient client;
    private Gson gson;
    private Handler handler;

    private OkHttpHelper() {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        gson = new Gson();
        handler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpHelper getInstance() {
        return new OkHttpHelper();
    }

    public void get(String url, BaseCallBack<Object> callBack) {
        Request request = buildRequest(url, null, HttpMethodType.GET);
        doRequest(request, callBack);
    }

    public void post(String url, Map<String, String> params, BaseCallBack<Object> callBack) {
        Request request = buildRequest(url, params, HttpMethodType.POST);
        doRequest(request, callBack);
    }

    public void doRequest(final Request request, final BaseCallBack<Object> callBack) {
        callBack.onRequestBefore(request);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailure(request, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callBack.onResponse(response);
                if (response.isSuccessful()) {
                    String resultStr = response.body().string();
                    if (callBack.type == String.class) {
                        callBackSuccess(callBack, response, resultStr);
                        //callBack.onSuccess(response, resultStr);
                    } else {
                        try {
                            Object obj = gson.fromJson(resultStr, callBack.type);
                            callBackSuccess(callBack, response, resultStr);

                           // callBack.onSuccess(response, obj);
                        } catch (JsonParseException e) {
                            callBackError(callBack, response, response.code(), e);
                            //callBack.onError(response, response.code(), e);
                        }
                    }
                } else {
                    callBackError(callBack, response, response.code(), null);

                   // callBack.onError(response, response.code(), null);
                }
            }
        });
    }

    private Request buildRequest(String url, Map<String, String> params, HttpMethodType methodType) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);

        if (methodType == HttpMethodType.GET) {
            builder.get();
        } else if (methodType == HttpMethodType.POST) {
            RequestBody body = buildFormData(params);
            builder.post(body);
        }
        return builder.build();
    }

    private RequestBody buildFormData(Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }

    private void callBackSuccess(final BaseCallBack<Object> callBack, final Response response, final Object object) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onSuccess(response, object);
            }
        });
    }

    private void callBackError(final BaseCallBack<Object> callBack, final Response response, final int code, final Exception e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onError(response, code, e);
            }
        });
    }

    enum HttpMethodType {
        GET,
        POST
    }
}
