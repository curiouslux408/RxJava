package com.example.rxjava.manager;

import android.util.Log;

import com.example.rxjava.obj.PhenologyObj;
import com.example.rxjava.obj.SurfaceObj;
import com.example.rxjava.observer.MyObserver;
import com.example.rxjava.observer.ObserverOnNextListener;

import org.reactivestreams.Subscriber;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RxJavaManager {

    private String TAG = "RxJavaManager";
    private Disposable mDisposable;

    /**
     * RxJava2 + Retrofit
     */
    public void retrofitResponse() {
        // 方法一
//        RetrofitManager.getApiService().getSurface()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<SurfaceObj[]>() {
//                    @Override
//                    public void accept(SurfaceObj[] result) throws Exception {
//                        Log.d(TAG, "response success!!");
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        Log.e(TAG, "response failed： " + throwable.getMessage());
//                    }
//                });

        // 方法二
//        ObserverOnNextListener<SurfaceObj[]> listener = new ObserverOnNextListener<SurfaceObj[]>() {
//            @Override
//            public void onNext(SurfaceObj[] result) {
//                Log.d(TAG, "onNext: " + result);
//            }
//        };
//        ApiSubscribeToObserver(RetrofitManager.getApiService().getSurface(), new MyObserver<SurfaceObj>(listener));

        // 方法三
        Observable<SurfaceObj[]> observable1 = RetrofitManager.getApiService().getSurface();
        Observable<PhenologyObj[]> observable2 = RetrofitManager.getApiService().getPhenology();

        mDisposable = Observable.zip(observable1, observable2, new BiFunction<SurfaceObj[], PhenologyObj[], Boolean>() {
            @NonNull
            @Override
            public Boolean apply(@NonNull SurfaceObj[] result1, @NonNull PhenologyObj[] result2) throws Exception {
                // 由此處進行資料操作
                return true;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean result) throws Exception {
                        Log.d(TAG, "response success!!");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.e(TAG, "response failed： " + throwable.getMessage());
                    }
                });
    }

    /**
     * RxJava2 + OkHttp
     */
    public void okHttpResponse(String url1, String url2) {
        mDisposable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> url) throws Exception {
                url.onNext(url1);
                url.onNext(url2);
                url.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String url) throws Exception {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url(url)
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
                        Log.d(TAG, "response success!!");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.e(TAG, "response failed： " + throwable.getMessage());
                    }
                });
    }

    public static void ApiSubscribeToObserver(Observable observable, Observer observer) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public static <T> void toObserver(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public static <T> void toSubscriber(Flowable<T> flowable, Subscriber<T> subscriber) {
        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void dispose() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }
}
