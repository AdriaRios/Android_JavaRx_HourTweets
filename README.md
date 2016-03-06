# Android_JavaRx_HourTweets

##Purpose

This is an Android application that searches and shows one Tweet related to the current time (the Tweet changes every minute) following the next format:

  It's _time_ and _message_
  
The Tweet showed will be most retweeted first, newer first if they have the same retweet count
  
The application runs in two modes:

  - Mode Online:
  
    The tweet is obtained through the Twitter Api with Internet Connection, is loaded inside the App and the user can interact with it opening Twitter App with a click.
  
  ![alt tag](http://52.11.144.116/images/tweet_loading.png)
  ![alt tag](http://52.11.144.116/images/tweet_online.png)
  
  - Mode Offline
  
    The tweet is obtained through the Local Storage of the device, is a Tweet stored previously. The user can't interact with it, only can see the information.

  ![alt tag](http://52.11.144.116/images/tweet_offline.png)

If there aren't any Tweets for the current hour the user will see a "No Tweets Found" message:

![alt tag](http://52.11.144.116/images/tweet_notfound.png)


  


##Architecture

##Tools
 - [RxJava /RxAndroid](https://github.com/ReactiveX/RxAndroid) 
 - [Fabric - Twitter SDK](https://fabric.io/kits/android/twitterkit/summary)
 - [Dagger](http://square.github.io/dagger/)
 - [Realm](https://realm.io/)

##Next Steps
