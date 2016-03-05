package org.adriarios.hourtweets.hourtweets.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.adriarios.hourtweets.hourtweets.di.App;

/**
 * Created by Adrian on 05/03/2016.
 */
public class Utils {
    App _application;

    public Utils(App application) {
        _application = application;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) _application.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public String paddingZero(int number) {
        String result;
        if (number < 10) {
            result = "0" + String.valueOf(number);
        }else{
            result = String.valueOf(number);
        }
        return result;
    }

}
