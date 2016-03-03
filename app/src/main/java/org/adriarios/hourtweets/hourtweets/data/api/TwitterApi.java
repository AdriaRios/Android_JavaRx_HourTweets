package org.adriarios.hourtweets.hourtweets.data.api;

import android.util.Log;

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

import java.util.Calendar;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Adrian on 03/03/2016.
 */
public class TwitterApi implements ITwitterApi {
    private static final String TWITTER_KEY = "IMtfeDKDQWo0UbHbsI7P90vca";
    private static final String TWITTER_SECRET = "sWHwnn9jfZ0NB8C6rlyCAQdRFUyGSAZ3W7ygWTz4we8nwFRZEQ";

    private TwitterApiClient twitterApiClient;
    private AppSession guestAppSession;

    public TwitterApi(App application) {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(application, new Twitter(authConfig));
        twitterApiClient = getTwitterApiClient();
    }

    private TwitterApiClient getTwitterApiClient() {
        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> result) {
                guestAppSession = result.data;
                Log.d("STATICS", "loginGuest.callback.success called");
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("STATICS", "loginGuest.callback.failure called");
                // unable to get an AppSession with guest auth
                throw exception;


            }
        });
        return twitterApiClient;
    }

    public void getNewTweet(){
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int hour_day = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        twitterApiClient = TwitterCore.getInstance().getApiClient(guestAppSession);
        String hourStr = String.valueOf(hour_day) + ":" + String.valueOf(minute);
        twitterApiClient.getSearchService().tweets("\""+hourStr+"\"", null, null, null, "mixed", 50, null, null, null, true, new GuestCallback<>(new Callback<Search>() {
            @Override
            public void success(Result<Search> result) {
                //ViewGroup parentView = (ViewGroup) findViewById(R.id.tweetView);
                Tweet test = result.data.tweets.get(0);
                //TweetView tweetView = new TweetView(TweetActivity.this, test);
                //tweetContainer.addView(tweetView);
                // use result tweets
                Log.d("TwitterKit1", result.toString());
            }

            @Override
            public void failure(TwitterException exception) {
                // handle exceptions
                Log.d("TwitterKit2", exception.toString());
            }
        }));
    }
}
