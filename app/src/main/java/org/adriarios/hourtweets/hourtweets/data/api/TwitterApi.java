package org.adriarios.hourtweets.hourtweets.data.api;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.GuestCallback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;

import org.adriarios.hourtweets.hourtweets.di.App;

import java.util.List;

import io.fabric.sdk.android.Fabric;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * This is the Twitter API Class. His goal is make calls to Twitter, process the response and
 * notify its subscribers in the Domain Layer.
 *
 * Created by Adrian on 03/03/2016.
 */
public class TwitterApi implements ITwitterApi {
    //Twitter Key of Twitter App
    private static final String TWITTER_KEY = "IMtfeDKDQWo0UbHbsI7P90vca";
    //Twitter Secret Key of Twitter App
    private static final String TWITTER_SECRET = "sWHwnn9jfZ0NB8C6rlyCAQdRFUyGSAZ3W7ygWTz4we8nwFRZEQ";

    //Twitter API Client, used to make calls
    private TwitterApiClient twitterApiClient;
    //Current Twitter Session
    private AppSession guestAppSession;

    //Observable prop to notify changes immediately to its subscribers
    private Observable<String> fetchFromTwitterResponse;

    //This is the observer subscribed to this class.
    private Observer<Object> myObserver;

    private App context;


    public TwitterApi(App application) {
        context = application;
    }

    /**
     * This method initializes the Twitter API and onSubscribe method
     */
    public void initApi() {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(context, new Twitter(authConfig));
        onSubscribe();
    }


    /**
     * This method indicates the actions that will be launch when a new subscriber is added. In this case,
     * when a new subscriber the method getTwiiterApiClient will be launch.
     */
    public void onSubscribe() {
        fetchFromTwitterResponse = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    twitterApiClient = getTwitterApiClient(subscriber);
                } catch (Exception e) {
                    subscriber.onError(e); // In case there are network errors
                }
            }
        });
    }

    /**
     * This method add a new subscriber to the TwitterAPI (to its Observable prop)
     * @param observer The new subscriber
     */
    @Override
    public void subscribe(Observer<Object> observer) {
        myObserver = observer;
        fetchFromTwitterResponse
                .subscribeOn(Schedulers.newThread()) // Create a new Thread
                .observeOn(AndroidSchedulers.mainThread()) // Use the UI thread
                .subscribe(observer);
    }

    /**
     * This method says if Twitter API have been initialized
     */
    public Boolean isTwitterApiInitialized() {
        return twitterApiClient != null;
    }

    /**
     * This method does the guestLogin with Twitter, necessary to make calls. If everything is ok
     * the subscribers will be notified
     *
     * @param subscriber Subscribers to be notified when login ok
     */
    private TwitterApiClient getTwitterApiClient(final Subscriber<? super String> subscriber) {
        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> result) {
                guestAppSession = result.data;
                subscriber.onNext("OK"); // Emit the contents of the URL
                subscriber.onCompleted(); // Nothing more to emit*/
            }

            @Override
            public void failure(TwitterException exception) {
                throw exception;
            }
        });
        return twitterApiClient;
    }

    /**
     * This method does a call to Twitter to get a new Tweet related to specific query. After that,
     * the subscribers will be notified with the result.
     *
     * @param hourStr Hour to compose the query to make the Twitter call
     */
    public void getNewTweet(String hourStr) {
        String query = "\"It's " + hourStr + " and\"";
        twitterApiClient = TwitterCore.getInstance().getApiClient(guestAppSession);
        twitterApiClient.getSearchService().tweets(
                query, null, null, null, "mixed", 50, null, null, null, true, new GuestCallback<>(new Callback<Search>() {
                    @Override
                    public void success(Result<Search> result) {
                        Tweet tweet = null;
                        if (result.data.tweets.size() > 0) {
                            getMostRetweetedTweet(result.data.tweets);
                            tweet = getMostRetweetedTweet(result.data.tweets);
                        }
                        myObserver.onNext(tweet);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        myObserver.onError(null);
                    }
                }));
    }

    /**
     * This method returns the most retweeted tweet from a list of tweets
     *
     * @param tweets List of tweets
     * @return mostRetweetedTweet The most retweeted tweet
     */
    private Tweet getMostRetweetedTweet(List<Tweet> tweets) {
        Tweet mostRetweetedTweet = tweets.get(0);
        for (int i = 1; i < tweets.size(); i++) {
            if (tweets.get(i).retweetCount > mostRetweetedTweet.retweetCount) {
                mostRetweetedTweet = tweets.get(i);
            }
        }
        return mostRetweetedTweet;
    }


}
