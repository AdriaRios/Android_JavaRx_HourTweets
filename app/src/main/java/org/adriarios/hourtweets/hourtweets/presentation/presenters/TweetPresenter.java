package org.adriarios.hourtweets.hourtweets.presentation.presenters;

import android.util.Log;

import com.twitter.sdk.android.core.models.Tweet;

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

    public void init(final TweetActivity tweetActivity) {
        Observer<Tweet> myObserver = new Observer<Tweet>() {
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
            public void onNext(Tweet tweet) {
                // Called each time the observable emits data
                tweetActivity.showTweet(tweet);
            }
        };
        tweetInteractor.subscribe(myObserver);
    }

}
