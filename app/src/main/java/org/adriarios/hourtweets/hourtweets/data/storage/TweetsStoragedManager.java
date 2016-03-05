package org.adriarios.hourtweets.hourtweets.data.storage;

import com.twitter.sdk.android.core.models.Tweet;

import org.adriarios.hourtweets.hourtweets.di.App;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by Adrian on 04/03/2016.
 */
public class TweetsStoragedManager implements ITweetsStoragedManager {
    private Realm realm;

    public TweetsStoragedManager(App application){
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(application).build();
        realm = Realm.getInstance(realmConfig);
    }

    public TweetRealmObjectVO getStoragedTweet(String currentTweetHour){
        TweetRealmObjectVO tweet = null;
        RealmResults<TweetRealmObjectVO> tweetResult = realm.where(TweetRealmObjectVO.class)
                .equalTo("id", currentTweetHour)
                .findAll();

        if (tweetResult.size() > 0){
            tweet = tweetResult.first();
        }
        return tweet;
    }

    public void addNewTweetToLocalStorage(Tweet tweet, String currentTweetHour){
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
}
