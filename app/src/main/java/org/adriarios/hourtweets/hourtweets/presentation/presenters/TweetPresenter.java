package org.adriarios.hourtweets.hourtweets.presentation.presenters;

import org.adriarios.hourtweets.hourtweets.di.App;
import org.adriarios.hourtweets.hourtweets.domain.IGetTweetInteractor;
import org.adriarios.hourtweets.hourtweets.presentation.activities.TweetActivity;

import javax.inject.Inject;

/**
 * Created by Adrian on 02/03/2016.
 */
public class TweetPresenter {
    @Inject
    IGetTweetInteractor tweetInteractor;


    public TweetPresenter(App application){
        application.getObjectGraph().inject(this);
    }

    public void init(TweetActivity tweetActivity) {
        tweetInteractor.getNewTweet();

    }
}
