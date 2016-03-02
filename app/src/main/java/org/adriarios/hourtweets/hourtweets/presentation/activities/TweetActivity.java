package org.adriarios.hourtweets.hourtweets.presentation.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;

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

import org.adriarios.hourtweets.hourtweets.R;
import org.adriarios.hourtweets.hourtweets.di.App;
import org.adriarios.hourtweets.hourtweets.presentation.presenters.TweetPresenter;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;

public class TweetActivity extends AppCompatActivity {
    @Inject
    TweetPresenter tweetPresenter;

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "IMtfeDKDQWo0UbHbsI7P90vca";
    private static final String TWITTER_SECRET = "sWHwnn9jfZ0NB8C6rlyCAQdRFUyGSAZ3W7ygWTz4we8nwFRZEQ";

    private TwitterApiClient twitterApiClient;
    private RelativeLayout tweetContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getObjectGraph().inject(this);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);
        tweetContainer = (RelativeLayout) findViewById(R.id.tweetContainer);
        twitterApiClient = getTwitterApiClient();
        timer();

        tweetPresenter.init(this);



    }

    public void timer(){
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @SuppressWarnings("unchecked")
                    public void run() {
                        try {
                            getTwitterApiClient();
                        }
                        catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 60000);
    }

    public TwitterApiClient getTwitterApiClient() {
        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> result) {
                Log.d("STATICS", "loginGuest.callback.success called");
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR);
                int hour_day = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                AppSession guestAppSession = result.data;
                twitterApiClient = TwitterCore.getInstance().getApiClient(guestAppSession);
                String hourStr = String.valueOf(hour_day) + ":" + String.valueOf(minute);
                twitterApiClient.getSearchService().tweets("\""+hourStr+"\"", null, null, null, "mixed", 50, null, null, null, true, new GuestCallback<>(new Callback<Search>() {
                    @Override
                    public void success(Result<Search> result) {
                        //ViewGroup parentView = (ViewGroup) findViewById(R.id.tweetView);
                        Tweet test = result.data.tweets.get(0);
                        TweetView tweetView = new TweetView(TweetActivity.this, test);
                        tweetContainer.addView(tweetView);
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
