package org.adriarios.hourtweets.hourtweets.di;

import org.adriarios.hourtweets.hourtweets.presentation.activities.TweetActivity;
import org.adriarios.hourtweets.hourtweets.presentation.presenters.TweetPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Adrian on 02/03/2016.
 */
@Module(injects = {
        TweetActivity.class
})
public class AppDIModule {
    private App application;

    public AppDIModule(App application) {
        this.application = application;
    }

    @Provides
    public TweetPresenter providesPresenter() {
        return new TweetPresenter();
    }

}
