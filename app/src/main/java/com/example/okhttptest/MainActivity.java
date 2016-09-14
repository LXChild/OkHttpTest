package com.example.okhttptest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.okhttptest.http.OkHttpHelper;
import com.example.okhttptest.http.SpotsCallBack;

import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestImage();
    }

    private void requestImage() {
        //String url = "http://112.124.22.238:8081/course_api/banner/query?type=1";
        String url = "http://www.sucaijiayuan.com/uploads/file/contents/2014/04/534a9b6b20fab.jpg";
        okHttpHelper.get(url, new SpotsCallBack(getApplicationContext()) {
            @Override
            public void onSuccess(Response response, Object o) {

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
}
