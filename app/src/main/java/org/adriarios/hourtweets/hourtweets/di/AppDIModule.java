package org.adriarios.hourtweets.hourtweets.di;

import org.adriarios.hourtweets.hourtweets.data.api.ITwitterApi;
import org.adriarios.hourtweets.hourtweets.data.api.TwitterApi;
import org.adriarios.hourtweets.hourtweets.data.storage.ITweetsStoragedManager;
import org.adriarios.hourtweets.hourtweets.data.storage.TweetsStoragedManager;
import org.adriarios.hourtweets.hourtweets.domain.GetTweetInteractor;
import org.adriarios.hourtweets.hourtweets.domain.IGetTweetInteractor;
import org.adriarios.hourtweets.hourtweets.presentation.activities.TweetActivity;
import org.adriarios.hourtweets.hourtweets.presentation.presenters.TweetPresenter;
import org.adriarios.hourtweets.hourtweets.utils.Utils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Adrian on 02/03/2016.
 */
@Module(injects = {
        TweetActivity.class,
        TweetPresenter.class,
        GetTweetInteractor.class
})
public class AppDIModule {
    private App application;

    public AppDIModule(App application) {
        this.application = application;
    }

    @Provides
    public TweetPresenter providesPresenter() {
        return new TweetPresenter(this.application);
    }

    @Provides
    public IGetTweetInteractor providesTweetInteractor() {
        return new GetTweetInteractor(this.application);
    }

    @Provides
    @Singleton
    public ITwitterApi providesTwitterApi() {
        return new TwitterApi(this.application);
    }

    @Provides
    @Singleton
    public ITweetsStoragedManager providesTweetsStoragedManager() {
        return new TweetsStoragedManager(this.application);
    }

    @Provides
    @Singleton
    public Utils providesUtils() {
        return new Utils(this.application);
    }

}
