package com.example.okhttptest.http;

import android.content.Context;

import java.io.IOException;

import dmax.dialog.SpotsDialog;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by LXChild on 9/13/16.
 */
public abstract class SpotsCallBack extends BaseCallBack {

    SpotsDialog dialog;

    public SpotsCallBack(Context ctx) {
        dialog = new SpotsDialog(ctx);
    }

    public void showDialog() {
        dialog.show();
    }

    public void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void setMessage(String message) {
        dialog.setMessage(message);
    }

    @Override
    public void onRequestBefore(Request request) {
        showDialog();
    }

    @Override
    public void onFailure(Request request, IOException e) {
        dismissDialog();
    }

    @Override
    public void onResponse(Response response) {
        dismissDialog();
    }
}
