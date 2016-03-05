package org.adriarios.hourtweets.hourtweets.presentation.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetView;

import org.adriarios.hourtweets.hourtweets.R;
import org.adriarios.hourtweets.hourtweets.data.storage.TweetRealmObjectVO;
import org.adriarios.hourtweets.hourtweets.di.App;
import org.adriarios.hourtweets.hourtweets.presentation.presenters.TweetPresenter;

import javax.inject.Inject;

public class TweetActivity extends AppCompatActivity {
    @Inject
    TweetPresenter tweetPresenter;

    //Header Text View to inform the user about the app state
    private TextView headerText;

    //Progress bar
    private ProgressBar progressBar;

    //Tweet Containers
    private RelativeLayout tweetOnlineContainer;
    private RelativeLayout tweetOffLineContainer;

    //Offline elements
    private TextView userName;
    private TextView userNick;
    private TextView tweetDesc;
    private TextView tweetDate;
    private TextView tweetRetweets;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getObjectGraph().inject(this);
        setContentView(R.layout.activity_main);
        headerText = (TextView) findViewById(R.id.headerText);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        tweetOnlineContainer = (RelativeLayout) findViewById(R.id.tweetOnlineContainer);
        tweetOffLineContainer = (RelativeLayout) findViewById(R.id.tweetOffLineContainer);

        userName = (TextView) findViewById(R.id.userName);
        userNick = (TextView) findViewById(R.id.userNick);
        tweetDesc = (TextView) findViewById(R.id.tweetDesc);
        tweetDate = (TextView) findViewById(R.id.tweetDate);
        tweetRetweets = (TextView) findViewById(R.id.tweetRetweets);

        initView();
        tweetPresenter.init(this);
    }

    private void initView(){
        headerText.setText("LOADING TWEETS");
        progressBar.setVisibility(View.VISIBLE);
        tweetOnlineContainer.setVisibility(View.GONE);
        tweetOffLineContainer.setVisibility(View.GONE);
    }

    public void showOnlineTweet(Tweet tweet) {
        headerText.setText("TWEET ONLINE");
        tweetOnlineContainer.setVisibility(View.VISIBLE);
        tweetOffLineContainer.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        TweetView tweetView = new TweetView(TweetActivity.this, tweet);
        tweetOnlineContainer.removeAllViews();
        tweetOnlineContainer.addView(tweetView);
    }

    public void showOfflineTweet(TweetRealmObjectVO tweetOffline) {
        headerText.setText("TWEET OFFLINE");
        tweetOnlineContainer.setVisibility(View.GONE);
        tweetOffLineContainer.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        userName.setText(tweetOffline.getUserName());
        userNick.setText(tweetOffline.getUserName());
        tweetDesc.setText(tweetOffline.getText());
        tweetDate.setText(tweetOffline.getDate());
        tweetRetweets.setText(String.valueOf(tweetOffline.getTweetCount()) + " Retweets");
    }

    public void showNotFoundMessage() {
        headerText.setText("NO TWEETS FOUND FOR THE CURRENT TIME");
        tweetOnlineContainer.setVisibility(View.GONE);
        tweetOffLineContainer.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }
}
