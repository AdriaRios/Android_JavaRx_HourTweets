package org.adriarios.hourtweets.hourtweets.presentation.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetView;

import org.adriarios.hourtweets.hourtweets.R;
import org.adriarios.hourtweets.hourtweets.di.App;
import org.adriarios.hourtweets.hourtweets.presentation.presenters.TweetPresenter;

import javax.inject.Inject;

public class TweetActivity extends AppCompatActivity {
    @Inject
    TweetPresenter tweetPresenter;

    private RelativeLayout tweetContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getObjectGraph().inject(this);
        setContentView(R.layout.activity_main);
        tweetContainer = (RelativeLayout) findViewById(R.id.tweetContainer);
        tweetPresenter.init(this);



    }

    public void showTweet(Tweet tweet) {
        TweetView tweetView = new TweetView(TweetActivity.this, tweet);
        tweetContainer.addView(tweetView);
    }
}
