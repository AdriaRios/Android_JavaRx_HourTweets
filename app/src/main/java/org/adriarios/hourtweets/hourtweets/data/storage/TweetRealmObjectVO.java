package org.adriarios.hourtweets.hourtweets.data.storage;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by Adrian on 04/03/2016.
 */
public class TweetRealmObjectVO extends RealmObject {
    @Required // Id cannot be null
    private String id;
    private String userName;
    private String date;
    private String text;
    private String imageURL;

    public int getTweetCount() {
        return tweetCount;
    }

    public void setTweetCount(int tweetCount) {
        this.tweetCount = tweetCount;
    }

    private int tweetCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


}
