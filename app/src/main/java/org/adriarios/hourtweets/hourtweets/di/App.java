package org.adriarios.hourtweets.hourtweets.di;

import android.app.Application;

import dagger.ObjectGraph;

/**
 * Created by Adrian on 02/03/2016.
 */
public class App extends Application {
    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        objectGraph = ObjectGraph.create(new AppDIModule(this));
    }

    public ObjectGraph getObjectGraph() {
        return objectGraph;
    }
}
