package com.example.rxjava.manager;

import android.util.Log;

import com.example.rxjava.obj.SurfaceObj;
import com.google.gson.Gson;

import org.reactivestreams.Subscriber;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RxJavaManager {

    private String TAG = "RxJavaManager";

    public <T> void toObserver(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                //.unsubscribeOn(Schedulers.newThread())
                .subscribe(observer);
    }

    public <T> void toSubscriber(Flowable<T> flowable, Subscriber<T> subscriber) {
        flowable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                //.unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber);
    }

    public void okHttpResponse() {
        // RxJava2 + OkHttp
        // 方法一
//        Observable.create(new ObservableOnSubscribe<Response>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<Response> emitter) throws Exception {
//                OkHttpClient client = new OkHttpClient();
//                Request request = new Request.Builder()
//                        .url("網址")
//                        .get()
//                        .build();
//                Response response = client.newCall(request).execute();
//                emitter.onNext(response);
//                emitter.onComplete();
//            }
//        })
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(Schedulers.io())
//                .map(new Function<Response, String>() {
//                    @Override
//                    public String apply(@NonNull Response response) throws Exception {
//                        if (response.isSuccessful()) {
//                            return response.body().string();
//                        }
//                        return null;
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String result) throws Exception {
//                        Log.d(TAG, "response success: " + result);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        Log.e(TAG, "response failed： " + throwable.getMessage());
//                    }
//                });

        // 方法二
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> url) throws Exception {
                url.onNext("https://coa.emct.tw/S2K5/api/Code/" + "Surface");
                url.onNext("https://coa.emct.tw/S2K5/api/Code/" + "Phenology");
                url.onComplete();
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String url) throws Exception {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url((String) url)
                                .get()
                                .build();
                        Response response = client.newCall(request).execute();
                        if (response.isSuccessful()) {
                            return response.body().string();
                        }
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String result) throws Exception {
                        Log.d(TAG, "response success: " + result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.e(TAG, "response failed： " + throwable.getMessage());
                    }
                });
    }
}
