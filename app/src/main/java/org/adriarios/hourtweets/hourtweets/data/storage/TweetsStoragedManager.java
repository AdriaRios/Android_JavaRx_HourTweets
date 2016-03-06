package org.adriarios.hourtweets.hourtweets.data.storage;

import com.twitter.sdk.android.core.models.Tweet;

import org.adriarios.hourtweets.hourtweets.di.App;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * This is the Tweets Storaged Manager. His goal to persist Tweets in the local storage of the
 * device and serve this tweets. The database selected for this task is Realm.
 *
 * Created by Adrian on 03/03/2016.
 */
public class TweetsStoragedManager implements ITweetsStoragedManager {
    private Realm realm;

    /**
     * Contructor of the class. Initializes the Realm Instance.
     * @param application
     */
    public TweetsStoragedManager(App application) {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(application).build();
        realm = Realm.getInstance(realmConfig);
    }

    /**
     * This method return a stored tweet by provided id (the hour of the tweet)
     * @param currentTweetHour Hour of the tweet, used as id to make the query
     * @return
     */
    public TweetRealmObjectVO getStoragedTweet(String currentTweetHour) {
        TweetRealmObjectVO tweet = null;
        RealmResults<TweetRealmObjectVO> tweetResult = realm.where(TweetRealmObjectVO.class)
                .equalTo("id", currentTweetHour)
                .findAll();

        if (tweetResult.size() > 0) {
            tweet = tweetResult.first();
        }
        return tweet;
    }

    /**
     * This method stores a new Tweet in local storage
     * @param tweet Tweet to store
     * @param currentTweetHour Hour of the tweet, used as id
     */
    public void addNewTweetToLocalStorage(Tweet tweet, String currentTweetHour) {
        deleteIfExists(currentTweetHour);
        TweetRealmObjectVO tweetRealm = new TweetRealmObjectVO();
        tweetRealm.setId(currentTweetHour);
        tweetRealm.setUserName(tweet.user.name);
        tweetRealm.setImageURL(tweet.user.profileImageUrl);
        tweetRealm.setDate(tweet.createdAt);
        tweetRealm.setText(tweet.text);
        tweetRealm.setTweetCount(tweet.retweetCount);

        realm.beginTransaction();
        realm.copyToRealm(tweetRealm);
        realm.commitTransaction();
    }

    /**
     * This method deletes a new Tweet from the local storage
     * @param id Id to tweet to be deleted
     */
    private void deleteIfExists(String id) {
        RealmResults<TweetRealmObjectVO> tweetResult = realm.where(TweetRealmObjectVO.class)
                .equalTo("id", id)
                .findAll();
        // All changes to data must happen in a transaction
        realm.beginTransaction();
        // remove single match
        if (tweetResult.size() > 0) {
            tweetResult.remove(0);
        }
        realm.commitTransaction();
    }
}
