# Android_JavaRx_HourTweets

##Purpose

This is an Android application that searches and shows one Tweet related to the current time (the Tweet changes every minute) following the next format:

  **It's _time_ and _message_**
  
The Tweet showed will be most retweeted first, newer first if they have the same retweet count
  
The application runs in two modes:

  - **Mode Online**:
  
    The tweet is obtained through the Twitter Api with Internet Connection, is loaded inside the App and the user can interact with it opening Twitter App with a click.
  
  ![alt tag](http://52.11.144.116/images/tweet_loading.png)
  ![alt tag](http://52.11.144.116/images/tweet_online.png)
  
  - **Mode Offline**
  
    The tweet is obtained through the Local Storage of the device, is a Tweet stored previously. The user can't interact with it, only can see the information.

  ![alt tag](http://52.11.144.116/images/tweet_offline.png)

If there aren't any Tweets for the current hour the user will see a "No Tweets Found" message:

  ![alt tag](http://52.11.144.116/images/tweet_notfound.png)


  


##Architecture

The application has been designed following [The Clean Architecture](https://github.com/android10/Android-CleanArchitecture) philosophy. This philosophy stands for a group of practices that produce systems that are:

- Testable
- Independent of UI
- Independent of Database
- Independent of external API's

As a result, the application is split of the following way:

###Presentation Layer

In this layer there are the logic related to Views such as rendering, it uses a MVP pattern. In the case of this app, take place the following actions:
- Render Online Tweet.
- Render Offline Tweet.
- Hide/Show View elements.
- Make calls to Domain Layer to get new Tweets.

###Domain Layer

In the Domain Layer, there are the business logic. This layer knows how to get, save and process information from Data Layer and has to provide it to Presentation layer. In the case of this app take place the following actions:

- Get online tweet from Twitter API.
- Get offline tweet from Local Storage.
- Save tweets in local storage

###Data Layer

All data needed for the applications. In the case of this app the information can be obtained by:

- Twitter API (with internet connection)
- Local Storage (withou internet connection)

##Tools
 - [RxJava /RxAndroid](https://github.com/ReactiveX/RxAndroid)
 - [Fabric - Twitter SDK](https://fabric.io/kits/android/twitterkit/summary)
 - [Dagger](http://square.github.io/dagger/)
 - [Realm](https://realm.io/)

##Next Steps

- Testing
- Implementation of the repository pattern
- Improve the style of Tweet Offline
- More lyfecycle managment
- More error managment 

