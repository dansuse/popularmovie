package com.example.danielsetyabudi.movies;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

/**
 * Created by daniel on 18/08/2017.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        final Context context = this;
        Stetho.initializeWithDefaults(this);
    }
}
