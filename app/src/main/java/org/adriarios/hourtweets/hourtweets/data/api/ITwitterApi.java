package org.adriarios.hourtweets.hourtweets.data.api;

import rx.Observer;

/**
 * Created by Adrian on 03/03/2016.
 */
public interface ITwitterApi {
    void getNewTweet();

    void subscribe(Observer<Object> twitterApiObserver);
}
