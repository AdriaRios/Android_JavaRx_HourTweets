package org.adriarios.hourtweets.hourtweets.domain;

import rx.Observer;

/**
 * Created by Adrian on 02/03/2016.
 */
public interface IGetTweetInteractor {
    void subscribe(Observer<Object> myObserver);
    void nextTweet();
}
