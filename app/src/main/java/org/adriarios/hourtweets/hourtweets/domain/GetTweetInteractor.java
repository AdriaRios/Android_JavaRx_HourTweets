package org.adriarios.hourtweets.hourtweets.domain;

import org.adriarios.hourtweets.hourtweets.data.api.ITwitterApi;
import org.adriarios.hourtweets.hourtweets.di.App;

import javax.inject.Inject;

import rx.Observer;

/**
 * Created by Adrian on 02/03/2016.
 */
public class GetTweetInteractor implements IGetTweetInteractor {
    @Inject
    ITwitterApi twitterApi;

    //Observable property to watch TwitterApi
    private Observer<Object> twitterApiObserver;

    //Interactors observers
    private Observer<Object> myObserver;

    public GetTweetInteractor(App application) {
        application.getObjectGraph().inject(this);
        initObserver();
        twitterApi.subscribe(twitterApiObserver);
    }

    private void initObserver() {
        twitterApiObserver = new Observer<Object>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object response) {
                myObserver.onNext(response);
            }
        };
    }

    public void subscribe(Observer<Object> observer) {
        myObserver = observer;
    }

    @Override
    public void nextTweet() {
        twitterApi.getNewTweet();
    }
}
