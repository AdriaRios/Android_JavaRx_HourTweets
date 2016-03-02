package org.adriarios.hourtweets.hourtweets.domain;

import android.util.Log;

import java.util.Random;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Adrian on 02/03/2016.
 */
public class GetTweetInteractor implements IGetTweetInteractor {

    Observable<String> fetchFromGoogle;

    public GetTweetInteractor() {
        fetchFromGoogle = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    Random rand = new Random();
                    int  n = rand.nextInt(50) + 1;
                    String data = "aaaaaa" + n;
                    subscriber.onNext(data); // Emit the contents of the URL
                    subscriber.onCompleted(); // Nothing more to emit
                }catch(Exception e){
                    subscriber.onError(e); // In case there are network errors
                }
            }
        });




    }

    public void getNewTweet(Observer<String> myObserver) {
        fetchFromGoogle
                .subscribeOn(Schedulers.newThread()) // Create a new Thread
                .observeOn(AndroidSchedulers.mainThread()) // Use the UI thread
                .subscribe(myObserver);
        Log.d("STATICS", "loginGuest.callback.success called");
    }
}
