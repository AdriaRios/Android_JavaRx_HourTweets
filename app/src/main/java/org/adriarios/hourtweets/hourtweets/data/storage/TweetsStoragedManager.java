package org.adriarios.hourtweets.hourtweets.data.storage;

import com.twitter.sdk.android.core.models.Tweet;

import org.adriarios.hourtweets.hourtweets.di.App;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Adrian on 04/03/2016.
 */
public class TweetsStoragedManager implements ITweetsStoragedManager {
    private Realm realm;

    public TweetsStoragedManager(App application){
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(application).build();
        realm = Realm.getInstance(realmConfig);
    }

    public void addNewTweetToLocalStorage(Tweet tweet){
        TweetRealmObjectVO tweetRealm = new TweetRealmObjectVO();
        tweetRealm.setId("111");
    }
}
