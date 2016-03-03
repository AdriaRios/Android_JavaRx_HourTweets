package org.adriarios.hourtweets.hourtweets.domain;

import com.twitter.sdk.android.core.models.Tweet;

import rx.Observer;

/**
 * Created by Adrian on 02/03/2016.
 */
public interface IGetTweetInteractor {
    void subscribe(Observer<Tweet> myObserver);
}
