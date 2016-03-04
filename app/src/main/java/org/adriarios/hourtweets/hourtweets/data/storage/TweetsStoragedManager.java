package org.adriarios.hourtweets.hourtweets.data.storage;

import android.util.Log;

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

    public void getStoragedTweet(String currentTweetHour){
        RealmResults<TweetRealmObjectVO> tweet = realm.where(TweetRealmObjectVO.class)
                .equalTo("id", currentTweetHour)
                .findAll();
        Log.d("Realm result", "d");
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
