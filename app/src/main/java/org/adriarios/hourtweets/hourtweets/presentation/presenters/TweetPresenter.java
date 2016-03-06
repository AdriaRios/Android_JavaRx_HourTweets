package org.adriarios.hourtweets.hourtweets.presentation.presenters;

import android.os.Handler;
import android.util.Log;

import com.twitter.sdk.android.core.models.Tweet;

import org.adriarios.hourtweets.hourtweets.data.storage.TweetRealmObjectVO;
import org.adriarios.hourtweets.hourtweets.di.App;
import org.adriarios.hourtweets.hourtweets.domain.IGetTweetInteractor;
import org.adriarios.hourtweets.hourtweets.presentation.activities.TweetActivity;
import org.adriarios.hourtweets.hourtweets.utils.Utils;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import rx.Observer;

/**
 * This is the presenter of the application. His goal is to get new tweets from the Tweets
 * Interactor and updates de view of the application (Activity) with these new Tweets.
 *
 * @author Adrian
 *
 */
public class TweetPresenter {
    @Inject
    IGetTweetInteractor tweetInteractor;
    @Inject
    Utils utils;

    private static final String TWEET_ONLINE = "com.twitter.sdk.android.core.models.Tweet";
    private static final String TWEET_OFFLINE = "io.realm.TweetRealmObjectVORealmProxy";

    private TweetActivity tweetActivity;

    /**
     * Contructor of the class. This method set this class as injectable and starts the interval
     * to get tweets
     * @param application
     */
    public TweetPresenter(App application) {
        application.getObjectGraph().inject(this);
        getTweetPerMinute();
    }


    /**
     * This method storages the Activity that presenter will control in a class prop and
     * initializes the observer.
     * @param activity
     */
    public void init(TweetActivity activity) {
        tweetActivity = activity;
        initObserver();
    }

    /**
     * This method initializes the observer property, subscribes the presenter to the Tweet
     * Interactor changes. We controls two type of Messages:
     *
     * onError: It was an error and notify the activity that
     * it has to show a 'not found message'
     *
     * onNext: There are a new tweet (if there were occurrences with the time), then process the
     * message
     */
    public void initObserver(){
        Observer<Object> observer = new Observer<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                tweetActivity.showNotFoundMessage();
            }

            @Override
            public void onNext(Object response) {
                processResponse(response);
            }
        };
        tweetInteractor.subscribe(observer);
    }

    /**
     * This method creates an interval that gets a new tweet every minute through the
     * Tweet Interactor.
     */
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
                            String hourStr = utils.getCurrentHour();
                            tweetInteractor.nextTweet(hourStr);
                        } catch (Exception e) {
                            Log.e("Excepcion in timer", e.toString());
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 60000);
    }

    /**
     * This method process the message received from the Tweet Interactor. This response can be
     * a new tweet (then check the type of new tweet) or can be null (then notify the activity that
     * it has to show a 'not found message'
     * @param response The objected received
     */
    private void processResponse(Object response){
        if (response == null){
            tweetActivity.showNotFoundMessage();
        }else{
            checkKindOfTweet(response);
        }
    }

    /**
     * This method informs the activity that it has to show a new Tweet. There are two
     * different kinds of tweet to show: Online and Offline.
     * @param tweet The new tweet received
     */
    private void checkKindOfTweet(Object tweet){
        String responseType = tweet.getClass().getName();
        switch(responseType) {
            case TWEET_ONLINE:
                tweetActivity.showOnlineTweet((Tweet) tweet);
                break;
            case TWEET_OFFLINE:
                tweetActivity.showOfflineTweet((TweetRealmObjectVO) tweet);
                break;
        }
    }

}
