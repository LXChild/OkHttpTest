package com.example.okhttptest.http;

import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by LXChild on 9/13/16.
 */
public abstract class BaseCallBack<T> {

    public Type type;

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public abstract void onRequestBefore(Request request);

    public abstract void onFailure(Request request, IOException e);

    public abstract void onResponse(Response response);

    public abstract void onSuccess(Response response, T t);
    public abstract void onError(Response response, int code, Exception e);
}
