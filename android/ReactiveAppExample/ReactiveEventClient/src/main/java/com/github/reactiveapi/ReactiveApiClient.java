package com.github.reactiveapi;

import android.app.Activity;

import com.github.reactiveapi.gcm.GCMClient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.concurrency.AndroidSchedulers;

/**
 * Created by felixgarcia on 04/04/2014.
 */
public class ReactiveApiClient {
    ExecutorService executor = Executors.newSingleThreadExecutor();

    private static ReactiveApiClient singleton;

    public static ReactiveApiClient getInstance() {
        if (singleton == null) {
            singleton = new ReactiveApiClient();
        }
        return singleton;
    }

    GCMClient gcmClient;
    Activity context;

    public void init(Activity context, String projectId) {
        this.context = context;
        gcmClient = new GCMClient();
        gcmClient.start(projectId, context, this);
        singleton = this;
    }


    BlockingQueue<Object> queuePending = new LinkedBlockingQueue<Object>();

    public void addToQueue(Object item) {
        queuePending.offer(item);
    }

    public Observable<Object> getHouseList() {
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(final Subscriber<? super Object> subscriber) {
                executor.execute(new Runnable() {

                    public void run() {
                        while (!subscriber.isUnsubscribed()) {
                            try {
                                subscriber.onNext(queuePending.take());
                                //subscriber.onCompleted();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                });
            }
        }).observeOn(AndroidSchedulers.mainThread());
    }


}
