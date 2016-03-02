package org.adriarios.hourtweets.hourtweets.presentation.presenters;

import android.util.Log;

import org.adriarios.hourtweets.hourtweets.di.App;
import org.adriarios.hourtweets.hourtweets.domain.IGetTweetInteractor;
import org.adriarios.hourtweets.hourtweets.presentation.activities.TweetActivity;

import javax.inject.Inject;

import rx.Observer;

/**
 * Created by Adrian on 02/03/2016.
 */
public class TweetPresenter {
    @Inject
    IGetTweetInteractor tweetInteractor;


    public TweetPresenter(App application){
        application.getObjectGraph().inject(this);
    }

    public void init(TweetActivity tweetActivity) {
        Observer<String> myObserver = new Observer<String>() {
            @Override
            public void onCompleted() {
                // Called when the observable has no more data to emit
                Log.d("MY OBSERVER","d");
            }

            @Override
            public void onError(Throwable e) {
                // Called when the observable encounters an error
            }

            @Override
            public void onNext(String s) {
                // Called each time the observable emits data
                Log.d("MY OBSERVER", s);
            }
        };
        tweetInteractor.getNewTweet(myObserver);
        tweetInteractor.getNewTweet(myObserver);
        tweetInteractor.getNewTweet(myObserver);
    }

    public void test(String str) {
        Log.d("STATICS", "loginGuest.callback.failure called");
    }
}
