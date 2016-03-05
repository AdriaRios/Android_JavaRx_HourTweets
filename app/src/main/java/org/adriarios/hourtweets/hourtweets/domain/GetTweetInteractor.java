package org.adriarios.hourtweets.hourtweets.domain;

import com.twitter.sdk.android.core.models.Tweet;

import org.adriarios.hourtweets.hourtweets.data.api.ITwitterApi;
import org.adriarios.hourtweets.hourtweets.data.storage.ITweetsStoragedManager;
import org.adriarios.hourtweets.hourtweets.di.App;
import org.adriarios.hourtweets.hourtweets.utils.Utils;

import javax.inject.Inject;

import rx.Observer;

/**
 * Created by Adrian on 02/03/2016.
 */
public class GetTweetInteractor implements IGetTweetInteractor {
    @Inject
    ITwitterApi twitterApi;

    @Inject
    ITweetsStoragedManager tweetsStoragedManager;

    @Inject
    Utils utils;

    private String currentTweetHour;

    //Observable property to watch TwitterApi
    private Observer<Object> twitterApiObserver;

    //Interactors observers
    private Observer<Object> myObserver;

    public GetTweetInteractor(App application) {
        application.getObjectGraph().inject(this);
        initObserver();
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
//                String type = response.getClass().getName();
                tweetsStoragedManager.addNewTweetToLocalStorage((Tweet)response, currentTweetHour);
                myObserver.onNext(response);
            }
        };
    }

    public void subscribe(Observer<Object> observer) {
        myObserver = observer;
    }

    @Override
    public void nextTweet(String hourStr) {
        currentTweetHour = hourStr;
        if(utils.isNetworkAvailable()){
            if(twitterApi.isTwitterApiInitialized()){
                twitterApi.getNewTweet(hourStr);
            }else{
                twitterApi.initApi();
                twitterApi.subscribe(twitterApiObserver);
            }
        }else{
            tweetsStoragedManager.getStoragedTweet(hourStr);
        }

    }
}
