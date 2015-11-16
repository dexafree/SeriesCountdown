package com.dexafree.seriescountdown.injection;

import android.database.sqlite.SQLiteDatabase;

import com.dexafree.seriescountdown.BuildConfig;
import com.dexafree.seriescountdown.SeriesCountdown;
import com.dexafree.seriescountdown.database.DatabaseOpenHelper;
import com.dexafree.seriescountdown.interactors.FavoriteSeriesInteractor;
import com.dexafree.seriescountdown.interactors.GetPopularSeriesInteractor;
import com.dexafree.seriescountdown.interactors.SearchSeriesInteractor;
import com.dexafree.seriescountdown.interactors.SearchSuggestionsInteractor;
import com.dexafree.seriescountdown.interactors.service.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

@Module
public class AppModule {

    private final static String ENDPOINT = "https://www.episodate.com/api/";

    private SeriesCountdown application;

    public AppModule(SeriesCountdown application){
        this.application = application;
    }

    @Singleton
    @Provides
    GetPopularSeriesInteractor providePopularSeriesInteractor(){
        return new GetPopularSeriesInteractor();
    }

    @Singleton
    @Provides
    FavoriteSeriesInteractor provideFavoriteSeriesInteractor(){
        return new FavoriteSeriesInteractor();
    }

    @Singleton
    @Provides
    SearchSeriesInteractor provideSearchSeriesInteractor(){
        return new SearchSeriesInteractor();
    }

    @Singleton
    @Provides
    SearchSuggestionsInteractor provideSearchSuggestionsInteractor(){
        return new SearchSuggestionsInteractor();
    }

    @Singleton
    @Provides
    SQLiteDatabase provideSQLiteDatabase(){
        return new DatabaseOpenHelper(application).getWritableDatabase();
    }

    @Singleton
    @Provides
    ApiService provideApiService(){
        Gson gson = new GsonBuilder()
                .create();

        RestAdapter.LogLevel logLevel = BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE;

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(logLevel)
                .setEndpoint(ENDPOINT)
                .setConverter(new GsonConverter(gson))
                .build();

        return restAdapter.create(ApiService.class);
    }

}
