package com.marvel.request;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.marvel.network.Requestor;
import com.marvel.network.exception.ResponseException;
import com.marvel.network.observer.ResponseObserver;
import com.marvel.network.observer.SimpleObserver;
import com.marvel.network.tranformer.ApiThreadTransformer;
import com.marvel.network.util.WLog;
import com.marvel.request.response.model.NewsResult;

import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        normal();
        simple();
    }

    private void normal() {
        Requestor.instance()
                .getApi(NewsApi.class)
                .getNews("yule")
                .compose(new ApiThreadTransformer<>())
                .compose(new NewsTransformer<>())
                .subscribe(new ResponseObserver<NewsResult>() {
                    @Override
                    protected void onSuccess(NewsResult result) {
                        WLog.json(new Gson().toJson(result.getData()));
                    }

                    @Override
                    protected void onFail(ResponseException responseException) {
                        WLog.e(responseException.message);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        WLog.p("开始发送请求...");
                    }

                    @Override
                    public void onComplete() {
                        WLog.p("请求处理完成...");
                    }
                });
    }

    private void simple() {
        Requestor.instance()
                .getApi(NewsApi.class)
                .getNews("yule")
                .compose(new ApiThreadTransformer<>())
                .compose(new NewsTransformer<>())
                .subscribe(new SimpleObserver<NewsResult>() {
                    @Override
                    protected void onSuccess(NewsResult result) {
                        WLog.json(new Gson().toJson(result.getData()));
                    }

                    @Override
                    protected void onFail(ResponseException responseException) {
                        WLog.e(responseException.message);
                    }
                });
    }
}
