package org.adriarios.hourtweets.hourtweets.presentation.presenters;

import android.os.Handler;
import android.util.Log;

import com.twitter.sdk.android.core.models.Tweet;

import org.adriarios.hourtweets.hourtweets.data.storage.TweetRealmObjectVO;
import org.adriarios.hourtweets.hourtweets.di.App;
import org.adriarios.hourtweets.hourtweets.domain.IGetTweetInteractor;
import org.adriarios.hourtweets.hourtweets.presentation.activities.TweetActivity;
import org.adriarios.hourtweets.hourtweets.utils.Utils;

import java.util.Calendar;
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
    @Inject
    Utils utils;

    private static final String TWEET_ONLINE = "com.twitter.sdk.android.core.models.Tweet";
    private static final String TWEET_OFFLINE = "io.realm.TweetRealmObjectVORealmProxy";

    private TweetActivity tweetActivity;


    public TweetPresenter(App application) {
        application.getObjectGraph().inject(this);
        getTweetPerMinute();
    }

    public void init(TweetActivity activity) {
        tweetActivity = activity;
        initObserver();
    }

    public void initObserver(){
        Observer<Object> myObserver = new Observer<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                // Called when the observable encounters an error
            }

            @Override
            public void onNext(Object response) {
                processResponse(response);
            }
        };
        tweetInteractor.subscribe(myObserver);
    }

    private void processResponse(Object response){
        if (response == null){
            tweetActivity.showNotFoundMessage();
        }else{
            checkKindOfTweet(response);
        }
    }

    private void checkKindOfTweet(Object response){
        String responseType = response.getClass().getName();
        switch(responseType) {
            case TWEET_ONLINE:
                tweetActivity.showOnlineTweet((Tweet) response);
                break;
            case TWEET_OFFLINE:
                tweetActivity.showOfflineTweet((TweetRealmObjectVO) response);
                break;
        }
    }


    public void getTweetPerMinute() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @SuppressWarnings("unchecked")
                    public void run() {
                        try {
                            Calendar c = Calendar.getInstance();
                            int hour = c.get(Calendar.HOUR);
                            int hour_day = c.get(Calendar.HOUR_OF_DAY);
                            String minute = utils.paddingZero(c.get(Calendar.MINUTE));
                            String hourStr = String.valueOf(hour_day) + "::" + minute;
                            tweetInteractor.nextTweet(hourStr);
                        } catch (Exception e) {
                            Log.d("MY OBSERVER", "d");
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 5000);
    }

}
