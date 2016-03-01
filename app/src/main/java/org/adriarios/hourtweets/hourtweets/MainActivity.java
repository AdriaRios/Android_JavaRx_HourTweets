package org.adriarios.hourtweets.hourtweets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;

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
import com.twitter.sdk.android.tweetui.TweetView;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "IMtfeDKDQWo0UbHbsI7P90vca";
    private static final String TWITTER_SECRET = "sWHwnn9jfZ0NB8C6rlyCAQdRFUyGSAZ3W7ygWTz4we8nwFRZEQ";

    private TwitterApiClient twitterApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);
        twitterApiClient = getTwitterApiClient();
    }

    public TwitterApiClient getTwitterApiClient() {
        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> result) {
                Log.d("STATICS", "loginGuest.callback.success called");
                AppSession guestAppSession = result.data;
                twitterApiClient = TwitterCore.getInstance().getApiClient(guestAppSession);
                twitterApiClient.getSearchService().tweets("It's time 19:13", null, null, null, null, 50, null, null, null, true, new GuestCallback<>(new Callback<Search>() {
                    @Override
                    public void success(Result<Search> result) {
                        final ViewGroup parentView = (ViewGroup) getWindow().getDecorView().getRootView();
                        Tweet test = result.data.tweets.get(0);
                        TweetView tweetView = new TweetView(MainActivity.this, test);
                        parentView.addView(tweetView);
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

            @Override
            public void failure(TwitterException exception) {
                Log.d("STATICS", "loginGuest.callback.failure called");
                // unable to get an AppSession with guest auth
                throw exception;
            }
        });

        return twitterApiClient;
    }
}
