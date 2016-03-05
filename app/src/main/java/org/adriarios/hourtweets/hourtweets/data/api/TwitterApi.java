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

import io.fabric.sdk.android.Fabric;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Adrian on 03/03/2016.
 */
public class TwitterApi implements ITwitterApi {
    private static final String TWITTER_KEY = "IMtfeDKDQWo0UbHbsI7P90vca";
    private static final String TWITTER_SECRET = "sWHwnn9jfZ0NB8C6rlyCAQdRFUyGSAZ3W7ygWTz4we8nwFRZEQ";

    private TwitterApiClient twitterApiClient;
    private AppSession guestAppSession;

    private Observable<String> fetchFromTwitterResponse;
    private Observer<Object> twitterApiObserver;

    private App context;

    public TwitterApi(App application) {
        context = application;
    }

    public void initApi(){
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(context, new Twitter(authConfig));
        onSubscribe();
    }

    public Boolean isTwitterApiInitialized() {
        return twitterApiClient != null;
    }

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

    public void getNewTweet(String hourStr) {
        String query = "\"It's " + hourStr + " and\"";
        //String query = hourStr;
        twitterApiClient = TwitterCore.getInstance().getApiClient(guestAppSession);
        twitterApiClient.getSearchService().tweets(
                query, null, null, null, "mixed", 50, null, null, null, true, new GuestCallback<>(new Callback<Search>() {
                    @Override
                    public void success(Result<Search> result) {
                        Tweet tweet = null;
                        if (result.data.tweets.size() > 0) {
                            tweet = result.data.tweets.get(0);
                        }
                        twitterApiObserver.onNext(tweet);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        twitterApiObserver.onError(null);
                    }
                }));
    }

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


    @Override
    public void subscribe(Observer<Object> observer) {
        twitterApiObserver = observer;
        fetchFromTwitterResponse
                .subscribeOn(Schedulers.newThread()) // Create a new Thread
                .observeOn(AndroidSchedulers.mainThread()) // Use the UI thread
                .subscribe(observer);
    }
}
