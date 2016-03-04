package org.adriarios.hourtweets.hourtweets.data.storage;

import com.twitter.sdk.android.core.models.Tweet;

/**
 * Created by Adrian on 04/03/2016.
 */
public interface ITweetsStoragedManager {
    void addNewTweetToLocalStorage(Tweet tweet);
}
