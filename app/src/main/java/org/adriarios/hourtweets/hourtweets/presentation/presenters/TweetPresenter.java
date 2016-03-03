package org.adriarios.hourtweets.hourtweets.presentation.presenters;

import android.os.Handler;
import android.util.Log;

import com.twitter.sdk.android.core.models.Tweet;

import org.adriarios.hourtweets.hourtweets.di.App;
import org.adriarios.hourtweets.hourtweets.domain.IGetTweetInteractor;
import org.adriarios.hourtweets.hourtweets.presentation.activities.TweetActivity;

import java.util.Timer;
import java.util.TimerTask;

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
        Observer<Object> myObserver = new Observer<Object>() {
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
            public void onNext(Object response) {
                // Called each time the observable emits data
                String type = response.getClass().getName();
                if (type == "java.lang.String"){
                    getTweetPerMinute();
                }else{
                    tweetActivity.showTweet((Tweet) response);
                }

            }
        };
        tweetInteractor.subscribe(myObserver);
    }

    public void getTweetPerMinute(){
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @SuppressWarnings("unchecked")
                    public void run() {
                        try {
                            tweetInteractor.nextTweet();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 60000);
    }

}
