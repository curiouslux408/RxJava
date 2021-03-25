package com.example.rxjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.rxjava.manager.RxJavaManager;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "MainActivity";
    private Button button1, button2, button3, button4;
    private Button button5;
    private RxJavaManager rxJavaManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        rxJavaManager = new RxJavaManager();

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5 = findViewById(R.id.button5);
        button5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1: // 正規寫法
                /**
                 * Observable subscribe to Observer
                 */
                Observer<String> observer = new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        // 當訂閱後會先調用此方法,相當於onStart()
                        // 此參數可用於請求數據或取消訂閱
                        Log.d(TAG, "onSubscribe: " + d);
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        Log.d(TAG, "onNext: " + s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete!");
                    }
                };
                Observable observable = Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) {
                        e.onNext("Hello");
                        e.onNext("Hi");
                        e.onNext("Aloha");
                        e.onComplete();
                    }
                });
                //observable.subscribe(observer);
                rxJavaManager.toObserver(observable, observer);

                /**
                 * Flowable subscribe to Subscriber
                 */
                Subscriber<String> subscriber = new Subscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        // 當訂閱後會先調用此方法,相當於onStart()
                        // 此參數可用於請求數據或取消訂閱
                        Log.d(TAG, "onSubscribe: " + s);
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "Item: " + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "Complete!");
                    }
                };
                Flowable flowable = Flowable.create(new FlowableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull FlowableEmitter<String> e) throws Exception {
                        e.onNext("Hello");
                        e.onNext("Hi");
                        e.onNext("Aloha");
                        e.onComplete();
                    }
                }, BackpressureStrategy.BUFFER); // 必須指定BackPressure
                //flowable.subscribe(subscriber);
                rxJavaManager.toSubscriber(flowable, subscriber);
                break;

            case R.id.button2: // 偷懶寫法
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> e) {
                        e.onNext("Hello");
                        e.onNext("Hi");
                        e.onNext("Aloha");
                        e.onComplete();
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                                Log.d(TAG, "onSubscribe: " + d);
                            }

                            @Override
                            public void onNext(String s) {
                                Log.d(TAG, "Item: " + s);
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "Complete!");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "Error!");
                            }
                        });

                /**
                 * buffer
                 */
                Observable.just(1, 2, 3, 4)
                        .buffer(4, 1)
                        //.subscribeOn(Schedulers.io())
                        //.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<Integer>>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(List<Integer> integers) {
                                for (Integer value : integers) {
                                    Log.d(TAG, "onNext: " + value);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });

                /**
                 * range
                 */
                Flowable.range(1, 4)
                        .delay(1, TimeUnit.SECONDS)
                        //.subscribeOn(Schedulers.io())
                        //.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Integer>() {

                            @Override
                            public void onSubscribe(Subscription s) {
                                s.request(Long.MAX_VALUE);
                            }

                            @Override
                            public void onNext(Integer integer) {
                                Log.d(TAG, "onNext: " + integer);
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                break;
            case R.id.button3:
//                Observable.just("logo")
//                        .map(new Func1<String, Object>() {
//                            @Override
//                            public Object call(String s) {
//                                Log.d(TAG, "return: " + s);
//                                return s;
//                            }
//                        })
//                        .subscribe(new Action1<Object>() {
//                            @Override
//                            public void call(Object o) {
//                                Log.d(TAG, "call: " + o);
//                            }
//                        });
                break;
            case R.id.button4:
                // 轉換線程
//                Observable.just("test")
//                        .subscribeOn(Schedulers.newThread())
//                        .observeOn(Schedulers.io())
//                        .map(new Func1<String, Object>() {
//                            @Override
//                            public Object call(String s) {
//                                Log.d(TAG, "call: " + s);
//                                return s;
//                            }
//                        })
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Subscriber<Object>() {
//                            @Override
//                            public void onCompleted() {
//                                Log.d(TAG, "onCompleted!");
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                Log.d(TAG, "onError: " + e);
//                            }
//
//                            @Override
//                            public void onNext(Object o) {
//                                Log.d(TAG, "onNext: " + o);
//                            }
//                        });
                break;
            case R.id.button5:
                rxJavaManager.retrofitResponse();
                //rxJavaManager.okHttpResponse(ApiService.baseUrl + "Surface", ApiService.baseUrl + "Phenology");
                break;
        }
    }
}