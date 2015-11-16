package com.dexafree.seriescountdown;

import android.app.Application;

import com.dexafree.seriescountdown.injection.AppComponent;
import com.dexafree.seriescountdown.injection.AppModule;
import com.dexafree.seriescountdown.injection.Dagger2Helper;


public class SeriesCountdown extends Application {

    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = Dagger2Helper.buildComponent(AppComponent.class, new AppModule(this));
    }

    public static void inject(Object target) {
        Dagger2Helper.inject(AppComponent.class, component, target);
    }

}
