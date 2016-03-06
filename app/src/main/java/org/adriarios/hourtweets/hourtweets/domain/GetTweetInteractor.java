package org.adriarios.hourtweets.hourtweets.domain;

import com.twitter.sdk.android.core.models.Tweet;

import org.adriarios.hourtweets.hourtweets.data.api.ITwitterApi;
import org.adriarios.hourtweets.hourtweets.data.storage.ITweetsStoragedManager;
import org.adriarios.hourtweets.hourtweets.data.storage.TweetRealmObjectVO;
import org.adriarios.hourtweets.hourtweets.di.App;
import org.adriarios.hourtweets.hourtweets.utils.Utils;

import javax.inject.Inject;

import rx.Observer;

/**
 * This is the Interactor of the application. His goal is to obtain data (tweets in this case) from
 * the Data Layer (Local Storage and Twitter Api) and provide it to presentation Layer.
 *
 * @author Adrian
 */
public class GetTweetInteractor implements IGetTweetInteractor {
    @Inject
    ITwitterApi twitterApi;

    @Inject
    ITweetsStoragedManager tweetsStoragedManager;

    @Inject
    Utils utils;

    private static final String STRING_TYPE = "java.lang.String";
    private String currentTweetHour;

    //This is the observer that are listening for the notifications of the Twitter Api
    private Observer<Object> twitterApiObserver;

    //This is the observer subscribed to this class.
    private Observer<Object> myObserver;

    /**
     * Constructor of the class. This method set this class as injectable and call a method to
     * initialize the twitterApiObserver
     * @param application
     */
    public GetTweetInteractor(App application) {
        application.getObjectGraph().inject(this);
        initObserver();
    }

    /**
     * This method initializes the twitter observer property, subscribes the Interactor to the Tweet
     * Api changes. We controls two type of Messages:
     *
     * onError: It was an error and we'll notify our subscribers (presentation layers)
     *
     * onNext: There are a new tweet (if there were occurrences with the time), then process storages
     * this tweet in local storage and notify our subscribers (presentation layer).
     */
    private void initObserver() {
        twitterApiObserver = new Observer<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                myObserver.onNext(null);
            }

            @Override
            public void onNext(Object response) {
                if (response != null) {
                    String type = response.getClass().getName();
                    if (type != STRING_TYPE) {
                        //When receives new Twitter
                        tweetsStoragedManager.addNewTweetToLocalStorage((Tweet) response, currentTweetHour);
                    } else {
                        //When TwitterApi is ready
                        twitterApi.getNewTweet(currentTweetHour);
                    }
                }
                myObserver.onNext(response);
            }
        };
    }

    /**
     *
     * This method stores in a class property a subscriber of this Interactor class. It will be notified when
     * a new tweet.
     * @param observer The observer to subscribe to this class
     */
    public void subscribe(Observer<Object> observer) {
        myObserver = observer;
    }

    /**
     * This method get the next Tweet. If the Network is avaiable the tweet will be take from
     * TwitterApi, otherwhise from local storage
     * @param hourStr The current hour get the Tweet
     */
    @Override
    public void nextTweet(String hourStr) {
        currentTweetHour = hourStr;
        if (utils.isNetworkAvailable()) {
            getTweetFromTwitterApi(hourStr);
        } else {
            getTweetFromLocalStorage(hourStr);
        }

    }

    /**
     * This method get a new tweet from Twitter API
     * @param hourStr The current hour get the Tweet
     */
    public void getTweetFromTwitterApi(String hourStr){
        if (twitterApi.isTwitterApiInitialized()) {
            twitterApi.getNewTweet(hourStr);
        } else {
            twitterApi.initApi();
            twitterApi.subscribe(twitterApiObserver);
        }
    }

    /**
     * This method get a new tweet from Local Storage
     * @param hourStr The current hour get the Tweet
     */
    public void getTweetFromLocalStorage(String hourStr){
        TweetRealmObjectVO tweetOffLine = tweetsStoragedManager.getStoragedTweet(hourStr);
        myObserver.onNext(tweetOffLine);
    }
}
