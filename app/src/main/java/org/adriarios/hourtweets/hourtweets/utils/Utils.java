package org.adriarios.hourtweets.hourtweets.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.adriarios.hourtweets.hourtweets.di.App;

import java.util.Calendar;

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

    public String getCurrentHour() {
        Calendar c = Calendar.getInstance();
        int hour_day = c.get(Calendar.HOUR_OF_DAY);
        String minute = paddingZero(c.get(Calendar.MINUTE));
        String hourStr = String.valueOf(hour_day) + ":" + minute;

        return  hourStr;
    }

}
